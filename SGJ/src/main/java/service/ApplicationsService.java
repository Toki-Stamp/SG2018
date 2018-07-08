package service;

import entity.*;
import entity.type.*;

import java.util.*;

public class ApplicationsService {
    /* индексы главного правила */
    /* 01 - тип заявки */
    /* 08 - количество участников группы */
    /* 17 - водитель или пассажир */
    /* 27 - тип обеспечения и проживания */
    private final static List<Integer> RULE_GENERAL = Arrays.asList(1, 8, 17, 27);
    private final static Map<Integer, List<Integer>> RULE_PERSON = new HashMap<>();
    private final static Map<Integer, List<Integer>> RULE_PROVISION = new HashMap<>();
    private final static Map<Integer, List<Integer>> RULE_ACCOMMODATION = new HashMap<>();
    private final static Map<Object, Object> RULE_INFORMATION = new HashMap<>();
    private final static Map<TransportationType, List<Integer>> RULE_TRANSPORTATION = new HashMap<>();

    static {
        /* индексы персональных данных */
        RULE_PERSON.put(0, Arrays.asList(2, 3, 4));
        RULE_PERSON.put(1, Arrays.asList(5, 6, 7));
        RULE_PERSON.put(2, Arrays.asList(null, 15, 16));
        RULE_PERSON.put(3, Arrays.asList(null, 13, 14));
        RULE_PERSON.put(4, Arrays.asList(null, 11, 12));
        RULE_PERSON.put(5, Arrays.asList(null, 9, 10));
        /* индексты данных по трансортировке */
        RULE_TRANSPORTATION.put(TransportationType.DRIVER, Arrays.asList(18, 19, 20, 21));
        RULE_TRANSPORTATION.put(TransportationType.PASSENGER, Arrays.asList(23, 24, 25));
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
        /* индексы данных дополнительной информации */
        RULE_INFORMATION.put(TransportationType.DRIVER, 22);
        RULE_INFORMATION.put(TransportationType.PASSENGER, 26);
        RULE_INFORMATION.put(ProvisionType.INDIVIDUAL, 36);
        RULE_INFORMATION.put(ProvisionType.GROUP_COMMON, 45);
        RULE_INFORMATION.put(ProvisionType.GROUP_DIFFERENTIATED, Arrays.asList(54, 91, 82, 73, 64));
        RULE_INFORMATION.put(null, 92);
    }

    private static class Options {
        ApplicationType applicationType;
        boolean isGroup;
        Integer groupSize;
        Integer groupId;
        TransportationType transportationType;
        ProvisionType provisionType;
        Integer provisionGroupSize;

        Options(ApplicationType applicationType, boolean isGroup, Integer groupSize, Integer groupId, TransportationType transportationType, ProvisionType provisionType, Integer provisionGroupSize) {
            this.applicationType = applicationType;
            this.isGroup = isGroup;
            this.groupSize = groupSize;
            this.groupId = groupId;
            this.transportationType = transportationType;
            this.provisionType = provisionType;
            this.provisionGroupSize = provisionGroupSize;
        }

        Options() {
            this(null, false, null, null, null, null, null);
        }
    }

    public static List<Application> getApplications(List<List<String>> data) {
        List<Application> store = new ArrayList<>();
        int groupId = 1;

        for (List<String> record : data) {
            Options options = new Options();
            /* подготавливаем параметры для сбора данных по заявке */
            options.applicationType = ApplicationsService.getApplicationType(record.get(RULE_GENERAL.get(0)));
            options.groupSize = getGroupSize(record.get(RULE_GENERAL.get(1)));
            options.isGroup = options.groupSize > 0;
            options.groupId = options.isGroup ? groupId++ : null;
            options.transportationType = ApplicationsService.getTransportationType(record.get(RULE_GENERAL.get(2)));
            options.provisionType = ApplicationsService.getProvisionType(record.get(RULE_GENERAL.get(3)));

            if (options.provisionType == ProvisionType.GROUP_COMMON) {
                options.provisionGroupSize = 0;
            } else if (options.provisionType == ProvisionType.GROUP_DIFFERENTIATED) {
                options.provisionGroupSize = getGroupSize(record.get(55));
            }

            store = ApplicationsService.extractApplications(store, record, options);
        }

        return store;
    }

    private static List<Application> extractApplications(List<Application> store, List<String> record, Options options) {
        Application application = new Application(record, options.applicationType);
        /* условие выхода из рекурсии */
        if (options.isGroup && (options.groupSize == 0)) {
            return store;
        } else if (options.groupSize > 0) {
            /* подготавливаем новые параметры для рекурсивного вызова */
            Options recursiveOptions = new Options(
                    options.applicationType,
                    options.isGroup,
                    (options.groupSize - 1),
                    options.groupId,
                    options.transportationType,
                    options.provisionType,
                    (
                            (options.provisionType == ProvisionType.GROUP_DIFFERENTIATED) ?
                                    (options.provisionGroupSize - 1) :
                                    options.provisionGroupSize
                    )
            );
            /* рекурсивно углубляемся */
            ApplicationsService.extractApplications(store, record, recursiveOptions);
        }
        /* собираем объект */
        application.setGroupId(options.groupId);
        application.setPerson(ApplicationsService.completePerson(record, options));
        application.setTransportation(ApplicationsService.completeTransportation(record, options));
        application.setProvision(ApplicationsService.completeProvision(record, options));
        application.setAccommodation(ApplicationsService.completeAccommodation(record, options));
        application.setInformation(ApplicationsService.completeInformation(record, options));
        /* добавляем объект к коллекции */
        store.add(application);
        /* возвращаем объект */
        return store;
    }

