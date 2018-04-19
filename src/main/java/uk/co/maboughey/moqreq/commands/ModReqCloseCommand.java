package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.Collection;
import java.util.UUID;

public class ModReqCloseCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Get sender's info
        UUID uuid = null;
        if (src instanceof CommandBlockSource) {
            return CommandResult.success();
        }
        else if (src instanceof Player) {
            uuid = ((Player)src).getUniqueId();
        }

        int id = args.<Integer>getOne(Text.of("id")).get();
        Collection<String> messageArgs = args.getAll(Text.of("message"));
        String message = String.join(" ", messageArgs);

        //Make sure the id is valid
        if (!(id >0)) {
            Messaging.sendMessage(src, "&4Invalid request id");
            return CommandResult.success();
        }

        //Try and get the Request
        ModRequest request = DBModRequest.getRequest(id);

        //Does the id exist?
        if (request == null) {
            Messaging.sendMessage(src, "&4Invalid request id");
            return CommandResult.success();
        }

        //Lets fill out the details
        request.responder = uuid;
        request.response = message;
        request.status = 2;

        //Save the request
        DBModRequest.updateRequestDone(request);

        //Tell the user
        Messaging.sendMessage(src, "You have closed request: "+id);

        //TODO: Notify player, if online, that their request has been completed

        //end of command
        return CommandResult.success();
    }
}
