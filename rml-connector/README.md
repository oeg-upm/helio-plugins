
# RMLConnector

This piece of software is a plugin for the [Helio platform](https://helio.linkeddata.es/) that generates RDF using the native [implementation of RML-Mapper](https://github.com/RMLio/rmlmapper-java), thus, allowing users to rely on RML mappings to generate the RDF and then use other Helio features to enhance or publish the RDF


## Quick Start

To enable Helio to use this plugin follow these steps:

* Create a mapping **using the Helio specification**
* In the mapping specify any of the RML connectors of this plugin
* Get the RMLConnector jar file and its dependencies folder, then place it in the plugins folder of Helio
* 	You can either compile the project as specified below or [download the compiled dependency](https://drive.upm.es/index.php/s/gztsJCKd8vQ9m5X)
* Run Helio normally

## Connector implementations

In the RML connector plugin there are two implementations that allow to use the RML-Mapper. 

| ClassName             | Arguments                                                                                  | Description                                                                              |
|-----------------------|--------------------------------------------------------------------------------------------|------------------------------------------------------------------------------------------|
| RMLConnector          | A list in which each element is the path, relative or absolute, to a RML mapping file      | This implementation is meant to be use when a few mappings are involved                  |
| RMLDirectoryConnector | A list with only one argument that must be a folder containing a set of RML mapping files  | This implementation is meant to be used when a large number of RML mappings are involved |


### RMLConnector example mapping

`````
{
  "datasources" : [
      {
        "id" : "RML Datasource",
        "type" : "RDFDatasource",
        "arguments" : [],
        "connector"  : {
         "arguments" : ["./mappings/mapping-rml-1.rml", "./mappings/mapping-rml-2.rml"],
         "type" : "RMLConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "RML RDF data",
      "datasource_ids" : ["RML Datasource"],
    }
  ]
}
`````

### RMLDirectoryConnector example mapping

`````
{
  "datasources" : [
      {
        "id" : "RML Datasource",
        "type" : "RDFDatasource",
        "arguments" : [],
        "connector"  : {
         "arguments" : ["./mappings/rml-mappings/"],
         "type" : "RMLConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "RML RDF data",
      "datasource_ids" : ["RML Datasource"],
    }
  ]
}
`````
### Compiling the plugin

To complile the *jar* of this plugin run the following command:
`````
mvn clean install
`````
A folder called dist will be created containing the compiled *jar* and a folder with dependencies called rml; both must be pasted in the folder contaning the plugins of Helio in order to use this plugin.

