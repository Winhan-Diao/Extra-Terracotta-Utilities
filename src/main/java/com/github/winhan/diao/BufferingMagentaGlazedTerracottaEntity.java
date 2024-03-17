package com.github.winhan.diao;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class BufferingMagentaGlazedTerracottaEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory;
    private final ViewerCountManager stateManager;
    private final Vec3d targetPosCenter;

    public BufferingMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        this.targetPosCenter = pos.offset(state.get(Properties.FACING)).toCenterPos();
        this.stateManager = new ViewerCountManager() {
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                BufferingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                BufferingMagentaGlazedTerracottaEntity.this.setOpen(state, true);
            }

            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                BufferingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                BufferingMagentaGlazedTerracottaEntity.this.setOpen(state, false);
            }

            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            protected boolean isPlayerViewing(PlayerEntity player) {
                if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                    Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                    return inventory == BufferingMagentaGlazedTerracottaEntity.this;
                } else {
                    return false;
                }
            }
        };
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        if (!this.writeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }

    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    protected DefaultedList<ItemStack> method_11282() {
        return this.inventory;
    }

    @Override
    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inventory = list;
    }

    @Override
    protected Text getContainerName() {
        return Text.translatable("block.extra_terracotta_utilities.buffering_magenta_glazed_terracotta");
    }

    @Override
    protected ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return GenericContainerScreenHandler.createGeneric9x3(syncId, playerInventory, this);
    }

    @Override
    public void onOpen(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.openContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }

    }

    @Override
    public void onClose(PlayerEntity player) {
        if (!this.removed && !player.isSpectator()) {
            this.stateManager.closeContainer(player, this.getWorld(), this.getPos(), this.getCachedState());
        }

    }

    public static void serverTick (World world, BlockPos pos, BlockState state, BufferingMagentaGlazedTerracottaEntity be) {
        if (state.get(Properties.TRIGGERED) && (!state.get(BufferingMagentaGlazedTerracotta.IMPULSE) || !state.get(Properties.POWERED))) {
            world.setBlockState(pos, state.with(Properties.TRIGGERED, false));
        }
        if (((!state.get(Properties.TRIGGERED) && state.get(BufferingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED)) || (!state.get(BufferingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED))) && !world.isClient) {
            if (!be.inventory.stream().allMatch(ItemStack::isEmpty)) {
                be.inventory.forEach(itemStack -> ItemPopout(be, itemStack, world));
                be.inventory.clear();
                be.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
//                world.setBlockState(pos, state.with(Properties.OPEN, true), Block.NOTIFY_ALL);
//                world.scheduleBlockTick(pos, state.getBlock(), 10);
                if (state.get(BufferingMagentaGlazedTerracotta.IMPULSE)) {
                    world.setBlockState(pos, state.with(Properties.TRIGGERED, true));
                }
            }
        }

    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(BarrelBlock.OPEN, open), Block.NOTIFY_ALL);
    }

    public void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = (state.get(BarrelBlock.FACING)).getVector();
        double d = (double)this.pos.getX() + 0.5 + (double)vec3i.getX() / 2.0;
        double e = (double)this.pos.getY() + 0.5 + (double)vec3i.getY() / 2.0;
        double f = (double)this.pos.getZ() + 0.5 + (double)vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    public static void ItemPopout(BufferingMagentaGlazedTerracottaEntity be, ItemStack itemStack, World world) {
        ItemEntity itemEntity = new ItemEntity(world, be.targetPosCenter.getX(), be.targetPosCenter.getY(), be.targetPosCenter.getZ(), itemStack, 0, 0, 0);
        world.spawnEntity(itemEntity);
    }

}
