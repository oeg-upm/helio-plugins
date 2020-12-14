package helio.plugins.data.providers.tests;
import java.io.File;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import helio.plugins.data.providers.BashProvider;

public class BashProviderTest {



	@Test
	public void testProvider2() throws Exception {
		JsonArray commands = new JsonArray();
		commands.add("pwd");
		commands.add("bash ./src/test/resources/script.sh Thisisworking ./src/test/resources/results.txt");
		JsonObject configure = new JsonObject();
		configure.addProperty("output", "./src/test/resources/results.txt");
		configure.add("commands", commands);
		BashProvider provider = new BashProvider();
		provider.configure(configure);
		String output = transformToString(provider.getData());
		System.out.println(output);
		File file = new File("./src/test/resources/results.txt");
		file.delete();
		Assert.assertTrue(output.trim().equals("Thisisworking"));
	}
	

	 private String transformToString(InputStream  input) {
			StringBuilder translatedStream = new StringBuilder();
			try {
				 int data = input.read();
				 while(data != -1){
						translatedStream.append((char) data);
			            data = input.read();
			           
			        }
					input.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				
				
			}
			return translatedStream.toString();
		} 
}
