package com.wurtzitane.gregoriusdrugworkspersistence.inhalation;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;

public final class InhalationParticleMotion {

    private InhalationParticleMotion() {
    }

    public static Vec3d horizontalForward(EntityPlayer player) {
        Vec3d look = player.getLookVec();
        Vec3d horizontal = new Vec3d(look.x, 0.0D, look.z);

        if (horizontal.lengthSquared() < 1.0E-6D) {
            double yawRadians = Math.toRadians(player.rotationYawHead);
            return new Vec3d(-Math.sin(yawRadians), 0.0D, Math.cos(yawRadians));
        }

        return horizontal.normalize();
    }

    public static Vec3d plumeDirection(EntityPlayer player) {
        Vec3d look = player.getLookVec().normalize();
        Vec3d horizontal = horizontalForward(player);
        double lift = 0.28D + Math.max(0.0D, look.y) * 0.35D;

        return horizontal.scale(0.92D).add(0.0D, lift, 0.0D).normalize();
    }

    public static Vec3d mouthOrigin(EntityPlayer player, double forwardDistance, double verticalOffset) {
        Vec3d eye = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
        double extraLift = Math.max(0.0D, player.getLookVec().y) * 0.04D;
        return eye.add(horizontalForward(player).scale(forwardDistance)).add(0.0D, verticalOffset + extraLift, 0.0D);
    }
}
