package com.viktoreeej.worldbridge.mixin;

import com.viktoreeej.worldbridge.INetworkSystemExtended;
import com.viktoreeej.worldbridge.Worldbridge;
import net.minecraft.network.NetworkSystem;
import org.spongepowered.asm.mixin.Mixin;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;

@Mixin(NetworkSystem.class)
public abstract class NetworkSystemMixin implements INetworkSystemExtended {
    private ServerSocket serverSocket;

    @Override
    public void addEndpoint(InetAddress address, int port) throws IOException {
        try {
            if (this.serverSocket != null && !this.serverSocket.isClosed()) {
                this.serverSocket.close();
            }
            this.serverSocket = new ServerSocket(port, 0, address);
        } catch(IOException e) {
            Worldbridge.LOGGER.error("Failed to create server socket on port " + port, e);
            throw e; // Propagate the error up so we can handle it in the IntegratedServerMixin
        }
    }
}