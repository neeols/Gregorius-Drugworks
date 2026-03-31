package com.wurtzitane.gregoriusdrugworkspersistence.machine;

import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterOpacityParser;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintStacks;
import com.wurtzitane.gregoriusdrugworkspersistence.blotter.BlotterPrintableRegistry;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksCreativeTabs;
import com.wurtzitane.gregoriusdrugworkspersistence.event.GregoriusDrugworksItems;
import gregtech.api.GTValues;
import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.gui.widgets.SimpleTextWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.gui.widgets.TextFieldWidget2;
import gregtech.api.items.itemhandlers.GTItemStackHandler;
import gregtech.api.capability.impl.EnergyContainerHandler;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.TieredMetaTileEntity;
import gregtech.api.util.GTTransferUtils;
import gregtech.client.renderer.texture.cube.SimpleSidedCubeRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Single-block blotter printer with text-driven print selection.
 *
 * @author wurtzitane
 */
public final class MetaTileEntityBlotterPrinter extends TieredMetaTileEntity {

    private static final SimpleSidedCubeRenderer BASE_TEXTURE =
            new SimpleSidedCubeRenderer("gregoriusdrugworkspersistence:machine/blotter_printer");
    private static final int PROCESS_DURATION_TICKS = 20;

    private String imageInput = "";
    private String opacityInput = "100%";
    private String imageErrorText = "";
    private String opacityErrorText = "";
    private int progressTicks;
    private int maxProgressTicks;
    private ItemStack pendingOutput = ItemStack.EMPTY;

