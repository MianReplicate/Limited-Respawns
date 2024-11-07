package mc.mian.limitedrespawns.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.data.event.GatherDataEvent;

public class LRDataGenerators {

    @SubscribeEvent
    public static void generateData(GatherDataEvent ev) {
        final DataGenerator gen = ev.getGenerator();
        final PackOutput packOutput = gen.getPackOutput();

        if (ev.includeServer()) {
            gen.addProvider(ev.includeServer(), new LRLangProvider(packOutput));
        }
    }
}
