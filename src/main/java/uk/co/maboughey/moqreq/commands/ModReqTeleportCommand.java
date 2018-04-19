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

import java.util.UUID;

public class ModReqTeleportCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Get sender's info
        UUID uuid = null;
        if (!(src instanceof Player)) {
            Messaging.sendMessage(src, "You have to be a player to use this command");
            return CommandResult.success();
        }
        Player player = (Player) src;
        uuid = player.getUniqueId();
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

        //Move the player
        request.teleport(player);

        //end of command
        return CommandResult.success();
    }
}
