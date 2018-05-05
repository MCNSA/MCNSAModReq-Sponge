package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import uk.co.maboughey.moqreq.ModReq;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.BookViewBuilder;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.ArrayList;
import java.util.List;

public class GetCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!ModReq.isEnabled) {
            Messaging.sendMessage(src, "&4Plugin is currently disabled");
            return CommandResult.success();
        }
        //Dont let commandblocks use the command
        if (src instanceof CommandBlockSource) {
            return CommandResult.success();
        }

        int id = args.<Integer>getOne(Text.of("id")).get();

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

        //Display
        if (src instanceof ConsoleSource) {
            Messaging.consoleRequest(request, src);
        }
        else {
                ((Player) src).sendBookView(BookViewBuilder.viewRequest(request, (Player) src));
        }
        return CommandResult.success();
    }
}
