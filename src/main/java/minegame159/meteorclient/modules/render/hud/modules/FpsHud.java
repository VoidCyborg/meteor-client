/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client/).
 * Copyright (c) 2020 Meteor Development.
 */

package minegame159.meteorclient.modules.render.hud.modules;

import minegame159.meteorclient.mixininterface.IMinecraftClient;
import minegame159.meteorclient.modules.render.hud.HUD;
import net.minecraft.client.MinecraftClient;

public class FpsHud extends DoubleTextHudModule {
    public FpsHud(HUD hud) {
        super(hud, "fps", "Displays your FPS.", "Fps: ");
    }

    @Override
    protected String getRight() {
        return Integer.toString(((IMinecraftClient) MinecraftClient.getInstance()).getFps());
    }
}
