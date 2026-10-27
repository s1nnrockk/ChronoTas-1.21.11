package com.sinrok.clearsight.mixin;

import com.sinrok.clearsight.ClearSightClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
    @Unique private boolean clearsight$heldScaleApplied;

    @Inject(method = "renderFirstPersonItem", at = @At("HEAD"))
    private void clearsight$scaleHeldTotem(AbstractClientPlayerEntity player, float tickProgress, float pitch,
                                            Hand hand, float swingProgress, ItemStack item, float equipProgress,
                                            MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                                            CallbackInfo ci) {
        clearsight$heldScaleApplied = false;
        float scale = ClearSightClient.CONFIG.heldTotemScale;
        if (!ClearSightClient.CONFIG.shrinkHeldTotem || item == null ||
                !item.isOf(Items.TOTEM_OF_UNDYING) || scale >= 0.999f) {
            return;
        }
        matrices.push();
        matrices.scale(scale, scale, scale);
        clearsight$heldScaleApplied = true;
    }

    @Inject(method = "renderFirstPersonItem", at = @At("RETURN"))
    private void clearsight$restoreHeldTotem(AbstractClientPlayerEntity player, float tickProgress, float pitch,
                                              Hand hand, float swingProgress, ItemStack item, float equipProgress,
                                              MatrixStack matrices, OrderedRenderCommandQueue queue, int light,
                                              CallbackInfo ci) {
        if (clearsight$heldScaleApplied) {
            matrices.pop();
            clearsight$heldScaleApplied = false;
        }
    }
}
