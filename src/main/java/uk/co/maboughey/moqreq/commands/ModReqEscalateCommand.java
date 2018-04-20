package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.Discord;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.Collection;

public class ModReqEscalateCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!ModReq.isEnabled) {
            Messaging.sendMessage(src, "&4Plugin is currently disabled");
            return CommandResult.success();
        }

        //Get the details we need
        int id = args.<Integer>getOne(Text.of("id")).get();
        Collection<String> messageCollection = args.getAll("message");
        String message = String.join(" ", messageCollection);

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

        //Check the status
        if (request.status > 1) {
            //Closed or unread
            Messaging.sendMessage(src, "&4That request has already been closed");
            return CommandResult.success();
        }

        //Send a message to discord
        Discord.sendAdmin(message, request.id, request.message, src);

        //Send a message to the sender
        Messaging.sendMessage(src, "&6The admins have been notified about this request");
        return CommandResult.success();
    }
}
