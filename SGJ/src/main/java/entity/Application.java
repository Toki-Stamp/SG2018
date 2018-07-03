package entity;

import entity.type.ApplicationType;
import entity.type.ProvisionType;
import entity.type.TransportationType;

/**
 * Created by Fomichev Yuri on 29.06.2018
 * Contact me at : toki.stamp@gmail.com
 */

public class Application {
    private static int counter = 1;

    public Application() {
        this.id = counter++;
    }

    /* Id */
    private int id;
    /* Тип заявки */
    private ApplicationType applicationType;
    /* Персональные данные */
    private Person person;
    /* Транспортировка */
    private TransportationType transportationType;
    private Transportation transportation;
    /* Обеспечение продуктами питания и алкогольными напитками */
    private ProvisionType provisionType;
    private Provision provision;
    /* Размещение и проживание */
    private Accommodation accommodation;

    public ApplicationType getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(ApplicationType applicationType) {
        this.applicationType = applicationType;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }

    public Transportation getTransportation() {
        return transportation;
    }

    public void setTransportation(Transportation transportation) {
        this.transportation = transportation;
    }

    public ProvisionType getProvisionType() {
        return provisionType;
    }

    public void setProvisionType(ProvisionType provisionType) {
        this.provisionType = provisionType;
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
}
