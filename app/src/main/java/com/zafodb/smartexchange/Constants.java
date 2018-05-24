package com.zafodb.smartexchange;

import com.zafodb.smartexchange.UI.DeployContractFragment;
import com.zafodb.smartexchange.UI.OfferStatusFragment;
import com.zafodb.smartexchange.UI.ValidateDeployFragment;
import com.zafodb.smartexchange.UI.WalletPickFragment;
import com.zafodb.smartexchange.UI.SendBitcoin;
import com.zafodb.smartexchange.Wrappers.EthereumWrapper;

/**
 * @Author Filip Adamik
 */

public class Constants {

    /***
     * Constants specifying type of fragment-to-activity interactions.
     */
    public static final int FROM_WALLET_PICK_TO_DEPLOY = 564;
    public static final int ETH_BALANCE_UPDATE = 565;
    public static final int FROM_DEPLOY_TO_VALIDATE = 567;
    public static final int VALIDATION_DENIED_BY_USER = 568;
    public static final int VALIDATION_SUCCESSFUL = 569;
    public static final int OFFERS_UPDATE = 570;
    public static final int FROM_OFFERS_TO_WALLETPICK = 571;
    public static final int FROM_OFFERS_TO_NEWOFFER = 572;
    public static final int CREATE_NEW_OFFER = 573;
    public static final int DELETE_OWN_OFFER = 574;
    public static final int REMOVE_VALUE_EVENT_LISTENER = 575;
    public static final int CONTINUE_TO_SEND_BITCOIN = 576;

    /**
     * Fragment tags.
     */
    public static final String OFFERS_FRAGMENT_TAG = "offerFragTag";
    public static final String WALLET_PICK_FRAGMENT_TAG = "walletPickFragTag";
    public static final String DEPLOY_FRAGMENT_TAG = "deployFragTag";
    public static final String VALIDATE_FRAGMENT_TAG = "validateFragTag";
    public static final String SENT_FRAGMENT_TAG = "sentFragTag";
    public static final String CREATE_OFFER_TAG = "newOfferFragTag";
    public static final String OFFER_STATUS_FRAGMENT_TAG = "offerStatusFragTag";
    public static final String CONFIRM_DIALOG_TAG = "ConfirmDialogFragTag";
    public static final String SEND_BITCOIN_TAG = "SendBitcoinFragTag";

    /**
     * General bundle parameter labels.
     */
    public static final String BALANCE_AS_BIGINTEGER = "balanceAsBigInt";
    public static final String TRANSACTION_HASH = "txHash";
    public static final String OFFERS_LIST = "btcOffers";
    public static final String BTC_OFFER_TAG = "newBtcOffer";
    public static final String OFFER_STATUS_TAG = "offerStatusChanged";
    public static final String OFFER_STATUS_TX_HASH = "offerTxHashChanged";
    public static final String OFFER_STATUS_BTC_ADDRESS = "offerDestinationBtcAddress";

    /**
     * Argument names for {@link DeployContractFragment} fragment.
     */
//    public static final String DEPLOY_CONTRACT_PARAM1 = "mWalletFileName";
//    public static final String DEPLOY_CONTRACT_PARAM2 = "walletAddress1";
    public static final String DEPLOY_CONTRACT_BTC_OFFER = "btcOffer";

    /**
     * Argument names for {@link WalletPickFragment} fragment.
     */
    public static final String WALLET_PICK_PARAM1 = "walletAddress2";

    /**
     * Argument names for {@link ValidateDeployFragment} fragment.
     */
    public static final String VALIDATE_DEPLOY_PARAM1 = "tradeDeal";
    public static final String VALIDATE_DEPLOY_PARAM2 = "btcOffer";

    /**
     * Argument names for {@link OfferStatusFragment} fragment.
     */
    public static final String BTC_OFFER_PARAM1 = "btcOfferParam";

    /**
     * Argument names for {@link SendBitcoin} fragment.
     */
    public static final String DESTINATION_BTC_ADDRESS_PARAM = "btcDestinationParam";
    public static final String DESTINATION_BTC_AMOUNT_PARAM = "btcAmountParam";

    /**
     * Firebase Database labels
     */
    public static final String DATA_BTC_OFFERS = "BtcOffers";
    public static final String DATA_OFFER_BTC = "btcOffered";
    public static final String DATA_OFFER_ETH = "ethWanted";
    public static final String DATA_OFFER_NICKNAME = "offerNickname";
    public static final String DATA_OFFER_ETHADDRESS = "destinationEthAddress";
    public static final String DATA_OFFER_STATUS = "offerStatus";
    public static final String DATA_OFFER_TX_HASH = "txHash";
    public static final String DATA_OFFER_BTC_ADDRESS = "destinationBtcAddress";
    public static final long DATA_STATUS_NEW = 11;
    public static final long DATA_STATUS_THEY_CONFIRMED = 12;
    public static final long DATA_STATUS_YOU_CONFIRMED = 13;
    public static final long DATA_STATUS_ACCEPTED = 14;

    /**
     * {@link EthereumWrapper} constants.
     */
    public static final String INFURA_NODE_URL = "https://kovan.infura.io/Ceux1wHF7EsQWKb9p8da";
//    Alternative URLS (for reference).
//    public static final String INFURA_NODE_URL = "https://kovan.infura.io/ROrdzkoD6Ua0TH7cyaSh";
//    public static final String INFURA_NODE_URL = "https://kovan.infura.io/IlXkpW67R8mNHL0HDIdO";
//
    public static final String ESTIMATED_CONTRACT_PRICE = "45000000000000000";
    public static final int BALANCE_DISPLAY_DECIMALS = 4;
    public static final String WEIS_IN_ETHER = "1000000000000000000";
    public static final String SATOSHIS_IN_BTC = "100000000";

    public static final String ETHERSCAN_KOVAN_REFERENCE = "https://kovan.etherscan.io/tx/";
    public static final String BLOCKCHAIN_EXPLORER_API = "https://testnet.blockchain.info/q/getreceivedbyaddress/";
}
