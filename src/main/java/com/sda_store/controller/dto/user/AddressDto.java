package com.sda_store.controller.dto.user;

import com.sda_store.model.Address;

public class AddressDto {

    private String country;
    private String city;
    private String street;
    private Long zipcode;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Long getZipcode() {
        return zipcode;
    }

    public void setZipcode(Long zipcode) {
        this.zipcode = zipcode;
    }

    public static AddressDto mapAddressToAddressDto(Address address){
        AddressDto addressDto = new AddressDto();
        addressDto.setCity(address.getCity());
        addressDto.setCountry(address.getCountry());
        addressDto.setZipcode(address.getZipcode());
        addressDto.setStreet(address.getStreet());
        return addressDto;
    }
}
