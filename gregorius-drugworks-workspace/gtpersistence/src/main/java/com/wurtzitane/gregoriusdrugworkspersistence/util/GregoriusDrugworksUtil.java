package com.wurtzitane.gregoriusdrugworkspersistence.util;

import com.wurtzitane.gregoriusdrugworkspersistence.Tags;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraft.util.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GregoriusDrugworksUtil {

    public static boolean isClient() {
        return FMLCommonHandler.instance().getEffectiveSide().isClient();
    }

    public static boolean isServer() {
        return FMLCommonHandler.instance().getEffectiveSide().isServer();
    }

    public static boolean isDedicatedServer() {
        return FMLCommonHandler.instance().getSide().isServer();
    }

    public static ResourceLocation makeName(String name) {
        return new ResourceLocation(Tags.MOD_ID, name);
    }

    public static void setTranslationKeyCompat(Item item, String translationKey) {
        invokeMethod(item, String.class, translationKey,
                "setTranslationKey", "setUnlocalizedName", "func_77655_b");
    }

    public static void setTranslationKeyCompat(Block block, String translationKey) {
        invokeMethod(block, String.class, translationKey,
                "setTranslationKey", "setUnlocalizedName", "func_149663_c");
    }

    public static void setCreativeTabCompat(Item item, CreativeTabs creativeTab) {
        invokeMethod(item, CreativeTabs.class, creativeTab,
                "setCreativeTab", "func_77637_a");
    }

    public static void setCreativeTabCompat(Block block, CreativeTabs creativeTab) {
        invokeMethod(block, CreativeTabs.class, creativeTab,
                "setCreativeTab", "func_149647_a");
    }

    public static void setMaxStackSizeCompat(Item item, int maxStackSize) {
        invokeMethod(item, int.class, maxStackSize,
                "setMaxStackSize", "func_77625_d");
    }

    public static void setMaxDamageCompat(Item item, int maxDamage) {
        invokeMethod(item, int.class, maxDamage,
                "setMaxDamage", "func_77656_e");
    }

    public static void setHardnessCompat(Block block, float hardness) {
        invokeMethod(block, float.class, hardness,
                "setHardness", "func_149711_c");
    }

    public static void setResistanceCompat(Block block, float resistance) {
        invokeMethod(block, float.class, resistance,
                "setResistance", "func_149752_b");
    }

    public static void setSoundTypeCompat(Block block, SoundType soundType) {
        invokeMethod(block, SoundType.class, soundType,
                "setSoundType", "func_149672_a");
    }

    private static void invokeMethod(Object target, Class<?> parameterType, Object argument, String... candidateNames) {
        if (target == null) {
            throw new IllegalArgumentException("Target cannot be null");
        }
        for (String candidateName : candidateNames) {
            try {
                Method method = findMethod(target.getClass(), candidateName, parameterType);
                method.invoke(target, argument);
                return;
            } catch (NoSuchMethodException ignored) {
                // Try the next candidate name.
            } catch (IllegalAccessException | InvocationTargetException exception) {
                throw new IllegalStateException("Failed to invoke " + candidateName + " on "
                        + target.getClass().getName(), exception);
            }
        }
        throw new IllegalStateException("Unable to invoke any of " + String.join(", ", candidateNames)
                + " on " + target.getClass().getName());
    }

    private static Method findMethod(Class<?> type, String name, Class<?> parameterType) throws NoSuchMethodException {
        Class<?> current = type;
        while (current != null) {
            try {
                Method method = current.getDeclaredMethod(name, parameterType);
                method.setAccessible(true);
                return method;
            } catch (NoSuchMethodException ignored) {
                current = current.getSuperclass();
            }
        }
        return type.getMethod(name, parameterType);
    }

    //public static ResourceLocation makeGroovyName(String name) {
    //    return new ResourceLocation(GroovyHelper.getPackId(), name);
    //}

}
