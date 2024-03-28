package com.github.winhan.diao.initialize;

import com.github.winhan.diao.blockentities.AllayedMagentaGlazedTerracottaEntity;
import com.github.winhan.diao.blocks.*;
import com.github.winhan.diao.items.ClogsItem;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Initializer.MODID)
public class Initializer {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "extra_terracotta_utilities";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "extra_terracotta_utilities" namespace
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "extra_terracotta_utilities" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    // Create a Deferred Register to hold BlockEntityTypes which will all be registered under the "extra_terracotta_utilities" namespace
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "extra_terracotta_utilities" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final RegistryObject<Block> POWERED_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("powered_magenta_glazed_terracotta", () -> new PoweredMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(DyeColor.RED).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(1.4F)));
    public static final RegistryObject<Block> STICKY_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("sticky_magenta_glazed_terracotta", () -> new StickyMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).friction(0.8F).sound(SoundType.SLIME_BLOCK).noOcclusion().instabreak()));
    public static final RegistryObject<Block> EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("explosive_magenta_glazed_terracotta", () -> new ExplosiveMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).instabreak().sound(SoundType.GRASS).isRedstoneConductor(((pState, pLevel, pPos) -> false))));
    public static final RegistryObject<Block> ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("enhanced_explosive_magenta_glazed_terracotta", () -> new EnhancedExplosiveMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.TERRACOTTA_BLACK).instabreak().sound(SoundType.GRASS).isRedstoneConductor(((pState, pLevel, pPos) -> false))));
    public static final RegistryObject<Block> WOODEN_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("wooden_magenta_glazed_terracotta", () -> new WoodenMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f).randomTicks()));
    public static final RegistryObject<Block> HAUNTED_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("haunted_magenta_glazed_terracotta", () -> new HauntedMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.DIDGERIDOO).strength(1.0F).sound(SoundType.WOOD).lightLevel((state)->15).isValidSpawn(((pState, pLevel, pPos, pValue) -> true))));
    public static final RegistryObject<Block> ALLAYED_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("allayed_magenta_glazed_terracotta", () -> new AllayedMagentaGlazedTerracotta(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.4f)));
    public static final RegistryObject<Block> ENDERIZED_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("enderized_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.8f)));
    public static final RegistryObject<Block> ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("allayed_enderized_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).strength(1.8f)));
    public static final RegistryObject<Block> ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("entropy_reducing_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.SNOW)));
    public static final RegistryObject<Block> BUFFERING_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("buffering_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> INSERTING_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("inserting_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).instrument(NoteBlockInstrument.BASS).strength(2.5F).sound(SoundType.WOOD)));
    public static final RegistryObject<Block> SPINNING_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("spinning_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).instabreak().sound(SoundType.GRASS)));
    public static final RegistryObject<Block> SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA = BLOCKS.register("silk_touching_magenta_glazed_terracotta", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.GUITAR).instabreak().sound(SoundType.WOOL)));
    public static final RegistryObject<Item> POWERED_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("powered_magenta_glazed_terracotta", () -> new BlockItem(POWERED_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> STICKY_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("sticky_magenta_glazed_terracotta", () -> new BlockItem(STICKY_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("explosive_magenta_glazed_terracotta", () -> new BlockItem(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("enhanced_explosive_magenta_glazed_terracotta", () -> new BlockItem(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> WOODEN_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("wooden_magenta_glazed_terracotta", () -> new BlockItem(WOODEN_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> HAUNTED_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("haunted_magenta_glazed_terracotta", () -> new BlockItem(HAUNTED_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("allayed_magenta_glazed_terracotta", () -> new BlockItem(ALLAYED_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("enderized_magenta_glazed_terracotta", () -> new BlockItem(ENDERIZED_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("allayed_enderized_magenta_glazed_terracotta", () -> new BlockItem(ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("entropy_reducing_magenta_glazed_terracotta", () -> new BlockItem(ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("buffering_magenta_glazed_terracotta", () -> new BlockItem(BUFFERING_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> INSERTING_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("inserting_magenta_glazed_terracotta", () -> new BlockItem(INSERTING_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> SPINNING_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("spinning_magenta_glazed_terracotta", () -> new BlockItem(SPINNING_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_ITEM = ITEMS.register("silk_touching_magenta_glazed_terracotta", () -> new BlockItem(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA.get(), new Item.Properties()));
    public static final RegistryObject<Item> STICKY_CLOG = ITEMS.register("sticky_clog", () -> new ClogsItem(new Item.Properties().stacksTo(1), 1));
    public static final RegistryObject<Item> STICKY_CLOG_PIECE = ITEMS.register("sticky_clog_piece", () -> new ClogsItem(new Item.Properties().stacksTo(16), 16));
    public static final RegistryObject<Item> STICKY_CLOG_FRAGMENT = ITEMS.register("sticky_clog_fragment", () -> new ClogsItem(new Item.Properties().stacksTo(64), 64));
    public static final RegistryObject<BlockEntityType<AllayedMagentaGlazedTerracottaEntity>> ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY = BLOCK_ENTITY_TYPES.register(
            "allayed_magenta_glazed_terracotta",
            () -> BlockEntityType.Builder.of(AllayedMagentaGlazedTerracottaEntity::new,
                                             ALLAYED_MAGENTA_GLAZED_TERRACOTTA.get())
                                         .build(null));

    public static final TagKey<Item> CLOGS = ItemTags.create(new ResourceLocation(MODID, "clogs"));
    // Creates a creative tab with the id "extra_terracotta_utilities:example_tab" for the example item, that is placed after the combat tab
    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("terracotta_utilities", () -> CreativeModeTab.builder()
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> POWERED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(STICKY_CLOG.get());
                output.accept(STICKY_CLOG_PIECE.get());
                output.accept(STICKY_CLOG_FRAGMENT.get());
                output.accept(POWERED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(STICKY_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(WOODEN_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(HAUNTED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(INSERTING_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(SPINNING_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
                output.accept(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_ITEM.get());
            }).build());

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.REDSTONE_BLOCKS)
            event.accept(POWERED_MAGENTA_GLAZED_TERRACOTTA_ITEM);
    }

    public Initializer() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        // Register the commonSetup method for modloading
        modEventBus.addListener(Config::commonSetup);
        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);
        BLOCK_ENTITY_TYPES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so tabs get registered
        CREATIVE_MODE_TABS.register(modEventBus);
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
