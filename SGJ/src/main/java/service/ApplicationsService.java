package service;

import entity.*;
import entity.type.*;

import java.util.*;

public class ApplicationsService {
    public static List<Application> getApplications(List rawData) {
        List<Application> result = new ArrayList<>();

        for (List<String> record : (List<List<String>>) rawData) {
            List<Application> applications = new ArrayList<>();
            Map<String, Integer> rule = ApplicationsService.getRule(record);
            ApplicationType applicationType = ApplicationsService.getApplicationType(rule.get(""));

            switch (applicationType) {
                case INDIVIDUAL_APPLICATION:
                    applications.add(ApplicationsService.completeApplication(record));
                    break;
                case GROUP_APPLICATION:

                    break;
            }

            result.addAll(applications);
        }

        return result;
    }

    private static Map<String, Integer> getRule(List<String> record) {
        Map<String, Integer> result = new HashMap<>();
        result.put("Type", 1);
        result.put("Participants", 8);

        return result;
    }

    /* complete */
    private static Application completeApplication(List<String> record) {
        Application result = null;

        return result;
    }

    private static Person completePerson(List<String> record, Application application) {
        Person person = new Person();

        person.setNickName(record.get(2));
        person.setNameAndSurname(record.get(3));
        person.setPhoneNumbers(ApplicationsService.getPhoneNumbers(record.get(4)));

        return person;
    }

    private static Transportation completeTransportation(List<String> row, Application application) {
        Transportation transportation = new Transportation();

        switch (application.getTransportationType()) {
            case DRIVER:
                transportation.setCity(row.get(18));
                transportation.setDepartureDay(ApplicationsService.getDepartureDay(row.get(19)));
                transportation.setTime(row.get(20));
                //todo заполнить пустые мест
                transportation.setFreeSits(null);
                break;
            case PASSENGER:
                transportation.setCity(row.get(23));
                transportation.setDepartureDay(ApplicationsService.getDepartureDay(row.get(24)));
                transportation.setTime(row.get(25));
                transportation.setFreeSits(null);
                break;
        }

        return transportation;
    }

    private static Provision completeProvision(List<String> row, Application application) {
        Provision provision = new Provision();

        switch (application.getProvisionType()) {
            case INDIVIDUAL:
                provision.setFoodOnFriday(ApplicationsService.getSupplyType(row.get(28)));
                provision.setFoodOnSaturday(ApplicationsService.getSupplyType(row.get(29)));
                provision.setFoodOnSunday(ApplicationsService.getSupplyType(row.get(30)));
                provision.setAlcoholOnFriday(ApplicationsService.getSupplyType(row.get(31)));
                provision.setAlcoholOnSaturday(ApplicationsService.getSupplyType(row.get(32)));
                provision.setAlcoholOnSunday(ApplicationsService.getSupplyType(row.get(33)));
                break;
            case GROUP_COMMON:
                provision.setFoodOnFriday(ApplicationsService.getSupplyType(row.get(37)));
                provision.setFoodOnSaturday(ApplicationsService.getSupplyType(row.get(38)));
                provision.setFoodOnSunday(ApplicationsService.getSupplyType(row.get(39)));
                provision.setAlcoholOnFriday(ApplicationsService.getSupplyType(row.get(40)));
                provision.setAlcoholOnSaturday(ApplicationsService.getSupplyType(row.get(41)));
                provision.setAlcoholOnSunday(ApplicationsService.getSupplyType(row.get(42)));
                break;
            case GROUP_DIFFERENTIATED:
                break;
        }

        return provision;
    }

    private static Accommodation completeAccommodation(List<String> row, Application reference) {
        Accommodation result = new Accommodation();

        switch (reference.getApplicationType()) {
            case INDIVIDUAL_APPLICATION:
                result.setAccommodationType(ApplicationsService.getAccommodationType(row.get(34)));
                result.setPreferences(ApplicationsService.getPreferencesType(row.get(35)));
                break;
            case GROUP_APPLICATION:
                break;
        }

        return result;
    }

    /* get */
    private static ApplicationType getApplicationType(String applicationType) {
        ApplicationType result = null;

        if (applicationType.contains("Индивидуальная")) {
            result = ApplicationType.INDIVIDUAL_APPLICATION;
        } else if (applicationType.contains("Групповая")) {
            result = ApplicationType.GROUP_APPLICATION;
        }

        return result;
    }

