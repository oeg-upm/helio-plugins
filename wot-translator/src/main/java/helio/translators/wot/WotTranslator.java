package helio.translators.wot;


import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.apache.jena.rdf.model.NodeIterator;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;
import helio.components.connector.URLConnector;
import helio.components.datasource.JsonDatasource;
import helio.framework.Connector;
import helio.framework.Datasource;
import helio.framework.MappingTranslator;
import helio.framework.exceptions.MalformedMappingException;
import helio.framework.mapping.DatasourceMapping;
import helio.framework.mapping.Mapping;
import helio.framework.mapping.PropertyRule;
import helio.framework.mapping.ResourceRule;
import helio.framework.objects.RDF;

public class WotTranslator implements MappingTranslator{

	private static final String CORE_PREFIX="http://iot.linkeddata.es/def/core#";
	private static final String MAP_PREFIX="http://iot.linkeddata.es/def/wot-mappings#";
	
	private static final String CORE_TYPE_TD="ThingDescription";
	private static final String CORE_PROPERTY_DESCRIBES="describes";
	private static final String MAP_PROPERTY_MAPS_RESOURCES_FROM="mapsResourceFrom";
	private static final String MAP_PROPERTY_HAS_ACCESS_MAPPINGS = "hasAccessMapping";
	private static final String CORE_PROPERTY_HREF = "http://iot.linkeddata.es/def/wot#href";
	private static final String MAP_PROPERTY_HAS_MAPPINGS = "hasMapping";
	
	private static final String MAP_PROPERTY_JSONPATH = "jsonPath";
	private static final String MAP_PROPERTY_PREDICATE = "predicate"; 
	private static final String MAP_PROPERTY_DATATYPE = "targetDatatype";
	private static final String MAP_PROPERTY_TARGET_CLASS = "targetClass";
	
	public static Logger log = Logger.getLogger(WotTranslator.class.getName());

	
	public Boolean isCompatible(String mappingContent) {
		Boolean parseable = false;
		try {
			RDF rmlRDFMapping = new RDF();
			rmlRDFMapping.parseRDF(mappingContent);
			if(!rmlRDFMapping.getRDF().isEmpty())
				parseable = rmlRDFMapping.getRDF().listSubjectsWithProperty(ResourceFactory.createProperty("http://iot.linkeddata.es/def/wot-mappings#hasAccessMapping")).hasNext();
		}catch(Exception e){
			// empty on purpose, this exception will happened when the mapping content is not RDF and an attempt of parsing it is requested 
		}
		return parseable;
	}

	public Mapping translate(String mappingContent) throws MalformedMappingException {
		Mapping mapping = new Mapping();
		if(isCompatible(mappingContent)) {
			RDF rmlRDFMapping = new RDF();
			rmlRDFMapping.parseRDF(mappingContent);
			// Splits the mapping into sub-mappings per each ThingDescription within
			ResIterator iterator = rmlRDFMapping.getRDF().listSubjectsWithProperty(org.apache.jena.vocabulary.RDF.type, (RDFNode) ResourceFactory.createResource(iri(CORE_PREFIX,CORE_TYPE_TD)));
			while(iterator.hasNext()) {
				Resource tdResource = iterator.next();
				Mapping newMapping = processWotMapping(rmlRDFMapping,tdResource);
				// the name of the datasources ID encodes the data endpoint URL, therefore when including the new DatasourceMapping
				// there is no need to include it twice, specially for efficency purposes. Include only those not present yet
				for(DatasourceMapping datasourceMapping:newMapping.getDatasources()) {
					if(mapping.getDatasourceById(datasourceMapping.getId())==null) {
						mapping.getDatasources().add(datasourceMapping);
					}
				}
				mapping.getResourceRules().addAll(newMapping.getResourceRules());
			}
		}
	
		
		return mapping;
	}

