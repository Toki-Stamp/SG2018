package entity;

import entity.type.ApplicationType;

import java.util.List;

/**
 * Created by Fomichev Yuri on 29.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Application {
    public Application(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    private Integer groupId;
    /* Тип заявки */
    private ApplicationType applicationType;
    /* Персональные данные */
    private Person person;
    /* Транспортировка */
    private Transportation transportation;
    /* Обеспечение продуктами питания и алкогольными напитками */
    private Provision provision;
    /* Размещение и проживание */
    private Accommodation accommodation;
    /* Дополнительная информация */
    private Information information;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public Provision getProvision() {
        return provision;
    }

    public void setProvision(Provision provision) {
        this.provision = provision;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public void setAccommodation(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }
}
