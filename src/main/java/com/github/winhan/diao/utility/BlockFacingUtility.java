package com.github.winhan.diao.utility;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;


public enum BlockFacingUtility {
    NORTH(Direction.NORTH, new Vec3i(0, 0, 1), new Vec3i(0, 0, -1), new Vec3d(0, 0, 1), new Vec3d(0, 0, -1)),
    SOUTH(Direction.SOUTH, new Vec3i(0, 0, -1), new Vec3i(0, 0, 1), new Vec3d(0, 0, -1), new Vec3d(0, 0, 1)),
    EAST(Direction.EAST, new Vec3i(-1, 0, 0), new Vec3i(1, 0, 0), new Vec3d(-1, 0, 0), new Vec3d(1, 0, 0)),
    WEST(Direction.WEST, new Vec3i(1, 0, 0), new Vec3i(-1, 0, 0), new Vec3d(1, 0, 0), new Vec3d(-1, 0, 0)),
    UP(Direction.UP, new Vec3i(0, -1, 0), new Vec3i(0, 1, 0), new Vec3d(0, -1, 0), new Vec3d(0, 1, 0)),
    DOWN(Direction.DOWN, new Vec3i(0, 1, 0), new Vec3i(0, -1, 0), new Vec3d(0, 1, 0), new Vec3d(0, -1, 0));

    private Direction direction;
    private Vec3i vec3i;
    private Vec3i vec3iOpposite;
    private Vec3d vec3d;
    private Vec3d vec3dOpposite;


    public Direction getDirection() {
        return direction;
    }

    public Vec3i getVec3i() {
        return vec3i;
    }

    public Vec3i getVec3iOpposite() {
        return vec3iOpposite;
    }

    public Vec3d getVec3d() {
        return vec3d;
    }

    public Vec3d getVec3dOpposite() {
        return vec3dOpposite;
    }


    BlockFacingUtility(Direction direction, Vec3i vec3i, Vec3i vec3iOpposite, Vec3d vec3d, Vec3d vec3dOpposite) {
        this.direction = direction;
        this.vec3i = vec3i;
        this.vec3iOpposite = vec3iOpposite;
        this.vec3d = vec3d;
        this.vec3dOpposite = vec3dOpposite;
    }

    public static BlockFacingUtility getByDirection(Direction direction) {
        for (BlockFacingUtility b : BlockFacingUtility.values()) {
            if (b.direction == direction) {
                return b;
            }
        }
        return null;
    }
}
