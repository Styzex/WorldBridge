package com.viktoreeej.worldbridge;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.ChatComponentText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collections;
import java.util.List;

public class ShareIPCommand implements ICommand {
    private static final String IP_CHECK_URL = "http://checkip.amazonaws.com";

    @Override
    public String getCommandName() {
        return "shareIP";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/shareIP";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server instanceof IntegratedServer) {
            IntegratedServer integratedServer = (IntegratedServer) server;
            if (integratedServer.getPublic()) {
                try {
                    URL url = new URL(IP_CHECK_URL);
                    BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
                    String externalIP = br.readLine().trim();
                    int port = integratedServer.getPort();

                    sender.addChatMessage(new ChatComponentText(
                            "Share this address with your friends: " + externalIP + ":" + port
                    ));
                } catch (Exception e) {
                    sender.addChatMessage(new ChatComponentText(
                            "Failed to get external IP address"
                    ));
                }
            } else {
                sender.addChatMessage(new ChatComponentText(
                        "You need to open to LAN first!"
                ));
            }
        }
    }

    @Override
    public List<String> getCommandAliases() {
        return null; // Optional: return a list of aliases for the command
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true; // You can add permission checks here
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false; // Optional: specify if the argument at the index is a username
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}