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

import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.momirealms.customfishing.api.mechanic.context.Context;
import net.momirealms.customfishing.common.plugin.feature.Reloadable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * The ActionManager interface manages custom action types and provides methods for handling actions.
 *
 * @param <T> the type of the context in which the actions are triggered.
 */
public interface ActionManager<T> extends Reloadable {

    /**
     * Registers a custom action type with its corresponding factory.
     *
     * @param type           The type identifier of the action.
     * @param actionFactory  The factory responsible for creating instances of the action.
     * @return True if registration was successful, false if the type is already registered.
     */
    boolean registerAction(String type, ActionFactory<T> actionFactory);

    /**
     * Unregisters a custom action type.
     *
     * @param type The type identifier of the action to unregister.
     * @return True if unregistration was successful, false if the type is not registered.
     */
    boolean unregisterAction(String type);

    /**
     * Checks if an action type is registered.
     *
     * @param type The type identifier of the action.
     * @return True if the action type is registered, otherwise false.
     */
    boolean hasAction(@NotNull String type);

    /**
     * Retrieves the action factory for the specified action type.
     *
     * @param type The type identifier of the action.
     * @return The action factory for the specified type, or null if no factory is found.
     */
    @Nullable
    ActionFactory<T> getActionFactory(@NotNull String type);

    /**
     * Parses an action from a configuration section.
     *
     * @param section The configuration section containing the action definition.
     * @return The parsed action.
     */
    Action<T> parseAction(Section section);

    /**
     * Parses an array of actions from a configuration section.
     *
     * @param section The configuration section containing the action definitions.
     * @return An array of parsed actions.
     */
    @NotNull
    Action<T>[] parseActions(Section section);

    /**
     * Parses an action from the given type and arguments.
     *
     * @param type  The type identifier of the action.
     * @param args  The arguments for the action.
     * @return The parsed action.
     */
    Action<T> parseAction(@NotNull String type, @NotNull Object args);

    /**
     * Generates a map of actions triggered by specific events from a configuration section.
     *
     * @param section The configuration section containing event-action mappings.
     * @return A map where the keys are action triggers and the values are arrays of actions associated with those triggers.
     */
    default Map<ActionTrigger, Action<T>[]> parseEventActions(Section section) {
        HashMap<ActionTrigger, Action<T>[]> actionMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : section.getStringRouteMappedValues(false).entrySet()) {
            if (entry.getValue() instanceof Section innerSection) {
                try {
                    actionMap.put(
                            ActionTrigger.valueOf(entry.getKey().toUpperCase(Locale.ENGLISH)),
                            parseActions(innerSection)
                    );
                } catch (IllegalArgumentException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return actionMap;
    }

    /**
     * Parses a configuration section to generate a map of timed actions.
     *
     * @param section The configuration section containing time-action mappings.
     * @return A TreeMap where the keys are time values (in integer form) and the values are arrays of actions associated with those times.
     */
    default TreeMap<Integer, Action<T>[]> parseTimesActions(Section section) {
        TreeMap<Integer, Action<T>[]> actionMap = new TreeMap<>();
        for (Map.Entry<String, Object> entry : section.getStringRouteMappedValues(false).entrySet()) {
            if (entry.getValue() instanceof Section innerSection) {
                actionMap.put(Integer.parseInt(entry.getKey()), parseActions(innerSection));
            }
        }
        return actionMap;
    }

    /**
     * Triggers a list of actions with the given context.
     * If the list of actions is not null, each action in the list is triggered.
     *
     * @param context The context associated with the actions.
     * @param actions The list of actions to trigger.
     */
    static <T> void trigger(@NotNull Context<T> context, @Nullable List<Action<T>> actions) {
        if (actions != null)
            for (Action<T> action : actions)
                action.trigger(context);
    }

    /**
     * Triggers an array of actions with the given context.
     * If the array of actions is not null, each action in the array is triggered.
     *
     * @param context The context associated with the actions.
     * @param actions The array of actions to trigger.
     */
    static <T> void trigger(@NotNull Context<T> context, @Nullable Action<T>[] actions) {
        if (actions != null)
            for (Action<T> action : actions)
                action.trigger(context);
    }
}
