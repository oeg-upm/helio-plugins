# EthereumConnector

This software is a plugin for the [Helio platform](https://helio.linkeddata.es/) that generates RDF using the Ethereum Blockchain, allowing users to extract one block, a list of block or the entire blockchain and use Helio features to publish the data on RDF.

WARNING: This software was used and tested in the Ethereum testnet, use in the Ethereum mainnet at your own risk.

## Quick Start

To use this plugin in Helio, follow these steps:

* Create a mapping using the [Helio specification](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users#helio-mappings)
* In the config, specify the Ethereum Connector jar
* Get the EthereumConnector jar file, then place it in the plugins folder of Helio.
* Run Helio and enjoy your semantic blockchain

### EthereumConnector example mapping

The connector has 4 arguments in the provider:

* type: EthereumDataProvider
* url: URL (and port if is necessary) of the Ethereum blockchain
* blocks: Block/s
    * *. Recover all the chain
    * Number of the block. Recover a specific block.
    * Block 1 - Block 2. Recover the blocks between 2 blocks.
    * Block 1 - * . Recover from 1 block to the final of the chain.
* decrypt: Enables the decodification Hexadecimal to String) of transaction input. Must be true or false .

An example can be:

["provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}]


#### Example 1. Retrieve 1 block.

`````
{
  "datasources" : [
      {
        "id" : "Ethereum Datasource",
        "refresh" : "999999",
		"handler": { "type": "JsonHandler", "iterator" : "$.Blocks.[*]" },
        "provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}
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
        "refresh" : "999999",
		"handler": { "type": "JsonHandler", "iterator" : "$.Blocks.[*]" },
        "provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}
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
        "refresh" : "999999",
		"handler": { "type": "JsonHandler", "iterator" : "$.Blocks.[*]" },
        "provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}
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
        "refresh" : "999999",
		"handler": { "type": "JsonHandler", "iterator" : "$.Blocks.[*]" },
        "provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}
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
        "refresh" : "999999",
		"handler": { "type": "JsonHandler", "iterator" : "$.Blocks.[*]" },
        "provider" : {"type": "EthereumDataProvider", "url" : "https://ropsten.infura.io/v3/****", "blocks" : "755-756", "decrypt" : false}
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
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#block_number", 
               "object" : "{$.Number}",
			   "datatype" : "http://www.w3.org/2001/XMLSchema#positiveInteger",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#difficulty", 
               "object" : "{$.Difficulty}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#gas_limit", 
               "object" : "{$.GasLimit}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#gas_used", 
               "object" : "{$.GasUsed}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#hash", 
               "object" : "{$.Hash}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#parentHash", 
               "object" : "{$.ParentHash}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#size", 
               "object" : "{$.Size}",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#timestamp", 
               "object" : "{$.Timestamp}",
			   "datatype" : "http://www.w3.org/2001/XMLSchema#timestamp",
               "is_literal" : true
            },{
               "predicate" : "http://www.oeg.es/ontology#transactions", 
               "object" : "http://localhost:8000/blockchain/transaction/[encodeB64(regexp_replace(escapeHtml4({$.Transactions}),'[\"\\']+',''))]",
               "is_literal" : false,
            }
        ]
    }
  ]
}
`````

### Config file configuration

An example of a config file could be the following:

`````
{
	"plugins" : [
		{ 
			"type" : "DataProvider", 
			"class":"helio.materialiser.data.providers.EthereumDataProvider", 
			"source":"./plugins/EthereumDataProvider.jar"
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
