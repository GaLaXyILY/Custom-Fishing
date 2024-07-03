package net.momirealms.customfishing.api.mechanic.item;

import net.kyori.adventure.util.Index;
import net.momirealms.customfishing.api.BukkitCustomFishingPlugin;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;

public class MechanicType {

    private static final HashMap<String, MechanicType> types = new HashMap<>();

    public static final MechanicType LOOT = of("loot");
    public static final MechanicType ROD = of("rod");
    public static final MechanicType UTIL = of("util");
    public static final MechanicType BAIT = of("bait");
    public static final MechanicType HOOK = of("hook");
    public static final MechanicType TOTEM = of("totem");
    public static final MechanicType ENTITY = of("entity");
    public static final MechanicType BLOCK = of("block");
    public static final MechanicType ENCHANT = of("enchant");

    private final String type;

    public MechanicType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    private static MechanicType of(String type) {
        return new MechanicType(type);
    }

    public static MechanicType[] values() {
        return new MechanicType[]{LOOT, ROD, UTIL, BAIT, HOOK, TOTEM, ENCHANT, ENTITY, BLOCK};
    }

    private static final Index<String, MechanicType> INDEX = Index.create(MechanicType::getType, values());

    public static Index<String, MechanicType> index() {
        return INDEX;
    }

    @ApiStatus.Internal
    public static void register(String id, MechanicType type) {
        MechanicType previous = types.put(id, type);
        if (previous != null) {
            BukkitCustomFishingPlugin.getInstance().getPluginLogger().warn(
                    "Attempted to register item type " + id + " twice, this is not a safe behavior. ["
                            + type.getType() + "," + previous.getType() + "]"
                    );
        }
    }

    @Nullable
    @ApiStatus.Internal
    public static MechanicType getTypeByID(String id) {
        return types.get(id);
    }

    @ApiStatus.Internal
    public static void reset() {
        types.clear();
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MechanicType mechanicType = (MechanicType) object;
        return Objects.equals(type, mechanicType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }

    @Override
    public String toString() {
        return type;
    }
}
