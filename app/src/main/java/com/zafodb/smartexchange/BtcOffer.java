package com.zafodb.smartexchange;

import com.zafodb.smartexchange.Wrappers.BitcoinWrapper;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.UUID;

/**
 * Class used to hold data about a Bitcoin offer. Data can be supplied by user or from the database.
 */

public class BtcOffer implements Serializable{

    private UUID offerId;
    private BigInteger amountSatoshiOffered;
    private BigInteger amountWeiWanted;
    private String destinationEthAddress;
    private String nickname;

    /**
     * Public constructor. Used when a new empty Bitcoin offer is created. Generates a new random UUID for
     * the offer.
     */
    public BtcOffer(){
        offerId = UUID.randomUUID();

        amountSatoshiOffered = null;
        amountWeiWanted = null;
        destinationEthAddress = null;
        nickname = null;
    }

    /**
     * Public constructor used to create a new instance of Bitcoin offer, when the parameters are
     * known. Generates new UUID for this offer.
     *
     * @param satoshi Amount of Satoshi offered by the creator of the offer.
     * @param wei Amount of Wei wanted in return.
     * @param address Destination Ethereum address, where Ether should be deposited, if the
     *                transaction is accepted.
     * @param name Nickname of the user who created this offer (optional).
     *             TODO: Make nickname optional.
     */
    public BtcOffer(BigInteger satoshi, BigInteger wei, String address, String name){
        offerId = UUID.randomUUID();

        amountSatoshiOffered = satoshi;
        amountWeiWanted = wei;
        destinationEthAddress = address;
        nickname = name;
    }

    /**
     * Public constructor used to reconstruct the offer from the database, if all the parameters are
     * known, including the previously created UUID.
     *
     * @param uuid See above.
     * @param satoshi See above.
     * @param wei See above.
     * @param address See above.
     * @param name See above.
     */
    public BtcOffer(String uuid, BigInteger satoshi, BigInteger wei, String address, String name){
        offerId = UUID.fromString(uuid);

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

    /**
     * Setter for the amount of Satoshi offered. Is the input from the user and needs to be converted
     * to BigInteger.
     *
     * @param amountSatoshiOffered User's input.
     * @throws ValidationException Thrown, if the amount cannot be converted or is smaller than 0
     * Satoshi.
     */
    public void setAmountSatoshiOffered(String amountSatoshiOffered) throws ValidationException {
        this.amountSatoshiOffered = BitcoinWrapper.btcStringToSatoshi(amountSatoshiOffered);
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
