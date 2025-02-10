<p align="center">
<img src ="https://github.com/PyvesB/AntiWorldFly/blob/master/images/banner.png?raw=true" />
</p>

**AntiWorldFly disables flying, elytras, and chosen commands when joining or playing in specific worlds, on Minecraft servers running Spigot, Paper, or one of the many forks.**

[![Build Status](https://img.shields.io/teamcity/build/s/AntiWorldFly_Build?server=https%3A%2F%2Fci.sidpatchy.com&style=flat-square)
](https://ci.sidpatchy.com/project/AntiWorldFly) 
[![License](https://img.shields.io/github/license/PyvesB/AntiWorldFly?style=flat-square)](https://github.com/PyvesB/AntiWorldFly/blob/master/LICENSE)

# Useful links

The **Wiki** of Anti World Fly is available by clicking on the *Wiki* tab at the top of the page, or directly on [this link](https://github.com/PyvesB/AntiWorldFly/wiki). Valuable information on how to set the plugin up or use it properly is at your disposal!

For more *information and download links*, please visit the project's webpages:
- [Spigot](https://www.spigotmc.org/resources/anti-world-fly.5357/)
- [Modrinth](https://modrinth.com/plugin/antiworldfly)
- [PaperMC](https://hangar.papermc.io/Sidpatchy/AntiWorldFly)
- [Bukkit](http://dev.bukkit.org/bukkit-plugins/anti-world-fly/)
- [Development Builds](https://ci.sidpatchy.com/project/AntiWorldFly/)

Feel free to visit **HelloMinecraft**, the plugin's official server (french server):
[![HelloMinecraft](http://img11.hostingpics.net/pics/487719servericon.png)](http://hellominecraft.fr/)

# Setting your own working copy of the project

Ensure you have a working version of the [JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) (Java Development Kit).

The following steps are for the [Eclipse](https://eclipse.org/) development environment, but will be similar on other IDEs.

* Download or clone the repository on your computer. You can also create your own fork by clicking on the *Fork* icon on the top right of the page.
* In Eclipse, go to *File* -> *Import...* -> *Maven* -> *Existing Maven Projects*.
* In the *Root Directory* field, select the location where you downloaded the Anti World Fly repository.
* Tick the *pom.xml* box that appears in the *Projects* field and click *Finish*.
* To compile the plugin, in the *Package Explorer* window, right click on the imported project, then *Run As* -> *Maven Install*.
* The plugin will be generated in the *target* folder of the project.

Please let me know if you encounter any problems by [opening an Issue](https://github.com/PyvesB/AntiWorldFly/issues/new/choose), I am happy to help.