    public MetaTileEntityBlotterPrinter(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, GTValues.ULV);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity tileEntity) {
        return new MetaTileEntityBlotterPrinter(metaTileEntityId);
    }

    @Override
    protected void reinitializeEnergyContainer() {
        this.energyContainer = EnergyContainerHandler.receiverContainer(this, 0L, 0L, 0L);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SimpleSidedCubeRenderer getBaseRenderer() {
        return BASE_TEXTURE;
    }

    @Override
    protected IItemHandlerModifiable createImportItemHandler() {
        return new GTItemStackHandler(this, 1) {
            @Nonnull
            @Override
            public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
                if (BlotterPrintStacks.isBlankBlotterPaper(stack)) {
                    return super.insertItem(slot, stack, simulate);
                }
                return stack;
            }
        };
    }

    @Override
    protected IItemHandlerModifiable createExportItemHandler() {
        return new GTItemStackHandler(this, 1);
    }

    @Override
    public void update() {
        super.update();
        if (getWorld().isRemote) {
            return;
        }

        if (!pendingOutput.isEmpty()) {
            if (progressTicks < maxProgressTicks) {
                progressTicks++;
                markDirty();
            } else if (GTTransferUtils.addItemsToItemHandler(exportItems, true, java.util.Collections.singletonList(pendingOutput))) {
                GTTransferUtils.addItemsToItemHandler(exportItems, false, java.util.Collections.singletonList(pendingOutput.copy()));
                pendingOutput = ItemStack.EMPTY;
                progressTicks = 0;
                maxProgressTicks = 0;
                markDirty();
                notifyBlockUpdate();
                scheduleRenderUpdate();
            }
        }

        if (getOffsetTimer() % 5L == 0L) {
            pushItemsIntoNearbyHandlers(getFrontFacing());
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound data) {
        super.writeToNBT(data);
        data.setString("ImageInput", imageInput);
        data.setString("OpacityInput", opacityInput);
        data.setInteger("ProgressTicks", progressTicks);
        data.setInteger("MaxProgressTicks", maxProgressTicks);
        if (!pendingOutput.isEmpty()) {
            data.setTag("PendingOutput", pendingOutput.writeToNBT(new NBTTagCompound()));
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound data) {
        super.readFromNBT(data);
        imageInput = data.getString("ImageInput");
        opacityInput = data.getString("OpacityInput");
        progressTicks = data.getInteger("ProgressTicks");
        maxProgressTicks = data.getInteger("MaxProgressTicks");
        pendingOutput = data.hasKey("PendingOutput") ? new ItemStack(data.getCompoundTag("PendingOutput")) : ItemStack.EMPTY;
    }

    @Override
    protected ModularUI createUI(EntityPlayer entityPlayer) {
        return ModularUI.builder(GuiTextures.BACKGROUND, 176, 198)
                .label(8, 6, getMetaFullName())
                .widget(new SlotWidget(importItems, 0, 26, 24, true, true)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.IN_SLOT_OVERLAY))
                .widget(new SlotWidget(exportItems, 0, 134, 24, true, false)
                        .setBackgroundTexture(GuiTextures.SLOT, GuiTextures.OUT_SLOT_OVERLAY))
                .widget(new ProgressWidget(this::getProgressPercent, 74, 27, 28, 18,
                        GuiTextures.PROGRESS_BAR_ARROW, ProgressWidget.MoveType.HORIZONTAL))
                .label(10, 64, "gregoriusdrugworkspersistence.machine.blotter_printer.image")
                .widget(new ImageWidget(54, 58, 112, 20, GuiTextures.DISPLAY))
                .widget(new TextFieldWidget2(58, 64, 104, 16, this::getImageInput, this::setImageInput)
                        .setMaxLength(96)
                        .setTextColor(0x202020))
                .label(10, 90, "gregoriusdrugworkspersistence.machine.blotter_printer.opacity")
                .widget(new ImageWidget(54, 84, 60, 20, GuiTextures.DISPLAY))
                .widget(new TextFieldWidget2(58, 90, 52, 16, this::getOpacityInput, this::setOpacityInput)
                        .setMaxLength(8)
                        .setTextColor(0x202020))
                .widget(new ClickButtonWidget(122, 84, 44, 20,
                        "gregoriusdrugworkspersistence.machine.blotter_printer.start", data -> startPrinting())
                        .setTooltipText("gregoriusdrugworkspersistence.machine.blotter_printer.start.tooltip"))
                .widget(new SimpleTextWidget(88, 106, "", 0xFF5555, this::getImageErrorText).setCenter(true))
                .widget(new SimpleTextWidget(88, 116, "", 0xFF5555, this::getOpacityErrorText).setCenter(true))
                .bindPlayerInventory(entityPlayer.inventory, GuiTextures.SLOT, 7, 116)
                .build(getHolder(), entityPlayer);
    }

    @Override
    public boolean isInCreativeTab(net.minecraft.creativetab.CreativeTabs creativeTab) {
        return super.isInCreativeTab(creativeTab) || creativeTab == GregoriusDrugworksCreativeTabs.MAIN;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregoriusdrugworkspersistence.machine.blotter_printer.tooltip"));
    }

    @Override
    public void addToolUsages(ItemStack stack, @Nullable World world, List<String> tooltip, boolean advanced) {
        tooltip.add(I18n.format("gregtech.tool_action.wrench.set_facing"));
        tooltip.add(I18n.format("gregtech.tool_action.screwdriver.access_covers"));
        super.addToolUsages(stack, world, tooltip, advanced);
    }

    private double getProgressPercent() {
        if (pendingOutput.isEmpty() || maxProgressTicks <= 0) {
            return 0.0D;
        }
        return Math.min(1.0D, progressTicks / (double) maxProgressTicks);
    }

    private void startPrinting() {
        clearValidationErrors();
        if (getWorld() == null || getWorld().isRemote || !pendingOutput.isEmpty()) {
            return;
        }

        ItemStack inputStack = importItems.getStackInSlot(0);
        BlotterPrintableRegistry.Entry printable = BlotterPrintableRegistry.resolve(imageInput);
        BlotterOpacityParser.ParseResult opacity = BlotterOpacityParser.parse(opacityInput);

        if (printable == null) {
            imageErrorText = "! IMAGE STRING INVALID !";
        }
        if (!opacity.isValid()) {
            opacityErrorText = "! OPACITY INVALID !";
        }
        if (printable == null || !opacity.isValid()) {
            markDirty();
            return;
        }
        if (!BlotterPrintStacks.isBlankBlotterPaper(inputStack)) {
            return;
        }

        ItemStack resultStack = BlotterPrintStacks.createPrintedBlotter(printable, opacity.getOpacityPercent());
        if (resultStack.isEmpty()) {
            return;
        }
        if (!GTTransferUtils.addItemsToItemHandler(exportItems, true, java.util.Collections.singletonList(resultStack))) {
            return;
        }

        ItemStack extracted = importItems.extractItem(0, 1, false);
        if (extracted.isEmpty()) {
            return;
        }

        pendingOutput = resultStack;
        progressTicks = 0;
        maxProgressTicks = PROCESS_DURATION_TICKS;
        markDirty();
        notifyBlockUpdate();
        scheduleRenderUpdate();
    }

    private void clearValidationErrors() {
        imageErrorText = "";
        opacityErrorText = "";
    }

    private String getImageInput() {
        return imageInput;
    }

    private void setImageInput(String imageInput) {
        this.imageInput = imageInput == null ? "" : imageInput;
        this.imageErrorText = "";
        markDirty();
    }

    private String getOpacityInput() {
        return opacityInput;
    }

    private void setOpacityInput(String opacityInput) {
        this.opacityInput = opacityInput == null ? "" : opacityInput;
        this.opacityErrorText = "";
        markDirty();
    }

    private String getImageErrorText() {
        return imageErrorText;
    }

    private String getOpacityErrorText() {
        return opacityErrorText;
    }
}
