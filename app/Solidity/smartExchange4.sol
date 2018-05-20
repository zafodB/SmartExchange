pragma solidity ^0.4.16;

// import "github.com/oraclize/ethereum-api/oraclizeAPI_0.4.sol";
import "oraclize-api-master/oraclizeAPI_0.4.sol";

contract SmartExchange4 is usingOraclize {
    
    string target_btc_address;
    address eth_dest_address;
    uint target_btc_amount;
    uint balance;
 
//  Constructor
    function SmartExchange4(string btc_address, address eth_address, uint satoshi_amount) payable{
        target_btc_address = btc_address;
        eth_dest_address = eth_address;
    
// Due to complicated type conversion, let's keep it as string.
        target_btc_amount = satoshi_amount;
        
        balance = msg.value;

	    update();
    }
        
// Oraclize callback
    function __callback(bytes32 myid, string result) {
        if (msg.sender != oraclize_cbAddress()) throw;
        
        uint resultAsInt = parseInt(result);
        
        if (resultAsInt == target_btc_amount){
            eth_dest_address.transfer(balance);
        }
    }
    
    function update()  {
        oraclize_query(600, "URL",target_btc_address);
    }
}