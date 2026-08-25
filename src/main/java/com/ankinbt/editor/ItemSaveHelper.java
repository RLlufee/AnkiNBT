package com.ankinbt.editor;

import com.ankinbt.compat.VersionCompat;
import com.ankinbt.util.DebugLog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public final class ItemSaveHelper {
    public enum SaveResult {
        SAVED,
        UNSUPPORTED,
        INVALID_SLOT
    }

    private ItemSaveHelper() {
    }

    public static SaveResult saveToPlayerInventory(Minecraft mc, ItemStack stack, int inventorySlot) {
        if (mc == null || mc.player == null || stack == null) {
            return SaveResult.INVALID_SLOT;
        }

        int playerSlot = resolvePlayerInventorySlot(mc, inventorySlot);
        if (playerSlot < 0) {
            DebugLog.info("Skipped item save for invalid slot {}", inventorySlot);
            return SaveResult.INVALID_SLOT;
        }

        ItemStack saved = stack.copy();
        if (trySaveToIntegratedServer(mc, saved, playerSlot)) {
            mc.player.getInventory().setItem(playerSlot, saved.copy());
            DebugLog.info("Saved item into integrated server player inventory slot {}", playerSlot);
            return SaveResult.SAVED;
        }

        if (sendCreativeSlotPacketIfAllowed(mc, saved, playerSlot)) {
            mc.player.getInventory().setItem(playerSlot, saved.copy());
            DebugLog.info("Saved item into creative player inventory slot {}", playerSlot);
            return SaveResult.SAVED;
        }

        // 多人/远程服务器的背包由服务端权威维护；只改客户端会在下一次同步时被覆盖。
        DebugLog.info("Skipped persistent item save without integrated server or creative permission for slot {}", playerSlot);
        return SaveResult.UNSUPPORTED;
    }

    public static boolean isSaved(SaveResult result) {
        return result == SaveResult.SAVED;
    }

    private static int resolvePlayerInventorySlot(Minecraft mc, int inventorySlot) {
        if (inventorySlot == 45) {
            return 40;
        }
        if ((inventorySlot >= 0 && inventorySlot < 36) || inventorySlot == 40) {
            return inventorySlot;
        }
        if (inventorySlot >= 36 && inventorySlot < 45) {
            return inventorySlot - 36;
        }
        return VersionCompat.get().getSelectedSlot(mc.player.getInventory());
    }

    private static boolean trySaveToIntegratedServer(Minecraft mc, ItemStack stack, int playerSlot) {
        IntegratedServer server;
        try {
            server = mc.getSingleplayerServer();
        }
        catch (Throwable ignored) {
            return false;
        }
        if (server == null || mc.player == null) {
            return false;
        }

        AtomicBoolean success = new AtomicBoolean(false);
        CountDownLatch latch = new CountDownLatch(1);
        server.execute(() -> {
            try {
                ServerPlayer serverPlayer = server.getPlayerList().getPlayer(mc.player.getUUID());
                if (serverPlayer == null) {
                    return;
                }
                ItemStack serverStack = stack.copy();
                serverPlayer.getInventory().setItem(playerSlot, serverStack);
                serverPlayer.inventoryMenu.broadcastFullState();
                serverPlayer.containerMenu.broadcastFullState();
                success.set(true);
            }
            catch (Throwable t) {
                DebugLog.warn("Integrated server item save failed: {}", t.toString());
            }
            finally {
                latch.countDown();
            }
        });

        try {
            if (!latch.await(3L, TimeUnit.SECONDS)) {
                DebugLog.warn("Timed out waiting for integrated server item save", new Object[0]);
                return false;
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        return success.get();
    }

    private static boolean sendCreativeSlotPacketIfAllowed(Minecraft mc, ItemStack stack, int playerSlot) {
        if (mc.gameMode == null || !mc.player.hasInfiniteMaterials()) {
            return false;
        }
        int packetSlot = creativePacketSlotFromPlayerInventory(playerSlot);
        if (packetSlot >= 0) {
            mc.gameMode.handleCreativeModeItemAdd(stack.copy(), packetSlot);
            return true;
        }
        return false;
    }

    private static int creativePacketSlotFromPlayerInventory(int playerSlot) {
        if (playerSlot == 40) {
            return 45;
        }
        if (playerSlot >= 0 && playerSlot < 9) {
            return 36 + playerSlot;
        }
        if (playerSlot >= 9 && playerSlot < 36) {
            return playerSlot;
        }
        return -1;
    }
}
