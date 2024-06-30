package net.momirealms.customfishing.api.mechanic.fishing.hook;

import net.momirealms.customfishing.api.BukkitCustomFishingPlugin;
import net.momirealms.customfishing.api.mechanic.context.Context;
import net.momirealms.customfishing.api.mechanic.context.ContextKeys;
import net.momirealms.customfishing.api.mechanic.effect.Effect;
import net.momirealms.customfishing.api.mechanic.effect.EffectProperties;
import net.momirealms.customfishing.common.plugin.scheduler.SchedulerTask;
import net.momirealms.sparrow.heart.SparrowHeart;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;

import java.util.concurrent.ThreadLocalRandom;

public class VanillaMechanic implements HookMechanic {

    private final FishHook hook;
    private final Context<Player> context;
    private SchedulerTask task;
    private boolean isHooked = false;

    public VanillaMechanic(FishHook hook, Context<Player> context) {
        this.hook = hook;
        this.context = context;
    }

    @Override
    public boolean canStart() {
        return hook.isInWater();
    }

    @Override
    public void preStart() {
        this.context.arg(ContextKeys.SURROUNDING, EffectProperties.WATER_FISHING.key());
    }

    @Override
    public void start(Effect finalEffect) {
        setWaitTime(hook, finalEffect);
        this.task = BukkitCustomFishingPlugin.getInstance().getScheduler().sync().runRepeating(() -> {
            if (isHooked) {
                if (!isHooked()) {
                    isHooked = false;
                    setWaitTime(hook, finalEffect);
                }
            } else {
                if (isHooked()) {
                    isHooked = true;
                }
            }
        }, 1, 1, hook.getLocation());
    }

    private void setWaitTime(FishHook hook, Effect effect) {
        BukkitCustomFishingPlugin.getInstance().getScheduler().sync().runLater(() -> {
            int before = hook.getWaitTime();
            int after = (int) Math.max(1, before * effect.waitTimeMultiplier() + effect.waitTimeAdder());
            BukkitCustomFishingPlugin.getInstance().debug("Wait time: " + before + " -> " + after + " ticks");
            hook.setWaitTime(after);
        }, 1, hook.getLocation());
    }

    @Override
    public boolean isHooked() {
        return SparrowHeart.getInstance().isFishingHookBit(hook);
    }

    @Override
    public void destroy() {
        if (this.task != null) this.task.cancel();
    }
}
