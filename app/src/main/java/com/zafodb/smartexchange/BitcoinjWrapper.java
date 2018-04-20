package com.zafodb.smartexchange;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.WrongNetworkException;
import org.bitcoinj.params.TestNet3Params;

public class BitcoinjWrapper {

    public static boolean validateAddress(String address) {

        try {
            Address adr = Address.fromBase58(TestNet3Params.get(), address);
            return true;
        } catch (WrongNetworkException we) {
            we.printStackTrace();
//            TODO Provide guide to user what happened
            return false;
        } catch (AddressFormatException ae){
            ae.printStackTrace();
//            TODO nothing
            return false;
        }
    }
}
