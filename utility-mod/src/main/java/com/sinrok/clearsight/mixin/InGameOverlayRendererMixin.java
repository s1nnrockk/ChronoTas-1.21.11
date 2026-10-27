package com.sinrok.clearsight.mixin;

import com.sinrok.clearsight.ClearSightClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public abstract class InGameOverlayRendererMixin {
    @Shadow private ItemStack floatingItem;
    @Shadow @Final private MinecraftClient client;

    @Unique private boolean clearsight$popScaleApplied;

    @Inject(method = "renderFireOverlay", at = @At("HEAD"), cancellable = true)
    private static void clearsight$hideFireOverlay(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Sprite sprite, CallbackInfo ci) {
        if (ClearSightClient.CONFIG.hideFire) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFloatingItem", at = @At("HEAD"))
    private void clearsight$scaleTotemPop(MatrixStack matrices, float tickProgress, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        clearsight$popScaleApplied = false;
        float scale = ClearSightClient.CONFIG.popTotemScale;
        if (!ClearSightClient.CONFIG.shrinkPopTotem || floatingItem == null ||
                !floatingItem.isOf(Items.TOTEM_OF_UNDYING) || scale >= 0.999f) {
            return;
        }

        double centerX = client.getWindow().getScaledWidth() / 2.0;
        double centerY = client.getWindow().getScaledHeight() / 2.0;
        matrices.push();
        matrices.translate(centerX * (1.0 - scale), centerY * (1.0 - scale), 0.0);
        matrices.scale(scale, scale, scale);
        clearsight$popScaleApplied = true;
    }

    @Inject(method = "renderFloatingItem", at = @At("RETURN"))
    private void clearsight$restoreTotemPopScale(MatrixStack matrices, float tickProgress, OrderedRenderCommandQueue queue, CallbackInfo ci) {
        if (clearsight$popScaleApplied) {
            matrices.pop();
            clearsight$popScaleApplied = false;
        }
    }
}
