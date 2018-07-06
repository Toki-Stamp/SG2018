package service;

import entity.*;
import entity.type.*;

import java.util.*;

public class ApplicationsService {
    /* индексы главного правила */
    /* 08 - количество участников группы? */
    /* 17 - водитель или пассажир ? */
    /* 27 - тип обеспечения и проживания? */
    private final static List<Integer> RULE_GENERAL = Arrays.asList(8, 17, 27);
    private final static Map<Integer, List<Integer>> RULE_PERSON = new HashMap<>();
    private final static Map<Integer, List<Integer>> RULE_PROVISION = new HashMap<>();
    private final static Map<Integer, List<Integer>> RULE_ACCOMMODATION = new HashMap<>();

    static {
        /* индексы персональных данных */
        RULE_PERSON.put(0, Arrays.asList(2, 3, 4));
        RULE_PERSON.put(1, Arrays.asList(5, 6, 7));
        RULE_PERSON.put(2, Arrays.asList(null, 15, 16));
        RULE_PERSON.put(3, Arrays.asList(null, 13, 14));
        RULE_PERSON.put(4, Arrays.asList(null, 11, 12));
        RULE_PERSON.put(5, Arrays.asList(null, 9, 10));
        /* индексы данных по продуктам питания и алкоголным напиткам */
        RULE_PROVISION.put(null, Arrays.asList(28, 29, 30, 31, 32, 33));
        RULE_PROVISION.put(0, Arrays.asList(37, 38, 39, 40, 41, 42));
        RULE_PROVISION.put(1, Arrays.asList(46, 47, 48, 49, 50, 51));
        RULE_PROVISION.put(2, Arrays.asList(83, 84, 85, 86, 87, 88));
        RULE_PROVISION.put(3, Arrays.asList(74, 75, 76, 77, 78, 79));
        RULE_PROVISION.put(4, Arrays.asList(65, 66, 67, 68, 69, 70));
        RULE_PROVISION.put(5, Arrays.asList(56, 57, 58, 59, 60, 61));
        /* индексы данных по размещению и проживанию */
        RULE_ACCOMMODATION.put(null, Arrays.asList(34, 35));
        RULE_ACCOMMODATION.put(0, Arrays.asList(43, 44));
        RULE_ACCOMMODATION.put(1, Arrays.asList(52, 53));
        RULE_ACCOMMODATION.put(2, Arrays.asList(89, 90));
        RULE_ACCOMMODATION.put(3, Arrays.asList(80, 81));
        RULE_ACCOMMODATION.put(4, Arrays.asList(71, 72));
        RULE_ACCOMMODATION.put(5, Arrays.asList(62, 63));
    }

    public static List<Application> getApplications(List data) {
        List<Application> store = new ArrayList<>();
        int groupId = 0;
        boolean isGroup = false;

        for (List<String> record : (List<List<String>>) data) {
            Integer provisionGroupSize = null;
            Integer groupSize = getGroupSize(record.get(RULE_GENERAL.get(0)));
            TransportationType transportationType = ApplicationsService.getTransportationType(record.get(RULE_GENERAL.get(1)));
            ProvisionType provisionType = ApplicationsService.getProvisionType(record.get(RULE_GENERAL.get(2)));

            if (provisionType == ProvisionType.GROUP_COMMON) {
                provisionGroupSize = 0;
            } else if (provisionType == ProvisionType.GROUP_DIFFERENTIATED) {
                provisionGroupSize = getGroupSize(record.get(55));
            }

            if (groupSize > 0) {
                isGroup = true;
                ++groupId;
            } else if (isGroup) {
                isGroup = false;
            }

            store = ApplicationsService.extractApplications(store, record, isGroup, groupSize, groupId, provisionType, provisionGroupSize);
        }

        return store;
    }

