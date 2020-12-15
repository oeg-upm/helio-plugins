package upm.oeg.helio.plugins.provider;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import helio.framework.materialiser.mappings.DataProvider;

/**
 * This class provides Helio with data retrieved from an MQTT broker
 * @author Lorenzo Gomez
 * @author Andrea Cimmino
 *
 */
public class MqttProvider implements DataProvider {

	// -- Attributes
	
	private static Log log = LogFactory.getLog(MqttProvider.class);
	private static final long serialVersionUID = 1L;
	// ---- MQTT objects
	private SubscriberCallback subscriber;
	private MqttClient client;
	// ---- Connection attributes
	private String brokerAddress; // configurable
	private String[] topics; // configurable
	private String clientId; // Auto-generated
	
	// -- Constructor
	
	/**
	 * Constructor
	 */
	public MqttProvider() {
		super();
		UUID uuid = UUID.randomUUID();
		clientId = "HelioMqttProvider-"+uuid.toString();
		subscriber = new SubscriberCallback();
	}

	// -- Data method
	
	@Override
	public InputStream getData() {
		JsonObject object = subscriber.getCallbackData();
		if(object == null)
			object = new JsonObject();
		return new ByteArrayInputStream(object.toString().getBytes());
	}
	
	// -- Configuration methods
	
	private static final String ADDRESS_TOKEN = "address";
	private static final String TOPICS_TOKEN = "topics";
	@Override
	public void configure(JsonObject configuration) {
		if(configuration.has(ADDRESS_TOKEN)) {
			this.brokerAddress = configuration.get(ADDRESS_TOKEN).getAsString();
			if(configuration.has(TOPICS_TOKEN)) {
				JsonArray topicsArray = configuration.get(TOPICS_TOKEN).getAsJsonArray();
				Gson gson = new Gson();
				this.topics = gson.fromJson(topicsArray , String[].class);
				connect(); // connect to the mqtt
			}else {
				log.error("Provide a JSON configuration file with the key 'topics' which value is an array of strings specifying topics to subscribe");
			}
		}else {
			log.error("Provide a JSON configuration file with the key 'address' which value is a string specifying an MQTT broker address");
		}
		
	}
	
	
	private void connect() {
		 try {
			 System.out.println(brokerAddress+" -> "+topics);
			 this.client= new MqttClient(brokerAddress, clientId);
			 client.setCallback(subscriber);
	         client.connect();
	         for(int index=0; index < topics.length;index++) {
	        	 	String topic = topics[index];
	        	 	client.subscribe(topic);
	         }
		 }
		 catch (MqttException e) {
			 log.error("No broker to connect was found");
			 log.error(e.toString());
	     }
	}
	
	public void disconnect() {
		 try {
			subscriber = null;
            client.close();
		 }
		 catch (MqttException e) {
			 log.error(e.toString());
        }
	 }

}
