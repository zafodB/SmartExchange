package com.zafodb.smartexchange;

import com.zafodb.smartexchange.Wrappers.BitcoinjWrapper;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

public class BtcOffer implements Serializable{



    private UUID offerId;
    private BigInteger amountSatoshiOffered;
    private BigInteger amountWeiWanted;
    private String destinationEthAddress;
    private String nickname;

    public BtcOffer(){
        offerId = UUID.randomUUID();

        amountSatoshiOffered = null;
        amountWeiWanted = null;
        destinationEthAddress = null;
        nickname = null;
    }

    public BtcOffer(BigInteger satoshi, BigInteger wei, String address, String name){
        offerId = UUID.randomUUID();

        amountSatoshiOffered = satoshi;
        amountWeiWanted = wei;
        destinationEthAddress = address;
        nickname = name;
    }

    public UUID getOfferId() {
        return offerId;
    }

    public BigInteger getAmountSatoshiOffered() {
        return amountSatoshiOffered;
    }

    public void setAmountSatoshiOffered(BigInteger amountSatoshiOffered){
        this.amountSatoshiOffered = amountSatoshiOffered;
    }

    public void setAmountSatoshiOffered(String amountSatoshiOffered) throws ValidationException {
        this.amountSatoshiOffered = BitcoinjWrapper.btcStringToSatoshi(amountSatoshiOffered);
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
