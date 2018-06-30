package entity;

import entity.type.ApplicationType;
import entity.type.ProvisionType;

/**
 * Created by Fomichev Yuri on 29.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Application {
    /* Тип заявки */
    private ApplicationType applicationType;
    /* Персональные данные */
    private Person person;
    /* Транспортировка */
    private Transportation transportation;
    /* Обеспечение продуктами питания и алкогольными напитками */
    private Provision provision;
    /* Размещение и проживание */
    private Accommodation accommodation;
}
