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

package net.momirealms.customfishing.mechanic.hook;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import net.momirealms.customfishing.api.CustomFishingPlugin;
import net.momirealms.customfishing.api.manager.HookManager;
import net.momirealms.customfishing.api.manager.RequirementManager;
import net.momirealms.customfishing.api.mechanic.condition.Condition;
import net.momirealms.customfishing.api.mechanic.effect.EffectCarrier;
import net.momirealms.customfishing.api.mechanic.hook.HookSetting;
import net.momirealms.customfishing.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public class HookManagerImpl implements Listener, HookManager {

    private final CustomFishingPlugin plugin;
    private final HashMap<String, HookSetting> hookSettingMap;

    public HookManagerImpl(CustomFishingPlugin plugin) {
        this.plugin = plugin;
        this.hookSettingMap = new HashMap<>();
    }

    public void load() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        loadConfig();
    }

    public void unload() {
        HandlerList.unregisterAll(this);
        hookSettingMap.clear();
    }

    public void disable() {
        unload();
    }

    @SuppressWarnings("DuplicatedCode")
    private void loadConfig() {
        Deque<File> fileDeque = new ArrayDeque<>();
        for (String type : List.of("hook")) {
            File typeFolder = new File(plugin.getDataFolder() + File.separator + "contents" + File.separator + type);
            if (!typeFolder.exists()) {
                if (!typeFolder.mkdirs()) return;
                plugin.saveResource("contents" + File.separator + type + File.separator + "default.yml", false);
            }
            fileDeque.push(typeFolder);
            while (!fileDeque.isEmpty()) {
                File file = fileDeque.pop();
                File[] files = file.listFiles();
                if (files == null) continue;
                for (File subFile : files) {
                    if (subFile.isDirectory()) {
                        fileDeque.push(subFile);
                    } else if (subFile.isFile() && subFile.getName().endsWith(".yml")) {
                        this.loadSingleFile(subFile);
                    }
                }
            }
        }
    }

    private void loadSingleFile(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        for (Map.Entry<String, Object> entry : config.getValues(false).entrySet()) {
            if (entry.getValue() instanceof ConfigurationSection section) {
                var setting = new HookSetting.Builder(entry.getKey())
                        .durability(section.getInt("max-durability", 16))
                        .lore(section.getStringList("lore-on-rod").stream().map(it -> "<!i>" + it).toList())
                        .build();
                hookSettingMap.put(entry.getKey(), setting);
            }
        }
    }

    @Nullable
    @Override
    public HookSetting getHookSetting(String id) {
        return hookSettingMap.get(id);
    }

    @EventHandler
    public void onDragDrop(InventoryClickEvent event) {
        if (event.isCancelled())
            return;
        final Player player = (Player) event.getWhoClicked();
        if (event.getClickedInventory() != player.getInventory())
            return;
        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() != Material.FISHING_ROD)
            return;
        if (player.getGameMode() != GameMode.SURVIVAL)
            return;

        ItemStack cursor = event.getCursor();
        if (cursor == null || cursor.getType() == Material.AIR) {
            if (event.getClick() == ClickType.RIGHT) {
                if (plugin.getFishingManager().hasPlayerCastHook(player.getUniqueId())) {
                    return;
                }

                NBTItem nbtItem = new NBTItem(clicked);
                NBTCompound cfCompound = nbtItem.getCompound("CustomFishing");
                if (cfCompound == null)
                    return;
                if (cfCompound.hasTag("hook_id")) {
                    event.setCancelled(true);
                    ItemStack hook = cfCompound.getItemStack("hook_item");
                    ItemUtils.setDurability(hook, cfCompound.getInteger("hook_dur"), true);
                    cfCompound.removeKey("hook_id");
                    cfCompound.removeKey("hook_item");
                    cfCompound.removeKey("hook_dur");
                    event.setCursor(hook);
                    ItemUtils.updateNBTItemLore(nbtItem);
                    clicked.setItemMeta(nbtItem.getItem().getItemMeta());
                }
            }
            return;
        }

        String hookID = plugin.getItemManager().getAnyItemID(cursor);
        HookSetting setting = getHookSetting(hookID);
        if (setting == null)
            return;

        Condition condition = new Condition(player, new HashMap<>());
        condition.insertArg("{rod}", plugin.getItemManager().getAnyItemID(clicked));
        EffectCarrier effectCarrier = plugin.getEffectManager().getEffect("hook", hookID);
        if (effectCarrier != null) {
            if (!RequirementManager.isRequirementsMet(effectCarrier.getRequirements(), condition)) {
                return;
            }
        }

        event.setCancelled(true);

        NBTItem rodNBTItem = new NBTItem(clicked);
        NBTCompound cfCompound = rodNBTItem.getOrCreateCompound("CustomFishing");
        String previousHookID = cfCompound.getString("hook_id");

        ItemStack clonedHook = cursor.clone();
        clonedHook.setAmount(1);
        cursor.setAmount(cursor.getAmount() - 1);

        if (previousHookID != null && !previousHookID.equals("")) {
            int previousHookDurability = cfCompound.getInteger("hook_dur");
            ItemStack previousItemStack = cfCompound.getItemStack("hook_item");
            ItemUtils.setDurability(previousItemStack, previousHookDurability, true);
            if (cursor.getAmount() == 0) {
                event.setCursor(previousItemStack);
            } else {
                ItemUtils.giveCertainAmountOfItem(player, previousItemStack, 1);
            }
        }

        cfCompound.setString("hook_id", hookID);
        cfCompound.setItemStack("hook_item", clonedHook);
        cfCompound.setInteger("hook_dur", ItemUtils.getDurability(clonedHook));

        ItemUtils.updateNBTItemLore(rodNBTItem);
        clicked.setItemMeta(rodNBTItem.getItem().getItemMeta());
    }
}