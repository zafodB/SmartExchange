package com.zafodb.smartexchange.Wrappers;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.ValidationException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.params.TestNet3Params;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

/**
 * @Author Filip Adamik
 *
 * Class to handle conversion between Bitcoin and Satoshi and verify if a supplied address is
 * a valid Bitcoin address.
 */

public class BitcoinWrapper {

    /**
     * Converts given amount in String (may have decimals) to Satoshi.
     *
     * @param btcAmount Bitcoin amount as String with possible decimal places.
     * @return Value of the amount in Satoshi as BigInteger.
     * @throws ValidationException This exception is thrown if the amount supplied is smaller
     * than 0 Satoshi or if the number could not be converted to BigInteger.
     */
    public static BigInteger btcStringToSatoshi(String btcAmount) throws ValidationException{
        try {
            BigDecimal btc = new BigDecimal(btcAmount);
            btc = btc.multiply(new BigDecimal(Constants.SATOSHIS_IN_BTC));

            if (btc.compareTo(BigDecimal.ONE) <= 0) {
                throw new ValidationException("Amount is too small.");
            }
            return btc.toBigInteger();

//        TODO Let user know whats wrong (if exception is thrown)
        } catch (NumberFormatException ne) {
            throw new ValidationException(ne.getMessage(), ne.getCause());
        }
    }

    /**
     * Convert amount of Satoshi to user-friendly Bitcoin String, that can be displayed in the UI.
     *
     * @param satoshi Amount of Satoshi as BigInteger.
     * @param decimals Desired decimal places of the output.
     * @return Amount of Bitcoin as String with desired amount of decimals.
     */
    public static String satoshiToBtcString(BigInteger satoshi, int decimals){
        BigDecimal divisor = new BigDecimal(Constants.SATOSHIS_IN_BTC);
        BigDecimal temp = new BigDecimal(satoshi);

        temp = temp.divide(divisor);
        temp = temp.setScale(decimals, RoundingMode.HALF_DOWN);

        return temp.toPlainString();
    }

    /**
     * Verifies that the Bitcoin address is valid and is a Testnet address.
     *
     * @param addressToValidate Bitcoin address to verify.
     * @throws ValidationException Throws, if the supplied address is not a valid address or
     * if the address is not a Testnet address.
     */
    public static void validateBtcAddress(String addressToValidate) throws ValidationException{
        try {
            Address.fromBase58(TestNet3Params.get(), addressToValidate);
        } catch (AddressFormatException ae) {
            throw new ValidationException(ae.getMessage(), ae.getCause());
        }
    }
}
