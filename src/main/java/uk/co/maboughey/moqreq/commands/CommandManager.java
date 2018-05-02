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
                .description(Text.of("Display your mod requests"))
                .permission("modreq.user")
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
                .executor(new ModReqUnclaimCommand())
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
        CommandSpec modReqTeleport = CommandSpec.builder()
                .description(Text.of("Teleport to request location"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .executor(new TeleportCommand())
                .build();
        CommandSpec modReqGet = CommandSpec.builder()
                .description(Text.of("View one Request"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id")))
                )
                .executor(new ModReqGetCommand())
                .build();
        CommandSpec modReqEscalate = CommandSpec.builder()
                .description(Text.of("Notify admins about a request"))
                .permission("modreq.mod")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("id"))),
                        GenericArguments.allOf(GenericArguments.string(Text.of("message")))
                )
                .executor(new ModReqEscalateCommand())
                .build();
        CommandSpec ModReqAdmin = CommandSpec.builder()
                .description(Text.of("Display escalated requests"))
                .permission("modreq.admin")
                .executor(new ModReqAdminCommand())
                .build();
        CommandSpec modReqMod = CommandSpec.builder()
                .description(Text.of("Mod commands"))
                .permission("modreq.mod")
                .executor(new ModReqOpenCommand())
                .child(modReqOpen, "open", "vo")
                .child(modReqClaimed, "claimed", "vc")
                .child(modReqClose, "close", "cl")
                .child(modReqClaim, "claim", "c")
                .child(modReqUnclaim, "unclaim", "uc")
                .child(modReqTeleport, "tp" , "teleport")
                .child(modReqEscalate, "escalate", "e")
                .child(modReqGet, "get", "g")
                .build();
        CommandSpec modreqCommand = CommandSpec.builder()
                .description(Text.of("Submit a Mod Request"))
                .permission("modreq.user")
                .executor(new ModReqCommand())
                .child(modReqList, "list")
                .child(modReqMod, "mod")
                .child(ModReqAdmin, "admin")
                .arguments(
                        GenericArguments.allOf(GenericArguments.string(Text.of("message")))
                )
                .build();
        Sponge.getCommandManager().register(plugin, modreqCommand, "modreq", "mr");
        Sponge.getCommandManager().register(plugin, modReqMod, "mrm");
        Sponge.getCommandManager().register(plugin, ModReqAdmin, "mra");
    }
}
