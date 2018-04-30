package com.zafodb.smartexchange;

import com.zafodb.smartexchange.Wrappers.BitcoinWrapper;
import com.zafodb.smartexchange.Wrappers.EthereumWrapper;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * TradaDeal class gets its properties from BtcOffer class, but contains additional parameter,
 * which is the destination Bitcoin address.
 *
 * TradeDeal is used when finalising, verifying and deploying the contract.
 */
public class TradeDeal implements Serializable{

    private String destinationBtcAddress;
    private BigInteger amountSatoshi;
    private String destinationEthAddress;
    private BigInteger amountWei;

    /**
     * Private constructor.
     *
     * @param destinationBtc Bitcoin address, that will be monitored in deployed Smart Contract.
     * @param amountSatoshi Amount of Satoshi, that will be the threshold for sending Ether to its
     *                      destination
     * @param destinationEth Destination Ethereum address. Target address, where Ether will be sent
     *                       if the Bitcoin condition was validated successfully.
     * @param amountWei Amount of Wei that will be transferred to the destination Ethereum address.
     */
    private TradeDeal(String destinationBtc, BigInteger amountSatoshi, String destinationEth, BigInteger amountWei) {
        this.destinationBtcAddress = destinationBtc;
        this.amountSatoshi = amountSatoshi;
        this.destinationEthAddress = destinationEth;
        this.amountWei = amountWei;
    }

    public static TradeDeal loadDeal(String destinationBtc, BigInteger amountSatoshi, String destinationEth, BigInteger amountWei) {
        return new TradeDeal(destinationBtc, amountSatoshi, destinationEth, amountWei);
    }

    /**
     * Static method that wraps the private constructor. Used when a Bitcoin offer has been accepted a
     * nd needs to be converted to a contract.
     *
     * @param amountSatoshi Amount of Satoshi for the contract (see above).
     * @param destinationEth Destination Eth address (see above).
     * @param amountWei Amount of wei to transfer (see above).
     *
     * @return New instance of TradeDeal.
     */
    public static TradeDeal loadDealFromOffer(BigInteger amountSatoshi, String destinationEth, BigInteger amountWei) {
        return new TradeDeal(null, amountSatoshi, destinationEth, amountWei);
    }

    /**
     * Method to wrap empty private constructor.
     *
     * @return New instance of TradeDeal
     */
    public static TradeDeal makeEmptyDeal() {
        return new TradeDeal(null, null, null, null);
    }

    public String getDestinationBtcAddress() {
        return destinationBtcAddress;
    }

    /**
     * Setter to verify that destination Bitcoin address is in valid format and is on Bitcoin Testnet.
     *
     * @param destinationBtcAddress Input to verify (usually from user).
     * @throws ValidationException Exception thrown if address is not in proper format or if the
     * address is on other network than Testnet.
     */
    public void setDestinationBtcAddress(String destinationBtcAddress) throws ValidationException {
        BitcoinWrapper.validateBtcAddress(destinationBtcAddress);

        this.destinationBtcAddress = destinationBtcAddress;
    }

    public BigInteger getAmountSatoshi() {
        return amountSatoshi;
    }

    public void setAmountSatoshi(BigInteger amountSatoshi) {
        this.amountSatoshi = amountSatoshi;
    }

    /**
     * Setter for the amount of Satoshi. Converts String to BigInteger and verifies that the amount
     * is greater than 0.
     *
     * @param amountBtcString Input, possibly from user. Can contain decimal number.
     * @throws ValidationException Exception thrown if conversion fails or if input is smaller than
     * 0 Satoshi.
     */
    public void setAmountSatoshi(String amountBtcString) throws ValidationException {
        this.amountSatoshi = BitcoinWrapper.btcStringToSatoshi(amountBtcString);
    }

    public String getDestinationEthAddress() {
        return destinationEthAddress;
    }

    /**
     * Method to verify that destination Ethereum address is in valid format.
     *
     * @param destinationEthAddress Input to verify (usually from user).
     * @throws ValidationException Exception thrown if verification fails.
     */
    public void setDestinationEthAddress(String destinationEthAddress) throws ValidationException {
        if (!EthereumWrapper.validateAddress(destinationEthAddress)) {
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
