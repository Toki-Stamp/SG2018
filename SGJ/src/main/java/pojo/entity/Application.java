package pojo.entity;

import java.util.List;

/**
 * Created by Fomichev Yuri on 29.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Application {
    private ApplicationType applicationType;
    /* personal data */
    private String nickName;
    private String nameAndSurname;
    private String phoneNumber;
    /* transportation */
    private TransportationType transportationType;
    private String cityOfDeparture;
    private String dateOfDeparture;
    private String timeOfDeparture;
    /* provision */
    private List<Provision> food;
    private List<Provision> alcohol;
}
