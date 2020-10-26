package helio.connector.rml;

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
public class RMLConnector implements Connector{

	private List<String> mappingFiles;
	private Logger log = Logger.getLogger(RMLConnector.class.getName());

	/**
	 * The constructor receives a list of arguments from which each element is the directory of an rml mapping
	 * @param arguments
	 */
	public RMLConnector(List<String> mappingFiles) {
		if(mappingFiles==null || mappingFiles.isEmpty())
			throw new IllegalArgumentException("RMLConnector requires to be provided with a not null list of arguments");
		this.mappingFiles = mappingFiles;
	}
	
	
	public String retrieveData() throws NotReachableEndpointException {
		RDF result = new RDF();
		int mappingsSize = mappingFiles.size();
		for(int index=0; index < mappingsSize; index++) {
			String mappingDirectory = mappingFiles.get(index);
			RDF auxRDF = virtualize(mappingDirectory);
			result.addRDF(auxRDF);
		}
		return result.toString("TURTLE");
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
