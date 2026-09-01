package com.intothevortex.interior;

import com.intothevortex.IntoTheVortex;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;
import java.util.Set;
import java.util.HashSet;

public final class InteriorRegistry {
    private static final Set<Identifier> INTERIORS = new HashSet<>();
    private static final java.util.Map<Identifier, InteriorDefinition> DEFINITIONS = new java.util.HashMap<>();
    private static final ResourceKey<Block> DOOR_KEY = ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "interior_door"));
    private static final ResourceKey<Block> DOOR_TOP_KEY = ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "interior_door_top"));
    private static final ResourceKey<Block> CONSOLE_KEY = ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "console"));
    private static final ResourceKey<Block> WALL_MONITOR_KEY = ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "wall_monitor"));
    public static final Block DOOR = Registry.register(BuiltInRegistries.BLOCK, DOOR_KEY, new InteriorDoorBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(DOOR_KEY)));
    public static final Block DOOR_TOP = Registry.register(BuiltInRegistries.BLOCK, DOOR_TOP_KEY, new InteriorDoorTopBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(DOOR_TOP_KEY)));
    public static final Block CONSOLE = Registry.register(BuiltInRegistries.BLOCK, CONSOLE_KEY, new InteriorPropBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(CONSOLE_KEY), true));
    public static final Block WALL_MONITOR = Registry.register(BuiltInRegistries.BLOCK, WALL_MONITOR_KEY, new InteriorPropBlock(net.minecraft.world.level.block.state.BlockBehaviour.Properties.of().setId(WALL_MONITOR_KEY)));
    public static final BlockEntityType<InteriorDoorBlockEntity> DOOR_ENTITY = Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, ResourceKey.create(net.minecraft.core.registries.Registries.BLOCK_ENTITY_TYPE, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "interior_door")), createDoorEntity());
    public static final Item DOOR_ITEM = registerItem("interior_door", DOOR, DOOR_KEY);
    public static final Item CONSOLE_ITEM = registerItem("console", CONSOLE, CONSOLE_KEY);
    public static final Item WALL_MONITOR_ITEM = registerItem("wall_monitor", WALL_MONITOR, WALL_MONITOR_KEY);
    private InteriorRegistry() {}
    private static Item registerItem(String name, Block block, ResourceKey<Block> blockKey) {
        ResourceKey<Item> itemKey = ResourceKey.create(net.minecraft.core.registries.Registries.ITEM, Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, name));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, new BlockItem(block, new Item.Properties().setId(itemKey)));
    }
    @SuppressWarnings("unchecked")
    private static BlockEntityType<InteriorDoorBlockEntity> createDoorEntity() {
        try {
            var constructor = java.util.Arrays.stream(BlockEntityType.class.getDeclaredConstructors()).filter(value -> value.getParameterCount() == 2).findFirst().orElseThrow();
            constructor.setAccessible(true);
            Class<?> supplierType = constructor.getParameterTypes()[0];
            Object supplier = java.lang.reflect.Proxy.newProxyInstance(supplierType.getClassLoader(), new Class<?>[]{supplierType}, (proxy, method, args) -> new InteriorDoorBlockEntity((net.minecraft.core.BlockPos) args[0], (net.minecraft.world.level.block.state.BlockState) args[1]));
            return (BlockEntityType<InteriorDoorBlockEntity>) constructor.newInstance(supplier, java.util.Set.of(DOOR));
        } catch (ReflectiveOperationException exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }
    public static void initialize() {
        register("intothevortex:70default");
        register("intothevortex:cavernwood");
        register("intothevortex:classic_neon");
        register("intothevortex:coppercoral");
        register("intothevortex:darkrock");
        register("intothevortex:missy");
        register("intothevortex:ravencrest");
        register("intothevortex:rosewood");
        register("intothevortex:second");
        register("intothevortex:second_alt");
        register("intothevortex:sprucewood");
        register("intothevortex:third");
        register("intothevortex:third_alt");
        register("intothevortex:toywardian");
        register("intothevortex:trenzaloremissy");
    }
    public static void register(String id) { register(id, id); }
    public static void register(String id, String model) {
        Identifier identifier = Identifier.parse(id);
        INTERIORS.add(identifier);
        DEFINITIONS.put(identifier, new InteriorDefinition(identifier, Identifier.parse(model), Identifier.fromNamespaceAndPath(IntoTheVortex.MOD_ID, "textures/entity/interior/policebox.png"), null, com.intothevortex.exterior.AnimationDefinition.DOOR_SWING));
    }
    public static Set<Identifier> registered() { return Set.copyOf(INTERIORS); }
    public static Identifier model(Identifier id) { return DEFINITIONS.get(id).model(); }
    public static InteriorDefinition definition(Identifier id) { return DEFINITIONS.get(id); }
}
