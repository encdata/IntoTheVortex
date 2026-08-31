package com.intothevortex.exterior;

import com.intothevortex.IntoTheVortex;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;

public final class ExteriorRegistry {
    public static final Identifier DEFAULT_ID = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "default");
    private static final Map<Identifier, ExteriorDefinition> EXTERIORS = new LinkedHashMap<>();

    private ExteriorRegistry() {
    }

    public static ExteriorDefinition register(ExteriorDefinition definition) {
        EXTERIORS.put(definition.id(), definition);
        return definition;
    }

    public static ExteriorDefinition get(Identifier id) {
        return EXTERIORS.getOrDefault(id, EXTERIORS.get(DEFAULT_ID));
    }

    public static Collection<ExteriorDefinition> values() {
        return List.copyOf(EXTERIORS.values());
    }

    public static void initialize() {
        register(new ExteriorDefinition(
                DEFAULT_ID,
                Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "exterior/police_box"),
                Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/exterior/default.png"),
                1.2F,
                2.8F,
                0.0F,
                0.0F,
                -0.6F
        ));
    }
}
