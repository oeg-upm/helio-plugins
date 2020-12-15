# Helio plugins

This repository contains the code and releases of the official plugins that can be used with both the Helio Materialiser [for users](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users) or [for developers](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-developers) and the [Helio Publisher](https://github.com/oeg-upm/helio/wiki/Helio-Publisher). The following table summarises the plugins available by their type:

| Plugin Type   | Plugin       | Description                                                                                                                                                 |
|---------------|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Data Provider | [BashProvider](https://github.com/oeg-upm/helio-plugins/tree/master/providers/bashprovider#helio-bash-provider-plugin) | This plugin allows to run a set of bash commands, and then, read a file with potential results; e.g., run a docker process and feed Helio with some results |
| Data Provider | [EthereumProvider](https://github.com/oeg-upm/helio-plugins/tree/master/providers/ethereum-provider) | This plugin allows to collect a block or a set of blocks of an [Ethereum Blockchain](https://ethereum.org/en/). Those blocks contain all the original data and meta-data, the data of the block is expressed in JSON |
|               |              |                                                                                                                                                             |


## Using a plugin

#### configuration file
#### invoking the plugin


## Developing a plugin

Helio has been designed to be extensible through custom plugins that are dynamically loaded by either the Helio Materialiser [for users](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users) or [for developers](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-developers) and the [Helio Publisher](https://github.com/oeg-upm/helio/wiki/Helio-Publisher). Developing a new plugin does not require extending the core code of these software artefacts, instead, a plugin consists in an independent project that once compiled as jar is automatically identified and loaded by Helio.

In order to develop a plugin, the following steps must be performed:
1. [Create a Fork of this repository](https://github.com/oeg-upm/helio-plugins#1-create-a-fork-of-this-repository)
2. [Clone the code from the fork and create a new plugin project](https://github.com/oeg-upm/helio-plugins#2-clone-the-code-from-the-fork-and-create-a-new-plugin-project)
3. [Develop the plugin code](https://github.com/oeg-upm/helio-plugins#3-develop-the-plugin-code)
4. [(Optional) Open a Pull Request to publish the plugin's code in the official Helio plugins repository](https://github.com/oeg-upm/helio-plugins#4-open-a-pull-request-to-publish-the-plugins-code-in-the-official-helio-plugins-repository)
5. [(Optional) Upload a release to the official Helio plugins repository](https://github.com/oeg-upm/helio-plugins#5-upload-a-release-to-the-official-helio-plugins-repository)


In following subsections, all these steps are explained in detail. Notice that **any plugin developed, published, and released in the official Helio plugins repository will have an Apache 2.0 license**.

##### [1. Create a Fork of this repository]()

A fork is a copy of the parent repository, which however, remains synchronised with the former repository. When new modifications are commited to the forked repository these are marked as changes from the original repository, and thus, can be later be pushed to the original repository requesting a Pull Request. The bottom line idea is to fork the Helio plugins repository, include the new code in the fork, and then create a Pull Request to merge the new plugin code in the original repository. To fork the Helio plugins repository click in the "Fork" button on the upper right-corner of the repository page.

![ForkButton](https://upload.wikimedia.org/wikipedia/commons/2/26/Fork_button.jpg)

Once forked the a copy of the Helio plugins repository must appear in the private account of the user who forked the repotiroty.

##### [2. Clone the code from the fork and create a new plugin project]()

Once the project is forked the code must be cloned, for this end, the following command can be used. Bear in mind that */username* should be replaced with a valid username.

`````
git clone https://github.com/username/helio-plugins.git
`````

After cloned, is recommended to create a new branch different from the master to isolate the new plugin's code. For this end, use the following command to create a new branch named *new-plugin*, however, any name could be given to the new branch.

`````
git branch new-plugin
`````
Before starting to develop any code, swap to the new brach using the command
`````
git checkout new-plugin
`````
Following, in this new branch create a new folder in one of the existing directories depending on the type of plugin that will be developed. For instance, if the new plugin is a [Data Provider](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users#data-providers) then the new folder should be created under the existing folder *providers*. This new folder should have a suitable name that describes the plugin, try to follow the rule *[name]-[plugin type]*. For instance, for an mqqt provider the new folder should be called *mqtt-provider*. **IT IS IMPORTANT THAT ANY MODIFICATION TO THE HELIO PLUGINS REPOSITORY OCCURS UNDER THE NEW PLUGIN FOLDER AND NOTHING ELSE IS MODIFIED OUTSIDE SUCH FOLDER**.

##### [3. Develop the plugin code]()

Once the folder for the new plugin is created, a new maven project must be created under that folder. After creating the new maven project, open the file *pom.xml* and add the following content.

`````
   <url>https://github.com/oeg-upm/helio-plugins</url>
  
   <properties>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.source>1.8</maven.compiler.source> 
        <java.version>1.8</java.version>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>   
  </properties>
  
  <dependencies>
    <!-- Helio framework -->
    <dependency>
      <groupId>upm.oeg.helio</groupId>
      <artifactId>framework</artifactId>
      <version>X.X</version>
    </dependency>
    
    <!-- Other dependencies go here below -->
   
  </dependencies>
  
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-assembly-plugin</artifactId>
        <version>3.1.1</version>
        <configuration>
          <descriptorRefs>
            <descriptorRef>jar-with-dependencies</descriptorRef>
          </descriptorRefs>
         </configuration>
         <executions>
          <execution>
            <id>make-assembly</id>
            <phase>package</phase>
            <goals>
              <goal>single</goal>
            </goals>
           </execution>
          </executions>
      </plugin> 
    </plugins>
  </build>
`````

Be aware that in the previous snippet the version of the Helio framework is marked as X.X, check and replace these tokens with the latest version of the [Helio Framework Maven dependency from the releases](https://github.com/oeg-upm/helio/releases). Then, download the latest version of this dependency from the releases and run the script *mvn-install.sh* to install this dependency in your local maven environment. 

Now your local environment and IDE are ready to start developing the plugin's code. For this end create a new java class for the plugin and extend any of the interfaces that Helio supports as pluggable. These interfaces and the goal of the code that implements them are the following: 

* *DataProvider* this kind of components retrieve and fetch data from a new data source, regarless the format of such data. For instance, a new provider could retrie the data from an MQTT broker regardless if the format of such data is json, xml, csv, or any other.
* *DataHandler* this kind of components are used to filter and handle the data relying on their format. For instance, a new handler could select some values from an XML file using Json Path expressions, or even iterate over a list in XML using these Json Path expressions.
* *HelioCache* this kind of components are used to store the RDF generated by Helio. For instance, a new cache could store in Git Hub the RDF been generated by Helio providing versioning capabilities to the RDF been produced by Helio in time.
* *Functions* this kind of components are used to extend the functions that can be called from the mappings in Helio.
* *MappingTranslator* this kind of components are used to extend the mapping languages that Helio understands.

Once specified the interface to be implemented the IDE will require a set of methods to be implemented (to know more about these methods and their socope check the [Helio Java Docs](https://oeg-upm.github.io/helio-framework/), otherwise, the plugins in this repository can be taken as an example). As a result, these methods will implement the functionalities of the new plugin.

Finally, **it is required to create a file README.md documenting the plugin**, especially, indicating the structure of the expected json document to setup the new plugin. Once all the code is developed, register the new changes in the git branch of the new plugin. For this end, follow this excerpt of commands.


`````
git checkout -b new-branch // to create and swapt to the new branch if this was not done before, replace new-branch for the name of your branch
git add .
git commit -am "write here a meaningful message"
git push origin new-branch //  replace new-branch for the name of your branch
`````

After pushing the changes to Git Hub, the last step to publish the plugin's code in the official Helio repository is opening a Pull Request


##### [4. Open a Pull Request to publish the plugin's code in the official Helio plugins repository]()

To request a Pull Request there are several options. The easiest is to open in a browser the forked repository and click the button *Compare & Pull Request* that will appear, as shown in the following capture in red.

![Opening a Pull Request](https://i.imgur.com/1U2Z2pQ.png)

After clicking the *Compare & Pull Request* the plugin's branch from the forked repository will appear compared against the master from the original (or the master branch from the forked repository against the master of the original if no additional branch was created). In the image below the red square indicates the  master from the original repository, and the red square the brach of the forked repository. Add an meaninful message for the Pull Request and submit the request.

![Requesting a Pull Request](https://i.imgur.com/8TBDGJR.png)

If the whole process was correctly carried out, as depicted in the figure below, in the tab section *Pull requests* (marked in blue in the figure) of the Helio plugins repository must appear the new created Pull Request (marked in red in the figure). 

![Checking if Pull Request was correctly created](https://i.imgur.com/6RMGOms.png)

##### [5. Upload a release to a repository]()

In order to publish a release of the plugin in the official Helio repository the code of the plugin must be pushed with a Pull Request as previously explained. After that, the managers of the Helio plugins repository will check the code, compile it, and publish the release. Additionally, they will update the current README including the plugin in the table of the available ones, for which end a good documentation of the plugin is required. 
