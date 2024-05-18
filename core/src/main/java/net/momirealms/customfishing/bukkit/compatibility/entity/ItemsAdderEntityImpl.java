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

package net.momirealms.customfishing.bukkit.compatibility.entity;

import dev.lone.itemsadder.api.CustomEntity;
import net.momirealms.customfishing.api.integration.EntityProvider;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class ItemsAdderEntityImpl implements EntityProvider {

    @Override
    public String identifier() {
        return "vanilla";
    }

    @NotNull
    @Override
    public Entity spawn(@NotNull Location location, @NotNull String id, @NotNull Map<String, Object> propertyMap) {
        CustomEntity customEntity = CustomEntity.spawn(
                id,
                location,
                (Boolean) propertyMap.getOrDefault("frustumCulling", true),
                (Boolean) propertyMap.getOrDefault("noBase", false),
                (Boolean) propertyMap.getOrDefault("noHitbox", false)
        );
        return customEntity.getEntity();
    }
}