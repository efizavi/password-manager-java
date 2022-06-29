package com.hit.dm;

import org.omg.CORBA.SystemException;
import org.w3c.dom.ranges.RangeException;

import java.time.Month;

public class CardSecret extends AbstractSecretDTO{

    private String cardOwnerName;

    public String getCardOwnerName() {
        return cardOwnerName;
    }

    public void setCardOwnerName(String cardOwnerName) {
        this.cardOwnerName = cardOwnerName;
    }

    private long cardNumber;

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }

    private byte expirationDay;

    public byte getExpirationDay() {
        return expirationDay;
    }

    public void setExpirationDay(byte expirationDay) {
        if (expirationDay > 31 || expirationDay < 1)
        {
            throw new IllegalArgumentException("The given day was outside of expected range");
        }

        this.expirationDay = expirationDay;
    }

    private Month expirationMonth;

    public Month getExpirationMonth() {
        return expirationMonth;
    }

    public void setExpirationMonth(Month expirationMonth) {
        this.expirationMonth = expirationMonth;
    }

    private short expirationYear;

    public short getExpirationYear() {
        return expirationYear;
    }

    public void setExpirationYear(short expirationYear) {
        this.expirationYear = expirationYear;
    }

    private CardType cardType;

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    private short cvv;

    public short getCvv() {
        return cvv;
    }

    public void setCvv(short cvv) {
        this.cvv = cvv;
    }
}

