# Lorenzo Il Magnifico

Welcome to the digital implementation of Cranio Creations' board game [Lorenzo il Magnifico](http://www.craniocreations.it/prodotto/lorenzo-il-magnifico/)!

# How to play

To play the game you need to:
1. Clone this repository
2. Open the folder `/LM45/Build`
3. Open a command line and type `java -jar Server.jar` to start the server.<br>
  When it outputs something like "SocketFactory listening on port xxx", it means it is ready to handle games.
4. Start Main.jar by doubleclicking or by opening another command line and typing `java -jar Main.jar`
5. Choose a username, a network connection (socket and RMI actually available) and an interface (GUI - Graphical User Interface, or CLI - Command Line Interface)
6. Type in the ip of the computer running Server.jar (`127.0.0.1` if it is on the same computer, otherwise you can find it by typing `ipconfig` on a command line on Windows or `ifconfig` in Linux\MacOS)
7. Repeat from point 4 for all the players that want to join the game
8. Have fun!

# Server configuration

If you like to modify some of the configurations of the server (like the timeouts for players' turns) you can find them as jsons under `/LM45/Build/Assets/Json/Config/ServerConfiguration.json`.
If you like to mod the game by changing cards' effects, costs, and things like that, you still find everything under `/LM45/Build/Assets/Json/`.
