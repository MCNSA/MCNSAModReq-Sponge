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
        CommandSpec modreqCommand = CommandSpec.builder()
                .description(Text.of("Submit a Mod Request"))
                .permission("modreq.request")
                .executor(new ModReqCommand())
                .arguments(
                        GenericArguments.allOf(GenericArguments.string(Text.of("message")))
                )
                .build();
        Sponge.getCommandManager().register(plugin, modreqCommand);
    }
}