    /* complete */
    private static Person completePerson(List<String> record, Options options) {
        Person person = new Person();
        List<Integer> rule;
        /* определяем правило сбора данных */
        if (options.isGroup) {
            rule = RULE_PERSON.get(options.groupSize);
        } else {
            rule = RULE_PERSON.get(0);
        }
        /* заполняем сущность */
        person.setNickName(
                (rule.get(0) == null) ?
                        "Гость " + record.get(5) :
                        record.get(rule.get(0))

        );
        person.setNameAndSurname(
                record.get(rule.get(1)).isEmpty() ?
                        "Аноним" :
                        record.get(rule.get(1))
        );

        if (!record.get(rule.get(2)).isEmpty()) {
            person.setPhoneNumbers(ApplicationsService.getPhoneNumbers(record.get(rule.get(2))));
        }

        /* вовращаем сущность */
        return person;
    }

    private static Transportation completeTransportation(List<String> record, Options options) {
        Transportation transportation = new Transportation(options.transportationType);
        /* определяем правило сбора данных */
        List<Integer> rule = RULE_TRANSPORTATION.get(options.transportationType);
        /* заполняем сущность */
        transportation.setCity(record.get(rule.get(0)));
        transportation.setDepartureDay(ApplicationsService.getDepartureDay(record.get(rule.get(1))));
        transportation.setTime(ApplicationsService.getDepartureTime(record.get(rule.get(2))));

        if (options.transportationType == TransportationType.DRIVER) {
            transportation.setEmptySits(ApplicationsService.getEmptySitsType(record.get(rule.get(3))));
        }

        /* возвращаем сущность */
        return transportation;
    }

    private static Provision completeProvision(List<String> record, Options options) {
        Provision provision = new Provision(options.provisionType);
        /* определяем правило сбора данных */
        List<Integer> rule = RULE_PROVISION.get(options.provisionGroupSize);
        /* заполняем сущность */
        provision.setFoodOnFriday(ApplicationsService.getSupplyType(record.get(rule.get(0))));
        provision.setFoodOnSaturday(ApplicationsService.getSupplyType(record.get(rule.get(1))));
        provision.setFoodOnSunday(ApplicationsService.getSupplyType(record.get(rule.get(2))));
        provision.setAlcoholOnFriday(ApplicationsService.getSupplyType(record.get(rule.get(3))));
        provision.setAlcoholOnSaturday(ApplicationsService.getSupplyType(record.get(rule.get(4))));
        provision.setAlcoholOnSunday(ApplicationsService.getSupplyType(record.get(rule.get(5))));
        /* возвращаем сущность */
        return provision;
    }

    private static Accommodation completeAccommodation(List<String> record, Options options) {
        Accommodation accommodation = new Accommodation();
        /* определяем правило сбора данных */
        List<Integer> rule = RULE_ACCOMMODATION.get(options.provisionGroupSize);
        /* заполняем сущность */
        accommodation.setAccommodationType(ApplicationsService.getAccommodationType(record.get(rule.get(0))));
        accommodation.setPreferences(ApplicationsService.getPreferencesType(record.get(rule.get(1))));
        /* возвращаем сущность */
        return accommodation;
    }

    private static Information completeInformation(List<String> record, Options options) {
        Information information = new Information();
        boolean hasError = false;
        List<Integer> rule;

        do {
            try {
                if (!hasError) {
                    rule = Arrays.asList(
                            (Integer) RULE_INFORMATION.get(options.transportationType),
                            (Integer) RULE_INFORMATION.get(options.provisionType),
                            (Integer) RULE_INFORMATION.get(null)
                    );
                } else {
                    List<Integer> indexes = (List<Integer>) RULE_INFORMATION.get(options.provisionType);
                    int theBigOne = indexes.get(options.groupSize - 1);
                    int sd = ((List<Integer>) RULE_INFORMATION.get(options.provisionType)).get(options.groupSize - 1);
                    String fdsaf = record.get(sd);

                    rule = Arrays.asList(
                            (Integer) RULE_INFORMATION.get(options.transportationType),
                            ((List<Integer>) RULE_INFORMATION.get(options.provisionType)).get(indexes.get(options.groupSize - 1)),
                            (Integer) RULE_INFORMATION.get(null)
                    );

                    System.out.println();
                }
                if (options.groupSize <= 1) {
                    if (!record.get(rule.get(0)).equals("")) {
                        information.setTransportationInfo(record.get(rule.get(0)));
                    }
                    if (!record.get(rule.get(2)).equals("")) {
                        information.setOtherInfo(record.get(rule.get(2)));
                    }
                }

                information.setAccommodationInfo(record.get(rule.get(1)));
                hasError = false;
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
                hasError = false;
                /* ignore */
            } catch (ClassCastException e) {
                e.printStackTrace();
                hasError = true;
            }
        } while (hasError);

        return information;
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

    private static List<String> getPhoneNumbers(String phoneNumbers) {
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

    private static String getDepartureTime(String departureTime) {
        return departureTime.substring(0, departureTime.lastIndexOf(":"));
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

    private static EmptySitsType getEmptySitsType(String emptySitsType) {
        EmptySitsType result = EmptySitsType.NO_EMPTY_SITS;

        if (emptySitsType.contains("1")) {
            result = EmptySitsType.ONE_SIT;
        } else if (emptySitsType.contains("2")) {
            result = EmptySitsType.TWO_SITS;
        } else if (emptySitsType.contains("3")) {
            result = EmptySitsType.THREE_SITS;
        } else if (emptySitsType.contains("4")) {
            result = EmptySitsType.FOUR_SITS;
        }

        return result;
    }
}
