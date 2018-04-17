package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class ModReqCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Check its actually a player
        if (!(src instanceof Player)) {
            Messaging.sendMessage(src, "&4 You have to be a player to use this command");
            return CommandResult.success();
        }

        //Get the arguments
        Collection<String> messageCollection = args.getAll("message");

        //Check there is a message
        if (messageCollection.size() < 1) {
            Messaging.errorMessage((Player) src, "Please include a message in the mod request");
        }

        //join up the collection to one string
        String message = String.join(" ", messageCollection);

        //Get the UUID
        UUID userUUID = ((Player) src).getUniqueId();

        //Create new modreq
        ModRequest request = new ModRequest();
        request.user = userUUID;
        request.message = message;
        request.location = ((Player) src).getLocation();
        request.rotation = ((Player) src).getRotation();

        DBModRequest.saveNewRequest(request);

        //notify mods
        List<Player> mods = ModReq.getMods();
        for (int i = 0; i < mods.size(); i++)  {
            Messaging.newModReqMod(mods.get(i));
        }
        //Tell the user
        Messaging.newModReqUser((Player)src);

        return CommandResult.success();
    }
}
