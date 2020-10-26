# EthereumConnector

This software is a plugin for the [Helio platform](https://helio.linkeddata.es/) that generates RDF using the Ethereum Blockchain, allowing users to extract one block, a list of block or the entire blockchain and use Helio features to publish the data on RDF.

WARNING: This software was used and tested in the blockchain testnet, use in the blockchain mainnet at your own risk.

## Quick Start

To use this plugin in Helio, follow these steps:

* Create a mapping using the Helio specification
* In the mapping, specify the Ethereum Connector
* Get the EthereumConnector jar file, then place it in the plugins folder of Helio.
* Run Helio and enjoy your semantic blockchain

### EthereumConnector example mapping

The arguments of the plugin MUST be three:

* Argument 1 -> URL (and port if is necessary)
* Argument 2 -> Block/s
    * *. Recover all the chain
    * Number of the block. Recover a specific block.
    * Block 1 - Block 2. Recover the blocks between 2 blocks.
    * Block 1 - * . Recover from 1 block to the final of the chain.  
* Decodification of transaction input On / Off (Hexadecimal to String).

An example can be:

["https://ropsten.infura.io/v3/****", "78533", "on"]

#### Example 1. Retrieve 1 block.

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "type" : "JsonDatasource",
        "arguments" : ["$.[*]"],
        "connector"  : {
         "arguments" : ["https://ropsten.infura.io/v3/****", "78533", "off"],
         "type" : "EthereumConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "Semantic Ethereum",
      "datasource_ids" : ["Ethereum Datasource"],
      ...
    }
  ]
}
`````

#### Example 2. Retrieve a range given 2 block.

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "type" : "JsonDatasource",
        "arguments" : ["$.[*]"],
        "connector"  : {
         "arguments" : ["https://ropsten.infura.io/v3/****", "78533-81267", "off"],
         "type" : "EthereumConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "Semantic Ethereum",
      "datasource_ids" : ["Ethereum Datasource"],
      ...
    }
  ]
}
`````

#### Example 3. Retrieve for one block to the last block.

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "type" : "JsonDatasource",
        "arguments" : ["$.[*]"],
        "connector"  : {
         "arguments" : ["https://ropsten.infura.io/v3/****", "78533-*", "off"],
         "type" : "EthereumConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "Semantic Ethereum",
      "datasource_ids" : ["Ethereum Datasource"],
      ...
    }
  ]
}
`````

#### Example 4. Retrieve the entire Ethereum blockchain.

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "type" : "JsonDatasource",
        "arguments" : ["$.[*]"],
        "connector"  : {
         "arguments" : ["https://ropsten.infura.io/v3/****", "*", "off"],
         "type" : "EthereumConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "Semantic Ethereum",
      "datasource_ids" : ["Ethereum Datasource"],
      ...
    }
  ]
}
`````

#### Example 5. Complete example.

A complete example of a mapping configuration is shown below:

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "type" : "JsonDatasource",
        "arguments" : ["$.Blocks.[*]"],
        "refresh" : "2592000000",
        "connector"  : {
         "arguments" : ["https://ropsten.infura.io/v3/[APIKEY]", "755", "off"],
         "type" : "EthereumConnector",
        }
      }
  ],
   "resource_rules" : [
    { 
      "id" : "Ethereum RDF data",
      "datasource_ids" : ["Ethereum Datasource"],
      "subject" : "http://localhost:8080/blockchain/block/{$.Number}",
      "properties"  : [
            {
               "predicate" : "http://www.w3.org/1999/02/22-rdf-syntax-ns#type", 
               "object" : "http://www.oeg.es/ontology#Block",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#block_number", 
               "object" : "{$.Number}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#difficulty", 
               "object" : "{$.Difficulty}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#gas_limit", 
               "object" : "{$.gasLimit}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#gas_used", 
               "object" : "{$.gasUsed}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#hash", 
               "object" : "{$.Hash}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#parentHash", 
               "object" : "{$.parentHash}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#size", 
               "object" : "{$.size}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#timestamp", 
               "object" : "{$.timestamp}",
               "is_literal" : "True",
            },{
               "predicate" : "http://www.oeg.es/ontology#transactions", 
               "object" : "http://localhost:8000/blockchain/transaction/[encodeB64(regexp_replace(escapeHtml4({$.Transactions}),'[\"\\']+',''))]",
               "is_literal" : "False",
            }
        ]
    }
  ]
}
`````

### Compiling the plugin

To complile the *jar* of this plugin run the following command:
`````
mvn clean package -DskipTests
`````
A folder called "target" will be created containing the compiled *jar* and a folder with dependencies; the *jar* file must be pasted in the folder plugins folder of Helio in order to use this plugin.
