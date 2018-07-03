import entity.Application;
import service.GoogleService;
import service.ApplicationsService;

import java.util.List;

/**
 * Created by Fomichev Yuri on 30.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class App {
    public static void main(String[] args) {
        List rawData = GoogleService.getRawData();
        List<Application> applications = ApplicationsService.getApplications(rawData);
    }
}
