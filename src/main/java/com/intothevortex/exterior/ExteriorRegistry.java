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
                Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/exterior/policebox_alt.png"),
                Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/exterior/policebox_alt_emission.png"),
                1.2F,
                2.8F,
                0.0F,
                0.0F,
                -0.6F,
                AnimationDefinition.DOOR_SWING
        ));
        registerVariant("gamblebox", "gamblebox.png", "gamblebox_emission.png");
        registerVariant("toyota_alt", "policebox_alt2.png", "policebox_alt2_emission.png");
        registerVariant("badwolf", "policebox_badwolf.png", "policebox_badwolf_emission.png");
        registerVariant("coral", "policebox_coral.png", "policebox_coral_emission.png");
        registerVariant("purple", "policebox_purple.png", "policebox_purple_emission.png");
       // registerVariant("texture", "policebox_texture.png");
        registerVariant("tokomak", "policebox_tokomak.png", "policebox_tokomak_emission.png");
        registerVariant("toyota_purple", "policebox_purple.png", "policebox_purple_emission.png");
        registerVariant("copper", "policebox_copper.png", "policebox_copper_emission.png");
        registerVariant("midnight", "policebox_midnight.png", "policebox_midnight_emission.png");
        registerVariant("dino", "policebox_dino.png", "policebox_dino_emission.png");
        registerVariant("candy", "policebox_candy.png", "policebox_candy_emission.png");
    }

    private static void registerVariant(String name, String texture, String emission) {
        Identifier id = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name);
        register(new ExteriorDefinition(id, DEFAULT_ID, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/exterior/" + texture), Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/exterior/" + emission), 1.2F, 2.8F, 0.0F, 0.0F, -0.6F, AnimationDefinition.DOOR_SWING));
    }
}
