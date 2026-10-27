package com.sinrok.clearsight.mixin;

import com.sinrok.clearsight.ClearSightClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    private void clearsight$hidePortalDistortion(DrawContext context, float nauseaStrength, CallbackInfo ci) {
        if (ClearSightClient.CONFIG.hidePortalDistortion) {
            ci.cancel();
        }
    }

    @Inject(method = "renderVignetteOverlay", at = @At("HEAD"), cancellable = true)
    private void clearsight$hideVignette(DrawContext context, Entity entity, CallbackInfo ci) {
        if (ClearSightClient.CONFIG.hideVignette) {
            ci.cancel();
        }
    }
}
