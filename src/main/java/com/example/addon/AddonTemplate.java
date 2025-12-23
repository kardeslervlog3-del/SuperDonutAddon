package com.example.addon;

import com.example.addon.modules.ChunkFinder;
import com.example.addon.modules.HoleTunnelESP;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;

public class SuperAddon extends MeteorAddon {
    @Override
    public void onInitialize() {
        Modules.registerModule(new ChunkFinder());
        Modules.registerModule(new HoleTunnelESP());
    }
}
