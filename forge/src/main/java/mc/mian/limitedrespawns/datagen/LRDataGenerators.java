package mc.mian.limitedrespawns.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

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
