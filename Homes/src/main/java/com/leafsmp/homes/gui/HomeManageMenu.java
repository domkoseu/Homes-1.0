package com.leafsmp.homes.gui;

import com.leafsmp.homes.HomesPlugin;
import com.leafsmp.homes.TeleportTask;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public final class HomeManageMenu {

    private HomeManageMenu() {
    }

    public static void open(HomesPlugin plugin, Player player, int index) {
        Location loc = plugin.storage().getHome(player.getUniqueId(), index);

        List<ActionButton> buttons = List.of(
                ActionButton.builder(Component.text("Teleport", NamedTextColor.GREEN))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                p.closeDialog();
                                TeleportTask.start(plugin, p, loc, index);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build(),
                ActionButton.builder(Component.text("Delete", NamedTextColor.RED))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                confirmDelete(plugin, p, index);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build(),
                ActionButton.builder(Component.text("Close", NamedTextColor.GRAY))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                HomesMenu.open(plugin, p);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build()
        );

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Home " + index))
                        .body(List.of(DialogBody.plainMessage(Component.text("What do you want to do?", NamedTextColor.WHITE))))
                        .build())
                .type(DialogType.multiAction(buttons).build()));

        player.showDialog(dialog);
    }

    private static void confirmDelete(HomesPlugin plugin, Player player, int index) {
        List<ActionButton> buttons = List.of(
                ActionButton.builder(Component.text("Yes, delete", NamedTextColor.RED))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                plugin.storage().deleteHome(p.getUniqueId(), index);
                                p.sendActionBar(Component.text("Home ", NamedTextColor.RED)
                                        .append(Component.text(index, NamedTextColor.YELLOW))
                                        .append(Component.text(" has been deleted", NamedTextColor.RED)));
                                HomesMenu.open(plugin, p);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build(),
                ActionButton.builder(Component.text("Cancel", NamedTextColor.GRAY))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                HomesMenu.open(plugin, p);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build()
        );

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Delete Home " + index + "?", NamedTextColor.RED))
                        .body(List.of(DialogBody.plainMessage(Component.text("The slot will be empty.", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons).build()));

        player.showDialog(dialog);
    }
}
