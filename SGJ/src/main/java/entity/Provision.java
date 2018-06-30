package entity;

import entity.type.ProvisionType;
import entity.type.SupplyType;

import java.util.List;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Provision {
    private ProvisionType provisionType;
    /* Обеспечение продуктами питания */
    SupplyType foodOnFriday;
    SupplyType foodOnSaturday;
    SupplyType foodOnSunday;
    /* Обеспечение алкогольными нпитания */
    SupplyType alcoholOnFriday;
    SupplyType alcoholOnSaturday;
    SupplyType alcoholOnSunday;

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

    public ProvisionType getProvisionType() {
        return provisionType;
    }

    public void setProvisionType(ProvisionType provisionType) {
        this.provisionType = provisionType;
    }
}
