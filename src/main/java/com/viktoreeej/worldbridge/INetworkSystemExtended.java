package com.viktoreeej.worldbridge;

import java.io.IOException;
import java.net.InetAddress;

public interface INetworkSystemExtended {
    void addEndpoint(InetAddress address, int port) throws IOException;
}