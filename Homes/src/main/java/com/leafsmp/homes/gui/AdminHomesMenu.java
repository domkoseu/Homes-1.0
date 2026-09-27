package com.leafsmp.homes.gui;

import com.leafsmp.homes.HomeStorage;
import com.leafsmp.homes.HomesPlugin;
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
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public final class AdminHomesMenu {

    private AdminHomesMenu() {
    }

    public static void open(HomesPlugin plugin, Player viewer, OfflinePlayer target) {
        HomeStorage storage = plugin.storage();
        String targetName = target.getName() != null ? target.getName() : "Unknown";

        List<ActionButton> buttons = new ArrayList<>();
        for (int index = HomeStorage.MIN_INDEX; index <= HomeStorage.MAX_INDEX; index++) {
            boolean set = storage.isSet(target.getUniqueId(), index);
            final int idx = index;

            if (set) {
                Location loc = storage.getHome(target.getUniqueId(), index);
                buttons.add(ActionButton.builder(Component.text("Home " + idx, NamedTextColor.GREEN))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                p.closeDialog();
                                p.teleport(loc);
                                p.sendActionBar(Component.text("Teleported to Home ", NamedTextColor.GREEN)
                                        .append(Component.text(idx, NamedTextColor.YELLOW))
                                        .append(Component.text(" of player " + targetName, NamedTextColor.GREEN)));
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build());
            } else {
                buttons.add(ActionButton.builder(Component.text("Home " + idx, NamedTextColor.GRAY))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                p.sendActionBar(Component.text("Home ", NamedTextColor.RED)
                                        .append(Component.text(idx, NamedTextColor.YELLOW))
                                        .append(Component.text(" is not set", NamedTextColor.RED)));
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build());
            }
        }

        buttons.add(ActionButton.builder(Component.text("Close", NamedTextColor.RED))
                .build());

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Admin: " + targetName))
                        .body(List.of(DialogBody.plainMessage(Component.text("Player homes.", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons).build()));

        viewer.showDialog(dialog);
    }
}
