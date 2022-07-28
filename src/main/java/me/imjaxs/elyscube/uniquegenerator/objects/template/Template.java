package me.imjaxs.elyscube.uniquegenerator.objects.template;

import lombok.Getter;
import me.imjaxs.elyscube.uniquegenerator.enums.ValueType;
import me.imjaxs.elyscube.uniquegenerator.objects.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class Template {
    @Getter private final ValueType value;
    @Getter private final double amount;
    @Getter private final List<Level> levels;

    public Template(ValueType value, double amount, List<Level> levels) {
        this.value = value;
        this.amount = amount;
        this.levels = levels;
    }

    public Level getMax() {
        Map<Integer, Level> levels = this.levels.stream().collect(Collectors.toMap(Level::getInteger, level -> level, (a, b) -> b));
        return levels.get(Collections.max(levels.keySet()));
    }
}
