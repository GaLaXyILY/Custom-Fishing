/*
 *  Copyright (C) <2022> <XiaoMoMi>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.momirealms.customfishing.gui.icon.property.loot;

import net.momirealms.customfishing.bukkit.adventure.ShadedAdventureComponentWrapper;
import net.momirealms.customfishing.gui.SectionPage;
import net.momirealms.customfishing.gui.page.property.ScoreEditor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;

public class ScoreItem extends AbstractItem {

    private final SectionPage itemPage;

    public ScoreItem(SectionPage itemPage) {
        this.itemPage = itemPage;
    }

    @Override
    public ItemProvider getItemProvider() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.NETHER_STAR)
                .setDisplayName(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                        CFLocale.GUI_LOOT_SCORE
                )));
        if (itemPage.getSection().contains("score")) {
            itemBuilder.addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                            CFLocale.GUI_CURRENT_VALUE + itemPage.getSection().getDouble("score")
                    )))
                    .addLoreLines("");
            itemBuilder.addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                    CFLocale.GUI_LEFT_CLICK_EDIT
            ))).addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                    CFLocale.GUI_RIGHT_CLICK_RESET
            )));
        } else {
            itemBuilder.addLoreLines(new ShadedAdventureComponentWrapper(AdventureHelper.getInstance().getComponentFromMiniMessage(
                    CFLocale.GUI_LEFT_CLICK_EDIT
            )));
        }
        return itemBuilder;
    }

    @Override
    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent event) {
        if (clickType.isLeftClick()) {
            new ScoreEditor(player, itemPage);
        } else if (clickType.isRightClick()) {
            itemPage.getSection().set("score", null);
            itemPage.save();
            itemPage.reOpen();
        }
    }
}
