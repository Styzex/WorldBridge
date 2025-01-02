package com.viktoreeej.worldbridge;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.command.CommandBase;
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
        return "/shareip";
    }
    @Override
    public List getCommandAliases() {
        return Collections.emptyList();
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
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
    public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
        return false;
    }

    @Override
    public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
        return Collections.emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
        return false;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
