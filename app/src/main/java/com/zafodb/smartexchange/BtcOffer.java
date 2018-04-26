package com.zafodb.smartexchange;

import java.io.Serializable;
import java.math.BigInteger;

public class BtcOffer implements Serializable{

    BigInteger amountSatoshiOffered;
    BigInteger amountWeiWanted;
    String destinationEthAddress;
    String nickname;

    public BtcOffer(BigInteger satoshi, BigInteger wei, String address, String name){
        amountSatoshiOffered = satoshi;
        amountWeiWanted = wei;
        destinationEthAddress = address;
        nickname = name;
    }

    public BigInteger getAmountSatoshiOffered() {
        return amountSatoshiOffered;
    }

    public void setAmountSatoshiOffered(BigInteger amountSatoshiOffered) {
        this.amountSatoshiOffered = amountSatoshiOffered;
    }

    public BigInteger getAmountWeiWanted() {
        return amountWeiWanted;
    }

    public void setAmountWeiWanted(BigInteger amountWeiWanted) {
        this.amountWeiWanted = amountWeiWanted;
    }

    public String getDestinationEthAddress() {
        return destinationEthAddress;
    }

    public void setDestinationEthAddress(String destinationEthAddress) {
        this.destinationEthAddress = destinationEthAddress;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
