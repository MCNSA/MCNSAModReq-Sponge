package uk.co.maboughey.moqreq.utils;

import fr.d0p1.hookscord.Hookscord;
import fr.d0p1.hookscord.utils.Message;
import org.spongepowered.api.command.CommandSource;

import java.io.IOException;
import java.net.MalformedURLException;

public class Discord {
    public static void sendMod(String message, CommandSource src) {
        try {
            Hookscord hk = new Hookscord(Configuration.DiscordModHook);
            Message msg = new Message("New Mod Request");
            msg.setText("**"+src.getName()+"** has submitted a new mod request on **"+Configuration.ServerName+"** with the text: *"+message+"*");
            hk.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void sendAdmin(String message, int id, String reqMessage, CommandSource src) {
        try {
            Hookscord hk = new Hookscord(Configuration.DiscordAdminHook);
            Message msg = new Message("Escalated Mod Request");
            msg.setText("**"+src.getName()+"** wants an admin to look at mod request on **"+Configuration.ServerName+"**\n" +
                    "id: "+id+"\nMessage: *"+message+"*\n" +
                    "Request message: *"+reqMessage+"*");
            hk.sendMessage(msg);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
