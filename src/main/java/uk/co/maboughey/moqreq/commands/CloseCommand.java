package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.Collection;
import java.util.UUID;

public class ModReqCloseCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!ModReq.isEnabled) {
            Messaging.sendMessage(src, "&4Plugin is currently disabled");
            return CommandResult.success();
        }
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

        //Don't override already closed commands
        if (request.status > 1) {
            Messaging.sendMessage(src, "&4This request is already closed.");
            return CommandResult.success();
        }

        //Check if escalated, if so check its an admin
        if (request.escalated && !src.hasPermission("modreq.admin")) {
            Messaging.sendMessage(src, "&4This request has been escalated. You cannot close it");
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

        //Tell the player who submitted the modrequest
        Messaging.notifyPlayerComplete(request.user);

        //end of command
        return CommandResult.success();
    }
}
