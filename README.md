<p align="center">
<img src ="https://github.com/PyvesB/AntiWorldFly/blob/master/images/banner.png?raw=true" />
</p>

**AntiWorldFly disables flying and chosen commands when joining or playing in specific worlds, on Minecraft servers running Bukkit or Spigot.**

# Project status

This project is no longer maintained by its original author. I, Sidpatchy, have taken over development of the plugin and intend to continue development in the future. New builds can be found in the [releases section of this repository](https://github.com/PyvesB/AntiWorldFly/releases), as well as on Bukkit and Spigot.

Over the next couple of days I will be working on writing new and improved documentation as well as cleaning up the plugin.

# Useful links

The **Wiki** of Anti World Fly is available by clicking on the *Wiki* tab at the top of the page, or directly on [this link](https://github.com/PyvesB/AntiWorldFly/wiki). Valuable information on how to set the plugin up or use it properly is at your disposal!

For more *information and download links*, please visit the project's webpages:

[AntiWorldFly - Bukkit](http://dev.bukkit.org/bukkit-plugins/anti-world-fly/)

[AntiWorldFly - Spigot](https://www.spigotmc.org/resources/anti-world-fly.86941/)

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

Please let me know if you encounter any problems by opening an Issue, I am happy to help.