	private Mapping processWotMapping(RDF rmlRDFMapping, Resource tdResource) {
		Mapping mapping = new Mapping();
		// 1. Find IRIs described by this mappings
		String describedSubjects = findDescribedIRIs(rmlRDFMapping, tdResource);
		// 2. Create datasources and translation rules
		if(describedSubjects!=null) {
			Property hasAccessMappings = ResourceFactory.createProperty(iri(MAP_PREFIX, MAP_PROPERTY_HAS_ACCESS_MAPPINGS));
			List<RDFNode> accessMappings = rmlRDFMapping.getRDF().listObjectsOfProperty(tdResource, hasAccessMappings).toList();
			int accessMappingsSize = accessMappings.size();
			for(int index=0; index < accessMappingsSize; index++) {
				RDFNode accessMapping = accessMappings.get(index);
				// 1. Extract datasources
				DatasourceMapping datasourceMapping = createDatasourceFromMapping(rmlRDFMapping, accessMapping);
				mapping.getDatasources().add(datasourceMapping);
				// 2. Extract resource Rules
				ResourceRule resourceRule = createResourceRule(describedSubjects, rmlRDFMapping, accessMapping);
				resourceRule.getDatasourcesId().add(datasourceMapping.getId());
				mapping.getResourceRules().add(resourceRule);
			}
		}
		return mapping;
	}
	

	
	/**
	 * Finds the subject IRI to which virtual RDF will be attached
	 * @param rmlRDFMapping a {@link RDF} model where the mapping is contained
	 * @param tdResource the {@link Resource} of the Thing Description
	 * @return The IRI of the subject described by the Thing Description {@link Resource}
	 */
	private String findDescribedIRIs(RDF rmlRDFMapping, Resource tdResource){
		String subjectFound = null;
		try {
			
			Property coreDescribes = ResourceFactory.createProperty(iri(CORE_PREFIX, CORE_PROPERTY_DESCRIBES));
			List<String> subjectIris = rmlRDFMapping.getRDF().listObjectsOfProperty(tdResource, coreDescribes).toList().stream().map(subject -> subject.toString()).collect(Collectors.toList());
			if(subjectIris.size()==1) {
				subjectFound = subjectIris.get(0);
			}else {
				throw new MalformedMappingException("Thing Description describes more than one Thing");
			}
		}catch(Exception e) {
			log.severe(e.getMessage());
		}
		return subjectFound;
	}
	
	/**
	 * Creates a {@link DatasourceMapping} from a given mapping
	 * @param rmlRDFMapping a {@link RDF} model where the mapping is contained
	 * @param accessMappingIRI the {@link Resource} of an AccessMapping node
	 * @return A {@link DatasourceMapping} object instantiated with the provided mapping features
	 */
	private DatasourceMapping createDatasourceFromMapping(RDF rmlRDFMapping, RDFNode accessMappingIRI) {
		DatasourceMapping datasourceMapping = null;
		String link = null;
		try {
			link = retrieveLinkFromAccessMapping(rmlRDFMapping, accessMappingIRI);
		}catch(Exception e) {
			log.severe(e.getMessage());
		}
		if(link!=null) {
			
			List<String> connectorArguments = new ArrayList<>();
			connectorArguments.add(link);
			Connector connector = new URLConnector(connectorArguments);
			List<String> datasourceArguments = new ArrayList<>();
			datasourceArguments.add("$");
			Datasource datasource = new JsonDatasource(connector,datasourceArguments);
			
			datasourceMapping = new DatasourceMapping();
			datasourceMapping.setId("Datasource for:"+link);
			datasourceMapping.setRefreshTime(Long.valueOf("-1"));
			datasourceMapping.setDatasource(datasource);
			}
		
		return datasourceMapping;
	}
	

	private String retrieveLinkFromAccessMapping(RDF rmlRDFMapping, RDFNode accessMappingIRI) throws MalformedMappingException {
		String link = null;
		Property hasLink = ResourceFactory.createProperty(iri(MAP_PREFIX, MAP_PROPERTY_MAPS_RESOURCES_FROM));
		List<RDFNode> linksIRIs = rmlRDFMapping.getRDF().listObjectsOfProperty(accessMappingIRI.asResource(), hasLink).toList();
		if(linksIRIs.size()==1) {
			Resource linkIRI = linksIRIs.get(0).asResource();
			Property href = ResourceFactory.createProperty(CORE_PROPERTY_HREF);
			List<RDFNode> hrefsLink = rmlRDFMapping.getRDF().listObjectsOfProperty(linkIRI, href).toList();
			if(hrefsLink.size()==1) {
				link = hrefsLink.get(0).toString();
			}else {
				throw new MalformedMappingException("The link provided '"+linkIRI+"'has more than one href");
			}
		}else {
			throw new MalformedMappingException("The access mapping provided '"+accessMappingIRI+"'has more than one link");
		}
		return link;
	}

