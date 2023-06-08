package com.example.demoCustomer.customer;

import org.springframework.data.cassandra.core.mapping.UserDefinedType;

import java.time.LocalDateTime;

@UserDefinedType
public class UpdateHistory {

    private LocalDateTime timestamp;
    private String firstNameHist;
    private String lastNameHist;
    private String addressLine1Hist;
    private String addressLine2Hist;
    private String addressCityHist;
    private String addressZipHist;
    private String addressStateHist;
    private String phoneNoHist;
    private String emailIdHist;
    private String conversationIdHist;

    public UpdateHistory() {
    }

    public UpdateHistory(LocalDateTime timestamp, String firstNameHist, String lastNameHist, String addressLine1Hist, String addressLine2Hist, String addressCityHist, String addressZipHist, String addressStateHist, String phoneNoHist, String emailIdHist, String conversationIdHist) {
        this.timestamp = timestamp;
        this.firstNameHist = firstNameHist;
        this.lastNameHist = lastNameHist;
        this.addressLine1Hist = addressLine1Hist;
        this.addressLine2Hist = addressLine2Hist;
        this.addressCityHist = addressCityHist;
        this.addressZipHist = addressZipHist;
        this.addressStateHist = addressStateHist;
        this.phoneNoHist = phoneNoHist;
        this.emailIdHist = emailIdHist;
        this.conversationIdHist = conversationIdHist;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getFirstNameHist() {
        return firstNameHist;
    }

    public void setFirstNameHist(String firstNameHist) {
        this.firstNameHist = firstNameHist;
    }

    public String getLastNameHist() {
        return lastNameHist;
    }

    public void setLastNameHist(String lastNameHist) {
        this.lastNameHist = lastNameHist;
    }

    public String getAddressLine1Hist() {
        return addressLine1Hist;
    }

    public void setAddressLine1Hist(String addressLine1Hist) {
        this.addressLine1Hist = addressLine1Hist;
    }

    public String getAddressLine2Hist() {
        return addressLine2Hist;
    }

    public void setAddressLine2Hist(String addressLine2Hist) {
        this.addressLine2Hist = addressLine2Hist;
    }

    public String getAddressCityHist() {
        return addressCityHist;
    }

    public void setAddressCityHist(String addressCityHist) {
        this.addressCityHist = addressCityHist;
    }

    public String getAddressZipHist() {
        return addressZipHist;
    }

    public void setAddressZipHist(String addressZipHist) {
        this.addressZipHist = addressZipHist;
    }

    public String getAddressStateHist() {
        return addressStateHist;
    }

    public void setAddressStateHist(String addressStateHist) {
        this.addressStateHist = addressStateHist;
    }

    public String getPhoneNoHist() {
        return phoneNoHist;
    }

    public void setPhoneNoHist(String phoneNoHist) {
        this.phoneNoHist = phoneNoHist;
    }

    public String getEmailIdHist() {
        return emailIdHist;
    }

    public void setEmailIdHist(String emailIdHist) {
        this.emailIdHist = emailIdHist;
    }

    public String getConversationIdHist() {
        return conversationIdHist;
    }

    public void setConversationIdHist(String conversationIdHist) {
        this.conversationIdHist = conversationIdHist;
    }

    @Override
    public String toString() {
        return "UpdateHistory{" +
                "timestamp=" + timestamp +
                ", firstNameHist='" + firstNameHist + '\'' +
                ", lastNameHist='" + lastNameHist + '\'' +
                ", addressLine1Hist='" + addressLine1Hist + '\'' +
                ", addressLine2Hist='" + addressLine2Hist + '\'' +
                ", addressCityHist='" + addressCityHist + '\'' +
                ", addressZipHist='" + addressZipHist + '\'' +
                ", addressStateHist='" + addressStateHist + '\'' +
                ", phoneNoHist='" + phoneNoHist + '\'' +
                ", emailIdHist='" + emailIdHist + '\'' +
                ", conversationIdHist='" + conversationIdHist + '\'' +
                '}';
    }
}
