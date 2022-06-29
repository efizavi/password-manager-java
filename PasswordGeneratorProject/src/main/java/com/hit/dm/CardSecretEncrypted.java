package com.hit.dm;

public class CardSecretEncrypted extends AbstractSecretEncrypted {

    private byte[] cardOwnerName;

    public byte[] getCardOwnerName() {
        return cardOwnerName;
    }

    public void setCardOwnerName(byte[] cardOwnerName) {
        this.cardOwnerName = cardOwnerName;
    }

    private byte[] cardNumber;

    public byte[] getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(byte[] cardNumber) {
        this.cardNumber = cardNumber;
    }

    private byte[] expirationDay;

    public byte[] getExpirationDay() {
        return expirationDay;
    }

    public void setExpirationDay(byte[] expirationDay) {
        this.expirationDay = expirationDay;
    }

    private byte[] expirationMonth;

    public byte[] getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(byte[] expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    private byte[] expirationYear;

    public byte[] getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(byte[] expirationYear) {
        this.expirationYear = expirationYear;
    }

    private byte[] cardType;

    public byte[] getCardType() {
        return cardType;
    }

    public void setCardType(byte[] cardType) {
        this.cardType = cardType;
    }

    private byte[] cvv;

    public byte[] getCvv() {
        return cvv;
    }

    public void setCvv(byte[] cvv) {
        this.cvv = cvv;
    }
}

