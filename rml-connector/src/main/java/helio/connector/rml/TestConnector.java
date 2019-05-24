package helio.connector.rml;

import java.util.ArrayList;
import java.util.List;

import helio.framework.exceptions.NotReachableEndpointException;

public class TestConnector {

	public static void main(String[] args) {
		
		
		List<String> arguments = new ArrayList<>();
		arguments.add("./test/mapping-rml.rml");
		RMLConnector connector = new RMLConnector(arguments);
		try {
			System.out.println(connector.retrieveData());
		} catch (NotReachableEndpointException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
