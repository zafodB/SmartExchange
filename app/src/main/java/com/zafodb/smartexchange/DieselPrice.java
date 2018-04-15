package com.zafodb.smartexchange;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
public class DieselPrice extends Contract {
    private static final String BINARY = "60606040526040516020806114fd833981016040528080519060200190919050507fd5dcbea6da87601e93af47cf7a4b7557a93c6d5a430fde6633cd17dc936509076101f482016040518082815260200191505060405180910390a1506114928061006b6000396000f300606060405260043610610062576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff16806327dc297e146100675780632b5f892f146100d157806338bbfa50146100fa578063a2e62045146101a7575b600080fd5b341561007257600080fd5b6100cf60048080356000191690602001909190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506101b1565b005b34156100dc57600080fd5b6100e46102a3565b6040518082815260200191505060405180910390f35b341561010557600080fd5b6101a560048080356000191690602001909190803590602001908201803590602001908080601f0160208091040260200160405190810160405280939291908181526020018383808284378201915050505050509190803590602001908201803590602001908080601f016020809104026020016040519081016040528093929190818152602001838380828437820191505050505050919050506102a9565b005b6101af6102ae565b005b6101b9610400565b73ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156101f257600080fd5b7f3e269f6ec1f86596440d1d097f1ad40a9c21b0bcfce01738d0a4e4570caf611e816040518080602001828103825283818151815260200191508051906020019080838360005b83811015610254578082015181840152602081019050610239565b50505050905090810190601f1680156102815780820380516001836020036101000a031916815260200191505b509250505060405180910390a16102998160026106ef565b6005819055505050565b60055481565b505050565b61036f6040805190810160405280600381526020017f55524c0000000000000000000000000000000000000000000000000000000000815250608060405190810160405280604581526020017f786d6c2868747470733a2f2f7777772e6675656c65636f6e6f6d792e676f762f81526020017f77732f726573742f6675656c707269636573292e6675656c5072696365732e6481526020017f696573656c0000000000000000000000000000000000000000000000000000008152506109e9565b507f46cb989ef9cef13e930e3b7f286225a086e716a90d63e0b7da85d310a9db0c9a6040518080602001828103825260358152602001807f4f7261636c697a65207175657279207761732073656e742c207374616e64696e81526020017f6720627920666f722074686520616e737765722e2e000000000000000000000081525060400191505060405180910390a1565b6000806000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614806104705750600061046e6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16610ef7565b145b156104815761047f6000610f02565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b151561050557600080fd5b5af1151561051257600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614151561064e576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156105f657600080fd5b5af1151561060357600080fd5b50505060405180519050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663c281d19e6040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b15156106d357600080fd5b5af115156106e057600080fd5b50505060405180519050905090565b60006106f96113ad565b60008060008693506000925060009150600090505b83518110156109ca5760307f010000000000000000000000000000000000000000000000000000000000000002848281518110151561074957fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191610158015610861575060397f01000000000000000000000000000000000000000000000000000000000000000284828151811015156107f157fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191611155b1561091257811561088457600086141561087a576109ca565b8580600190039650505b600a830292506030848281518110151561089a57fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027f0100000000000000000000000000000000000000000000000000000000000000900403830192506109bd565b602e7f010000000000000000000000000000000000000000000000000000000000000002848281518110151561094457fe5b9060200101517f010000000000000000000000000000000000000000000000000000000000000090047f0100000000000000000000000000000000000000000000000000000000000000027effffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff191614156109bc57600191505b5b808060010191505061070e565b60008611156109dc5785600a0a830292505b8294505050505092915050565b60008060008060009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161480610a5b57506000610a596000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff16610ef7565b145b15610a6c57610a6a6000610f02565b505b6000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610af057600080fd5b5af11515610afd57600080fd5b5050506040518051905073ffffffffffffffffffffffffffffffffffffffff16600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16141515610c39576000809054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166338cc48316040518163ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401602060405180830381600087803b1515610be157600080fd5b5af11515610bee57600080fd5b50505060405180519050600160006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055505b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663524f3889856040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018080602001828103825283818151815260200191508051906020019080838360005b83811015610ce3578082015181840152602081019050610cc8565b50505050905090810190601f168015610d105780820380516001836020036101000a031916815260200191505b5092505050602060405180830381600087803b1515610d2e57600080fd5b5af11515610d3b57600080fd5b50505060405180519050905062030d403a02670de0b6b3a764000001811115610d6a5760006001029150610ef0565b600160009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1663adf59f9982600087876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808481526020018060200180602001838103835285818151815260200191508051906020019080838360005b83811015610e22578082015181840152602081019050610e07565b50505050905090810190601f168015610e4f5780820380516001836020036101000a031916815260200191505b50838103825284818151815260200191508051906020019080838360005b83811015610e88578082015181840152602081019050610e6d565b50505050905090810190601f168015610eb55780820380516001836020036101000a031916815260200191505b50955050505050506020604051808303818588803b1515610ed557600080fd5b5af11515610ee257600080fd5b505050506040518051905091505b5092915050565b6000813b9050919050565b600080610f22731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed610ef7565b1115610fc357731d3b2638a7cc9f2cb3d298a3da7a90b67e5506ed6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550610fba6040805190810160405280600b81526020017f6574685f6d61696e6e6574000000000000000000000000000000000000000000815250611393565b6001905061138e565b6000610fe273c03a2615d5efaf5f49f60b7bb6583eaec212fdf1610ef7565b11156110835773c03a2615d5efaf5f49f60b7bb6583eaec212fdf16000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061107a6040805190810160405280600c81526020017f6574685f726f707374656e330000000000000000000000000000000000000000815250611393565b6001905061138e565b60006110a273b7a07bcf2ba2f2703b24c0691b5278999c59ac7e610ef7565b11156111435773b7a07bcf2ba2f2703b24c0691b5278999c59ac7e6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555061113a6040805190810160405280600981526020017f6574685f6b6f76616e0000000000000000000000000000000000000000000000815250611393565b6001905061138e565b600061116273146500cfd35b22e4a392fe0adc06de1a1368ed48610ef7565b11156112035773146500cfd35b22e4a392fe0adc06de1a1368ed486000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506111fa6040805190810160405280600b81526020017f6574685f72696e6b656279000000000000000000000000000000000000000000815250611393565b6001905061138e565b6000611222736f485c8bf6fc43ea212e93bbf8ce046c7f1cb475610ef7565b111561128557736f485c8bf6fc43ea212e93bbf8ce046c7f1cb4756000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905061138e565b60006112a47320e12a1f859b3feae5fb2a0a32c18f5a65555bbf610ef7565b1115611307577320e12a1f859b3feae5fb2a0a32c18f5a65555bbf6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905061138e565b60006113267351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa610ef7565b1115611389577351efaf4c8b3c9afbd5ab9f4bbc82784ab6ef8faa6000806101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff1602179055506001905061138e565b600090505b919050565b80600290805190602001906113a99291906113c1565b5050565b602060405190810160405280600081525090565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f1061140257805160ff1916838001178555611430565b82800160010185558215611430579182015b8281111561142f578251825591602001919060010190611414565b5b50905061143d9190611441565b5090565b61146391905b8082111561145f576000816000905550600101611447565b5090565b905600a165627a7a72305820b034995d06a105e390d88ffcb49c6260c4b0b02050413f3f6b93ef950bebf25a0029";

