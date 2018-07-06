package entity;

import entity.type.DepartureDayType;
import entity.type.EmptySitsType;
import entity.type.TransportationType;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Transportation {
    private TransportationType transportationType;
    private String city;
    private DepartureDayType departureDay;
    private String time;
    private EmptySitsType emptySits;

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public DepartureDayType getDepartureDay() {
        return departureDay;
    }

    public void setDepartureDay(DepartureDayType departureDay) {
        this.departureDay = departureDay;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public EmptySitsType getEmptySits() {
        return emptySits;
    }

    public void setEmptySits(EmptySitsType emptySits) {
        this.emptySits = emptySits;
    }
}
