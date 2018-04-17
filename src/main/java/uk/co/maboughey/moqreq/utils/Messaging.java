package uk.co.maboughey.moqreq.utils;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class Messaging {

    public static void notifyModLogin(Player player) {
        //get amount of open mod requests
        int count = 0; // placeholder TODO: get amount open

        if (count > 0) {
            //more than one, lets send message
            player.sendMessage(colour("&6There are currently &F"+count+" &6open mod requests."));
        }
    }

    public static void notifyPlayerLogin(Player player) {
        StringBuilder output = new StringBuilder();
        output.append("&6Your current mod requests: ");

        //Get counts
        int openCount = 0; //placeholder TODO: get open amount
        int claimedCount = 0; //placeholder TODO: get claimed amount
        int closedUnseen = 0; //Placeholder TODO: get closed & unseen amount

        if (openCount > 0 || claimedCount > 0 || closedUnseen > 0 ) {
            //Build the rest of the message
            if (openCount > 0)
                output.append("&F" + openCount + " &6Open, ");
            if (claimedCount > 0)
                output.append("&F" + claimedCount + " &6Processing, ");
            if (closedUnseen > 0)
                output.append("&F" + closedUnseen + " &6Replies, ");

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