    private static ParticipantsType getParticipantsType(String participantsType) {
        ParticipantsType result = null;

        if (participantsType.contains("2")) {
            result = ParticipantsType.TWO_PARTICIPANTS;
        } else if (participantsType.contains("3")) {
            result = ParticipantsType.THREE_PARTICIPANTS;
        } else if (participantsType.contains("4")) {
            result = ParticipantsType.FOUR_PARTICIPANTS;
        } else if (participantsType.contains("5")) {
            result = ParticipantsType.FIVE_PARTICIPANTS;
        }

        return result;
    }

    private static AccommodationType getAccommodationType(String accommodationType) {
        AccommodationType result = null;

        if (accommodationType.contains("пятницы") && !accommodationType.contains("субботы")) {
            result = AccommodationType.FROM_FRIDAY_TO_SATURDAY;
        } else if (!accommodationType.contains("пятницы") && accommodationType.contains("субботы")) {
            result = AccommodationType.FROM_SATURDAY_TO_SUNDAY;
        } else if (accommodationType.contains("пятницы") && accommodationType.contains("субботы")) {
            result = AccommodationType.FULL_ACCOMMODATION;
        } else if (accommodationType.contains("Без")) {
            result = AccommodationType.NOT_REQUIRED;
        }

        return result;
    }

    private static PreferencesType getPreferencesType(String preferencesType) {
        PreferencesType result = null;

        if (preferencesType.contains("спальном")) {
            result = PreferencesType.LIVING_BUILDING;
        } else if (preferencesType.contains("летнем")) {
            result = PreferencesType.SUMMER_HOUSE;
        } else if (preferencesType.contains("особого")) {
            result = PreferencesType.DOES_NOT_MATTER;
        }

        return result;
    }

    private static SupplyType getSupplyType(String supplyType) {
        SupplyType result = null;

        if (supplyType.contains("день") && !supplyType.contains("вечер")) {
            result = SupplyType.FIRST_HALF;
        } else if (!supplyType.contains("день") && supplyType.contains("вечер")) {
            result = SupplyType.SECOND_HALF;
        } else if (supplyType.contains("день") && supplyType.contains("вечер")) {
            result = SupplyType.FULL_DAY;
        } else if (supplyType.contains("Без")) {
            result = SupplyType.NOT_REQUIRED;
        }

        return result;
    }

    private static DepartureType getDepartureDay(String departureDay) {
        DepartureType result = null;

        if (departureDay.contains("Пятница")) {
            result = DepartureType.FRIDAY;
        } else if (departureDay.contains("Суббота")) {
            result = DepartureType.SATURDAY;
        } else if (departureDay.contains("Воскресенье")) {
            result = DepartureType.SUNDAY;
        }

        return result;
    }

    private static TransportationType getTransportationType(String transportationType) {
        TransportationType result = null;

        if (transportationType.contains("водитель")) {
            result = TransportationType.DRIVER;
        } else if (transportationType.contains("пассажир")) {
            result = TransportationType.PASSENGER;
        }

        return result;
    }

    private static ProvisionType getProvisionType(String provisionType) {
        ProvisionType result = null;

        if (provisionType.contains("Индивидуальная")) {
            result = ProvisionType.INDIVIDUAL;
        } else if (provisionType.contains("единые")) {
            result = ProvisionType.GROUP_COMMON;
        } else if (provisionType.contains("дифференцированные")) {
            result = ProvisionType.GROUP_DIFFERENTIATED;
        }

        return result;
    }

    private static List<String> getPhoneNumbers(String rawPhoneNumberData) {
        String[] rawData = rawPhoneNumberData.replaceAll("[^0-9+]", "").split("\\D");
        List<String> phoneNumbers = new ArrayList<>();

        for (String s : rawData) {
            if (s.length() > 0) {
                String phoneNumber = "(" + s.substring(s.length() - 9, s.length() - 7) + ") " +
                        s.substring(s.length() - 7, s.length() - 4) + "-" + s.substring(s.length() - 4, s.length());

                phoneNumbers.add(phoneNumber);
            }
        }

        return phoneNumbers;
    }

    private static Integer getIntValue(String string) {
        Integer result = 1;

        if (string.contains("нет")) {
            result = null;
        } else if (string.contains("2")) {
            result = 2;
        } else if (string.contains("3")) {
            result = 3;
        } else if (string.contains("4")) {
            result = 4;
        } else if (string.contains("5")) {
            result = 5;
        }

        return result;
    }
}
