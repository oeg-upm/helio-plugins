package helio.materialiser.data.providers;

import org.web3j.protocol.Web3j;

public class Web3jConnector {

	public Web3j connection = null;
	private boolean decoder;

	public boolean isDecoder() {
		return decoder;
	}

	public void setDecoder(boolean decoder) {
		this.decoder = decoder;
	}
	
	public Web3j getConnection() {
		return connection;
	}

	public void setConnection(Web3j connection) {
		this.connection = connection;
	}
	
}
