package com.wurtzitane.gregoriusdrugworkspersistence.pill.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public final class PillAnimationState {

    private final int playerEntityId;
    private final String itemId;
    private final EnumHand hand;
    private final long startTick;
    private final int durationTicks;
    private final float arcHeight;
    private final float launchForward;
    private final float mouthOffsetY;
    private final float spinXPerTick;
    private final float spinYPerTick;
    private final float spinZPerTick;
    private final boolean lockCamera;
    private final int sequenceId;
    private final ItemStack renderStack;
    private final Vec3d anchorPlayerPos;
    private final Vec3d anchorStart;
    private final Vec3d anchorControl;
    private final Vec3d anchorApex;
    private final Vec3d anchorDescentAnchor;
    private final Vec3d anchorMouth;

    public PillAnimationState(
            int playerEntityId,
            String itemId,
            EnumHand hand,
            long startTick,
            int durationTicks,
            float arcHeight,
            float launchForward,
            float mouthOffsetY,
            float spinXPerTick,
            float spinYPerTick,
            float spinZPerTick,
            boolean lockCamera,
            int sequenceId,
            ItemStack renderStack,
            Vec3d anchorPlayerPos,
            Vec3d anchorStart,
            Vec3d anchorControl,
            Vec3d anchorApex,
            Vec3d anchorDescentAnchor,
            Vec3d anchorMouth
    ) {
        this.playerEntityId = playerEntityId;
        this.itemId = itemId;
        this.hand = hand;
        this.startTick = startTick;
        this.durationTicks = durationTicks;
        this.arcHeight = arcHeight;
        this.launchForward = launchForward;
        this.mouthOffsetY = mouthOffsetY;
        this.spinXPerTick = spinXPerTick;
        this.spinYPerTick = spinYPerTick;
        this.spinZPerTick = spinZPerTick;
        this.lockCamera = lockCamera;
        this.sequenceId = sequenceId;
        this.renderStack = renderStack.copy();
        this.anchorPlayerPos = anchorPlayerPos;
        this.anchorStart = anchorStart;
        this.anchorControl = anchorControl;
        this.anchorApex = anchorApex;
        this.anchorDescentAnchor = anchorDescentAnchor;
        this.anchorMouth = anchorMouth;
    }

    public static PillAnimationState capture(
            int playerEntityId,
            String itemId,
            EnumHand hand,
            long startTick,
            int durationTicks,
            float arcHeight,
            float launchForward,
            float mouthOffsetY,
            float spinXPerTick,
            float spinYPerTick,
            float spinZPerTick,
            boolean lockCamera,
            int sequenceId,
            ItemStack renderStack,
            EntityPlayer player
    ) {
        Vec3d anchorPlayerPos = getInterpolatedPlayerPos(player, 1.0F);
        Vec3d eye = player.getPositionEyes(1.0F);
        Vec3d look = player.getLook(1.0F).normalize();

        Vec3d right = look.crossProduct(new Vec3d(0.0D, 1.0D, 0.0D));
        if (right.lengthSquared() < 1.0E-6D) {
            right = new Vec3d(1.0D, 0.0D, 0.0D);
        } else {
            right = right.normalize();
        }

        double handSign = 1.0D;
        if (player.getPrimaryHand() == EnumHandSide.LEFT) {
            handSign = -1.0D;
        }
        if (hand == EnumHand.OFF_HAND) {
            handSign = -handSign;
        }

        Vec3d start = offset(
                eye.add(right.scale(0.23D * handSign)).add(look.scale(0.18D)),
                0.0D, -0.14D, 0.0D
        );

        Vec3d mouth = offset(
                eye.add(look.scale(0.10D)),
                0.0D, mouthOffsetY - 0.15D, 0.0D
        );

        Vec3d apex = offset(
                eye.add(look.scale(launchForward)),
                0.0D, arcHeight, 0.0D
        );

        Vec3d control = offset(
                start.add(look.scale(launchForward * 0.55D)),
                0.0D, arcHeight * 0.70D, 0.0D
        );

        Vec3d descentAnchor = new Vec3d(mouth.x, mouth.y + (arcHeight * 0.45D), mouth.z);

        return new PillAnimationState(
                playerEntityId,
                itemId,
                hand,
                startTick,
                durationTicks,
                arcHeight,
                launchForward,
                mouthOffsetY,
                spinXPerTick,
                spinYPerTick,
                spinZPerTick,
                lockCamera,
                sequenceId,
                renderStack,
                anchorPlayerPos,
                start,
                control,
                apex,
                descentAnchor,
                mouth
        );
    }

    public int getPlayerEntityId() {
        return playerEntityId;
    }

    public String getItemId() {
        return itemId;
    }

    public EnumHand getHand() {
        return hand;
    }

    public boolean isLockCamera() {
        return lockCamera;
    }

    public int getSequenceId() {
        return sequenceId;
    }

    public ItemStack getRenderStack() {
        return renderStack.copy();
    }

    public boolean isExpired(long worldTime) {
        return worldTime >= startTick + durationTicks;
    }

    public float getProgress(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        return MathHelper.clamp(age / (float) durationTicks, 0.0F, 1.0F);
    }

    public float getRotationX(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        return age * spinXPerTick;
    }

    public float getRotationY(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        return age * spinYPerTick;
    }

    public float getRotationZ(long worldTime, float partialTicks) {
        float age = (worldTime - startTick) + partialTicks;
        return age * spinZPerTick;
    }

    public Vec3d getPillPosition(EntityPlayer player, long worldTime, float partialTicks) {
        float progress = getProgress(worldTime, partialTicks);
        Vec3d movementDelta = getInterpolatedPlayerPos(player, partialTicks).subtract(anchorPlayerPos);
        Vec3d start = anchorStart.add(movementDelta);
        Vec3d control = anchorControl.add(movementDelta);
        Vec3d apex = anchorApex.add(movementDelta);
        Vec3d descentAnchor = anchorDescentAnchor.add(movementDelta);
        Vec3d mouth = anchorMouth.add(movementDelta);

        if (progress <= 0.55F) {
            float launchProgress = smooth(progress / 0.55F);
            return quadraticBezier(start, control, apex, launchProgress);
        }

        float descendProgress = (progress - 0.55F) / 0.45F;
        if (descendProgress <= 0.35F) {
            float settle = smooth(descendProgress / 0.35F);
            return lerp(apex, descentAnchor, settle);
        }

        float drop = smooth((descendProgress - 0.35F) / 0.65F);
        return lerp(descentAnchor, mouth, drop);
    }

    private static Vec3d getInterpolatedPlayerPos(EntityPlayer player, float partialTicks) {
        return new Vec3d(
                player.prevPosX + (player.posX - player.prevPosX) * partialTicks,
                player.prevPosY + (player.posY - player.prevPosY) * partialTicks,
                player.prevPosZ + (player.posZ - player.prevPosZ) * partialTicks
        );
    }

    private static Vec3d quadraticBezier(Vec3d start, Vec3d control, Vec3d end, float t) {
        double inv = 1.0D - t;
        return start.scale(inv * inv)
                .add(control.scale(2.0D * inv * t))
                .add(end.scale(t * t));
    }

    private static Vec3d offset(Vec3d vec, double x, double y, double z) {
        return new Vec3d(vec.x + x, vec.y + y, vec.z + z);
    }

    private static Vec3d lerp(Vec3d a, Vec3d b, float t) {
        return new Vec3d(
                a.x + (b.x - a.x) * t,
                a.y + (b.y - a.y) * t,
                a.z + (b.z - a.z) * t
        );
    }

    private static float smooth(float value) {
        float v = MathHelper.clamp(value, 0.0F, 1.0F);
        return v * v * (3.0F - 2.0F * v);
    }
}
