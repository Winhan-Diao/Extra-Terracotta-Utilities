package com.github.winhan.diao;

import com.github.winhan.diao.utility.BlockFacingUtility;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.github.winhan.diao.EntropyReducingMagentaGlazedTerracotta.BREAK_CHANCE;
import static net.minecraft.block.Block.NOTIFY_ALL;
import static net.minecraft.block.Block.NOTIFY_LISTENERS;

public class EntropyReducingMagentaGlazedTerracottaEntity extends BlockEntity {
    public static final int ENTROPY_COOLDOWN = 400;
    private String target = "null";
    private int cooldown = ENTROPY_COOLDOWN;

    public EntropyReducingMagentaGlazedTerracottaEntity(BlockPos pos, BlockState state) {
        super(Initializer.ENTROPY_REDUCING_MAGENTA_GLAZED_TERRACOTTA_ENTITY, pos, state);
    }

    public String getTarget() {
        return target;
    }

    /**To do:
     * remove awkward usage of utility ( )
     * improve code logic ( )**/
     public static void constantTick(World world, BlockPos pos, BlockState state, EntropyReducingMagentaGlazedTerracottaEntity be) {
        Vec3i posOffset = BlockFacingUtility.getByDirection(state.get(Properties.FACING)).getVec3i();
        if (!world.isClient) {
            List<ItemEntity> list = world.getEntitiesByClass(ItemEntity.class, new Box(pos.add(posOffset)), EntityPredicates.VALID_ENTITY);
            NbtCompound nbt = new NbtCompound();
            be.writeNbt(nbt);
            if (be.cooldown < 0) {      //Triggered at the end of cooldown.
                if (!list.isEmpty() && !list.get(0).getStack().isOf(Items.AIR)) {
                    be.placeBlockFromStack(world, pos.add(posOffset), list.get(0).getStack());
                    world.playSound(null, pos, SoundEvents.BLOCK_POWDER_SNOW_STEP, SoundCategory.BLOCKS, 1, 1);
                    list.get(0).setStack(ItemStack.EMPTY);
                    chanceToBreakIce(pos.subtract(posOffset), world, BREAK_CHANCE);
                }
                nbt.putInt("cooldown", ENTROPY_COOLDOWN);
                nbt.putString("target", "null");
                be.readNbt(nbt);
                world.updateListeners(pos, state, state, NOTIFY_LISTENERS);
                be.markDirty();
            } else {
                if (be.target.equals("null")) {     //Triggered when there's no target. Will search for target.
                    if (list.size() == 1 && list.get(0).getStack().getCount() == 1
                            && world.getBlockState(pos.add(posOffset)).isAir()
                            && world.getBlockState(pos.subtract(posOffset)).isIn(BlockTags.ICE)) {
                        if (list.get(0).getStack().getItem() instanceof BlockItem) {
                            world.playSound(null, pos, SoundEvents.BLOCK_POWDER_SNOW_STEP, SoundCategory.BLOCKS, 1, 1);
                            nbt.putString("target", list.get(0).getStack().getTranslationKey());        /*Set the target from the list of one stack*/
                            be.readNbt(nbt);
                            world.updateListeners(pos, state, state, NOTIFY_LISTENERS);
                            be.markDirty();
                        }
                    }
                } else {        //Triggered when there's a target already. Will count down the cooldown.
                    if (list.size() == 1
                            && world.getBlockState(pos.add(posOffset)).isAir()
                            && world.getBlockState(pos.subtract(posOffset)).isIn(BlockTags.ICE)) {
                        be.cooldown--;
                    } else {
                        be.target = "null";
                        be.cooldown = ENTROPY_COOLDOWN;
                        world.updateListeners(pos, state, state, NOTIFY_LISTENERS);
                        be.markDirty();
                    }
                }
            }
        } else {
            if (!be.target.equals("null")) {
                Random random = world.getRandom();
                world.addParticle(ParticleTypes.SNOWFLAKE,
                        pos.add(posOffset).getX() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                        pos.add(posOffset).getY() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                        pos.add(posOffset).getZ() + .5 + MathHelper.nextBetween(random, -.4f, .4f),
                        0, 0, 0);
            }
        }
    }


        @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.cooldown = nbt.getInt("cooldown");
        this.target = nbt.getString("target");
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("cooldown", this.cooldown);
        nbt.putString("target", this.target);
    }

    public void placeBlockFromStack(World world, BlockPos pos, ItemStack itemStack) {
        BlockState state = ((BlockItem)itemStack.getItem()).getBlock().getDefaultState();
        world.setBlockState(pos, state, NOTIFY_ALL);
        try {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            blockEntity.readNbt(itemStack.getNbt().getCompound("BlockEntityTag"));
            blockEntity.markDirty();
        } catch (NullPointerException ignored) {}
    }

    public static void chanceToBreakIce(BlockPos pos, World world, int chance) {
        Random random = world.getRandom();
        if (random.nextInt(100) < chance) {
            world.breakBlock(pos, true);
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

}
