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
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Equivalent of openHomesMenu(p): one button per slot (1-10, permission
 * gated for 6-10), a set-on-click for empty slots, opens the manage menu
 * for slots that already have a home.
 */
public final class HomesMenu {

    private HomesMenu() {
    }

    public static void open(HomesPlugin plugin, Player player) {
        List<ActionButton> buttons = new ArrayList<>();
        HomeStorage storage = plugin.storage();

        for (int index = HomeStorage.MIN_INDEX; index <= HomeStorage.MAX_INDEX; index++) {
            if (index > HomeStorage.FREE_MAX_INDEX && !player.hasPermission("homes.homes." + index)) {
                continue;
            }

            boolean set = storage.isSet(player.getUniqueId(), index);
            final int idx = index;

            if (set) {
                buttons.add(ActionButton.builder(Component.text("Home " + idx, NamedTextColor.GREEN))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                HomeManageMenu.open(plugin, p, idx);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build());
            } else {
                buttons.add(ActionButton.builder(Component.text("Home " + idx, NamedTextColor.WHITE))
                        .action(DialogAction.customClick((view, audience) -> {
                            if (audience instanceof Player p) {
                                storage.setHome(p.getUniqueId(), idx, p.getLocation());
                                p.sendActionBar(Component.text("Home ", NamedTextColor.GREEN)
                                        .append(Component.text(idx, NamedTextColor.YELLOW))
                                        .append(Component.text(" has been set", NamedTextColor.GREEN)));
                                open(plugin, p);
                            }
                        }, ClickCallback.Options.builder().build()))
                        .build());
            }
        }

        buttons.add(ActionButton.builder(Component.text("Close", NamedTextColor.RED))
                .build());

        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(DialogBase.builder(Component.text("Homes"))
                        .body(List.of(DialogBody.plainMessage(Component.text("Your homes:", NamedTextColor.GRAY))))
                        .build())
                .type(DialogType.multiAction(buttons).build()));

        player.showDialog(dialog);
    }
}
