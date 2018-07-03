package entity;

import entity.type.ProvisionType;
import entity.type.SupplyType;

import java.util.List;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Provision {
    /* Обеспечение продуктами питания */
    private SupplyType foodOnFriday;
    private SupplyType foodOnSaturday;
    private SupplyType foodOnSunday;
    /* Обеспечение алкогольными нпитания */
    private SupplyType alcoholOnFriday;
    private SupplyType alcoholOnSaturday;
    private SupplyType alcoholOnSunday;

    public SupplyType getFoodOnFriday() {
        return foodOnFriday;
    }

    public void setFoodOnFriday(SupplyType foodOnFriday) {
        this.foodOnFriday = foodOnFriday;
    }

    public SupplyType getFoodOnSaturday() {
        return foodOnSaturday;
    }

    public void setFoodOnSaturday(SupplyType foodOnSaturday) {
        this.foodOnSaturday = foodOnSaturday;
    }

    public SupplyType getFoodOnSunday() {
        return foodOnSunday;
    }

    public void setFoodOnSunday(SupplyType foodOnSunday) {
        this.foodOnSunday = foodOnSunday;
    }

    public SupplyType getAlcoholOnFriday() {
        return alcoholOnFriday;
    }

    public void setAlcoholOnFriday(SupplyType alcoholOnFriday) {
        this.alcoholOnFriday = alcoholOnFriday;
    }

    public SupplyType getAlcoholOnSaturday() {
        return alcoholOnSaturday;
    }

    public void setAlcoholOnSaturday(SupplyType alcoholOnSaturday) {
        this.alcoholOnSaturday = alcoholOnSaturday;
    }

    public SupplyType getAlcoholOnSunday() {
        return alcoholOnSunday;
    }

    public void setAlcoholOnSunday(SupplyType alcoholOnSunday) {
        this.alcoholOnSunday = alcoholOnSunday;
    }
}
