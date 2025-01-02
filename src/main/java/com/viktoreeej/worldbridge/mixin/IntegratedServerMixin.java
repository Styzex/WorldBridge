package com.viktoreeej.worldbridge.mixin;

import com.viktoreeej.worldbridge.INetworkSystemExtended;
import com.viktoreeej.worldbridge.UPnPManager;
import com.viktoreeej.worldbridge.Worldbridge;
import net.minecraft.network.NetworkSystem;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.integrated.IntegratedServer;

import java.io.IOException;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    private static boolean isLANOpen = false;
    private int port = 25565;

    static {
        Worldbridge.LOGGER.info("IntegratedServerMixin loaded!");
    }

    @Inject(method = "shareToLAN", at = @At("HEAD"), cancellable = true)
    private void onShareToLAN(WorldSettings.GameType p_71206_1_, boolean p_71206_2_, CallbackInfoReturnable<String> cir) throws IOException {
        if (isLANOpen) {
            Worldbridge.LOGGER.info("LAN is already open!");
            cir.setReturnValue(String.valueOf(port));
            return;
        }

        IntegratedServer server = (IntegratedServer)(Object)this;

        try {
            boolean portForwardingSuccess = UPnPManager.openPort(port);

            if (portForwardingSuccess) {
                NetworkSystem networkSystem = server.func_147137_ag();
                if (networkSystem != null) {
                    Worldbridge.LOGGER.info("NetworkSystem is available");
                    try {
                        ((INetworkSystemExtended)networkSystem).addEndpoint(null, port);
                        isLANOpen = true;
                        cir.setReturnValue(String.valueOf(port));
                    } catch (IOException e) {
                        Worldbridge.LOGGER.error("Failed to set up network endpoint", e);
                        UPnPManager.closePort(port);
                        cir.setReturnValue(null);
                    }
                } else {
                    Worldbridge.LOGGER.error("NetworkSystem is null");
                    UPnPManager.closePort(port);
                    cir.setReturnValue(null);
                }
            } else {
                Worldbridge.LOGGER.error("Port forwarding failed");
                cir.setReturnValue(null);
            }
        } catch (Exception e) {
            Worldbridge.LOGGER.error("Unexpected error during LAN share setup", e);
            try {
                UPnPManager.closePort(port);
            } catch (Exception cleanupError) {
                Worldbridge.LOGGER.error("Failed to clean up port forwarding after error", cleanupError);
            }
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void onStopServer(CallbackInfo ci) {
        if (port != -1) {
            try {
                UPnPManager.closePort(port);
                Worldbridge.LOGGER.info("Successfully closed port " + port);
            } catch (Exception e) {
                Worldbridge.LOGGER.error("Failed to close port " + port, e);
            }
        }
    }
}