package com.github.winhan.diao;

import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.HopperBlockEntity;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
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
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.List;

public class InsertingMagentaGlazedTerracottaEntity extends LootableContainerBlockEntity {
    private DefaultedList<ItemStack> inventory;
    private final ViewerCountManager stateManager;
    public static final int COOL_DOWN = 4;
    private int cooldown = COOL_DOWN;

    public InsertingMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.INSERTING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
        this.inventory = DefaultedList.ofSize(27, ItemStack.EMPTY);
        this.stateManager = new ViewerCountManager() {
            @Override
            protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
                InsertingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                InsertingMagentaGlazedTerracottaEntity.this.setOpen(state, true);
            }

            @Override
            protected void onContainerClose(World world, BlockPos pos, BlockState state) {
                InsertingMagentaGlazedTerracottaEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE);
                InsertingMagentaGlazedTerracottaEntity.this.setOpen(state, false);
            }

            @Override
            protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
            }

            @Override
            protected boolean isPlayerViewing(PlayerEntity player) {
                if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                    Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                    return inventory == InsertingMagentaGlazedTerracottaEntity.this;
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
        nbt.putInt("cooldown", this.cooldown);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.inventory = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.readLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inventory);
        }
        this.cooldown = nbt.getInt("cooldown");
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
        return Text.translatable("block.extra_terracotta_utilities.inserting_magenta_glazed_terracotta");
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

    public static void serverTick (World world, BlockPos pos, BlockState state, InsertingMagentaGlazedTerracottaEntity be) {
        if (state.get(Properties.TRIGGERED) && (!state.get(InsertingMagentaGlazedTerracotta.IMPULSE) || !state.get(Properties.POWERED))) {
            world.setBlockState(pos, state.with(Properties.TRIGGERED, false));
        }
        if (((!state.get(Properties.TRIGGERED) && state.get(InsertingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED)) || (!state.get(BufferingMagentaGlazedTerracotta.IMPULSE) && state.get(Properties.POWERED) && be.cooldown--<0)) && !world.isClient) {
            if (!be.inventory.stream().allMatch(ItemStack::isEmpty)) {
                Direction direction = state.get(Properties.FACING);
                Inventory to = HopperBlockEntity.getInventoryAt(world, pos.offset(direction));
                if (to != null) {
                    List<ItemStack> formerBe = be.inventory.stream().toList();
                    for (int i = 0; i < be.inventory.size(); i++) {
                        ItemStack itemStack2 = HopperBlockEntity.transfer(be, to, be.getStack(i).copy(), direction.getOpposite());
                        if (itemStack2.getCount() != be.inventory.get(i).getCount()) {
                            be.inventory.set(i, itemStack2);
                        }
                    }
                    if (!formerBe.equals(be.inventory.stream().toList())) {
                        world.updateNeighbors(pos, state.getBlock());
                        be.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
                    }
                }           //^ Run when target inventory is !empty
                be.cooldown = COOL_DOWN;
            }           //^ Run when inventory is !empty
            if (state.get(InsertingMagentaGlazedTerracotta.IMPULSE)) {
                world.setBlockState(pos, state.with(Properties.TRIGGERED, true));
            }
        }           //^ Run when "impulse + !triggered + powered" or "!impulse + powered + !cooldown"

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

    private void debugMessage(String string) {
        this.getWorld().getPlayers().forEach(player -> player.sendMessage(Text.literal(string)));
    }

}
//world.getPlayers().forEach(player -> player.sendMessage(Text.literal("Yes")));