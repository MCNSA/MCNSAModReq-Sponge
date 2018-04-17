package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import uk.co.maboughey.moqreq.database.DBModRequest;

public class Messaging {

    public static void notifyModLogin(Player player) {
        //get amount of open mod requests
        int openCount = DBModRequest.getModCount(0, player.getUniqueId());
        int claimedCount = DBModRequest.getModCount(1, player.getUniqueId());

        if (openCount > 0) {
            //more than one, lets send message
            player.sendMessage(colour("&6There are currently &F"+openCount+" &6open mod requests."));
        }

        if (claimedCount > 0) {
            //Mod has more than one claimed modreq
            player.sendMessage(colour("&6You have &F"+claimedCount+"&6 Mod Requests assigned to you"));
        }
    }

    public static void notifyPlayerLogin(Player player) {
        StringBuilder output = new StringBuilder();
        output.append("&6Your current mod requests: ");

        //Get counts
        int openCount = DBModRequest.getCount(0, player.getUniqueId());
        int claimedCount = DBModRequest.getCount(1, player.getUniqueId());
        int closedUnseen = DBModRequest.getCount(2, player.getUniqueId());

        if (openCount > 0 || claimedCount > 0 || closedUnseen > 0 ) {
            //Build the rest of the message
            if (openCount > 0)
                output.append("&F" + openCount + " &6Open, ");
            if (claimedCount > 0)
                output.append("&F" + claimedCount + " &6Processing, ");
            if (closedUnseen > 0)
                output.append("&F" + closedUnseen + " &6 Unread replies, ");

            //End message
            output.append("Please use /modreq list to view");


        }
        else {
            output.append("You currently have none that require your attention");
        }

        //Send the message to the player
        player.sendMessage(colour(output.toString()));
    }

    public static Text colour(String string) {
        return TextSerializers.FORMATTING_CODE.deserialize(string);
    }
}
