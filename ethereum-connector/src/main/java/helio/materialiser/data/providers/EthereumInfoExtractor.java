package helio.materialiser.data.providers;

import java.io.IOException;
import java.math.BigInteger;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.EthBlock.Block;
import org.web3j.protocol.core.methods.response.EthBlock.TransactionResult;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import oeg.helio.plugins.helio.connector.auxiliar_package.JSONManipulation;

public class EthereumInfoExtractor{

	public Web3jConnector web3jConnector;
	private JSONManipulation jsonManipulation;

	public EthereumInfoExtractor() {
		web3jConnector = EthereumDataProvider.web3j;
		jsonManipulation = new JSONManipulation();
	}
	
	/**
	 * 
	 * Extract the entire blockchain or extract the range of one block to the final block of the blockchain.
	 * 
	 */
	public String BlockhainExtractor(BigInteger blockNumber, BigInteger blockNumber2) throws IOException {
		//If there are more than 1 block, we must list all block in a tag called "Blocks"
		JsonObject resultBlock = new JsonObject();
		//Store all the blocks and the transactions that contains each block.
		JsonArray arrayOfBlocks = new JsonArray();
		//If the third parameter in the argument is a *, then the blockNumber2 equals -1, so thats mean that we want the lastest block
		if(blockNumber2 == BigInteger.valueOf(-1)) {
			blockNumber2 = web3jConnector.getConnection().ethGetBlockByNumber(DefaultBlockParameterName.LATEST, true).send().getBlock().getNumber();
		}
		//Browse the blockchain
		for(int i = blockNumber.intValue(); i<= blockNumber2.intValue();i++){
			Block block = web3jConnector.getConnection().ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(i)), true).send().getBlock();
			JsonObject blockJson = new JsonObject();
			JsonArray transactionsArray = new JsonArray();
			blockJson.addProperty("Number", block.getNumber());
			blockJson.addProperty("Author", block.getAuthor());
			blockJson.addProperty("Difficulty", block.getDifficulty());
			blockJson.addProperty("ExtraData", block.getExtraData());
			blockJson.addProperty("GasLimit", block.getGasLimit());
			blockJson.addProperty("GasUsed", block.getGasUsed());
			blockJson.addProperty("Hash", block.getHash());
			blockJson.addProperty("ParentHash", block.getParentHash());
			blockJson.addProperty("Size", block.getSize());
			blockJson.addProperty("Timestamp", block.getTimestamp());
			blockJson.addProperty("LogsBloom", block.getLogsBloom());
			blockJson.addProperty("Miner", block.getMiner());
			blockJson.addProperty("MixHash", block.getMixHash());
			blockJson.addProperty("ReceiptsRoot", block.getReceiptsRoot());
			blockJson.addProperty("Sha3Uncles", block.getSha3Uncles());
			blockJson.addProperty("StateRoot", block.getStateRoot());
			blockJson.addProperty("TransactionsRoot", block.getTransactionsRoot());
			blockJson.addProperty("Nonce", block.getNonce());
			blockJson.addProperty("TotalDifficulty", block.getTotalDifficulty());
			//Inside each block, can be 1 or more transactions.
			for(int j=0; j<block.getTransactions().size(); j++) {
				TransactionResult<Transaction> tr = block.getTransactions().get(j);
				JsonObject transactionJson = new JsonObject();
				transactionJson.addProperty("BlockHash", tr.get().getBlockHash());
				transactionJson.addProperty("BlockNumber", tr.get().getBlockNumber());
				transactionJson.addProperty("From", tr.get().getFrom());
				transactionJson.addProperty("Gas", tr.get().getGas());
				transactionJson.addProperty("GasPrice", tr.get().getGasPrice());
				transactionJson.addProperty("Hash", tr.get().getHash());
				//Check if the user want to decode the input data inside the transaction
				if(!web3jConnector.isDecoder()) {
					transactionJson.addProperty("Input", tr.get().getInput());	
				}else {
					transactionJson.add("Input", jsonManipulation.HexToString(tr.get().getInput().substring(2)));
				}
				transactionJson.addProperty("TransactionIndex", tr.get().getTransactionIndex());	
				transactionJson.addProperty("Creates", tr.get().getCreates());
				transactionJson.addProperty("PublicKey", tr.get().getPublicKey());
				transactionJson.addProperty("Nonce", tr.get().getNonce());
				transactionJson.addProperty("Value", tr.get().getValue());
				transactionJson.addProperty("To", tr.get().getTo());
				transactionJson.addProperty("ChainId", tr.get().getChainId());
				transactionJson.addProperty("S", tr.get().getS());
				transactionJson.addProperty("V", tr.get().getV());
				transactionJson.addProperty("R", tr.get().getR());
				transactionsArray.add(transactionJson);
				blockJson.add("Transactions", transactionsArray);
			}
			arrayOfBlocks.add(blockJson);
			resultBlock.add("Blocks", arrayOfBlocks);
		}
		return resultBlock.toString();
	}
}
