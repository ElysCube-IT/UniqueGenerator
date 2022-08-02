package me.imjaxs.elyscube.uniquegenerator.workload;

import lombok.RequiredArgsConstructor;
import me.imjaxs.elyscube.uniquegenerator.objects.Generator;
import me.imjaxs.elyscube.uniquegenerator.workload.abstraction.Workload;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GenWorkLoad extends Workload {
    private final Generator generator;

    @Override
    public void compute() {
        Player player = Bukkit.getPlayer(this.generator.getUniqueID());
        if (player == null)
            return;
        this.generator.deposit();
    }
}
