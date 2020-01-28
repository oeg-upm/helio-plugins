package mqtt;

import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;

import helio.framework.Connector;
import helio.framework.exceptions.NotReachableEndpointException;
import mqtt.MQTTSubscriber;

/**
 * MQTTConector is meant to access to a remote data from an MQTT server
 *@author Lorenzo Gomez
 */
public class MQTTConnector implements Connector {
	
	private MQTTSubscriber subscriber;
	private int queueSize;
	private Queue<String> queue;
	
	
	/**
	 * The constructor of this class receives as input a {@link List} in which the first argument is ...
	 * clientId: Customer identifier that uses the service
	 * brokerAdress: Address of the broker where the MQTT data is receiving the MQTT data
	 * queueSize: number of results you want to get
	 * topics: List of subscriptions of all the information to be provided
	 * @param arguments
	 */
	public MQTTConnector(List<String> arguments) {
		if(arguments==null) 
			throw new IllegalArgumentException("El conector MQTT no soporta una lista nula de parametros");
		if(arguments.get(0).isEmpty())
			throw new IllegalArgumentException("El identificador del cliente no puede estar vacio");
		if(arguments.get(1).isEmpty()) 
			throw new IllegalArgumentException("Debe existir una direccin de la cual recibir informacin, la direccin del broker no puede estar vacia");	
		if(arguments.get(2).isEmpty()) 
			throw new IllegalArgumentException("Debe indicar el numero de resultados que quiere obtener");	
		if(arguments.size() < 4 ) 
			throw new IllegalArgumentException("Debe existir al menos un topic al que estar subscrito");
		
		String clientId=arguments.get(0);
		String brokerAdress=arguments.get(1);
		
		queueSize=Integer.parseInt(arguments.get(2));
		this.queue= new LinkedList<>();
		
		List<String> topics=arguments.subList(3, arguments.size());
		
		
		
		this.subscriber=new MQTTSubscriber(brokerAdress, clientId, topics);
		this.subscriber.start();
	}
	
	/**
	 * The funcionality of this method is to show the data that is received
	 * and show the data in Json format
	 * @throws NotReachableEndpointException
	 */
	public String retrieveData() throws NotReachableEndpointException {
		
		
		Map <String, String> data= subscriber.getCallback().getLastMessages();
		String currentJsonData= generateReading(data);
		
		
		if(queue.size()==queueSize) {
			queue.poll();
		}
		queue.add(currentJsonData);
		
		StringBuilder jsonArray = new StringBuilder("[");
		for(String currentMap: queue) {
			
			jsonArray.append(currentMap).append(", ");
		}
		
		
		
		return jsonArray.substring(0,jsonArray.length()-2).concat("]");
		
	
		
	}
	
	private String generateReading(Map<String,String> data) {
		StringBuilder jsonData= new StringBuilder("{") ;
		for (Entry<String,String> entry: data.entrySet()) {
			jsonData .append("\"").append(entry.getKey());
			jsonData .append("\" : \"").append(entry.getValue()).append("\",\n");
		}
		jsonData .append("\"").append("timestamp");
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		jsonData .append("\" : \"").append(timestamp.toString()).append("\",\n");
		
		return jsonData.substring(0,jsonData .length()-2).concat("}");
	}

}


