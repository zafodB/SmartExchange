package com.zafodb.smartexchange;

import com.zafodb.smartexchange.Wrappers.Web3jwrapper;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.params.TestNet3Params;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

public class TradeDeal implements Serializable{

    private String destinationBtcAddress;
    private BigInteger amountSatoshi;
    private String destinationEthAddress;
    private BigInteger amountWei;

    private TradeDeal(String destinationBtc, BigInteger amountSatoshi, String destinationEth, BigInteger amountWei) {
        this.destinationBtcAddress = destinationBtc;
        this.amountSatoshi = amountSatoshi;
        this.destinationEthAddress = destinationEth;
        this.amountWei = amountWei;
    }

    public static TradeDeal loadDeal(String destinationBtc, BigInteger amountSatoshi, String destinationEth, BigInteger amountWei) {
        return new TradeDeal(destinationBtc, amountSatoshi, destinationEth, amountWei);
    }

    public static TradeDeal makeEmptyDeal() {
        return new TradeDeal(null, null, null, null);
    }


    public String getDestinationBtcAddress() {
        return destinationBtcAddress;
    }

    public void setDestinationBtcAddress(String destinationBtcAddress) throws ValidationException {
        try {
            Address.fromBase58(TestNet3Params.get(), destinationBtcAddress);
        } catch (AddressFormatException ae) {
            throw new ValidationException(ae.getMessage(), ae.getCause());
        }

        this.destinationBtcAddress = destinationBtcAddress;
    }

    public BigInteger getAmountSatoshi() {
        return amountSatoshi;
    }

    public void setAmountSatoshi(BigInteger amountSatoshi) {
        this.amountSatoshi = amountSatoshi;
    }

    public void setAmountSatoshi(String amountBtcString) throws ValidationException {

        try {
            BigDecimal btc = new BigDecimal(amountBtcString);

            btc = btc.multiply(new BigDecimal("100000000"));

            if (btc.compareTo(BigDecimal.ONE) <= 0) {
                throw new ValidationException("Amount is too small.");
            }

            this.amountSatoshi = btc.toBigInteger();

//        TODO Let user know whats wrong (if exception is thrown)
        } catch (NumberFormatException ne) {
            throw new ValidationException(ne.getMessage(), ne.getCause());
        }
    }


    public String getDestinationEthAddress() {
        return destinationEthAddress;
    }

    public void setDestinationEthAddress(String destinationEthAddress) throws ValidationException {
        if (!Web3jwrapper.validateAddress(destinationEthAddress)) {
            throw new ValidationException("Address is not valid.");
        }

        this.destinationEthAddress = destinationEthAddress;
    }

    public BigInteger getAmountWei() {
        return amountWei;
    }

    public void setAmountWei(BigInteger amountWei) {
        this.amountWei = amountWei;
    }
}
