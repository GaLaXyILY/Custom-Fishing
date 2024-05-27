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

package net.momirealms.customfishing.api.integration;

import net.momirealms.customfishing.common.plugin.feature.Reloadable;
import net.momirealms.customfishing.common.util.Pair;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Interface for managing integration providers in the custom fishing API.
 * This allows for the registration and retrieval of various types of providers
 * such as Leveler, Enchantment, and Season providers.
 */
public interface IntegrationManager extends Reloadable {

    /**
     * Registers a LevelerProvider.
     *
     * @param levelerProvider the LevelerProvider to register
     * @return true if registration is successful, false otherwise
     */
    boolean registerLevelerProvider(@NotNull LevelerProvider levelerProvider);

    /**
     * Unregisters a LevelerProvider by its ID.
     *
     * @param id the ID of the LevelerProvider to unregister
     * @return true if unregistration is successful, false otherwise
     */
    boolean unregisterLevelerProvider(@NotNull String id);

    /**
     * Registers an EnchantmentProvider.
     *
     * @param enchantmentProvider the EnchantmentProvider to register
     * @return true if registration is successful, false otherwise
     */
    boolean registerEnchantmentProvider(@NotNull EnchantmentProvider enchantmentProvider);

    /**
     * Unregisters an EnchantmentProvider by its ID.
     *
     * @param id the ID of the EnchantmentProvider to unregister
     * @return true if unregistration is successful, false otherwise
     */
    boolean unregisterEnchantmentProvider(@NotNull String id);

    /**
     * Registers a SeasonProvider.
     *
     * @param seasonProvider the SeasonProvider to register
     * @return true if registration is successful, false otherwise
     */
    boolean registerSeasonProvider(@NotNull SeasonProvider seasonProvider);

    /**
     * Unregisters the SeasonProvider.
     *
     * @return true if unregistration is successful, false otherwise
     */
    boolean unregisterSeasonProvider();

    boolean registerEntityProvider(@NotNull EntityProvider entityProvider);

    boolean unregisterEntityProvider(@NotNull String id);

    /**
     * Retrieves a registered LevelerProvider by its ID.
     *
     * @param id the ID of the LevelerProvider to retrieve
     * @return the LevelerProvider if found, or null if not found
     */
    @Nullable
    LevelerProvider getLevelerProvider(String id);

    /**
     * Retrieves a registered EnchantmentProvider by its ID.
     *
     * @param id the ID of the EnchantmentProvider to retrieve
     * @return the EnchantmentProvider if found, or null if not found
     */
    @Nullable
    EnchantmentProvider getEnchantmentProvider(String id);

    /**
     * Retrieves a registered SeasonProvider by its ID.
     *
     * @return the SeasonProvider if found, or null if not found
     */
    @Nullable
    SeasonProvider getSeasonProvider();

    List<Pair<String, Short>> getEnchantments(ItemStack itemStack);

    boolean registerItemProvider(@NotNull ItemProvider itemProvider);

    boolean unregisterItemProvider(@NotNull String id);

    boolean registerBlockProvider(@NotNull BlockProvider block);

    boolean unregisterBlockProvider(@NotNull String id);
}
