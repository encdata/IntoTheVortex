package com.intothevortex.client.render;

import com.intothevortex.IntoTheVortex;
import net.minecraft.client.model.Model;
import net.minecraft.resources.Identifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class TardisModelRegistry {
    private static final Identifier DEFAULT_EXTERIOR = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "exterior/police_box");
    private static final Identifier DEFAULT_INTERIOR = Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "interior/police_box");
    private static final Map<Identifier, Supplier<Model<TardisExteriorRenderState>>> EXTERIOR_MODELS = new LinkedHashMap<>();
    private static final Map<Identifier, Supplier<Model<TardisExteriorRenderState>>> INTERIOR_MODELS = new LinkedHashMap<>();

    private TardisModelRegistry() {
    }

    public static void initialize() {
        register(DEFAULT_EXTERIOR, DEFAULT_INTERIOR, () -> new PoliceBoxModel(PoliceBoxModel.createBodyLayer().bakeRoot()), () -> new PoliceBoxInteriorModel(PoliceBoxInteriorModel.createBodyLayer().bakeRoot()));
    }

    public static void register(Identifier exteriorId, Identifier interiorId, Supplier<Model<TardisExteriorRenderState>> exteriorFactory, Supplier<Model<TardisExteriorRenderState>> interiorFactory) {
        EXTERIOR_MODELS.put(exteriorId, exteriorFactory);
        INTERIOR_MODELS.put(interiorId, interiorFactory);
    }

    public static Model<TardisExteriorRenderState> exterior(Identifier id) {
        return EXTERIOR_MODELS.getOrDefault(id, EXTERIOR_MODELS.get(DEFAULT_EXTERIOR)).get();
    }

    public static Model<TardisExteriorRenderState> interior(Identifier id) {
        return INTERIOR_MODELS.getOrDefault(id, INTERIOR_MODELS.get(DEFAULT_INTERIOR)).get();
    }
}
