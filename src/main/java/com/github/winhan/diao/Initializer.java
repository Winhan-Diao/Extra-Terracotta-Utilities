package com.github.winhan.diao;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.*;
import net.minecraft.block.dispenser.FallibleItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.Instrument;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static net.minecraft.block.Block.NOTIFY_ALL;
import static net.minecraft.item.Items.*;


public class Initializer implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("extra_terracotta_utilities");
	public static final Block POWERED_MAGENTA_GLAZED_TERRACOTTA = new PoweredMagentaGlazedTerracotta(FabricBlockSettings.of(Material.STONE, DyeColor.RED).requiresTool().strength(1.4F));
	public static final Block STICKY_MAGENTA_GLAZED_TERRACOTTA = new StickyMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.ORGANIC_PRODUCT, MapColor.GREEN).slipperiness(0.8F).sounds(BlockSoundGroup.SLIME).nonOpaque().breakInstantly());
	public static final Block ENDERIZED_MAGENTA_GLAZED_TERRACOTTA = new EnderizedMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(1.8F));
	public static final Block ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA = new AllayedEnderizedMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.STONE).requiresTool().strength(1.8F));
	public static final Block EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA = new ExplosiveMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.TNT, DyeColor.RED).breakInstantly().sounds(BlockSoundGroup.GRASS).solidBlock(Blocks::never));
	public static final Block ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA = new EnhancedExplosiveMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.TNT, DyeColor.BLACK).breakInstantly().sounds(BlockSoundGroup.GRASS).strength(1.0f).solidBlock(Blocks::never));
	public static final Block HAUNTED_MAGENTA_GLAZED_TERRACOTTA = new HauntedMagentaGlazedTerracotta(FabricBlockSettings.of(Material.GOURD, MapColor.ORANGE).strength(1.0F).sounds(BlockSoundGroup.WOOD).luminance((state) -> 15).allowsSpawning(Blocks::always));
	public static final Block ALLAYED_MAGENTA_GLAZED_TERRACOTTA = new AllayedMagentaGlazedTerracotta(FabricBlockSettings.of(Material.STONE).requiresTool().strength(1.4F));
	public static final Block SPINNING_MAGENTA_GLAZED_TERRACOTTA = new SpinningMagentaGlazedTerracotta(FabricBlockSettings.of(Material.SOLID_ORGANIC).mapColor(MapColor.YELLOW).breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final Block ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA = new EntropyReducingMagentaGlazedTerracotta(FabricBlockSettings.of(Material.SNOW_BLOCK).mapColor(MapColor.WHITE).strength(2.0F).sounds(BlockSoundGroup.SNOW).ticksRandomly());
	public static final Block WOODEN_MAGENTA_GLAZED_TERRACOTTA = new WoodenMagentaGlazedTerracotta(FabricBlockSettings.of(Material.WOOD).mapColor(MapColor.BROWN).strength(2.0F).sounds(BlockSoundGroup.WOOD).ticksRandomly());
	public static final Block SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA = new SilkTouchingMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.WOOL).mapColor(MapColor.WHITE).breakInstantly().sounds(BlockSoundGroup.WOOL).solidBlock(Blocks::never));
	public static final Block SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_UNCOVERED = new SilkTouchingMagentaGlazedTerracottaUncovered(AbstractBlock.Settings.of(Material.WOOL).mapColor(MapColor.WHITE).breakInstantly().sounds(BlockSoundGroup.WOOL).solidBlock(Blocks::never));
	public static final Block BUFFERING_MAGENTA_GLAZED_TERRACOTTA = new BufferingMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.WOOD).mapColor(MapColor.OAK_TAN).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final Block INSERTING_MAGENTA_GLAZED_TERRACOTTA = new InsertingMagentaGlazedTerracotta(AbstractBlock.Settings.of(Material.WOOD).mapColor(MapColor.OAK_TAN).strength(2.5F).sounds(BlockSoundGroup.WOOD));
	public static final Item STICKY_CLOG = new ClogsItem(new FabricItemSettings().maxCount(1), 1);
	public static final Item STICKY_CLOG_PIECE = new ClogsItem(new FabricItemSettings().maxCount(16), 16);
	public static final Item STICKY_CLOG_FRAGMENT = new ClogsItem(new FabricItemSettings().maxCount(64), 64);
	public static final TagKey<Item> CLONGS = TagKey.of(RegistryKeys.ITEM, new Identifier("extra_terracotta_utilities:clogs"));
	private static final ItemGroup TERRACOTTA_UTILITIES = FabricItemGroup.builder(new Identifier("extra_terracotta_utilities", "terracotta_utilities"))
			.icon(()->new ItemStack(POWERED_MAGENTA_GLAZED_TERRACOTTA))
			.build();


	public static final BlockEntityType<AllayedMagentaGlazedTerracottaEntity> ALLAYED_MAGENTA_GLAZED_TERRACOTTA_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			new Identifier("extra_terracotta_utilities", "allayed_magenta_glazed_terracotta"),
			FabricBlockEntityTypeBuilder.create(AllayedMagentaGlazedTerracottaEntity::new, ALLAYED_MAGENTA_GLAZED_TERRACOTTA).build());
	public static final BlockEntityType<AllayedEnderizedMagentaGlazedTerracottaEntity> ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			new Identifier("extra_terracotta_utilities", "allayed_enderized_magenta_glazed_terracotta"),
			FabricBlockEntityTypeBuilder.create(AllayedEnderizedMagentaGlazedTerracottaEntity::new, ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA).build());
	public static final BlockEntityType<EntropyReducingMagentaGlazedTerracottaEntity> ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			new Identifier("extra_terracotta_utilities", "entropy_reducing_magenta_glazed_terracotta"),
			FabricBlockEntityTypeBuilder.create(EntropyReducingMagentaGlazedTerracottaEntity::new, ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA).build());
	public static final BlockEntityType<BufferingMagentaGlazedTerracottaEntity> BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			new Identifier("extra_terracotta_utilities", "buffering_magenta_glazed_terracotta"),
			FabricBlockEntityTypeBuilder.create(BufferingMagentaGlazedTerracottaEntity::new, BUFFERING_MAGENTA_GLAZED_TERRACOTTA).build());
	public static final BlockEntityType<InsertingMagentaGlazedTerracottaEntity> INSERTING_MAGENTA_GLAZED_TERRACOTTA_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
			new Identifier("extra_terracotta_utilities", "inserting_magenta_glazed_terracotta"),
			FabricBlockEntityTypeBuilder.create(InsertingMagentaGlazedTerracottaEntity::new, INSERTING_MAGENTA_GLAZED_TERRACOTTA).build());

	public static final List<Item> CARPETS_WOOLS = Arrays.asList(WHITE_CARPET, CYAN_CARPET, BLUE_CARPET, BLACK_CARPET,
			BROWN_CARPET, GRAY_CARPET, GREEN_CARPET, LIGHT_BLUE_CARPET,
			LIME_CARPET, LIGHT_GRAY_CARPET, YELLOW_CARPET, ORANGE_CARPET,
			RED_CARPET, MAGENTA_CARPET, PINK_CARPET, PURPLE_CARPET,
			WHITE_WOOL, CYAN_WOOL, BLACK_WOOL, BLUE_WOOL,
			BROWN_WOOL, GRAY_WOOL, GREEN_WOOL, LIGHT_BLUE_WOOL,
			LIME_WOOL, LIGHT_GRAY_WOOL, YELLOW_WOOL, ORANGE_WOOL,
			RED_WOOL, MAGENTA_WOOL, PURPLE_WOOL, PINK_WOOL);

	public static void registerDispenserBehaviorForStmgt(Item item) {
		DispenserBlock.registerBehavior(item, new FallibleItemDispenserBehavior() {
			public ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
				Direction direction = pointer.getBlockState().get(DispenserBlock.FACING);
				BlockPos blockPos = pointer.getPos().offset(direction);
				World world = pointer.getWorld();
				BlockState blockState = world.getBlockState(blockPos);
				this.setSuccess(true);
				if (blockState.isOf(Initializer.SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_UNCOVERED)) {
					world.setBlockState(blockPos, Initializer.SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA.getDefaultState().with(Properties.FACING, blockState.get(Properties.FACING)), NOTIFY_ALL);
					stack.decrement(1);
					return stack;
				} else {
					return super.dispenseSilently(pointer, stack);
				}
			}
		});



	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");


		//Add a powered_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "powered_magenta_glazed_terracotta"), POWERED_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "powered_magenta_glazed_terracotta"), new BlockItem(POWERED_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a sticky_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "sticky_magenta_glazed_terracotta"), STICKY_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "sticky_magenta_glazed_terracotta"), new BlockItem(STICKY_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a enderized_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "enderized_magenta_glazed_terracotta"), ENDERIZED_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "enderized_magenta_glazed_terracotta"), new BlockItem(ENDERIZED_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a allayed_enderized_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "allayed_enderized_magenta_glazed_terracotta"), ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "allayed_enderized_magenta_glazed_terracotta"), new BlockItem(ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a explosive_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "explosive_magenta_glazed_terracotta"), EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "explosive_magenta_glazed_terracotta"), new BlockItem(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a haunted_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "haunted_magenta_glazed_terracotta"), HAUNTED_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "haunted_magenta_glazed_terracotta"), new BlockItem(HAUNTED_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a enhanced_explosive_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "enhanced_explosive_magenta_glazed_terracotta"), ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "enhanced_explosive_magenta_glazed_terracotta"), new BlockItem(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a allayed_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "allayed_magenta_glazed_terracotta"), ALLAYED_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "allayed_magenta_glazed_terracotta"), new BlockItem(ALLAYED_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a spinning_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "spinning_magenta_glazed_terracotta"), SPINNING_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "spinning_magenta_glazed_terracotta"), new BlockItem(SPINNING_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a entropy_reducing_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "entropy_reducing_magenta_glazed_terracotta"), ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "entropy_reducing_magenta_glazed_terracotta"), new BlockItem(ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a wooden_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "wooden_magenta_glazed_terracotta"), WOODEN_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "wooden_magenta_glazed_terracotta"), new BlockItem(WOODEN_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a silk_touching_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "silk_touching_magenta_glazed_terracotta"), SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "silk_touching_magenta_glazed_terracotta"), new BlockItem(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "silk_touching_magenta_glazed_terracotta_uncovered"), SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_UNCOVERED);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "silk_touching_magenta_glazed_terracotta_uncovered"), new BlockItem(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA_UNCOVERED, new FabricItemSettings()));
		//Add a buffering_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "buffering_magenta_glazed_terracotta"), BUFFERING_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "buffering_magenta_glazed_terracotta"), new BlockItem(BUFFERING_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));
		//Add a inserting_magenta_glazed_terracotta
		Registry.register(Registries.BLOCK, new Identifier("extra_terracotta_utilities", "inserting_magenta_glazed_terracotta"), INSERTING_MAGENTA_GLAZED_TERRACOTTA);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "inserting_magenta_glazed_terracotta"), new BlockItem(INSERTING_MAGENTA_GLAZED_TERRACOTTA, new FabricItemSettings()));

		//Add clogs
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "sticky_clog"), STICKY_CLOG);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "sticky_clog_piece"), STICKY_CLOG_PIECE);
		Registry.register(Registries.ITEM, new Identifier("extra_terracotta_utilities", "sticky_clog_fragment"), STICKY_CLOG_FRAGMENT);

		//Register to itemgroup
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(POWERED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(SPINNING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(BUFFERING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(content -> content.add(INSERTING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(POWERED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(STICKY_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(SPINNING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ENDERIZED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(HAUNTED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(WOODEN_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ALLAYED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(BUFFERING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(INSERTING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(POWERED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(STICKY_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(ENHANCED_EXPLOSIVE_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(SPINNING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(SILK_TOUCHING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(ENDERIZED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(HAUNTED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(WOODEN_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(ALLAYED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(ALLAYED_ENDERIZED_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(BUFFERING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(INSERTING_MAGENTA_GLAZED_TERRACOTTA));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(STICKY_CLOG));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(STICKY_CLOG_PIECE));
		ItemGroupEvents.modifyEntriesEvent(TERRACOTTA_UTILITIES).register(content -> content.add(STICKY_CLOG_FRAGMENT));


		//Register a dispenser block behavior
		CARPETS_WOOLS.forEach(Initializer::registerDispenserBehaviorForStmgt);

	}
}