	/**
	 * Creates a {@link ResourceRule} from a given mapping
	 * @param describedSubject the IRI of the subject to which the virtual rdf will be attached
	 * @param rmlRDFMapping a {@link RDF} model where the mapping is contained
	 * @param accessMapping the {@link Resource} of an AccessMapping node
	 * @return A {@link ResourceRule} object instantiated with the provided mapping features
	 */
	private ResourceRule createResourceRule(String describedSubject, RDF rmlRDFMapping, RDFNode accessMappingIRI) {
		// 1. Create basic resource rule
		ResourceRule resourceRule = new ResourceRule();
		resourceRule.setSubjectIRI(describedSubject);
		resourceRule.setResourceRuleId(createIdentifier(accessMappingIRI.toString()));
		// 2. Find all mappings
		Property hasMappings = ResourceFactory.createProperty(iri(MAP_PREFIX, MAP_PROPERTY_HAS_MAPPINGS));
		NodeIterator mappingsIterator = rmlRDFMapping.getRDF().listObjectsOfProperty(accessMappingIRI.asResource(), hasMappings);
		while(mappingsIterator.hasNext()) {
			RDFNode mappingIRI = mappingsIterator.next();
			// 2.1 Include the property rules from the mapping
			try {
				PropertyRule propertyRule = createPropertyRule(rmlRDFMapping, mappingIRI);
				resourceRule.getProperties().add(propertyRule);
			}catch (Exception e) {
				log.severe(e.getMessage());
			}
		}
		return resourceRule;
	}
	
	private PropertyRule createPropertyRule(RDF rmlRDFMapping, RDFNode mappingIRI) throws MalformedMappingException {
		PropertyRule propertyRule = new PropertyRule();
		String predicate = extractUnaryPropertyValue(rmlRDFMapping, mappingIRI, MAP_PREFIX.concat(MAP_PROPERTY_PREDICATE));
		if(predicate!=null) {
			propertyRule.setPredicate(predicate);
			String jsonPath = extractUnaryPropertyValue(rmlRDFMapping, mappingIRI, MAP_PREFIX.concat(MAP_PROPERTY_JSONPATH));
			if(jsonPath!=null)
				propertyRule.setObject("{"+jsonPath+"}");
			NodeIterator iterator = rmlRDFMapping.getRDF().listObjectsOfProperty(mappingIRI.asResource(), org.apache.jena.vocabulary.RDF.type);
			while(iterator.hasNext()) {
				RDFNode node = iterator.next();
				if(node.toString().equals("http://iot.linkeddata.es/def/wot-mappings#DataProperty")) {
					String datatype= extractUnaryPropertyValue(rmlRDFMapping, mappingIRI, MAP_PREFIX.concat(MAP_PROPERTY_DATATYPE));
					if(datatype!=null)
						propertyRule.setDataType(datatype);
					propertyRule.setIsLiteral(true);
				}else if(node.toString().equals("http://iot.linkeddata.es/def/wot-mappings#ObjectProperty")){
					propertyRule.setIsLiteral(false);
					String targetCLass = extractUnaryPropertyValue(rmlRDFMapping, mappingIRI, MAP_PREFIX.concat(MAP_PROPERTY_TARGET_CLASS)); // TODO: this is not unary
					if(targetCLass!=null) {
						propertyRule.setObject(targetCLass);
					}else {
						// check that we have the property that links to another ThingDescription, and retrieve the subject described by it 
						//TODO:
					}
				}else {
					throw new MalformedMappingException("Provided mapping is not a map:DataProperty nor map:ObjectProperty "+mappingIRI);
				}
			}
		}else {
			throw new MalformedMappingException("No map:predicate values was found in mapping "+mappingIRI);
		}
		return propertyRule;
	}

	
	
	
	private String createIdentifier(String accessMappingIRI) {
		StringBuilder id  = new StringBuilder();
		Date now = new Date();
		String basicBase64format= Base64.getEncoder().encodeToString(now.toString().getBytes());
		id.append(accessMappingIRI.hashCode()).append(":").append(basicBase64format);
		return id.toString();
	}
	
	private String extractUnaryPropertyValue(RDF rmlRDFMapping, RDFNode mappingIRI, String property) {
		String value = null;
		List<String> values = rmlRDFMapping.getLiteralObjectProperties(mappingIRI.toString(), property);
		if(values.size()==1) {
			value = values.get(0);
		}else if(values.size()==0){
			log.warning("Mapping "+mappingIRI.toString()+"' contains no value for property: "+property);
		} else {
			log.severe("Error in mapping '"+mappingIRI.toString()+"' it contains more than one value for property: "+property);
		}
		return value;
	}
	
	
	// -- Aux methods
	private String iri(String prefix, String property) {
		StringBuilder iri= new StringBuilder();
		iri.append(prefix).append(property);
		return iri.toString();
	}
}
