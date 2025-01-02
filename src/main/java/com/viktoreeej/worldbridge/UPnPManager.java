package com.viktoreeej.worldbridge;

import org.bitlet.weupnp.GatewayDevice;
import org.bitlet.weupnp.GatewayDiscover;

import java.net.InetAddress;
import java.util.Map;

public class UPnPManager {
    private static GatewayDevice currentDevice;

    public static boolean openPort(int port) {
        try {
            GatewayDiscover discover = new GatewayDiscover();
            Map<InetAddress, GatewayDevice> devices = discover.discover();

            if (devices.isEmpty()) {
                Worldbridge.LOGGER.warn("No UPnP devices found");
                return false;
            }

            GatewayDevice device = devices.values().iterator().next();
            currentDevice = device;

            String localAddress = device.getLocalAddress().getHostAddress();

            // Add TCP mapping
            device.addPortMapping(
                    port,
                    port,
                    localAddress,
                    "TCP",
                    "Minecraft Server"
            );

            // Add UDP mapping for server discovery
            device.addPortMapping(
                    port,
                    port,
                    localAddress,
                    "UDP",
                    "Minecraft Server Discovery"
            );

            Worldbridge.LOGGER.info("Successfully mapped port {}", port);
            return true;
        } catch (Exception e) {
            Worldbridge.LOGGER.error("Failed to set up UPnP port forwarding", e);
            return false;
        }
    }

    public static void closePort(int port) {
        try {
            if (currentDevice != null) {
                currentDevice.deletePortMapping(port, "TCP");
                currentDevice.deletePortMapping(port, "UDP");
                Worldbridge.LOGGER.info("Successfully closed port {}", port);
            }
        } catch (Exception e) {
            Worldbridge.LOGGER.error("Failed to close port mappings", e);
        }
    }
}
