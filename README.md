# Helio plugins

This repository contains the code and releases of the official plugins that can be used with both the Helio Materialiser [for users](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-Users) or [for developers](https://github.com/oeg-upm/helio/wiki/Helio-Materialiser-for-developers) and the [Helio Publisher](https://github.com/oeg-upm/helio/wiki/Helio-Publisher). The following table summarises the plugins available by type:

| Plugin Type   | Plugin       | Description                                                                                                                                                 |
|---------------|--------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Data Provider | [BashProvider](https://github.com/oeg-upm/helio-plugins/tree/master/providers/bashprovider#helio-bash-provider-plugin) | This plugin allows to run a set of bash commands, and then, read a file with potential results; e.g., run a docker process and feed Helio with some results |
| Data Provider | EthereumProvider | This plugin allows to recopile a block or a set of block of the [Ethereum Blockchain](https://ethereum.org/en/) with all the contaned data and meta-data. |
|               |              |                                                                                                                                                             |

## Using a plugin

#### configuration file
#### invoking the plugin


## Developing a plugin

#### fork+PR

##### Creating a fork

A fork is an exact copy of the parent repository. The new fork will remain in sync with the parent repository until you modify your forked copy or if the parent repository is modified. To fork this repository, click in the "Fork" button on your apper right-hand side of the page to start the forking process.

![ForkButton](https://upload.wikimedia.org/wikipedia/commons/2/26/Fork_button.jpg)

Once the process is done, you will have the same repository with your username before the repository name and above, the parent repository.

##### Modify the repository

First of all, clone the fork in your device:

`````
git clone https://github.com/username/helio-plugins.git
`````

You can create a new branch to isolate your code. Its very important create a new branch off of the master branch.

`````
git branch update-readme
`````

You can change to the new branch with the following command:

`````
git checkout readme-update
`````

`````
git checkout readme-update
`````

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
