package service;

import entity.*;
import entity.type.*;

import java.util.*;

public class ApplicationsService {
    private final static Map<Integer, List<Integer>> PERSON_RULE = new HashMap<>();
    private final static Map<Integer, List<Integer>> FOOD_RULE = new HashMap<>();
    private final static Map<Integer, List<Integer>> ALCOHOL_RULE = new HashMap<>();
    private final static Map<Integer, List<Integer>> ACCOMMODATION_RULE = new HashMap<>();

    static {
        PERSON_RULE.put(0, Arrays.asList(2, 3, 4));
        PERSON_RULE.put(1, Arrays.asList(5, 6, 7));
        PERSON_RULE.put(2, Arrays.asList(null, 15, 16));
        PERSON_RULE.put(3, Arrays.asList(null, 13, 14));
        PERSON_RULE.put(4, Arrays.asList(null, 11, 12));
        PERSON_RULE.put(5, Arrays.asList(null, 9, 10));

        FOOD_RULE.put(0, Arrays.asList(28, 29, 30));
        FOOD_RULE.put(null, Arrays.asList(37, 38, 39));
        FOOD_RULE.put(1, Arrays.asList(46, 47, 48));
        FOOD_RULE.put(2, Arrays.asList(83, 84, 85));
        FOOD_RULE.put(3, Arrays.asList(74, 75, 76));
        FOOD_RULE.put(4, Arrays.asList(65, 66, 67));
        FOOD_RULE.put(5, Arrays.asList(56, 57, 58));
    }

    public static List<Application> getApplications(List data) {
        List<Application> store = new ArrayList<>();
        int index = 0;

        for (List<String> record : (List<List<String>>) data) {
            Integer size = getGroupSize(record.get(8));
            store = ApplicationsService.extractApplications(record, size, store, (size > 0));
            index++;
        }

        System.out.println(store);
        return store;
    }

    private static List<Application> extractApplications(List<String> record, Integer size, List<Application> store, boolean group) {
        Application application = new Application(record);
        /* условие выхода из рекурсии */
        if (group && size == 0) {
            return store;
        } else if (size > 0) {
            /* рекурсивно углубляемся */
            ApplicationsService.extractApplications(record, size - 1, store, true);
            /* собираем объект по определённым правилам */
            application.setPerson(ApplicationsService.completePerson(record, PERSON_RULE.get(size)));
            application.setProvision(ApplicationsService.completeProvision(record, FOOD_RULE.get(size)));
        } else if (!group) {
            /* собираем объект по определённым правилам */
            application.setPerson(ApplicationsService.completePerson(record, PERSON_RULE.get(0)));
            application.setProvision(ApplicationsService.completeProvision(record, FOOD_RULE.get(0)));
        }
        /* добавляем объект к коллекции */
        store.add(application);
        return store;
    }

    private static Integer getGroupSize(String participants) {
        Integer result = 0;

        if (participants.contains("2")) {
            result = 2;
        } else if (participants.contains("3")) {
            result = 3;
        } else if (participants.contains("4")) {
            result = 4;
        } else if (participants.contains("5")) {
            result = 5;
        }

        return result;
    }

    /* complete */

    private static Person completePerson(List<String> record, List<Integer> indexes) {
        Person person = new Person();

        person.setNickName(indexes.get(0) != null ? record.get(indexes.get(0)) : "Гость " + record.get(5));
        person.setNameAndSurname(record.get(indexes.get(1)).isEmpty() ? "Аноним" : record.get(indexes.get(1)));
        person.setPhoneNumbers(record.get(indexes.get(2)).isEmpty() ? null : ApplicationsService.getPhoneNumbers(record.get(indexes.get(2))));

        return person;
    }

    private static Provision completeProvision(List<String> record, List<Integer> indexes) {
        return null;
    }

