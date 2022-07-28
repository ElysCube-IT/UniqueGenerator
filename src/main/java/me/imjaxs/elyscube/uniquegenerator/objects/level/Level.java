package me.imjaxs.elyscube.uniquegenerator.objects.level;

import lombok.Getter;

public class Level {
    @Getter private final int integer;
    @Getter private final double price;
    @Getter private final int speed;

    public Level(int integer, double price, int speed) {
        this.integer = integer;
        this.price = price;
        this.speed = speed;
    }
}
