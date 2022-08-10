package com.sweber.tennis.model.gear;

import com.sweber.tennis.model.config.Attributes;
import com.sweber.tennis.model.config.ConfigValues;
import com.sweber.tennis.service.BeanUtil;
import com.sweber.tennis.service.GearItemService;

import java.util.Objects;

public class GearItem {
    private final String name;
    private final ConfigValues configValues;
    private final GearType gearType;

    public GearItem(String name, GearType gearType, ConfigValues configValues) {
        this.name = name;
        this.gearType = gearType;
        this.configValues = configValues;
    }

    public String getName() {
        return name;
    }

    public Attributes getAttributes() {
        return configValues.getAttributes();
    }

    public int getCost() {
        return BeanUtil.getBean(GearItemService.class).ownedLevel(this) - getLevel() >= 0 ? 0 : configValues.getCost();
    }

    public int getLevel() {
        return configValues.getLevel();
    }

    public GearType getGearType() {
        return gearType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GearItem gearItem = (GearItem) o;
        return Objects.equals(name, gearItem.name) && gearType == gearItem.gearType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, gearType);
    }
}
