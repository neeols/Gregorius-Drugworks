package com.wurtzitane.gregoriusdrugworkspersistence.recipe.chance;

import gregtech.api.GregTechAPI;
import gregtech.api.recipes.properties.RecipeProperty;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public final class ChancedInputRecipeProperty extends RecipeProperty<ChancedInputRecipePropertyValue> {

    public static final String KEY = "gdw_chanced_inputs";

    private static final String ITEMS_TAG = "Items";
    private static final String FLUIDS_TAG = "Fluids";
    private static final String INGREDIENT_TAG = "Ingredient";
    private static final String CHANCE_TAG = "Chance";
    private static final String CHANCE_BOOST_TAG = "ChanceBoost";
    private static final String TIER_REDUCTION_RATE_TAG = "TierReductionRate";

    private static ChancedInputRecipeProperty INSTANCE;

    private ChancedInputRecipeProperty() {
        super(KEY, ChancedInputRecipePropertyValue.class);
    }

    public static ChancedInputRecipeProperty getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ChancedInputRecipeProperty();
            GregTechAPI.RECIPE_PROPERTIES.register(KEY, INSTANCE);
        }
        return INSTANCE;
    }

    @Override
    public @NotNull NBTBase serialize(@NotNull Object value) {
        ChancedInputRecipePropertyValue propertyValue = castValue(value);
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList items = new NBTTagList();
        for (ChancedItemInputEntry entry : propertyValue.getItemInputs()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setTag(INGREDIENT_TAG, entry.getIngredient().writeToNBT(new NBTTagCompound()));
            entryTag.setInteger(CHANCE_TAG, entry.getChance());
            entryTag.setInteger(CHANCE_BOOST_TAG, entry.getChanceBoost());
            entryTag.setInteger(TIER_REDUCTION_RATE_TAG, entry.getTierReductionRate());
            items.appendTag(entryTag);
        }
        NBTTagList fluids = new NBTTagList();
        for (ChancedFluidInputEntry entry : propertyValue.getFluidInputs()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setTag(INGREDIENT_TAG, entry.getIngredient().writeToNBT(new NBTTagCompound()));
            entryTag.setInteger(CHANCE_TAG, entry.getChance());
            entryTag.setInteger(CHANCE_BOOST_TAG, entry.getChanceBoost());
            entryTag.setInteger(TIER_REDUCTION_RATE_TAG, entry.getTierReductionRate());
            fluids.appendTag(entryTag);
        }
        tag.setTag(ITEMS_TAG, items);
        tag.setTag(FLUIDS_TAG, fluids);
        return tag;
    }

    @Override
    public @NotNull Object deserialize(@NotNull NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        ChancedInputRecipePropertyValue propertyValue = ChancedInputRecipePropertyValue.empty();
        NBTTagList items = tag.getTagList(ITEMS_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < items.tagCount(); i++) {
            NBTTagCompound entryTag = items.getCompoundTagAt(i);
            ItemStack ingredient = new ItemStack(entryTag.getCompoundTag(INGREDIENT_TAG));
            propertyValue = ChancedInputRecipePropertyValue.merge(propertyValue,
                    ChancedInputRecipePropertyValue.ofItem(new ChancedItemInputEntry(
                            ingredient,
                            entryTag.getInteger(CHANCE_TAG),
                            entryTag.getInteger(CHANCE_BOOST_TAG),
                            entryTag.getInteger(TIER_REDUCTION_RATE_TAG))));
        }
        NBTTagList fluids = tag.getTagList(FLUIDS_TAG, Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < fluids.tagCount(); i++) {
            NBTTagCompound entryTag = fluids.getCompoundTagAt(i);
            FluidStack ingredient = Objects.requireNonNull(
                    FluidStack.loadFluidStackFromNBT(entryTag.getCompoundTag(INGREDIENT_TAG)),
                    "Failed to deserialize chanced fluid input");
            propertyValue = ChancedInputRecipePropertyValue.merge(propertyValue,
                    ChancedInputRecipePropertyValue.ofFluid(new ChancedFluidInputEntry(
                            ingredient,
                            entryTag.getInteger(CHANCE_TAG),
                            entryTag.getInteger(CHANCE_BOOST_TAG),
                            entryTag.getInteger(TIER_REDUCTION_RATE_TAG))));
        }
        return propertyValue;
    }

    @Override
    public void drawInfo(Minecraft minecraft, int x, int y, int color, Object value) {
    }

    @Override
    public boolean isHidden() {
        return true;
    }
}
