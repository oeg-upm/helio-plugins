package helio.plugins.data.providers;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import helio.framework.materialiser.mappings.DataProvider;


public class BashProvider implements DataProvider {


	private static final long serialVersionUID = 1L;
	private static Logger logger = LogManager.getLogger(BashProvider.class);

	private List<List<String>> commands;
	private String outputStrFile;
	public BashProvider() {
		super();
		commands = new ArrayList<>();
	}
	
	/**
	 * Json expected { commands = [], output="" }
	 */
	@Override
	public void configure(JsonObject arguments) {
		if(arguments.has("commands")) {
			if(arguments.has("output")) {
				// Load output file
				this.outputStrFile = arguments.get("output").getAsString();
				// Load commands
				JsonArray array = arguments.get("commands").getAsJsonArray();
				for(int index=0; index < array.size(); index++) {
					String oneLineCommand = array.get(index).getAsString();
					String[] commandTokens = oneLineCommand.split(" ");
					List<String> command = new ArrayList<>();
					for(int count =0; count < commandTokens.length; count++) {
						command.add(commandTokens[count]);
					}
					commands.add(command);
				}
				
			}else {
				logger.error("Json configuration is missing mandatory key 'output' that points to a file that will be read by this plugin");
			}
		}else {
			logger.error("Json configuration is missing mandatory key 'commands' that points to an array of bash commands");
		}
		
	}
	
	public void executeCommand(String ... args)  {
	    try {
	        ProcessBuilder pb = new ProcessBuilder(args);
	        pb.inheritIO();
	        Process process = pb.start();
	        process.waitFor();
	        process.exitValue();
	    } catch (Exception e) {
	    		logger.error(e.toString());
		}
	}


	@Override
	public InputStream getData() {
		for(int index=0; index < this.commands.size(); index++) {
			String[] command = this.commands.get(index).toArray(new String[0]);
			executeCommand(command);
		}
		InputStream input = null;
		try {
			input = new FileInputStream(new File(outputStrFile));
		}catch(Exception e) {
			logger.error(e.toString());
		}
		return input;
	}
	

	
	

}
