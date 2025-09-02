package leoric.pizzacipollastorage.common;

import jakarta.persistence.Embeddable;

@Embeddable
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;
}