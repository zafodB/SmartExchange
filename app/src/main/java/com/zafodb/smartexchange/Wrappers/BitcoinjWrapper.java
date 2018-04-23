package com.zafodb.smartexchange.Wrappers;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.WrongNetworkException;
import org.bitcoinj.params.TestNet3Params;

import java.math.BigDecimal;

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
