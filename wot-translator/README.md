
# WoT Mapping Translator Plugin

This piece of software is a plugin for the Helio platform that allows to define mappings using the [Web of Things (WoT) mapping specification](http://iot.linkeddata.es/def/wot-mappings/index-en.html).

The WoT mapping specifications are meant to be used in the context of IoT services that offer data of their sensors. The bottom line is to have on, the one hand, informative descriptions of IoT infrastructures such as the type of devices and sensors within, or the properties that they observed. For this purpose we suggest to use the [WoT ontology](https://www.w3.org/TR/wot-thing-description/) or the [ontology developed by us in the European project VICINITY](http://vicinity.iot.linkeddata.es/vicinity/) **that extends WoT with enhanced capabilities**. On the other hand, once the IoT infrastructures are described, we can specify a mapping that encodes from where the  data related to such infrastructure can be retrieved and how it should be treated in order to generate its RDF version and combine it with the descriptions. As a result, we will obtain an unified view of the description of the IoT infrastructures and their captured data on real-time.

## Quick start

To enable Helio to use this plugin follow these steps:

 * Create a mapping file using the specification of WoT mapping, you may find some example [here](https://github.com/oeg-upm/helio-plugins/tree/master/wot-translator/src/test/resources/mappings) 
 * Place the mapping in the folder from which Helio will read them. 
 * Get the WoT Mapping Translator Plugin *jar* file and place it in the plugins folder of Helio
	 * You can either compile the project as specified below or take the *jar* from the [target folder](https://github.com/oeg-upm/helio-plugins/blob/master/wot-translator/target/helio.translators.wot-0.1.1.jar)
* Run Helio normally


## Compiling the plugin

To compile the plugin and obtain a *jar* file to be used by Helio just run the following command:

````
mvn clean package -DskipTests
````

## Writing a mapping

### Scenario I: one sensor that is related to one endpoint 

Let's imagine we have a photometer. First we are going to generate a description using the [VICINITY ontology](http://vicinity.iot.linkeddata.es/vicinity/) :

``````
prefix wot: <http://iot.linkeddata.es/def/wot#>
prefix core: <http://iot.linkeddata.es/def/core#>
prefix foaf: <http://xmlns.com/foaf/0.1/>
prefix sosa: <http://www.w3.org/ns/sosa/>
prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
prefix ssn: <http://www.w3.org/ns/ssn/>
prefix adp: <http://iot.linkeddata.es/def/adapters#> 
prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix map: <http://iot.linkeddata.es/def/wot-mappings#> 

<http://iot.linkeddata.eu/sensors/Ax0002> rdf:type wot:Thing;
    core:represents <http://iot.linkeddata.eu/object/Ax0002>;
	core:isLocatedAt <http://iot.linkeddata.eu/City/AS123-123-asd>;
<http://iot.linkeddata.eu/object/Ax0002> rdf:type  apd:Photomether;
	core:deviceName "Ax0002" .
     wot:providesInteractionPattern <http://iot.linkeddata.eu/object/Ax0002/patterns/0001>.
     
<http://iot.linkeddata.eu/City/AS123-123-asd> rdf:type s4city:City;	

<http://iot.linkeddata.eu/object/Ax0002/patterns/0001> rdf:type wot:Property;
    wot:interactionName "Magnitude captured by this sensor" ;
	sosa:observes adp:Magnitude ;
	wot:isMeasuredIn om:Radiance_unit ;
	core:hasValue <http://iot.linkeddata.eu/object/Ax0002/value> ;
	wot:isReadableThrough <http://iot.linkeddata.es/link/85288bed-95fb-4d4d-8fe7-1cf9df3f0d44> .
	
<http://iot.linkeddata.eu/object/Ax0002/value> rdf:type core:Value .

<http://iot.linkeddata.es/link/85288bed-95fb-4d4d-8fe7-1cf9df3f0d44> a <http://iot.linkeddata.es/def/wot#Link>;
	<http://iot.linkeddata.es/def/wot#href> "http://api.stars4all.eu/photometers/stars2";
	<http://iot.linkeddata.es/def/wot#mediaType> "application/json" .
``````

The previous description is really naive, and lacks of important elements such as the value for the luminance captured, or some contextual data (GPS, city or country). This kind of information is usually provided by the sensors itself and shall be combined with this description on the fly by Helio. The following mapping allows to fill the lacks of this description.

``````
prefix wot: <http://iot.linkeddata.es/def/wot#>
prefix core: <http://iot.linkeddata.es/def/core#>
prefix foaf: <http://xmlns.com/foaf/0.1/>
prefix sosa: <http://www.w3.org/ns/sosa/>
prefix geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>
prefix ssn: <http://www.w3.org/ns/ssn/>
prefix adp: <http://iot.linkeddata.es/def/adapters#> 
prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
prefix map: <http://iot.linkeddata.es/def/wot-mappings#> 

#### Step 1: define where the endpoint is, i.e., a wot:Link instance. 
####  * For this case the are going to use <http://iot.linkeddata.es/link/85288bed-95fb-4d4d-8fe7-1cf9df3f0d44>

#### Step 2: select the Resources to which we want to append the RDF fetched from the endpoints.
####   We specify where the new RDF will be included by relating such Resource to a core:ThingDescription
####  *  In this case we want to:
####        A) Include in our s4city:City resource  the city extracted from the endpoint				
####        B) Include in our core:Value the value and timestamp provided by the sensors in the endpoint

#### 2.A 
<http://iot.linkeddata.eu/City/AS123-123-asd> core:isDescribedBy <http://iot.linkeddata.es/TD/City>.
<http://iot.linkeddata.es/TD/City> a core:ThingDescription;
  <http://iot.linkeddata.es/def/core#describes> <http://iot.linkeddata.eu/City/AS123-123-asd>;
  <http://iot.linkeddata.es/def/wot-mappings#hasAccessMapping> <http://iot.linkeddata.es/accessmappings/city> .

  <http://iot.linkeddata.es/accessmappings/city> <http://iot.linkeddata.es/def/wot-mappings#hasMapping> <http://iot.linkeddata.es/mappings/10>  <http://iot.linkeddata.es/mappings/11>  <http://iot.linkeddata.es/mappings/12>;
  <http://iot.linkeddata.es/def/wot-mappings#mapsResourceFrom> <http://iot.linkeddata.es/link/85288bed-95fb-4d4d-8fe7-1cf9df3f0d44> .

 <http://iot.linkeddata.es/mappings/10> a <http://iot.linkeddata.es/def/wot-mappings#ObjectProperty> ;
  	<http://iot.linkeddata.es/def/wot-mappings#predicate> "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
  	<http://iot.linkeddata.es/def/wot-mappings#targetClass> "http://schema.org/City".
 <http://iot.linkeddata.es/mappings/11> a <http://iot.linkeddata.es/def/wot-mappings#DataProperty> ;
  	<http://iot.linkeddata.es/def/wot-mappings#predicate> "http://schema.org/name";
  	<http://iot.linkeddata.es/def/wot-mappings#jsonPath> "$.city".
 <http://iot.linkeddata.es/mappings/12> a <http://iot.linkeddata.es/def/wot-mappings#DataProperty> ;
  	<http://iot.linkeddata.es/def/wot-mappings#predicate> "http://schema.org/location";
  	<http://iot.linkeddata.es/def/wot-mappings#jsonPath> "$.location".
  	
#### 2.B
<http://iot.linkeddata.eu/object/Ax0002/value> core:isDescribedBy <http://iot.linkeddata.es/TD/Values>.
<http://iot.linkeddata.es/TD/Values> a core:ThingDescription;
  <http://iot.linkeddata.es/def/core#describes> <http://iot.linkeddata.eu/object/Ax0002/value>;
  <http://iot.linkeddata.es/def/wot-mappings#hasAccessMapping> <http://iot.linkeddata.es/accessmappings/values> .

  <http://iot.linkeddata.es/accessmappings/values> <http://iot.linkeddata.es/def/wot-mappings#hasMapping> <http://iot.linkeddata.es/mappings/20>  <http://iot.linkeddata.es/mappings/21>;
  <http://iot.linkeddata.es/def/wot-mappings#mapsResourceFrom> <http://iot.linkeddata.es/link/85288bed-95fb-4d4d-8fe7-1cf9df3f0d44> .

 <http://iot.linkeddata.es/mappings/20> a <http://iot.linkeddata.es/def/wot-mappings#DataProperty> ;
  	<http://iot.linkeddata.es/def/wot-mappings#predicate> "http://iot.linkeddata.es/def/core#literalValue";
  	<http://iot.linkeddata.es/def/wot-mappings#jsonPath> "$.zero_point".
 <http://iot.linkeddata.es/mappings/21> a <http://iot.linkeddata.es/def/wot-mappings#DataProperty> ;
  	<http://iot.linkeddata.es/def/wot-mappings#predicate> "http://iot.linkeddata.es/def/core#timezone";
  	<http://iot.linkeddata.es/def/wot-mappings#jsonPath> "$.local_timezone".
  	
``````