package com.hit.dm;

public class IdentitySecretEncrypted extends AbstractSecretEncrypted {

    private byte[] firstName;

    public byte[] getFirstName() {
        return firstName;
    }

    public void setFirstName(byte[] firstName) {
        this.firstName = firstName;
    }

    private byte[] middleName;

    public byte[] getMiddleName() {
        return middleName;
    }

    public void setMiddleName(byte[] middleName) {
        this.middleName = middleName;
    }

    private byte[] lastName;

    public byte[] getLastName() {
        return lastName;
    }

    public void setLastName(byte[] lastName) {
        this.lastName = lastName;
    }

    private byte[] identityType;

    public byte[] getIdentityType() {
        return identityType;
    }

    public void setIdentityType(byte[] identityType) {
        this.identityType = identityType;
    }

    private byte[] company;

    public byte[] getCompany() {
        return company;
    }

    public void setCompany(byte[] company) {
        this.company = company;
    }

    private byte[] licenseNumber;

    public byte[] getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(byte[] licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    private byte[] passportNumber;

    public byte[] getPassportNumber() {
        return passportNumber;
    }

    public void setPassportNumber(byte[] passportNumber) {
        this.passportNumber = passportNumber;
    }

    private byte[] email;

    public byte[] getEmail() {
        return email;
    }

    public void setEmail(byte[] email) {
        this.email = email;
    }

    private byte[] phoneNumber;

    public byte[] getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(byte[] phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    private byte[] address;

    public byte[] getAddress() {
        return address;
    }

    public void setAddress(byte[] address) {
        this.address = address;
    }

    private byte[] postalCode;

    public byte[] getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(byte[] postalCode) {
        this.postalCode = postalCode;
    }
}
