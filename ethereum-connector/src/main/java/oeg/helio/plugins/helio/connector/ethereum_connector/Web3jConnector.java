package oeg.helio.plugins.helio.connector.ethereum_connector;

import org.web3j.protocol.Web3j;

public class Web3jConnector {

	public Web3j connection = null;

	public Web3j getConnection() {
		return connection;
	}

	public void setConnection(Web3j connection) {
		this.connection = connection;
	}
	
}
