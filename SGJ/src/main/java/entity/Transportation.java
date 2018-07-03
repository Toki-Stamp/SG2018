package entity;

import entity.type.DepartureType;
import entity.type.SitsType;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Transportation {
    private String city;
    private DepartureType departureDay;
    private String time;
    private SitsType freeSits;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DepartureType getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(DepartureType departureDay) {
        this.departureDay = departureDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public SitsType getFreeSits() {
        return freeSits;
    }

    public void setFreeSits(SitsType freeSits) {
        this.freeSits = freeSits;
    }
}
