package oeg.helio.plugins.helio.connector.ethereum_connector;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import helio.framework.Connector;
import helio.framework.exceptions.NotReachableEndpointException;

public class EthereumConnector implements Connector{

	private String resultToHelio;
	private EthereumInfoExtractor results = new EthereumInfoExtractor();
	public static Web3jConnector web3j = new Web3jConnector();

	/**
	 * Constructor. 
	 * Arg0 -> Direction for a Ethereum node (MainNet or TestNet).
	 * Arg1 -> Number of first block.
	 * Arg2 -> Number of second block.
	 * @throws NotReachableEndpointException 
	 */
	public EthereumConnector(List<String> arguments){
		web3j.setConnection(Web3j.build(new HttpService(arguments.get(0))));
		if(arguments.get(1).contentEquals("*")) {
			arguments.set(1, "-1");
			resultToHelio = GetBlockData(Integer.parseInt((arguments.get(1))),null);	
		}else if(arguments.get(1).contains("-")){
			String[] msg = arguments.get(1).split("-");
			int x = Integer.parseInt(msg[0]);
			if(msg[1].contentEquals("*")) {
				msg[1] = "-1";
			}
			int y = Integer.parseInt(msg[1]);
			resultToHelio = GetBlockData(x,y);	
		}else{
			resultToHelio = GetBlockData(Integer.parseInt((arguments.get(1))),null);	
		}
	}

	/**
	 * Connection to HELIO
	 */
	@Override
	public String retrieveData() throws NotReachableEndpointException {
		return resultToHelio;
	}

	/**
	 * This function retrieve a block or a list of block, in JSON.
	 */
	public String GetBlockData(Integer blockNum1, Integer blockNum2) {
		try {
			if(blockNum1 == -1) {
				//If the second element of the list is *, the blockNum1 is 0 and the blockNum2 is -1
				return results.BlockhainExtractor(BigInteger.valueOf(0), BigInteger.valueOf(blockNum1));
			}else if(blockNum2 == null){
				//If the user only wants 1 element, we only iterate 1 block
				return results.BlockhainExtractor(BigInteger.valueOf(blockNum1), BigInteger.valueOf(blockNum1));
			}else {
				//To iterate between blocks or one block to the final block.
				return results.BlockhainExtractor(BigInteger.valueOf(blockNum1), BigInteger.valueOf(blockNum2));
			}
		}catch(IOException | NullPointerException e) {
			return e.getMessage();
		}
	}
}
