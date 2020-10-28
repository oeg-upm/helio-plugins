package helio.materialiser.data.providers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.List;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import com.google.gson.JsonObject;

import helio.framework.materialiser.mappings.DataProvider;

public class EthereumDataProvider implements DataProvider{

	private String resultToHelio = "";
	private EthereumInfoExtractor results = new EthereumInfoExtractor();
	public static Web3jConnector web3j = new Web3jConnector();

	/**
	 * Constructor. 
	 */
	public EthereumDataProvider(){
		web3j.setDecoder(false);
	}

	@Override
	public void configure(JsonObject arg0) {
		if(arg0.get("url") != null) {
			web3j.setConnection(Web3j.build(new HttpService(arg0.get("url").getAsString())));
		}
		if(arg0.get("blocks") != null) {
			if(arg0.get("blocks").getAsString().contentEquals("*")) {
				resultToHelio = GetBlockData(Integer.parseInt(arg0.get("blocks").getAsString()),null);
			}else if(arg0.get("blocks").getAsString().contains("-")) {
				String[] msg = arg0.get("blocks").getAsString().split("-");
				int x = Integer.parseInt(msg[0]);
				if(msg[1].contentEquals("*")) {
					msg[1] = "-1";
				}
				int y = Integer.parseInt(msg[1]);
				resultToHelio = GetBlockData(x,y);	
			}else {
				resultToHelio = GetBlockData(Integer.parseInt(arg0.get("blocks").getAsString()),null);	
			}
		}
		if(arg0.get("decrypt") != null) {
			if(arg0.get("decrypt").getAsBoolean()) {
				web3j.setDecoder(true);
			}
		}
		System.out.println(resultToHelio);
	}

	/**
	 * Connection to HELIO
	 */
	@Override
	public InputStream getData() {
		return new ByteArrayInputStream(resultToHelio.getBytes());
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


	/*
	 * For testing, uncomment these lines
	 */
//		public static void main(String [] args) {
//			JsonObject jsonO = new JsonObject();
//			jsonO.addProperty("url", "https://ropsten.infura.io/v3/d706a367d1c147e795bfee102e622a28");
//			jsonO.addProperty("blocks", "755-757");
//			jsonO.addProperty("decrypt", false);
//			EthereumDataProvider ec = new EthereumDataProvider();
//			ec.configure(jsonO);
//		}
}
