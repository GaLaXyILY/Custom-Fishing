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

package net.momirealms.customfishing.bukkit.integration.enchant;

import net.momirealms.customfishing.api.integration.EnchantmentProvider;
import net.momirealms.customfishing.common.util.Pair;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VanillaEnchantmentsProvider implements EnchantmentProvider {

    @Override
    public String identifier() {
        return "vanilla";
    }

    @Override
    public List<Pair<String, Short>> getEnchants(@NotNull ItemStack itemStack) {
        Map<Enchantment, Integer> enchantments = itemStack.getEnchantments();
        List<Pair<String, Short>> enchants = new ArrayList<>(enchantments.size());
        for (Map.Entry<Enchantment, Integer> en : enchantments.entrySet()) {
            enchants.add(Pair.of(en.getKey().getKey().toString(), en.getValue().shortValue()));
        }
        return enchants;
    }
}