package minegame159.meteorclient.modules.render;

import minegame159.jes.SubscribeEvent;
import minegame159.meteorclient.events.RenderEvent;
import minegame159.meteorclient.modules.Category;
import minegame159.meteorclient.modules.Module;
import minegame159.meteorclient.settings.Setting;
import minegame159.meteorclient.settings.builders.BoolSettingBuilder;
import minegame159.meteorclient.settings.builders.ColorSettingBuilder;
import minegame159.meteorclient.utils.Color;
import minegame159.meteorclient.utils.EntityUtils;
import minegame159.meteorclient.utils.RenderUtils;
import net.minecraft.block.entity.BarrelBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class Tracers extends Module {
    private Setting<Color> color = addSetting(new ColorSettingBuilder()
            .name("color")
            .description("Color.")
            .defaultValue(new Color(205, 205, 205, 255))
            .build()
    );

    private Setting<Boolean> players = addSetting(new BoolSettingBuilder()
            .name("players")
            .description("See players.")
            .defaultValue(true)
            .build()
    );

    private Setting<Boolean> animals = addSetting(new BoolSettingBuilder()
            .name("animals")
            .description("See animals.")
            .defaultValue(true)
            .build()
    );

    private Setting<Boolean> mobs = addSetting(new BoolSettingBuilder()
            .name("mobs")
            .description("See mobs.")
            .defaultValue(true)
            .build()
    );

    private Setting<Boolean> storage = addSetting(new BoolSettingBuilder()
            .name("storage")
            .description("See chests, barrels and shulkers.")
            .defaultValue(false)
            .build()
    );

    private Vec3d vec1;

    public Tracers() {
        super(Category.Render, "tracers", "Displays lines to entities.");
    }

    private void render(Entity entity) {
        Vec3d vec2 = entity.getPos().add(0, entity.getEyeHeight(entity.getPose()), 0);

        RenderUtils.line(vec1.x, vec1.y, vec1.z, vec2.x, vec2.y, vec2.z, color.value());
    }

    @SubscribeEvent
    private void onRender(RenderEvent e) {
        vec1 = new Vec3d(0, 0, 75).rotateX(-(float) Math.toRadians(mc.cameraEntity.pitch)).rotateY(-(float) Math.toRadians(mc.cameraEntity.yaw)).add(mc.cameraEntity.getPos().add(0, mc.cameraEntity.getEyeHeight(mc.cameraEntity.getPose()), 0));

        for (Entity entity : mc.world.getEntities()) {
            if (players.value() && EntityUtils.isPlayer(entity) && entity != mc.player) render(entity);
            else if (animals.value() && EntityUtils.isAnimal(entity)) render(entity);
            else if (mobs.value() && EntityUtils.isMob(entity)) render(entity);
        }

        if (storage.value()) {
            for (BlockEntity blockEntity : mc.world.blockEntities) {
                if (blockEntity instanceof ChestBlockEntity || blockEntity instanceof BarrelBlockEntity || blockEntity instanceof ShulkerBoxBlockEntity) {
                    BlockPos pos = blockEntity.getPos();
                    RenderUtils.line(vec1.x, vec1.y, vec1.z, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5f, color.value());
                }
            }
        }
    }
}