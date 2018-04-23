package com.zafodb.smartexchange;

import com.zafodb.smartexchange.Wrappers.Web3jwrapper;

public class Constants {

    /***
     * Constants specifying type of fragment-to-activity interactions.
     */
    public static final int FROM_WALLET_PICK_TO_DEPLOY = 564;
    public static final int ETH_BALANCE_UPDATE = 565;
    public static final int FROM_DEPLOY_TO_VALIDATE = 567;
    public static final int VALIDATION_UNSUCCESSFUL = 568;
    public static final int VALIDATION_SUCCESSFUL = 589;

    public static final String DEPLOY_FRAGMENT_TAG = "deployFragTag";
    public static final String VALIDATE_FRAGMENT_TAG = "validateFragTag";
    public static final String SENT_FRAGMENT_TAG = "sentFragTag";

    public static final String BALANCE_AS_BIGINTEGER = "balanceAsBigInt";
    public static final String TRANSACTION_HASH = "txHash";

    /**
     * Argument names for {@link com.zafodb.smartexchange.UI.DeployContract} fragment.
     */
    public static final String DEPLOY_CONTRACT_PARAM1 = "mWalletFileName";
    public static final String DEPLOY_CONTRACT_PARAM2 = "walletAddress1";

    /**
     * Argument names for {@link com.zafodb.smartexchange.UI.WalletPick} fragment.
     */
    public static final String WALLET_PICK_PARAM1 = "walletAddress2";

    /**
     * Argument names for {@link com.zafodb.smartexchange.UI.ValidateDeploy} fragment.
     */
    public static final String VALIDATE_DEPLOY_PARAM1 = "tradeDeal";

    /**
     * {@link Web3jwrapper} constants.
     */
    public static final String INFURA_NODE_URL = "https://kovan.infura.io/Ceux1wHF7EsQWKb9p8da";
//    Alternative URLS (for reference).
//    public static final String INFURA_NODE_URL = "https://kovan.infura.io/ROrdzkoD6Ua0TH7cyaSh";
//    public static final String INFURA_NODE_URL = "https://kovan.infura.io/IlXkpW67R8mNHL0HDIdO";
//
    public static final String ESTIMATED_CONTRACT_PRICE = "90000000000000000";
    public static final int BALANCE_DISPLAY_DECIMALS = 4;
    public static final String WEIS_IN_ETHER = "1000000000000000000";

}
