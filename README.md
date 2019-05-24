# Helio plugins

In this repository you will find several plugins developed by our team for the [Helio platform](http://helio.linkeddata.es/). There are namely three extendion points: 
 * Translators: that allow Helio to understand other mapping languages, for instace the [WoT mapping](http://iot.linkeddata.es/def/wot-mappings/index-en.html) for which we have implemented [this plugin.](https://github.com/oeg-upm/helio-plugins/tree/master/wot-translator)
 * Connectors: allow Helio to fetch data from different endpoints, for instance a REST service considering the OAuth authentication.
 * Datasources: allow Helio to filter fetched data, for instance for JSON format the Datasource should be able to process JSONPaths
 
 
 # Soon in Helio
 
 Soon we will count with more plugins to integrate the existing technologies to generate RDF based on mappings. Find below our road map:
 
 | Extension point | Plugin name             | Description                                                                                                                                                         |
|-----------------|-------------------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Translator      | RMLTranslator           | This plugin will allow users to define RML mappings to setup Helio                                                                                                  |
| Connector       | RMLConnector            | This connector will allow users to generate RDF using RML mappings relying on the original RML implementation instead of using Helio engine                         |
| Connector       | SPARQLGenerateConnector | This connector will allow users to generate RDF using SPARQL Generate mappings relying on the original SPARQL Generate implementation instead of using Helio engine |