    protected DieselPrice(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected DieselPrice(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<NewOraclizeQueryEventResponse> getNewOraclizeQueryEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("newOraclizeQuery", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewOraclizeQueryEventResponse> responses = new ArrayList<NewOraclizeQueryEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewOraclizeQueryEventResponse typedResponse = new NewOraclizeQueryEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.description = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewOraclizeQueryEventResponse> newOraclizeQueryEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("newOraclizeQuery", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewOraclizeQueryEventResponse>() {
            @Override
            public NewOraclizeQueryEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewOraclizeQueryEventResponse typedResponse = new NewOraclizeQueryEventResponse();
                typedResponse.log = log;
                typedResponse.description = (String) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<NewConstructorMessageEventResponse> getNewConstructorMessageEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("newConstructorMessage", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewConstructorMessageEventResponse> responses = new ArrayList<NewConstructorMessageEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewConstructorMessageEventResponse typedResponse = new NewConstructorMessageEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.message = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewConstructorMessageEventResponse> newConstructorMessageEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("newConstructorMessage", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewConstructorMessageEventResponse>() {
            @Override
            public NewConstructorMessageEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewConstructorMessageEventResponse typedResponse = new NewConstructorMessageEventResponse();
                typedResponse.log = log;
                typedResponse.message = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<NewDieselPriceEventResponse> getNewDieselPriceEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("newDieselPrice", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<NewDieselPriceEventResponse> responses = new ArrayList<NewDieselPriceEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            NewDieselPriceEventResponse typedResponse = new NewDieselPriceEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.price = (String) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<NewDieselPriceEventResponse> newDieselPriceEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("newDieselPrice", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, NewDieselPriceEventResponse>() {
            @Override
            public NewDieselPriceEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                NewDieselPriceEventResponse typedResponse = new NewDieselPriceEventResponse();
                typedResponse.log = log;
                typedResponse.price = (String) eventValues.getNonIndexedValues().get(0).getValue();
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

    public RemoteCall<BigInteger> DieselPriceUSD() {
        final Function function = new Function("DieselPriceUSD", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public static RemoteCall<DieselPrice> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, BigInteger constr_message) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(constr_message)));
        return deployRemoteCall(DieselPrice.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static RemoteCall<DieselPrice> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger initialWeiValue, BigInteger constr_message) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new org.web3j.abi.datatypes.generated.Uint256(constr_message)));
        return deployRemoteCall(DieselPrice.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor, initialWeiValue);
    }

    public static DieselPrice load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new DieselPrice(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static DieselPrice load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new DieselPrice(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class NewOraclizeQueryEventResponse {
        public Log log;

        public String description;
    }

    public static class NewConstructorMessageEventResponse {
        public Log log;

        public BigInteger message;
    }

    public static class NewDieselPriceEventResponse {
        public Log log;

        public String price;
    }
}
