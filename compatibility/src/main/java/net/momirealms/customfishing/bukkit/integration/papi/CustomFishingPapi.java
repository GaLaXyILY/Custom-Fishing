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

package net.momirealms.customfishing.bukkit.integration.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.momirealms.customfishing.api.BukkitCustomFishingPlugin;
import net.momirealms.customfishing.api.mechanic.context.Context;
import net.momirealms.customfishing.api.storage.user.UserData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CustomFishingPapi extends PlaceholderExpansion {

    private final BukkitCustomFishingPlugin plugin;

    public CustomFishingPapi(BukkitCustomFishingPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        super.register();
    }

    public void unload() {
        super.unregister();
    }

    @Override
    public @NotNull String getIdentifier() {
        return "customfishing";
    }

    @Override
    public @NotNull String getAuthor() {
        return "XiaoMoMi";
    }

    @Override
    public @NotNull String getVersion() {
        return "2.2";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer offlinePlayer, @NotNull String params) {
        String[] split = params.split("_");
        Player player = offlinePlayer.getPlayer();
        if (player == null)
            return "";
        switch (split[0]) {
            case "market" -> {
                if (split.length < 2)
                    return null;
                switch (split[1]) {
                    case "limit" -> {
                        if (split.length < 3) {
                            return String.format("%.2f", plugin.getMarketManager().earningLimit(Context.player(player)));
                        } else {
                            Player another = Bukkit.getPlayer(split[2]);
                            if (another == null) {
                                return "";
                            }
                            return String.format("%.2f", plugin.getMarketManager().earningLimit(Context.player(another)));
                        }
                    }
                    case "earnings" -> {
                        UserData user;
                        if (split.length < 3) {
                            user = plugin.getStorageManager().getOnlineUser(player.getUniqueId()).orElse(null);
                        } else {
                            Player another = Bukkit.getPlayer(split[2]);
                            if (another == null) {
                                return "";
                            }
                            user = plugin.getStorageManager().getOnlineUser(another.getUniqueId()).orElse(null);
                        }
                        if (user == null)
                            return "";
                        return String.format("%.2f", user.earningData().earnings());
                    }
                    case "canearn" -> {
                        if (split.length < 3) {
                            UserData user = plugin.getStorageManager().getOnlineUser(player.getUniqueId()).orElse(null);
                            if (user == null)
                                return "";
                            return String.format("%.2f", plugin.getMarketManager().earningLimit(Context.player(player)) - user.earningData().earnings());
                        } else {
                            Player another = Bukkit.getPlayer(split[2]);
                            if (another == null) {
                                return "";
                            }
                            UserData user = plugin.getStorageManager().getOnlineUser(another.getUniqueId()).orElse(null);
                            if (user == null)
                                return "";
                            return String.format("%.2f", plugin.getMarketManager().earningLimit(Context.player(another)) - user.earningData().earnings());
                        }
                    }
                }
            }
        }
        return null;
    }
}