    private static Application completeApplication(List<String> record, Map<String, Object> rule, Integer index) {
        Application application = new Application(record);

        application.setPerson(ApplicationsService.completePerson(record, ((List<List<Integer>>) rule.get("PersonList")).get(index != null ? index : 0)));

        return application;
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

    private static Provision completeProvision_old(List<String> row, Application application) {
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

    public static List<Application> getApplications_old(List<List<String>> rawData) {
        List<Application> result = new ArrayList<>();

        for (List<String> record : rawData) {
            Map rule = ApplicationsService.getRule(record);

            switch ((ApplicationType) rule.get("ApplicationType")) {
                case INDIVIDUAL_APPLICATION:
                    result.add(ApplicationsService.completeApplication(record, rule, null));
                    break;
                case GROUP_APPLICATION:
                    for (int i = 0, size = ((List<Integer>) rule.get("PersonList")).size(); i < size; i += 1) {
                        result.add(ApplicationsService.completeApplication(record, rule, i));
                    }
                    break;
            }
        }

        return result;
    }

    /* get */
    private static Map getRule(List<String> record) {
        Map<String, Object> rule = new LinkedHashMap<>();
        List<List<Integer>> participantList = new ArrayList<>();
        List<Map<String, List<Integer>>> supplyList = new ArrayList<>();

        ApplicationType applicationType = ApplicationsService.getApplicationType(record.get(1));
        ProvisionType provisionType = ApplicationsService.getProvisionType(record.get(27));

        rule.put("ApplicationType", applicationType);

        if (applicationType == ApplicationType.INDIVIDUAL_APPLICATION) {
            participantList.add(Arrays.asList(2, 3, 4));
        } else if (applicationType == ApplicationType.GROUP_APPLICATION) {
            ParticipantsType participantsType = ApplicationsService.getParticipantsType(record.get(8));

            rule.put("GroupParticipantsType", participantsType);

            switch (participantsType) {
                case FIVE_PARTICIPANTS:
                    participantList.add(Arrays.asList(null, 9, 10));
                case FOUR_PARTICIPANTS:
                    participantList.add(Arrays.asList(null, 11, 12));
                case THREE_PARTICIPANTS:
                    participantList.add(Arrays.asList(null, 13, 14));
                case TWO_PARTICIPANTS:
                    participantList.add(Arrays.asList(null, 15, 16));
                default:
                    participantList.add(Arrays.asList(5, 6, 7));
            }
        }

        rule.put("ProvisionType", provisionType);

        if (provisionType == ProvisionType.INDIVIDUAL) {
            Map<String, List<Integer>> supply = new LinkedHashMap<>();

            supply.put("Food", Arrays.asList(28, 29, 30));
            supply.put("Alcohol", Arrays.asList(31, 32, 33));
            supply.put("Accommodation", Arrays.asList(34, 35));

            supplyList.add(supply);
        } else if (provisionType == ProvisionType.GROUP_COMMON) {
            Map<String, List<Integer>> supply = new LinkedHashMap<>();

            supply.put("Food", Arrays.asList(37, 38, 39));
            supply.put("Alcohol", Arrays.asList(40, 41, 42));
            supply.put("Accommodation", Arrays.asList(43, 44));

            supplyList.add(supply);
        } else if (provisionType == ProvisionType.GROUP_DIFFERENTIATED) {
            ParticipantsType participantsType = ApplicationsService.getParticipantsType(record.get(55));
            Map<String, List<Integer>> supply = new LinkedHashMap<>();

            switch (participantsType) {
                case FIVE_PARTICIPANTS:
                    supply.put("Food", Arrays.asList(56, 57, 58));
                    supply.put("Alcohol", Arrays.asList(59, 60, 61));
                    supply.put("Accommodation", Arrays.asList(62, 63));
                    supplyList.add(supply);
                case FOUR_PARTICIPANTS:
                    supply.put("Food", Arrays.asList(65, 66, 67));
                    supply.put("Alcohol", Arrays.asList(68, 69, 70));
                    supply.put("Accommodation", Arrays.asList(71, 72));
                    supplyList.add(supply);
                case THREE_PARTICIPANTS:
                    supply.put("Food", Arrays.asList(74, 75, 76));
                    supply.put("Alcohol", Arrays.asList(77, 78, 79));
                    supply.put("Accommodation", Arrays.asList(80, 81));
                    supplyList.add(supply);
                case TWO_PARTICIPANTS:
                    supply.put("Food", Arrays.asList(83, 84, 85));
                    supply.put("Alcohol", Arrays.asList(86, 87, 88));
                    supply.put("Accommodation", Arrays.asList(89, 90));
                    supplyList.add(supply);
                default:
                    supply.put("Food", Arrays.asList(46, 47, 48));
                    supply.put("Alcohol", Arrays.asList(49, 50, 51));
                    supply.put("Accommodation", Arrays.asList(52, 53));
                    supplyList.add(supply);
            }
        }

        rule.put("PersonList", participantList);
        rule.put("ProvisionList", supplyList);

        return rule;
    }

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

    private static List getPhoneNumbers(String phoneNumbers) {
        List<String> result = new ArrayList<>();
        String[] rawData = phoneNumbers.replaceAll("[^0-9+]", "").split("\\D");

        for (String s : rawData) {
            if (s.length() > 0) {
                String phoneNumber = "(" + s.substring(s.length() - 9, s.length() - 7) + ") " +
                        s.substring(s.length() - 7, s.length() - 4) + "-" + s.substring(s.length() - 4, s.length());

                result.add(phoneNumber);
            }
        }

        return result;
    }
}
