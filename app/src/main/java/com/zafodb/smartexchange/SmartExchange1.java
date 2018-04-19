package com.zafodb.smartexchange;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class SmartExchange1 extends Contract {
    private static final String BINARY = "6060604052606060405190810160405280603781526020017f68747470733a2f2f746573746e65742e626c6f636b636861696e2e696e666f2f81526020017f712f67657472656365697665646279616464726573732f000000000000000000815250600590805190602001906200007892919062001331565b5060405162002cd038038062002cd0833981016040528080518201919060200180519060200190919080518201919050508260069080519060200190620000c192919062001331565b5081600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600890805190602001906200011b92919062001331565b5034600981905550620001416200014a640100000000026200049e176401000000009004565b50505062001408565b620002fc603c6040805190810160405280600381526020017f55524c0000000000000000000000000000000000000000000000000000000000815250620002e260058054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015620002245780601f10620001f85761010080835404028352916020019162000224565b820191906000526020600020905b8154815290600101906020018083116200020657829003601f168201915b505050505060068054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015620002c35780601f106200029757610100808354040283529160200191620002c3565b820191906000526020600020905b815481529060010190602001808311620002a557829003601f168201915b5050505050620003676401000000000262000976176401000000009004565b620003cc64010000000002620009c3176401000000009004565b507f2da49ac810e00dfaa00ca0a76665b434bdca51afb437994cbd47784dbe2e04006040518080602001828103825260178152602001807f51756572792073656e7420746f204f7261636c697a652e00000000000000000081525060200191505060405180910390a1565b62000371620013b8565b620003c483836020604051908101604052806000815250602060405190810160405280600081525060206040519081016040528060008152506200091c640100000000026200136d176401000000009004565b905092915050565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614806200045557506000620004536000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1662000d796401000000000262000ed1176401000000009004565b145b156200047d576200047b600062000d846401000000000262000edc176401000000009004565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156200050257600080fd5b5af115156200051057600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156200064f576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515620005f657600080fd5b5af115156200060457600080fd5b50505060405180519050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663524f3889856040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015620006fb578082015181840152602081019050620006de565b50505050905090810190601f168015620007295780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b15156200074857600080fd5b5af115156200075657600080fd5b50505060405180519050905062030d403a02670de0b6b3a76400000181111562000787576000600102915062000914565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f99828787876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b838110156200084057808201518184015260208101905062000823565b50505050905090810190601f1680156200086e5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015620008a95780820151818401526020810190506200088c565b50505050905090810190601f168015620008d75780820380516001836020036101000a031916815260200191505b50955050505050506020604051808303818588803b1515620008f857600080fd5b5af115156200090657600080fd5b505050506040518051905091505b509392505050565b62000926620013b8565b62000930620013cc565b6200093a620013cc565b62000944620013cc565b6200094e620013cc565b62000958620013cc565b62000962620013b8565b6200096c620013cc565b6000808e98508d97508c96508b95508a94508451865188518a518c51010101016040518059106200099a5750595b9080825280601f01601f1916602001820160405250935083925060009150600090505b885181101562000a74578881815181101515620009d657fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151562000a3657fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050620009bd565b600090505b875181101562000b3057878181518110151562000a9257fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151562000af257fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505062000a79565b600090505b865181101562000bec57868181518110151562000b4e57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151562000bae57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505062000b35565b600090505b855181101562000ca857858181518110151562000c0a57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151562000c6a57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505062000bf1565b600090505b845181101562000d6457848181518110151562000cc657fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151562000d2657fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505062000cad565b82995050505050505050505095945050505050565b6000813b9050919050565b60008062000dba731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed62000d796401000000000262000ed1176401000000009004565b111562000e7357731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555062000e696040805190810160405280600b81526020017f6574685f6d61696e6e65740000000000000000000000000000000000000000008152506200131564010000000002620017a5176401000000009004565b6001905062001310565b600062000ea873c03a2615d5efaf5f49f60b7bb6583eaec212fdf162000d796401000000000262000ed1176401000000009004565b111562000f615773c03a2615d5efaf5f49f60b7bb6583eaec212fdf16000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555062000f576040805190810160405280600c81526020017f6574685f726f707374656e3300000000000000000000000000000000000000008152506200131564010000000002620017a5176401000000009004565b6001905062001310565b600062000f9673b7a07bcf2ba2f2703b24c0691b5278999c59ac7e62000d796401000000000262000ed1176401000000009004565b11156200104f5773b7a07bcf2ba2f2703b24c0691b5278999c59ac7e6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550620010456040805190810160405280600981526020017f6574685f6b6f76616e00000000000000000000000000000000000000000000008152506200131564010000000002620017a5176401000000009004565b6001905062001310565b60006200108473146500cfd35b22e4a392fe0adc06de1a1368ed4862000d796401000000000262000ed1176401000000009004565b11156200113d5773146500cfd35b22e4a392fe0adc06de1a1368ed486000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550620011336040805190810160405280600b81526020017f6574685f72696e6b6562790000000000000000000000000000000000000000008152506200131564010000000002620017a5176401000000009004565b6001905062001310565b600062001172736f485c8bf6fc43ea212e93bbf8ce046c7f1cb47562000d796401000000000262000ed1176401000000009004565b1115620011d757736f485c8bf6fc43ea212e93bbf8ce046c7f1cb4756000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062001310565b60006200120c7320e12a1f859b3feae5fb2a0a32c18f5a65555bbf62000d796401000000000262000ed1176401000000009004565b111562001271577320e12a1f859b3feae5fb2a0a32c18f5a65555bbf6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062001310565b6000620012a67351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa62000d796401000000000262000ed1176401000000009004565b11156200130b577351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905062001310565b600090505b919050565b80600290805190602001906200132d92919062001331565b5050565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200137457805160ff1916838001178555620013a5565b82800160010185558215620013a5579182015b82811115620013a457825182559160200191906001019062001387565b5b509050620013b49190620013e0565b5090565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b6200140591905b8082111562001401576000816000905550600101620013e7565b5090565b90565b6118b880620014186000396000f300606060405260043610610057576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806327dc297e1461005c57806338bbfa50146100c6578063a2e6204514610173575b600080fd5b341561006757600080fd5b6100c460048080356000191690602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190505061017d565b005b34156100d157600080fd5b61017160048080356000191690602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050610499565b005b61017b61049e565b005b610185610687565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101be57600080fd5b806040518082805190602001908083835b6020831015156101f457805182526020820191506020810190506020830392506101cf565b6001836020036101000a038019825116818451168082178552505050505050905001915050604051809103902060001916600860405180828054600181600116156101000203166002900480156102825780601f10610260576101008083540402835291820191610282565b820191906000526020600020905b81548152906001019060200180831161026e575b5050915050604051809103902060001916141561036a57600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc6009549081150290604051600060405180830381858888f1935050505015156102fd57600080fd5b7f2da49ac810e00dfaa00ca0a76665b434bdca51afb437994cbd47784dbe2e04006040518080602001828103825260208152602001807f5472616e66657272656420657468657220746f2064657374696e6174696f6e2e81525060200191505060405180910390a1610495565b7f2da49ac810e00dfaa00ca0a76665b434bdca51afb437994cbd47784dbe2e04006040518080602001828103825260238152602001807f556e6b6e6f776e204f7261636c697a6520726573706f6e73652072656365697681526020017f65642e000000000000000000000000000000000000000000000000000000000081525060400191505060405180910390a17f2da49ac810e00dfaa00ca0a76665b434bdca51afb437994cbd47784dbe2e0400816040518080602001828103825283818151815260200191508051906020019080838360005b8381101561045a57808201518184015260208101905061043f565b50505050905090810190601f1680156104875780820380516001836020036101000a031916815260200191505b509250505060405180910390a15b5050565b505050565b61061c603c6040805190810160405280600381526020017f55524c000000000000000000000000000000000000000000000000000000000081525061061760058054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156105725780601f1061054757610100808354040283529160200191610572565b820191906000526020600020905b81548152906001019060200180831161055557829003601f168201915b505050505060068054600181600116156101000203166002900480601f01602080910402602001604051908101604052809291908181526020018280546001816001161561010002031660029004801561060d5780601f106105e25761010080835404028352916020019161060d565b820191906000526020600020905b8154815290600101906020018083116105f057829003601f168201915b5050505050610976565b6109c3565b507f2da49ac810e00dfaa00ca0a76665b434bdca51afb437994cbd47784dbe2e04006040518080602001828103825260178152602001807f51756572792073656e7420746f204f7261636c697a652e00000000000000000081525060200191505060405180910390a1565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614806106f7575060006106f56000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16610ed1565b145b15610708576107066000610edc565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561078c57600080fd5b5af1151561079957600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415156108d5576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561087d57600080fd5b5af1151561088a57600080fd5b50505060405180519050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561095a57600080fd5b5af1151561096757600080fd5b50505060405180519050905090565b61097e6117bf565b6109bb838360206040519081016040528060008152506020604051908101604052806000815250602060405190810160405280600081525061136d565b905092915050565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161480610a3557506000610a336000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16610ed1565b145b15610a4657610a446000610edc565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610aca57600080fd5b5af11515610ad757600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610c13576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610bbb57600080fd5b5af11515610bc857600080fd5b50505060405180519050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663524f3889856040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610cbd578082015181840152602081019050610ca2565b50505050905090810190601f168015610cea5780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b1515610d0857600080fd5b5af11515610d1557600080fd5b50505060405180519050905062030d403a02670de0b6b3a764000001811115610d445760006001029150610ec9565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f99828787876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610dfb578082015181840152602081019050610de0565b50505050905090810190601f168015610e285780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610e61578082015181840152602081019050610e46565b50505050905090810190601f168015610e8e5780820380516001836020036101000a031916815260200191505b50955050505050506020604051808303818588803b1515610eae57600080fd5b5af11515610ebb57600080fd5b505050506040518051905091505b509392505050565b6000813b9050919050565b600080610efc731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed610ed1565b1115610f9d57731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610f946040805190810160405280600b81526020017f6574685f6d61696e6e65740000000000000000000000000000000000000000008152506117a5565b60019050611368565b6000610fbc73c03a2615d5efaf5f49f60b7bb6583eaec212fdf1610ed1565b111561105d5773c03a2615d5efaf5f49f60b7bb6583eaec212fdf16000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506110546040805190810160405280600c81526020017f6574685f726f707374656e3300000000000000000000000000000000000000008152506117a5565b60019050611368565b600061107c73b7a07bcf2ba2f2703b24c0691b5278999c59ac7e610ed1565b111561111d5773b7a07bcf2ba2f2703b24c0691b5278999c59ac7e6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506111146040805190810160405280600981526020017f6574685f6b6f76616e00000000000000000000000000000000000000000000008152506117a5565b60019050611368565b600061113c73146500cfd35b22e4a392fe0adc06de1a1368ed48610ed1565b11156111dd5773146500cfd35b22e4a392fe0adc06de1a1368ed486000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506111d46040805190810160405280600b81526020017f6574685f72696e6b6562790000000000000000000000000000000000000000008152506117a5565b60019050611368565b60006111fc736f485c8bf6fc43ea212e93bbf8ce046c7f1cb475610ed1565b111561125f57736f485c8bf6fc43ea212e93bbf8ce046c7f1cb4756000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060019050611368565b600061127e7320e12a1f859b3feae5fb2a0a32c18f5a65555bbf610ed1565b11156112e1577320e12a1f859b3feae5fb2a0a32c18f5a65555bbf6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060019050611368565b60006113007351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa610ed1565b1115611363577351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555060019050611368565b600090505b919050565b6113756117bf565b61137d6117d3565b6113856117d3565b61138d6117d3565b6113956117d3565b61139d6117d3565b6113a56117bf565b6113ad6117d3565b6000808e98508d97508c96508b95508a94508451865188518a518c51010101016040518059106113da5750595b9080825280601f01601f1916602001820160405250935083925060009150600090505b88518110156114b057888181518110151561141457fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561147357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535080806001019150506113fd565b600090505b87518110156115685787818151811015156114cc57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561152b57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535080806001019150506114b5565b600090505b865181101561162057868181518110151561158457fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f01000000000000000000000000000000000000000000000000000000000000000283838060010194508151811015156115e357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a905350808060010191505061156d565b600090505b85518110156116d857858181518110151561163c57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561169b57fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a9053508080600101915050611625565b600090505b84518110156117905784818151811015156116f457fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f010000000000000000000000000000000000000000000000000000000000000002838380600101945081518110151561175357fe5b9060200101907effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff1916908160001a90535080806001019150506116dd565b82995050505050505050505095945050505050565b80600290805190602001906117bb9291906117e7565b5050565b602060405190810160405280600081525090565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061182857805160ff1916838001178555611856565b82800160010185558215611856579182015b8281111561185557825182559160200191906001019061183a565b5b5090506118639190611867565b5090565b61188991905b8082111561188557600081600090555060010161186d565b5090565b905600a165627a7a7230582055121cb6c66bba7a80674baca100577fb07bf0c3398b383a4f1a845cb55305950029";

    protected SmartExchange1(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SmartExchange1(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<LogMeEventResponse> getLogMeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("logMe", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LogMeEventResponse> responses = new ArrayList<LogMeEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            LogMeEventResponse typedResponse = new LogMeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogMeEventResponse> logMeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("logMe", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LogMeEventResponse>() {
            @Override
            public LogMeEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LogMeEventResponse typedResponse = new LogMeEventResponse();
                typedResponse.log = log;
                typedResponse.message = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] myid, String result) {
        final Function function = new Function(
                "__callback", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(myid), 
                new org.web3j.abi.datatypes.Utf8String(result)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> __callback(byte[] myid, String result, byte[] proof) {
        final Function function = new Function(
                "__callback", 
                Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Bytes32(myid), 
                new org.web3j.abi.datatypes.Utf8String(result), 
                new org.web3j.abi.datatypes.DynamicBytes(proof)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> update(BigInteger weiValue) {
        final Function function = new Function(
                "update", 
                Arrays.<Type>asList(), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public static RemoteCall<SmartExchange1> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String btc_address, String eth_address, String satoshi_amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(btc_address), 
                new org.web3j.abi.datatypes.Address(eth_address), 
                new org.web3j.abi.datatypes.Utf8String(satoshi_amount)));
        return deployRemoteCall(SmartExchange1.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<SmartExchange1> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, String btc_address, String eth_address, String satoshi_amount) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.Utf8String(btc_address), 
                new org.web3j.abi.datatypes.Address(eth_address), 
                new org.web3j.abi.datatypes.Utf8String(satoshi_amount)));
        return deployRemoteCall(SmartExchange1.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static SmartExchange1 load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartExchange1(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SmartExchange1 load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SmartExchange1(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class LogMeEventResponse {
        public Log log;

        public String message;
    }
}