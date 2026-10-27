package com.sinrok.clearsight;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public final class ClearSightConfig {
    public boolean hideFire = true;
    public boolean noHurtCamera = true;
    public boolean hidePortalDistortion = false;
    public boolean hideVignette = false;
    public boolean shrinkHeldTotem = true;
    public boolean shrinkPopTotem = true;
    public float heldTotemScale = 0.65f;
    public float popTotemScale = 0.50f;

    private Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("clearsight-utilities.properties");
    }

    public void load() {
        Path path = path();
        if (!Files.isRegularFile(path)) {
            save();
            return;
        }

        Properties props = new Properties();
        try (InputStream input = Files.newInputStream(path)) {
            props.load(input);
            hideFire = Boolean.parseBoolean(props.getProperty("hideFire", Boolean.toString(hideFire)));
            noHurtCamera = Boolean.parseBoolean(props.getProperty("noHurtCamera", Boolean.toString(noHurtCamera)));
            hidePortalDistortion = Boolean.parseBoolean(props.getProperty("hidePortalDistortion", Boolean.toString(hidePortalDistortion)));
            hideVignette = Boolean.parseBoolean(props.getProperty("hideVignette", Boolean.toString(hideVignette)));
            shrinkHeldTotem = Boolean.parseBoolean(props.getProperty("shrinkHeldTotem", Boolean.toString(shrinkHeldTotem)));
            shrinkPopTotem = Boolean.parseBoolean(props.getProperty("shrinkPopTotem", Boolean.toString(shrinkPopTotem)));
            heldTotemScale = readScale(props, "heldTotemScale", heldTotemScale);
            popTotemScale = readScale(props, "popTotemScale", popTotemScale);
        } catch (IOException exception) {
            System.err.println("[ClearSight] Could not load config: " + exception.getMessage());
        }
    }

    private float readScale(Properties props, String key, float fallback) {
        try {
            return Math.max(0.25f, Math.min(1.0f, Float.parseFloat(props.getProperty(key, Float.toString(fallback)))));
        } catch (NumberFormatException ignored) {
            return fallback;
        }
    }

    public void save() {
        Path path = path();
        Properties props = new Properties();
        props.setProperty("hideFire", Boolean.toString(hideFire));
        props.setProperty("noHurtCamera", Boolean.toString(noHurtCamera));
        props.setProperty("hidePortalDistortion", Boolean.toString(hidePortalDistortion));
        props.setProperty("hideVignette", Boolean.toString(hideVignette));
        props.setProperty("shrinkHeldTotem", Boolean.toString(shrinkHeldTotem));
        props.setProperty("shrinkPopTotem", Boolean.toString(shrinkPopTotem));
        props.setProperty("heldTotemScale", Float.toString(heldTotemScale));
        props.setProperty("popTotemScale", Float.toString(popTotemScale));

        try {
            Files.createDirectories(path.getParent());
            try (OutputStream output = Files.newOutputStream(path)) {
                props.store(output, "ClearSight Utilities settings");
            }
        } catch (IOException exception) {
            System.err.println("[ClearSight] Could not save config: " + exception.getMessage());
        }
    }
}
