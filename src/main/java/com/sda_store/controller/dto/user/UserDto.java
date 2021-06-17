package com.sda_store.controller.dto.user;

import com.sda_store.model.MessagingChannel;

public class UserDto {
    private String email;
    private String password;
    private String imageUrl;
    private String firstName;
    private String lastName;
    private MessagingChannel messagingChannel;
    private String role;
    private AddressDto addressDto;

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public MessagingChannel getMessagingChannel() {
        return messagingChannel;
    }

    public void setMessagingChannel(MessagingChannel messagingChannel) {
        this.messagingChannel = messagingChannel;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
