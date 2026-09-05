package com.intothevortex.entity;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntityTypes {
    public static final EntityType<TardisExteriorEntity> TARDIS_EXTERIOR = register(
    "tardis_exterior",
    EntityType.Builder.<TardisExteriorEntity>of(TardisExteriorEntity::new, MobCategory.MISC).sized(1.5F, 3.1F)
    );
    public static final EntityType<ControlHitboxEntity> CONTROL_HITBOX = register(
    "control_hitbox",
    EntityType.Builder.<ControlHitboxEntity>of(ControlHitboxEntity::new, MobCategory.MISC).sized(0.2F, 0.2F).clientTrackingRange(64).updateInterval(1)
    );

    private ModEntityTypes() {
    }

    private static <T extends Entity> EntityType<T> register(String name, EntityType.Builder<T> builder) {
        ResourceKey<EntityType<?>> key = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ENTITY_TYPE, key, builder.build(key));
    }

    public static void initialize() {
    }
}
