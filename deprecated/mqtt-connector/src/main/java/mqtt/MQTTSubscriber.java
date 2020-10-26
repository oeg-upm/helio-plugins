package mqtt;


import java.util.List;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.*;

/**
 * In the MQTTSubscriber class we will subscribe to the different topics.
 * The class have a constructor with the different parameters, a method for start the service
 * and other for stop the service.
 * @author Lorenzo Gomez
 *
 */
public class MQTTSubscriber {
	
	private Logger log = Logger.getLogger(MQTTSubscriber.class.getName());

	private MqttClient client;
	private String brokerAddress;
	private SubscriberCallback callback;
	private List<String> topics;
	private String clientName;
	
	/**
	 * The constructor of this class receives as input two Strings and a List of topics
	 * brokerAdress: Address of the broker
	 * clientName: Customer identifier that uses the service
	 * topics: List of subscriptions of all the information to be provided
	 * @param brokerAddress
	 * @param clientName
	 * @param topics
	 */
	public MQTTSubscriber(String brokerAddress, String clientName, List<String> topics) {
		this.topics = topics;
		this.brokerAddress = brokerAddress;
		this.clientName = clientName;
	}
	/**
	 * This method initiates the communication
	 */
	 public void start() {
		 try {
			 this.client= new MqttClient(brokerAddress, clientName);
			 callback = new SubscriberCallback(topics);
			 client.setCallback(callback);
             client.connect();
             for(int index=0; index < topics.size();index++) {
            	 	String topic = topics.get(index);
            	 	client.subscribe(topic);
             }
		 }
		 catch (MqttException e) {
			 log.severe("No broker to connect was found");
			 log.severe(e.toString());
         }
	 }
	 /**
	  * 
	  */
	 public void stop() {
		 try {
			 callback = null;
             client.close();
		 }
		 catch (MqttException e) {
			 log.severe(e.toString());
         }
	 }
	 
	 /**
	  * 
	  * @return
	  */
	 public SubscriberCallback getCallback() {
		 return callback;
	 }

	/**
	 * @return the topics to which the MQTTSunscriber is subscribed
	 */
	public List<String> getTopics() {
		return topics;
	}

	/**
	 * @param topics the topics to set, the MQTTSunscriber will subscribed when started
	 */
	public void setTopics(List<String> topics) {
		this.topics = topics;
	}
	 
	 
}