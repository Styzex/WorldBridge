package com.viktoreeej.worldbridge.mixin;

import com.viktoreeej.worldbridge.UPnPManager;
import net.minecraft.network.NetworkSystem;
import net.minecraft.world.WorldSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.server.integrated.IntegratedServer;

import java.io.IOException;
import java.util.Random;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Unique
    private static final Logger LOGGER = LogManager.getLogger();

    static {
        LOGGER.info("IntegratedServerMixin loaded!");
    }

    @Inject(method = "shareToLAN", at = @At("HEAD"), cancellable = true)
    private void onShareToLAN(WorldSettings.GameType p_71206_1_, boolean p_71206_2_, CallbackInfoReturnable<String> cir) throws IOException {
        IntegratedServer server = (IntegratedServer)(Object)this;

        int port = 25565 + new Random().nextInt(100);

        boolean portForwardingSuccess = UPnPManager.openPort(port);

        if (portForwardingSuccess) {
            NetworkSystem networkSystem = server.func_147137_ag();
            networkSystem.addLanEndpoint(null, port);
            cir.setReturnValue(String.valueOf(port));
        } else {
            cir.setReturnValue(null);
        }
    }

    @Inject(method = "stopServer", at = @At("HEAD"))
    private void onStopServer(CallbackInfo ci) {
        IntegratedServer server = (IntegratedServer)(Object)this;
        NetworkSystem networkSystem = server.func_147137_ag();
        if (networkSystem != null) {
            UPnPManager.closePort(server.getPort());
            LOGGER.info("Closing server port!!!");
        }
    }
}