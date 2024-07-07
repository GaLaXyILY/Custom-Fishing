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

package net.momirealms.customfishing.api.mechanic.context;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

/**
 * The Context interface represents a generic context for custom fishing mechanics.
 * It allows for storing and retrieving arguments, as well as getting the holder
 * of the context. This can be used to maintain state or pass parameters within
 * the custom fishing mechanics.
 *
 * @param <T> the type of the holder object for this context
 */
public interface Context<T> {

    /**
     * Retrieves the map of arguments associated with this context.
     *
     * @return a map where the keys are argument names and the values are argument values.
     */
    Map<ContextKeys<?>, Object> args();

    /**
     * Converts the context to a map of placeholders
     *
     * @return a map of placeholders
     */
    Map<String, String> placeholderMap();

    /**
     * Adds or updates an argument in the context.
     * This method allows adding a new argument or updating the value of an existing argument.
     *
     * @param <C>   the type of the value being added to the context.
     * @param key   the ContextKeys key representing the argument to be added or updated.
     * @param value the value to be associated with the specified key.
     * @return the current context instance, allowing for method chaining.
     */
    <C> Context<T> arg(ContextKeys<C> key, C value);

    /**
     * Combines one context with another
     *
     * @param other other
     * @return this context
     */
    Context<T> combine(Context<T> other);

    /**
     * Retrieves the value of a specific argument from the context.
     * This method fetches the value associated with the specified ContextKeys key.
     *
     * @param <C> the type of the value being retrieved.
     * @param key the ContextKeys key representing the argument to be retrieved.
     * @return the value associated with the specified key, or null if the key does not exist.
     */
    @Nullable
    <C> C arg(ContextKeys<C> key);

    @Nullable
    <C> C remove(ContextKeys<C> key);

    /**
     * Gets the holder of this context.
     *
     * @return the holder object of type T.
     */
    T getHolder();

    /**
     * Creates a player-specific context.
     *
     * @param player the player to be used as the holder of the context.
     * @return a new Context instance with the specified player as the holder.
     */
    static Context<Player> player(@Nullable Player player) {
        return new PlayerContextImpl(player, false);
    }

    static Context<Player> player(@Nullable Player player, boolean threadSafe) {
        return new PlayerContextImpl(player, threadSafe);
    }
}
