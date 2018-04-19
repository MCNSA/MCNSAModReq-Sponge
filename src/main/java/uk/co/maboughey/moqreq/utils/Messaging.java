package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;

import java.util.List;
import java.util.UUID;

public class Messaging {

    public static void notifyModLogin(Player player) {
        //get amount of open mod requests
        int openCount = DBModRequest.getModCount(0, player.getUniqueId());
        int claimedCount = DBModRequest.getModCount(1, player.getUniqueId());

        if (openCount > 0) {
            Text clickableOpen = Text.builder("[Click to view]")
                    .color(TextColors.BLUE)
                    .onClick(TextActions.runCommand("/modreq open"))
                    .build();
            //more than one, lets send message
            player.sendMessage(colour("&6There are currently &F"+openCount+" &6open mod requests. ").concat(clickableOpen));
        }

        if (claimedCount > 0) {
            Text clickableClaimed = Text.builder("[Click to view]")
                    .color(TextColors.BLUE)
                    .onClick(TextActions.runCommand("/modreq claimed"))
                    .build();
            //Mod has more than one claimed modreq
            player.sendMessage(colour("&6You have &F"+claimedCount+"&6 Mod Requests assigned to you. ").concat(clickableClaimed));
        }
    }

    public static void notifyPlayerLogin(Player player) {
        //Get counts
        int openCount = DBModRequest.getCount(0, player.getUniqueId());
        int claimedCount = DBModRequest.getCount(1, player.getUniqueId());
        int closedUnseen = DBModRequest.getCount(2, player.getUniqueId());

        //Do we need to notify the player?
        if (openCount > 0 || claimedCount > 0 || closedUnseen > 0 ) {
            StringBuilder output = new StringBuilder();
            output.append("&6Your current mod requests: ");

            //Build the rest of the message
            if (openCount > 0)
                output.append("&F" + openCount + " &6Open, ");
            if (claimedCount > 0)
                output.append("&F" + claimedCount + " &6Processing, ");
            if (closedUnseen > 0)
                output.append("&F" + closedUnseen + " &6Unread replies, ");

            //End message
            Text clickable = Text.builder("[Click to view]")
                    .color(TextColors.BLUE)
                    .onClick(TextActions.runCommand("/modreq list"))
                    .build();

            //Send the message to the player
            player.sendMessage(colour(output.toString()).concat(clickable));
        }
    }
    public static void notifyModsNew() {
        List<Player> mods = ModReq.getMods();
        if (mods.size() == 0)
            return;

        for (Player player: mods) {
            Text linkText = Text.builder("Click to view")
                    .color(TextColors.BLUE)
                    .onClick(TextActions.runCommand("/modreq open"))
                    .build();

            player.sendMessage(colour("&6New mod request submitted. ").concat(linkText));
        }
    }
    public static void notifyPlayerComplete(UUID uuid) {
        Player player = ModReq.getPlayer(uuid);
        if (player != null) {
            Text linkText = Text.builder("Click to view")
                    .color(TextColors.BLUE)
                    .onClick(TextActions.runCommand("/modreq list"))
                    .build();
            player.sendMessage(colour("&A mod request you submitted has been completed ").concat(linkText));
        }
    }
    public static void errorMessage(Player player, String message) {
        player.sendMessage(colour("&4"+message));
    }

    public static void newModReqUser(Player player) {
        player.sendMessage(colour("&6Your mod request has been submitted"));
    }
    public static void sendMessage(CommandSource src, String message) {
        src.sendMessage(colour(message));
    }
    public static Text colour(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }


}
