package com.zafodb.smartexchange.Wrappers;

import com.zafodb.smartexchange.BtcOffer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class FirebaseWrapper {

    public static boolean createBtcOffer(){
        return false;
    }

    public static boolean deleteOwnOffer(){
        return false;
    }

    public static List<BtcOffer> fetchExistingOffers(){
//        DEVELOP SECTION START
        BigInteger satoshi = new BigInteger("54261");
        BigInteger wei = new BigInteger("8989997");
        String address = "0x98945480002160ffds465";
        String name = "zafod";

        BtcOffer offer1 = new BtcOffer(satoshi, wei, address, name);

        satoshi = new BigInteger("40000");
        wei = new BigInteger("7770000000");
        address = "0x11111ffffffaaaaa";
        name = "Filip";

        BtcOffer offer2 = new BtcOffer(satoshi, wei, address, name);

        List<BtcOffer> offers = new ArrayList<>();

        offers.add(offer1);
        offers.add(offer2);

//        DEVELOP SECTION END

        return offers;
    }

    public static boolean authenticateUser(){
        return false;
    }


}
