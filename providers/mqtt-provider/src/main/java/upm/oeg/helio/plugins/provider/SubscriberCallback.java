package upm.oeg.helio.plugins.provider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import com.google.gson.JsonObject;

/**
 * This class handles the call backs from the queue topic where the {@link MqttProvider} has been subscribed. It provides a JSON containing the topics and the values retrieved
 * @author Lorenzo Gomez
 * @author Andrea Cimmino
 */
public class SubscriberCallback implements MqttCallback {

	private JsonObject jsonObject;
	private static Log log = LogFactory.getLog(SubscriberCallback.class);
	
	/**
	 * Constructor for the class {@link SubscriberCallback} 
	 */
	public SubscriberCallback() {
		jsonObject = new JsonObject();
	}
	
	/**
	 * This method provides log information in case of losing the MQTT connection 
	 */
	@Override
	public void connectionLost(Throwable cause) {
		log.warn(cause.toString());
		
	}

	/**
	 * This method updates the JSON object that contains the values retrieved from the topics in the queue
	 */
	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		if(jsonObject.has(topic))
			jsonObject.remove(topic);
		jsonObject.addProperty(topic, message.toString());
	}

	
	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// empty
	}
	
	/**
	 * This method returns a JSON object containing the latest information per topic provided as call back
	 * @return a {@link JsonObject} containing last call back info from queue per topic
	 */
	public JsonObject getCallbackData() {
		JsonObject temporalObject = jsonObject.deepCopy();
		jsonObject = new JsonObject();
		return temporalObject;
	}
	

}
