package service;

import entity.*;
import entity.type.ApplicationType;
import entity.type.TransportationType;

import java.util.Arrays;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class KickStarter {
    public static void main(String[] args) {

    }

    private void stuff() {
        TransportationType passenger = TransportationType.PASSENGER;
        TransportationType driver = TransportationType.DRIVER;

        System.out.printf("%s, name: %s, ordinal: %s%n", passenger, passenger.name(), passenger.ordinal());
        System.out.printf("%s, name: %s, ordinal: %s%n", driver, driver.name(), driver.ordinal());

        TransportationType[] values = TransportationType.values();

        System.out.println(Arrays.toString(values));

        System.out.println("passenger == passenger: " + (passenger == passenger));
        System.out.println("driver == driver: " + (driver == driver));
        System.out.println("passenger == driver: " + (passenger == driver));
        System.out.println("driver == passenger: " + (driver == passenger));
        System.out.println("passenger != driver: " + (passenger != driver));
        System.out.println("driver != passenger: " + (driver != passenger));
    }
}
