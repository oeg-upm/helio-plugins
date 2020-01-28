
package mqtt;
import org.eclipse.paho.client.mqttv3.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.paho.client.mqttv3.MqttCallback;


/**
 * In this class we recover the values to which we have subscribed
 * @author Lorenzo Gomez
 *
 */
public class SubscriberCallback implements MqttCallback{
	
	private Logger log = Logger.getLogger(SubscriberCallback.class.getName());
	private Map<String,String> map;
	
	/**
	 * The constructor of this class receives as input a {@link List}
	 * @param topics
	 */
	public SubscriberCallback(List<String> topics) {
		map = new HashMap<>();
		topics.stream().forEach(topic -> map.put(topic, "NaN"));
	}
	
	/**
	 * If we lose the connection this method gives us a comment
	 * about the reason for this loss 
	 */
	public void connectionLost(Throwable cause) {
		log.severe(cause.toString());
	}
	
	/**
	 * This method put in a map the results received from the MQTT
	 * If the map contains the topic, we replace the value
	 * If the map not contains the topic, we put the topic with the message
	 */
    public void  messageArrived(String topic, MqttMessage message) {
    		if(map.containsKey(topic)){
	    		map.replace(topic, message.toString());
	    	}else {
	    		map.put(topic, message.toString());
	    	}
    }
    
    /**
     * Return the map with the last information received.
     * This method will be used in the MQTTConnector to provide the information
     * @return
     */
    public Map<String,String> getLastMessages() {
	    	return map;
    }
    
   
    public void deliveryComplete(IMqttDeliveryToken token) {
    		//empty
    }
}