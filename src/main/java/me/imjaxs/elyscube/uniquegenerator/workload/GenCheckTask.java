package me.imjaxs.elyscube.uniquegenerator.workload;

import lombok.RequiredArgsConstructor;
import me.imjaxs.elyscube.uniquegenerator.handlers.GeneratorHandler;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import me.imjaxs.elyscube.uniquegenerator.workload.abstraction.WorkloadThread;
import org.bukkit.scheduler.BukkitRunnable;

@RequiredArgsConstructor
public class GenCheckTask extends BukkitRunnable {
    private final WorkloadThread workload;
    private final GeneratorHandler generators;

    @Override
    public void run() {
        for (Generator generator : this.generators.getOnlineGenerators().values()) {
            if (System.currentTimeMillis() - generator.getLastDepositTime() >= generator.getLevel().getSpeed() * 1000L)
                this.workload.addLoad(new GenWorkLoad(generator));
        }
    }
}
