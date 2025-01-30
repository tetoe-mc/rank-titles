package net.nocpiun.ranktitles.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.nocpiun.ranktitles.RankTitlesPlugin;
import net.nocpiun.ranktitles.title.Title;
import net.nocpiun.ranktitles.utils.Message;

import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class TitlesCommand implements Command<ServerCommandSource> {
    public static final String cmd = "titles";

    private final RankTitlesPlugin plugin;

    public TitlesCommand(CommandDispatcher<ServerCommandSource> dispatcher, RankTitlesPlugin plugin) {
        final RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

        final LiteralCommandNode<ServerCommandSource> command = literal(cmd)
                .executes(this)
                .build();

        final ArgumentCommandNode<ServerCommandSource, String> args = argument("operation", string())
                .suggests((context, builder) -> {
                    builder = builder.createOffset(builder.getInput().lastIndexOf(" ") + 1);
                    String[] inputs = context.getInput().split(" ");

                    if(inputs.length == 1 || inputs.length == 2) {
                        builder.suggest("give");
                        builder.suggest("deprive");

                        return builder.buildFuture();
                    }

                    return new CompletableFuture<>();
                })
                .then(
                        argument("player", string())
                                .then(
                                        argument("title", string())
                                                .suggests((context, builder) -> {
                                                    builder = builder.createOffset(builder.getInput().lastIndexOf(" ") + 1);

                                                    for(Title title : plugin.getTitles()) {
                                                        builder.suggest(title.getId());
                                                    }

                                                    return builder.buildFuture();
                                                })
                                                .executes(this)
                                                .build()
                                )
                                .executes(this)
                                .build()
                )
                .executes(this)
                .build();

        command.addChild(args);
        root.addChild(command);

        this.plugin = plugin;
    }

    @Override
    public int run(CommandContext<ServerCommandSource> ctx) {
        ServerCommandSource source = ctx.getSource();
        String[] inputs = ctx.getInput().split(" ");

        if(!source.hasPermissionLevel(4) || inputs.length < 4) {
            return 0;
        }

        ServerPlayerEntity player = plugin.getServer().getPlayerManager().getPlayer(inputs[2]);
        String playerName = player.getName().getString();
        Title title = plugin.getTitle(inputs[3]);

        if(title == null) {
            source.sendMessage(Message.formatted("Cannot find the specified title."));
            return 0;
        }

        switch(inputs[1].toLowerCase()) {
            case "give" -> {
                if(title.getPlayerUUIDList().contains(player.getUuidAsString())) {
                    source.sendMessage(Message.formatted(playerName +" has had the title ["+ title.getTitleString() +"&r]"));
                    return 0;
                }

                plugin.giveTitleToPlayer(player, title.getId());
                source.sendMessage(Message.formatted("Title ["+ title.getTitleString() +"&r] is given to "+ playerName));
            }
            case "deprive" -> {
                if(!title.getPlayerUUIDList().contains(player.getUuidAsString())) {
                    source.sendMessage(Message.formatted(playerName +" doesn't have the title ["+ title.getTitleString() +"&r]"));
                    return 0;
                }

                plugin.depriveTitleFromPlayer(player, title.getId());
                source.sendMessage(Message.formatted("Title ["+ title.getTitleString() +"&r] is deprived from "+ playerName));
            }
        }

        return 1;
    }
}
