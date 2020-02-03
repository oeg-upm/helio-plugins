package oeg.helio.plugins.helio.connector.auxiliar_package;

import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class JSONManipulation {

	private static final Gson gson = new Gson();

	public JSONManipulation(){

	}
	
	/**
	 * Check if the string is a JSON.
	 */
	public static boolean isJSONValid(String jsonInString) {
		try {
			gson.fromJson(jsonInString, Object.class);
			return true;
		} catch(com.google.gson.JsonSyntaxException ex) { 
			return false;
		}
	}

	/**
	 * If the input in the transaction is wanted to decoder, call this function.
	 */
	public JsonObject HexToString(String hexadecimal)  {
		JsonObject contentInString = new JsonObject();
		try {
			String stringConverter = new String(Hex.decodeHex(hexadecimal.toCharArray()), "UTF-8");
			boolean aux = isJSONValid(stringConverter);
			//If the input is not a JSON or valid JSON, return the string already decode.
			if(!aux) {
				contentInString.addProperty("Input", stringConverter);
			//Else, return the json inside the Input field
			}else {
				JsonElement jsonElement = new JsonParser().parse(stringConverter);
				contentInString = jsonElement.getAsJsonObject();
			}
		} catch (DecoderException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return contentInString;
	}
}
