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
1. [Create a Fork of this repository](https://github.com/oeg-upm/helio-plugins/blob/master)
2. [Clone the code from the fork and create a new plugin project](https://github.com/oeg-upm/helio-plugins/blob/master)
3. Develop the plugins code
4. (Optional) Open a Pull Request to publish the plugin in the official Helio plugins repository 
6. Upload a release to the official Helio plugins repository
5. Use the plugin

In following subsections, all these steps are explained in detail.

#### [1. Create a Fork of this repository]()

A fork is a copy of the parent repository, which however, remains synchronised with the former repository. When new modifications are commited to the forked repository these are marked as changes from the original repository, and thus, can be later be pushed to the original repository requesting a Pull Request. The bottom line idea is to fork the Helio plugins repository, include the new code in the fork, and then create a Pull Request to merge the new plugin code in the original repository. To fork the Helio plugins repository click in the "Fork" button on the upper right-corner of the repository page.

![ForkButton](https://upload.wikimedia.org/wikipedia/commons/2/26/Fork_button.jpg)

Once forked the a copy of the Helio plugins repository must appear in the private account of the user who forked the repotiroty.

#### [2. Clone the code from the fork and create a new plugin project]()

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

And them, in this new branch copy the folder *./plugin-template* to the correct folder depending on the type of plugin that will be developed. For instance, if the new plugin is a [Data Provider](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users#data-providers) then the target folder should be *providers*. Then, rename the copied folder *plugin-template* with a suitable name that describe the plugin, try to follow the rule *[name]-[plugin type]*. For instance, for an mqqt provider the folder should be called *mqtt-provider*. Once this task is done, you are ready to import this folder in your IDE as a new Maven project. **IT IS IMPORTANT THAT THE ONLY MODIFICATIONS TO THE HELIO PLUGINS REPOSITORY OCCURS UNDER THE NEW PLUGIN FOLDER AND NOTHING ELSE IS MODIFIED OUTSIDE SUCH FOLDER**.

##### Develop the plugin code

You can add the new or modified files to your local repository with: 

`````
git add README.me
`````

You should add a message to reflect your contribution on the project. This could be done with a commit message:

`````
git commit -m "Update README"
`````

This commit with the -m is for short messages. Then, you can verify the git commited with:

`````
git status
`````

Finally, you can use git push to push the change to the current branch of your forked repository:

`````
git push --set-upstream origin readme-update
`````

##### Open a Pull Request

To do a Pull Request, you must click in the Pull Request button and create a new Pull Request

![PullRequest](https://i.imgur.com/tygTzQj.png)

Then, create the Pull Request:

![CreatePullRequest](https://i.imgur.com/KzI5OcA.png)

Select your branch or the master (depending if you created a branch or not) 

#### template 
#### code
#### Join fork
#### create release