    private static List<Application> extractApplications(List<Application> store, List<String> record, boolean isGroup, int groupSize, int groupId, ProvisionType provisionType, Integer provisionGroupSize) {
        Application application = new Application(record);
        /* условие выхода из рекурсии */
        if (isGroup && (groupSize == 0)) {
            return store;
        } else if (groupSize > 0) {
            /* рекурсивно углубляемся */
            ApplicationsService.extractApplications(
                    store, record, isGroup, groupSize - 1, groupId, provisionType,
                    (provisionType == ProvisionType.GROUP_DIFFERENTIATED) ? (provisionGroupSize - 1) : provisionGroupSize
            );
            /* собираем объект */
            application.setGroupId(groupId);
            application.setPerson(ApplicationsService.completePerson(record, true, groupSize));
//            application.setProvision(ApplicationsService.completeProvision(record, provisionType, provisionGroupSize));
//            application.setAccommodation(ApplicationsService.completeAccommodation(record, provisionGroupSize));
        } else if (!isGroup) {
            /* собираем объект */
            application.setPerson(ApplicationsService.completePerson(record, false, groupSize));
//            application.setProvision(ApplicationsService.completeProvision(record, provisionType, provisionGroupSize));
//            application.setAccommodation(ApplicationsService.completeAccommodation(record, provisionGroupSize));
        }
        /* собираем объект */
        application.setProvision(ApplicationsService.completeProvision(record, provisionType, provisionGroupSize));
        application.setAccommodation(ApplicationsService.completeAccommodation(record, provisionGroupSize));
        /* добавляем объект к коллекции */
        store.add(application);

        return store;
    }

    private static int getGroupSize(String participants) {
        int result = 0;

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

    private static Person completePerson(List<String> record, boolean isGroup, int groupSize) {
        Person person = new Person();
        List<Integer> rule;
        /* определяем правило сбора данных */
        if (isGroup) {
            rule = RULE_PERSON.get(groupSize);
        } else {
            rule = RULE_PERSON.get(0);
        }
        /* заполняем сущность */
        person.setNickName(rule.get(0) != null ? record.get(rule.get(0)) : "Гость " + record.get(5));
        person.setNameAndSurname(record.get(rule.get(1)).isEmpty() ? "Аноним" : record.get(rule.get(1)));
        person.setPhoneNumbers(record.get(rule.get(2)).isEmpty() ? null : ApplicationsService.getPhoneNumbers(record.get(rule.get(2))));

        return person;
    }

    private static Provision completeProvision(List<String> record, ProvisionType provisionType, Integer provisionGroupSize) {
        Provision provision = new Provision(provisionType);
        /* определяем правило сбора данных */
        List<Integer> rule = RULE_PROVISION.get(provisionGroupSize);
        /* заполняем сущность */
        provision.setFoodOnFriday(ApplicationsService.getSupplyType(record.get(rule.get(0))));
        provision.setFoodOnSaturday(ApplicationsService.getSupplyType(record.get(rule.get(1))));
        provision.setFoodOnSunday(ApplicationsService.getSupplyType(record.get(rule.get(2))));
        provision.setAlcoholOnFriday(ApplicationsService.getSupplyType(record.get(rule.get(3))));
        provision.setAlcoholOnSaturday(ApplicationsService.getSupplyType(record.get(rule.get(4))));
        provision.setAlcoholOnSunday(ApplicationsService.getSupplyType(record.get(rule.get(5))));

        return provision;
    }

    private static Accommodation completeAccommodation(List<String> record, Integer provisionGroupSize) {
        Accommodation accommodation = new Accommodation();
        /* определяем правило сбора данных */
        List<Integer> rule = RULE_ACCOMMODATION.get(provisionGroupSize);
        /* заполняем сущность */
        accommodation.setAccommodationType(ApplicationsService.getAccommodationType(record.get(rule.get(0))));
        accommodation.setPreferences(ApplicationsService.getPreferencesType(record.get(rule.get(1))));

        return accommodation;
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

    private static DepartureDayType getDepartureDay(String departureDay) {
        DepartureDayType result = null;

        if (departureDay.contains("Пятница")) {
            result = DepartureDayType.FRIDAY;
        } else if (departureDay.contains("Суббота")) {
            result = DepartureDayType.SATURDAY;
        } else if (departureDay.contains("Воскресенье")) {
            result = DepartureDayType.SUNDAY;
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
