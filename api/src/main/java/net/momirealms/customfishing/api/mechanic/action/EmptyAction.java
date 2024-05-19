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

package net.momirealms.customfishing.api.mechanic.action;

import net.momirealms.customfishing.api.mechanic.context.Context;
import org.bukkit.entity.Player;

/**
 * An implementation of the Action interface that represents an empty action with no behavior.
 * This class serves as a default action to prevent NPE.
 */
public class EmptyAction implements Action<Player> {

    public static final EmptyAction INSTANCE = new EmptyAction();

    @Override
    public void trigger(Context<Player> context) {
    }
}
