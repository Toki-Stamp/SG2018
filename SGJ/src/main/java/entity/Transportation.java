package entity;

import entity.type.TransportationType;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Transportation {
    private TransportationType transportationType;
    private String city;
    private String date;
    private String time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }
}
