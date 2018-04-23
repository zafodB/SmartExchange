package com.zafodb.smartexchange;

import java.math.BigInteger;

public class BtcOffer {

    BigInteger amountSatoshiOffered;
    BigInteger amountWeiWanted;
    String destinationEthAddress;
    String nickname;

    BtcOffer(BigInteger satoshi, BigInteger wei, String address, String name){
        amountSatoshiOffered = satoshi;
        amountWeiWanted = wei;
        destinationEthAddress = address;
        nickname = name;
    }

}
