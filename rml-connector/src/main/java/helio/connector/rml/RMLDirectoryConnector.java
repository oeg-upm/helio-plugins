package helio.connector.rml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import be.ugent.rml.DataFetcher;
import be.ugent.rml.Executor;
import be.ugent.rml.records.RecordsFactory;
import be.ugent.rml.store.RDF4JStore;
import helio.framework.Connector;
import helio.framework.exceptions.NotReachableEndpointException;
import helio.framework.objects.RDF;

/**
 * RMLConnector is meant to retrieve data from heterogeneous data sources using the RML-Mapper implementation.
 * 
 * @author Andrea Cimmino
 *
 */
public class RMLDirectoryConnector implements Connector{

	
	private String mappingDirectory;
	private Logger log = Logger.getLogger(RMLDirectoryConnector.class.getName());

	/**
	 * The constructor receives a list with only one argument that is a directory containing a set of RML mappings
	 * @param arguments
	 */
	public RMLDirectoryConnector(List<String> mappingDirectory) {
		if(mappingDirectory==null || mappingDirectory.isEmpty())
			throw new IllegalArgumentException("RMLConnector requires to be provided with a not null list of arguments");
		if(mappingDirectory.size()>1)
			throw new IllegalArgumentException("RMLConnector requires only one argument that is a directory contaning a set of RML mappings");
		this.mappingDirectory = mappingDirectory.get(0);
	}
	
	
	@Override
	public String retrieveData() throws NotReachableEndpointException {
		RDF result = new RDF();
		try {
			File mappingsFolderFile = new File(mappingDirectory);
			File[] files = mappingsFolderFile.listFiles();
			int filesSize = files.length;
			for(int index=0; index <filesSize; index++) {
				File file = files[index];
				if(!file.isDirectory() && file.getName().endsWith(".rml")) {
					RDF rmlMappingResult = virtualize(file.getAbsolutePath());
					result.addRDF(rmlMappingResult);
				}
			}
			
		}catch (Exception e) {
			log.severe(e.getMessage());
		}
	
		return result.toString();
	}
	
	
	private RDF virtualize(String mappingDirectory) {
		RDF rdf = new RDF();
		String mappingFile = mappingDirectory; //path to the mapping file
		RDFFormat mappingFormat = RDFFormat.TURTLE;
		try {
			InputStream mappingStream = new FileInputStream(mappingFile);
			Model model = Rio.parse(mappingStream, "", mappingFormat);
			RDF4JStore rmlStore = new RDF4JStore(model);
			
			Executor executor = new Executor(rmlStore, new RecordsFactory(new DataFetcher(".", rmlStore)),"");
			// Clean the RDF since virtualRDF contains null instead of . at the end of the N3 representation lines
			String cleanRDF = cleanRDF(executor.execute(null).toString());
			rdf.parseRDF(cleanRDF);
			
			mappingStream.close();
		}catch (Exception e) {
			log.severe(e.getMessage());
		}
		return rdf;
	}

	
	private String cleanRDF(String dirtyRDF) {
		return dirtyRDF.replaceAll("\\s*null\\s*\n", " .\n");
	}

}
