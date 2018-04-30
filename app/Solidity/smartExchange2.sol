pragma solidity ^0.4.21;

// import "github.com/oraclize/ethereum-api/oraclizeAPI.sol";
import "oraclize-api-master/oraclizeAPI_0.4.sol";

contract SmartExchange2 is usingOraclize {
    
    string blockchainInfoApiAddress = "https://testnet.blockchain.info/q/getreceivedbyaddress/";
    
    string target_btc_address;
    address eth_dest_address;
    string target_btc_amount;
    uint balance;
    
    event logMe (string message);
    event logAddress (address dest_address);
 
//  Constructor
    function SmartExchange2(string btc_address, address eth_address, string satoshi_amount) payable{
        target_btc_address = btc_address;
        eth_dest_address = eth_address;
    
        // Due to complicated type conversion, let's keep it as string.
        target_btc_amount = satoshi_amount;
        
        balance = msg.value;
        
        emit logAddress(eth_address);
        emit logMe(strConcat("Will transfer contract balance to address from previous event, if ",
        satoshi_amount,
        " satoshi will be tranferred to ",
        btc_address));
    }
        
// Oraclize callback
    function __callback(bytes32 myid, string result) {
        if (msg.sender != oraclize_cbAddress()) throw;
        
        if (keccak256(target_btc_amount) == keccak256(result)){
            eth_dest_address.transfer(balance);
        }
        else {
            emit logMe("Unknown Oraclize response received.");
            emit logMe(result);
        }
    }
    
    function update()  {
        oraclize_query(60, "URL", strConcat(blockchainInfoApiAddress, target_btc_address));
        emit logMe("Query sent to Oraclize.");
    }
    
}