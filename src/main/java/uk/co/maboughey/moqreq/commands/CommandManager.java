package uk.co.maboughey.moqreq.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

public class CommandManager {

    private PluginContainer plugin;

    public CommandManager(PluginContainer plugin) {
        this.plugin = plugin;

        //Register commands
        modreqCommand();
    }

    public void modreqCommand() {
        CommandSpec modReqList = CommandSpec.builder()
                .description(Text.of("List open and unread Mod requests"))
                .permission("modreq.request")
                .executor(new ModReqListCommand())
                .build();
        CommandSpec modReqOpen = CommandSpec.builder()
                .description(Text.of("List open mod requests"))
                .permission("modreq.mod")
                .executor(new ModReqOpenCommand())
                .build();
        CommandSpec modReqClaimed = CommandSpec.builder()
                .description(Text.of("List claimed mod requests"))
                .permission("modreq.mod")
                .executor(new ModReqClaimedCommand())
                .build();
        CommandSpec modReqUnclaim = CommandSpec.builder()
                .description(Text.of("Unassign mod from request"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .executor(new modReqUnclaimCommand())
                .build();
        CommandSpec modReqClose = CommandSpec.builder()
                .description(Text.of("Close a mod request"))
                .permission("modreq.mod")
                .executor(new ModReqCloseCommand())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id"))),
                        GenericArguments.allOf(GenericArguments.string(Text.of("message")))
                )
                .build();
        CommandSpec modReqClaim = CommandSpec.builder()
                .description(Text.of("Claim a mod request"))
                .permission("modreq.mod")
                .executor(new ModReqClaimCommand())
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .build();
        CommandSpec modReqShowMessage = CommandSpec.builder()
                .description(Text.of("View a request's message"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .executor(new modReqShowMessage())
                .build();
        CommandSpec modReqTeleport = CommandSpec.builder()
                .description(Text.of("Teleport to request location"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .executor(new ModReqTeleportCommand())
                .build();
        CommandSpec modreqCommand = CommandSpec.builder()
                .description(Text.of("Submit a Mod Request"))
                .permission("modreq.request")
                .executor(new ModReqCommand())
                .child(modReqList, "list")
                .child(modReqOpen, "open")
                .child(modReqClaimed, "claimed")
                .child(modReqClose, "close")
                .child(modReqClaim, "claim")
                .child(modReqUnclaim, "unclaim")
                .child(modReqShowMessage, "showmessage")
                .child(modReqTeleport, "tp" , "teleport")
                .arguments(
                        GenericArguments.allOf(GenericArguments.string(Text.of("message")))
                )
                .build();
        Sponge.getCommandManager().register(plugin, modreqCommand, "modreq");
    }
}
