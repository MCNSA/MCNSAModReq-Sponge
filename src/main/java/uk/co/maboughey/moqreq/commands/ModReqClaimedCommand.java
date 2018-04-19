package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.source.CommandBlockSource;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import uk.co.maboughey.moqreq.database.DBModRequest;
import uk.co.maboughey.moqreq.type.ModRequest;
import uk.co.maboughey.moqreq.utils.BookViewBuilder;
import uk.co.maboughey.moqreq.utils.Messaging;

import java.util.List;

public class ModReqClaimedCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Check it isnt commandBlock
        if (src instanceof CommandBlockSource) { return CommandResult.success(); }

        //Get the mod requests
        List<ModRequest> requests = DBModRequest.getRequests(1);

        //Check if there is any
        if (requests.size() < 1) {
            //No requests. Lets tell them and finish
            Messaging.sendMessage(src, "&6You have no claimed mod requests");
            return CommandResult.success();
        }

        //Display to sender
        if (src instanceof ConsoleSource) {
            //TODO: Console output
        }
        else if (src instanceof Player) {
            //Player Source
            ((Player) src).sendBookView(BookViewBuilder.viewRequests(requests, 1));
        }

        return CommandResult.success();
    }
}