package net.momirealms.customfishing.api.mechanic.effect;

import net.momirealms.customfishing.api.mechanic.context.Context;
import net.momirealms.customfishing.api.mechanic.requirement.Requirement;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * EffectModifier interface for modifying effects in the CustomFishing plugin.
 * This interface allows defining conditions and modifications for effects applied to players.
 */
public interface EffectModifier {

    /**
     * Returns an array of requirements that must be met by a Player for the effect to be applied.
     *
     * @return an array of requirements
     */
    Requirement<Player>[] requirements();

    /**
     * Returns a list of modifiers that apply changes to an effect within a given context.
     *
     * @return a list of effect modifiers
     */
    List<BiConsumer<Effect, Context<Player>>> modifiers();

    /**
     * Creates and returns a new Builder instance for constructing EffectModifier instances.
     *
     * @return a new Builder instance
     */
    static Builder builder() {
        return new EffectModifierImpl.BuilderImpl();
    }

    /**
     * Builder interface for constructing EffectModifier instances.
     */
    interface Builder {

        /**
         * Sets the requirements for the EffectModifier being built.
         *
         * @param requirements a list of requirements
         * @return the current Builder instance
         */
        Builder requirements(List<Requirement<Player>> requirements);

        /**
         * Sets the modifiers for the EffectModifier being built.
         *
         * @param modifiers a list of effect modifiers
         * @return the current Builder instance
         */
        Builder modifiers(List<BiConsumer<Effect, Context<Player>>> modifiers);

        /**
         * Builds and returns the EffectModifier instance.
         *
         * @return the built EffectModifier instance
         */
        EffectModifier build();
    }
}