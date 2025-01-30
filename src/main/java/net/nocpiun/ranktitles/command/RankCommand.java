package net.nocpiun.ranktitles.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.nocpiun.ranktitles.RankTitlesPlugin;
import net.nocpiun.ranktitles.title.Title;
import net.nocpiun.ranktitles.utils.Message;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class RankCommand implements Command<ServerCommandSource> {
    public static final String cmd = "rank";

    private final RankTitlesPlugin plugin;

    public RankCommand(CommandDispatcher<ServerCommandSource> dispatcher, RankTitlesPlugin plugin) {
        final RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

        final LiteralCommandNode<ServerCommandSource> command = literal(cmd)
                .executes(this)
                .build();

        final ArgumentCommandNode<ServerCommandSource, String> args = argument("operation", string())
                .suggests((context, builder) -> {
                    builder = builder.createOffset(builder.getInput().lastIndexOf(" ") + 1);
                    String[] inputs = context.getInput().split(" ");

                    if(inputs.length == 1 || inputs.length == 2) {
                        builder.suggest("list"); // /rank list
                        builder.suggest("create"); // /rank create <id> <displayName>
                        builder.suggest("remove"); // /rank remove <id>

                        return builder.buildFuture();
                    }

                    return new CompletableFuture<>();
                })
                .then(
                        argument("id", string())
                                .suggests((context, builder) -> {
                                    builder = builder.createOffset(builder.getInput().lastIndexOf(" ") + 1);
                                    String[] inputs = context.getInput().split(" ");

                                    if(inputs[1].equals("remove")) {
                                        for(Title title : plugin.getTitles()) {
                                            builder.suggest(title.getId());
                                        }
                                    }

                                    return builder.buildFuture();
                                })
                                .then(
                                        argument("name", string())
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

        if(!source.hasPermissionLevel(4)) {
            return 0;
        }

        switch(inputs[1].toLowerCase()) {
            case "list" -> {
                ArrayList<Title> list = plugin.getTitles();
                source.sendMessage(Message.formatted("Created titles:"));
                for(Title title : list) {
                    source.sendMessage(Message.colorize("["+ title.getTitleString() +"&r] id: "+ title.getId()));
                }
            }
            case "create" -> {
                if(inputs.length < 4) return 0;
                final String id = inputs[2];
                final String name = inputs[3].replaceAll("\"", "");

                if(plugin.isTitleExisted(id)) {
                    source.sendMessage(Message.formatted("The id of the title is existed."));
                    return 0;
                }

                Title title = plugin.createTitle(id, name);
                source.sendMessage(Message.formatted("Successfully created the title ["+ title.getTitleString() +"&r]."));
            }
            case "remove" -> {
                if(inputs.length < 3) return 0;
                if(!plugin.isTitleExisted(inputs[2])) {
                    source.sendMessage(Message.formatted("Cannot find the specified title to remove."));
                    return 0;
                }

                plugin.removeTitle(inputs[2]);
                source.sendMessage(Message.formatted("Successfully removed the title."));
            }
        }

        return 1;
    }
}
