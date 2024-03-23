package com.github.winhan.diao.utility;

import net.minecraft.util.math.Vec3d;

public class VelocityToPos {
    public static Vec3d getVelocityToPos(Vec3d targetPos, Vec3d currentPos, double velocity) {
        Vec3d vec3dDistance = targetPos.subtract(currentPos);
        double x = vec3dDistance.getX();
        double y = vec3dDistance.getY();
        double z = vec3dDistance.getZ();
        double distance = Math.sqrt(x*x+y*y+z*z);
        return vec3dDistance.multiply((distance <= 0.01) ? 0 : (velocity / distance));
    }
}
