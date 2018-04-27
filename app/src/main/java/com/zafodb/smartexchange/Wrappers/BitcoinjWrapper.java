package com.zafodb.smartexchange.Wrappers;

import com.zafodb.smartexchange.Constants;
import com.zafodb.smartexchange.ValidationException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.WrongNetworkException;
import org.bitcoinj.params.TestNet3Params;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class BitcoinjWrapper {

//    public static boolean validateAddress(String address) {
//        try {
//            Address adr = Address.fromBase58(TestNet3Params.get(), address);
//            return true;
//        } catch (WrongNetworkException we) {
//            we.printStackTrace();
////            TODO Provide guide to user what happened
//            return false;
//        } catch (AddressFormatException ae) {
//            ae.printStackTrace();
////            TODO nothing
//            return false;
//        }
//    }

    public static BigInteger btcStringToSatoshi(String btcBalance) throws ValidationException{
        try {
            BigDecimal btc = new BigDecimal(btcBalance);

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

    public static String satoshiToBtcString(BigInteger satoshi, int decimals){
        BigDecimal divisor = new BigDecimal(Constants.SATOSHIS_IN_BTC);
        BigDecimal temp = new BigDecimal(satoshi);

        temp = temp.divide(divisor);
        temp = temp.setScale(decimals, RoundingMode.HALF_DOWN);

        String out = temp.toPlainString();

        return out + " BTC";
    }




//    public static boolean isValidBtcAmount(String btcAsString) {
//        if (btcAsString == null) {
//            return false;
//        } else {
//            try {
//                BigDecimal btc = new BigDecimal(btcAsString);
//
//                btc = btc.multiply(new BigDecimal("100000000"));
//
//                return btc.compareTo(BigDecimal.ONE) >= 0;
//
////        TODO Let user know whats wrong (if returns false)
//            } catch (NumberFormatException ne) {
////                TODO Let user know whats wrong
//                return false;
//            }
//        }
//
//    }


}
