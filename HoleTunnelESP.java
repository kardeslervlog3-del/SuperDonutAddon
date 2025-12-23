package com.example.addon.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.BoxRenderer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import java.util.ArrayList;
import java.util.List;

public class HoleTunnelESP extends Module {
    private final Setting<Boolean> holes = settings.getDefaultGroup().add(new BoolSetting.Builder()
        .name("holes")
        .description("Boşluk (hole) ESP")
        .defaultValue(true)
        .build());

    private final Setting<Boolean> tunnels = settings.getDefaultGroup().add(new BoolSetting.Builder()
        .name("tunnels")
        .description("Tünel (obsidian) ESP")
        .defaultValue(true)
        .build());

    public HoleTunnelESP() {
        super(Categories.Render, "hole-tunnel-esp", "Yakın hole ve tünel ESP (base avı).");
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        if (mc.player == null || mc.world == null) return;

        var playerPos = mc.player.getBlockPos();
        List<BlockPos> espList = new ArrayList<>();

        for (int x = -25; x <= 25; x++) {
            for (int z = -25; z <= 25; z++) {
                BlockPos pos = playerPos.add(x, 0, z);
                var state = mc.world.getBlockState(pos);
                if (holes.get() && state.isAir() && mc.world.getBlockState(pos.up()).isAir()) {
                    espList.add(pos);
                } else if (tunnels.get() && state.getBlock() == Blocks.OBSIDIAN) {
                    espList.add(pos);
                }
            }
        }

        for (var pos : espList) {
            Box box = new Box(pos);
            BoxRenderer.draw(event.context, box, -0xFF00FF00, ShapeMode.Sides, false); // Yeşil
        }
    }
}
