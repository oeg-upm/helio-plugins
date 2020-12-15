package plugins.mqqprovider;

import java.io.InputStream;
import java.util.Scanner;

import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import upm.oeg.helio.plugins.provider.MqttProvider;

public class MqttProviderTest {

	
	@SuppressWarnings("resource")
	@Test
	public void testConnection() throws InterruptedException {
		JsonArray topics = new JsonArray();
		topics.add("home/garden/fountain");
		JsonObject configuration = new JsonObject();
		configuration.addProperty("address", "tcp://localhost");
		configuration.add("topics", topics);
		MqttProvider provider = new MqttProvider();
		provider.configure(configuration);
		while(true) {
		InputStream stream = provider.getData();
		// transform stream to string
		 StringBuffer sb = new StringBuffer();
		 Scanner sc = new Scanner(stream);
	      while(sc.hasNext()){
	         sb.append(sc.nextLine());
	      }
	      System.out.println(sb.toString());
		  Thread.sleep(1000);
		  
		}

		
	}
}
