package com.example.demoCustomer.customer;

import org.springframework.data.cassandra.core.mapping.Embedded;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Customer {

    private String firstName;
    private String lastName;
    @Embedded.Nullable
    private Address address;
    private String phoneNo; //Need to check for validity
    private String emailId;
    private String conversationId;

    @PrimaryKey
    private String billingAccountNumber;

    public Customer() {
    }

    public Customer(String firstName, String lastName, Address address, String phoneNo, String emailId, String conversationId, String billingAccountNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.conversationId = conversationId;
        this.billingAccountNumber = billingAccountNumber;
    }

    public Customer(String firstName, String lastName, Address address, String phoneNo, String emailId, String conversationId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNo = phoneNo;
        this.emailId = emailId;
        this.conversationId = conversationId;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getBillingAccountNumber() {
        return billingAccountNumber;
    }

    public void setBillingAccountNumber(String billingAccountNumber) {
        this.billingAccountNumber = billingAccountNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address=" + address +
                ", phoneNo='" + phoneNo + '\'' +
                ", emailId='" + emailId + '\'' +
                ", conversationId='" + conversationId + '\'' +
                ", billingAccountNumber='" + billingAccountNumber + '\'' +
                '}';
    }
}
