package com.intothevortex.client.monitor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.resources.Identifier;

public final class MonitorPageRegistry {
    private static final Map<Identifier, MonitorPageDefinition> PAGES = new LinkedHashMap<>();

    private MonitorPageRegistry() {
    }

    public static void register(MonitorPageDefinition definition) {
        if (definition == null || definition.id() == null || definition.factory() == null) {
            throw new IllegalArgumentException("A monitor page requires an id and factory");
        }
        PAGES.put(definition.id(), definition);
    }

    public static void unregister(Identifier id) {
        PAGES.remove(id);
    }

    public static Collection<MonitorPageDefinition> definitions() {
        return new ArrayList<>(PAGES.values());
    }
}
