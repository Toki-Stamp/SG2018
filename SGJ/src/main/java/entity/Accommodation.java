package entity;

import entity.type.AccommodationType;
import entity.type.FacilityPreferencesType;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Accommodation {
    /* Проживание на с пятницы на субботу */
    AccommodationType fromFridayToSaturday;
    /* Проживание на с субботы на воскресенье */
    AccommodationType fromSaturdayToSunday;
    /* Предпочтение на размещение */
    FacilityPreferencesType preferences;
}
