package entity;

import entity.type.AccommodationType;
import entity.type.PreferencesType;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Accommodation {
    /* Проживание с пятницы на субботу / с субботы на воскресенье */
    AccommodationType accommodationType;
    /* Предпочтение на размещение */
    PreferencesType preferences;

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public PreferencesType getPreferences() {
        return preferences;
    }

    public void setPreferences(PreferencesType preferences) {
        this.preferences = preferences;
    }
}
