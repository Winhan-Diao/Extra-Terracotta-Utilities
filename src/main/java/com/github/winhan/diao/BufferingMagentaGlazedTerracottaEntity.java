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
    private final Vec3d popoutVec;
    private final Vec3d popoutVelocity;

    public BufferingMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.BUFFERING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        this.popoutVec = pos.offset(state.get(Properties.FACING)).toCenterPos().offset(state.get(Properties.FACING), -.3).subtract(0, .2, 0);
        this.popoutVelocity = new Vec3d(state.get(Properties.FACING).getOffsetX()*.18, state.get(Properties.FACING).getOffsetY()*.18, state.get(Properties.FACING).getOffsetZ()*.18);
        this.stateManager = new ViewerCountManager() {
            @Override
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                BufferingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                BufferingMagentaGlazedTerracottaEntity.this.setOpen(state, true);
            }

            @Override
            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                BufferingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                BufferingMagentaGlazedTerracottaEntity.this.setOpen(state, false);
            }

            @Override
            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            @Override
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
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inventory);
        }

    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }

    }

    @Override
    public int size() {
        return 27;
    }

    @Override
    protected DefaultedList<ItemStack> getInvStackList() {
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

    /**To do:
     * fix messed up properties ( )**/
    public static void constantTick(World world, BlockPos pos, BlockState state, BufferingMagentaGlazedTerracottaEntity be) {
        if (state.get(Properties.TRIGGERED) && (!state.get(BufferingMagentaGlazedTerracotta.IMPULSE) || !state.get(Properties.POWERED))) {
            world.setBlockState(pos, state.with(Properties.TRIGGERED, false));
        }
        if (((!state.get(Properties.TRIGGERED) && state.get(BufferingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED)) || (!state.get(BufferingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED))) && !world.isClient) {
            if (!be.inventory.stream().filter(itemStack -> !isClogs(itemStack)).allMatch(ItemStack::isEmpty)) {
                be.inventory.stream().filter(itemStack -> !isClogs(itemStack)).forEach(itemStack -> itemPopout(be, itemStack, world));
                clear(be.inventory);
                be.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
            }
            if (state.get(BufferingMagentaGlazedTerracotta.IMPULSE)) {
                world.setBlockState(pos, state.with(Properties.TRIGGERED, true));
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

    public static void itemPopout(BufferingMagentaGlazedTerracottaEntity be, ItemStack itemStack, World world) {
        ItemEntity itemEntity = new ItemEntity(world, be.popoutVec.getX(), be.popoutVec.getY(), be.popoutVec.getZ(), itemStack, be.popoutVelocity.x, be.popoutVelocity.y, be.popoutVelocity.z);
        world.spawnEntity(itemEntity);
    }

    public static boolean isClogs(ItemStack itemStack) {
        return itemStack.isIn(Initializer.CLONGS);
    }

    public static void clear(DefaultedList<ItemStack> inventory) {
        for(int i = 0; i < inventory.size(); ++i) {
            if (!inventory.get(i).isIn(Initializer.CLONGS)) {
                inventory.set(i, ItemStack.EMPTY);
            }
        }
    }
}
