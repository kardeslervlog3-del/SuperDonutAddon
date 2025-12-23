package com.example.addon.modules;

import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.events.world.ChunkEvent;
import meteordevelopment.meteorclient.renderer.ShapeMode;
import meteordevelopment.meteorclient.settings.IntSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.BoxRenderer;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ChunkFinder extends Module {
    private final Setting<Integer> threshold = settings.getDefaultGroup().add(new IntSetting.Builder()
        .name("threshold")
        .description("Kaç block bulunursa base saysın (50 önerilir)")
        .defaultValue(50)
        .min(20).max(200)
        .sliderRange(20, 200)
        .build());

    private final List<ChunkPos> suspiciousChunks = new CopyOnWriteArrayList<>();
    private static final List<net.minecraft.block.Block> suspiciousBlocks = List.of(
        Blocks.OBSIDIAN, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.SPAWNER,
        Blocks.ENDER_CHEST, Blocks.SHULKER_BOX, Blocks.BEEHIVE, Blocks.BEE_NEST);

    public ChunkFinder() {
        super(Categories.Render, "chunk-finder", "Donut SMP base chunk'larını bulur.");
    }

    @EventHandler
    private void onChunkLoad(ChunkEvent.Load event) {
        int count = 0;
        var chunk = event.chunk();
        for (int sy = chunk.getBottomSectionCoord(); sy < chunk.getTopSectionCoord(); sy++) {
            var section = chunk.getSection(sy);
            if (section != null) {
                for (var state : section.getBlockStates()) {
                    if (suspiciousBlocks.contains(state.getBlock())) count++;
                    if (count > threshold.get()) break;
                }
            }
        }
        if (count >= threshold.get()) {
            suspiciousChunks.add(chunk.getPos());
            info("§bBase chunk bulundu: §e%d, %d §f(%d block)".formatted(chunk.getPos().x, chunk.getPos().z, count));
        }
    }

    @EventHandler
    private void onRender(Render3DEvent event) {
        for (var pos : suspiciousChunks) {
            double x1 = pos.getStartX(), z1 = pos.getStartZ();
            Box box = new Box(x1, 0, z1, x1 + 16, 256, z1 + 16);
            BoxRenderer.draw(event.context, box, -0xFFFF0000, ShapeMode.Lines, false); // Kırmızı
        }
    }

    @Override
    public void onDeactivate() {
        suspiciousChunks.clear();
    }
}
