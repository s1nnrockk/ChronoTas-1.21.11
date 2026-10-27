package com.sinrok.clearsight;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ClearSightSettingsScreen extends Screen {
    private static final float[] SCALES = {1.0f, 0.8f, 0.65f, 0.5f, 0.35f, 0.25f};

    public ClearSightSettingsScreen() {
        super(Text.literal("ClearSight Utilities"));
    }

    @Override
    protected void init() {
        int left = this.width / 2 - 155;
        int right = this.width / 2 + 5;
        int y = Math.max(50, this.height / 2 - 86);

        addDrawableChild(ButtonWidget.builder(fireLabel(), button -> {
            ClearSightClient.CONFIG.hideFire = !ClearSightClient.CONFIG.hideFire;
            button.setMessage(fireLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(left, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(hurtLabel(), button -> {
            ClearSightClient.CONFIG.noHurtCamera = !ClearSightClient.CONFIG.noHurtCamera;
            button.setMessage(hurtLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(right, y, 150, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(portalLabel(), button -> {
            ClearSightClient.CONFIG.hidePortalDistortion = !ClearSightClient.CONFIG.hidePortalDistortion;
            button.setMessage(portalLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(left, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(vignetteLabel(), button -> {
            ClearSightClient.CONFIG.hideVignette = !ClearSightClient.CONFIG.hideVignette;
            button.setMessage(vignetteLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(right, y, 150, 20).build());

        y += 30;
        addDrawableChild(ButtonWidget.builder(heldTotemLabel(), button -> {
            ClearSightClient.CONFIG.shrinkHeldTotem = !ClearSightClient.CONFIG.shrinkHeldTotem;
            button.setMessage(heldTotemLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(left, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(heldScaleLabel(), button -> {
            ClearSightClient.CONFIG.heldTotemScale = nextScale(ClearSightClient.CONFIG.heldTotemScale);
            button.setMessage(heldScaleLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(right, y, 150, 20).build());

        y += 26;
        addDrawableChild(ButtonWidget.builder(popTotemLabel(), button -> {
            ClearSightClient.CONFIG.shrinkPopTotem = !ClearSightClient.CONFIG.shrinkPopTotem;
            button.setMessage(popTotemLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(left, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(popScaleLabel(), button -> {
            ClearSightClient.CONFIG.popTotemScale = nextScale(ClearSightClient.CONFIG.popTotemScale);
            button.setMessage(popScaleLabel());
            ClearSightClient.CONFIG.save();
        }).dimensions(right, y, 150, 20).build());

        y += 32;
        addDrawableChild(ButtonWidget.builder(Text.literal("Сбросить"), button -> {
            ClearSightClient.CONFIG.hideFire = true;
            ClearSightClient.CONFIG.noHurtCamera = true;
            ClearSightClient.CONFIG.hidePortalDistortion = false;
            ClearSightClient.CONFIG.hideVignette = false;
            ClearSightClient.CONFIG.shrinkHeldTotem = true;
            ClearSightClient.CONFIG.shrinkPopTotem = true;
            ClearSightClient.CONFIG.heldTotemScale = 0.65f;
            ClearSightClient.CONFIG.popTotemScale = 0.50f;
            ClearSightClient.CONFIG.save();
            clearAndInit();
        }).dimensions(left, y, 150, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Закрыть"), button -> close())
                .dimensions(right, y, 150, 20).build());
    }

    private static Text fireLabel() {
        return Text.literal("Огненный HUD: " + onOff(ClearSightClient.CONFIG.hideFire));
    }

    private static Text hurtLabel() {
        return Text.literal("Тряска: " + (ClearSightClient.CONFIG.noHurtCamera ? "ВЫКЛ" : "ВКЛ"));
    }

    private static Text portalLabel() {
        return Text.literal("Портал: " + (ClearSightClient.CONFIG.hidePortalDistortion ? "скрыт" : "обычно"));
    }

    private static Text vignetteLabel() {
        return Text.literal("Виньетка: " + (ClearSightClient.CONFIG.hideVignette ? "скрыта" : "обычно"));
    }

    private static Text heldTotemLabel() {
        return Text.literal("Тотем в руке: " + onOff(ClearSightClient.CONFIG.shrinkHeldTotem));
    }

    private static Text popTotemLabel() {
        return Text.literal("Тотем при активации: " + onOff(ClearSightClient.CONFIG.shrinkPopTotem));
    }

    private static Text heldScaleLabel() {
        return Text.literal("Размер в руке: " + percent(ClearSightClient.CONFIG.heldTotemScale));
    }

    private static Text popScaleLabel() {
        return Text.literal("Размер анимации: " + percent(ClearSightClient.CONFIG.popTotemScale));
    }

    private static String onOff(boolean enabled) {
        return enabled ? "ВКЛ" : "ВЫКЛ";
    }

    private static String percent(float scale) {
        return Math.round(scale * 100) + "%";
    }

    private static float nextScale(float current) {
        for (int i = 0; i < SCALES.length; i++) {
            if (Math.abs(SCALES[i] - current) < 0.02f) {
                return SCALES[(i + 1) % SCALES.length];
            }
        }
        return 0.5f;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, this.width / 2, 23, 0xFFFFFF);
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("Клавиша H открывает меню. Настройки сохраняются автоматически."),
                this.width / 2,
                36,
                0xAAAAAA
        );
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        ClearSightClient.CONFIG.save();
        super.close();
    }
}
