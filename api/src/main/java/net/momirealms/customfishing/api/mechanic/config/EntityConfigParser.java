package net.momirealms.customfishing.api.mechanic.config;

import dev.dejvokep.boostedyaml.block.implementation.Section;
import net.momirealms.customfishing.api.mechanic.config.function.*;
import net.momirealms.customfishing.api.mechanic.effect.LootBaseEffect;
import net.momirealms.customfishing.api.mechanic.entity.EntityConfig;
import net.momirealms.customfishing.api.mechanic.event.EventCarrier;
import net.momirealms.customfishing.api.mechanic.item.MechanicType;
import net.momirealms.customfishing.api.mechanic.loot.Loot;
import net.momirealms.customfishing.common.config.node.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EntityConfigParser {

    private final String id;
    private final List<Consumer<EntityConfig.Builder>> entityBuilderConsumers = new ArrayList<>();
    private final List<Consumer<LootBaseEffect.Builder>> effectBuilderConsumers = new ArrayList<>();
    private final List<Consumer<Loot.Builder>> lootBuilderConsumers = new ArrayList<>();
    private final List<Consumer<EventCarrier.Builder>> eventBuilderConsumers = new ArrayList<>();

    public EntityConfigParser(String id, Section section, Map<String, Node<ConfigParserFunction>> functionMap) {
        this.id = id;
        analyze(section, functionMap);
    }

    private void analyze(Section section, Map<String, Node<ConfigParserFunction>> functionMap) {
        Map<String, Object> dataMap = section.getStringRouteMappedValues(false);
        for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
            String key = entry.getKey();
            Node<ConfigParserFunction> node = functionMap.get(key);
            if (node == null) continue;
            ConfigParserFunction function = node.nodeValue();
            if (function != null) {
                switch (function.type()) {
                    case ENTITY -> {
                        EntityParserFunction entityParserFunction = (EntityParserFunction) function;
                        Consumer<EntityConfig.Builder> consumer = entityParserFunction.accept(entry.getValue());
                        entityBuilderConsumers.add(consumer);
                    }
                    case BASE_EFFECT -> {
                        BaseEffectParserFunction baseEffectParserFunction = (BaseEffectParserFunction) function;
                        Consumer<LootBaseEffect.Builder> consumer = baseEffectParserFunction.accept(entry.getValue());
                        effectBuilderConsumers.add(consumer);
                    }
                    case LOOT -> {
                        LootParserFunction lootParserFunction = (LootParserFunction) function;
                        Consumer<Loot.Builder> consumer = lootParserFunction.accept(entry.getValue());
                        lootBuilderConsumers.add(consumer);
                    }
                    case EVENT -> {
                        EventParserFunction eventParserFunction = (EventParserFunction) function;
                        Consumer<EventCarrier.Builder> consumer = eventParserFunction.accept(entry.getValue());
                        eventBuilderConsumers.add(consumer);
                    }
                }
                continue;
            }
            if (entry.getValue() instanceof Section innerSection) {
                analyze(innerSection, node.getChildTree());
            }
        }
    }

    public EntityConfig getEntity() {
        EntityConfig.Builder builder = EntityConfig.builder()
                .id(id);
        for (Consumer<EntityConfig.Builder> consumer : entityBuilderConsumers) {
            consumer.accept(builder);
        }
        return builder.build();
    }

    private LootBaseEffect getBaseEffect() {
        LootBaseEffect.Builder builder = LootBaseEffect.builder();
        for (Consumer<LootBaseEffect.Builder> consumer : effectBuilderConsumers) {
            consumer.accept(builder);
        }
        return builder.build();
    }

    public Loot getLoot() {
        Loot.Builder builder = Loot.builder()
                .id(id)
                .lootBaseEffect(getBaseEffect());
        for (Consumer<Loot.Builder> consumer : lootBuilderConsumers) {
            consumer.accept(builder);
        }
        return builder.build();
    }

    public EventCarrier getEventCarrier() {
        EventCarrier.Builder builder = EventCarrier.builder()
                .id(id)
                .type(MechanicType.LOOT);
        for (Consumer<EventCarrier.Builder> consumer : eventBuilderConsumers) {
            consumer.accept(builder);
        }
        return builder.build();
    }
}