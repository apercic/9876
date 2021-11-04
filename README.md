# 9876

The aplication is in two git repositories:

server-side: https://github.com/apercic/9876/tree/development

client-side: https://github.com/apercic/6789/tree/development

\
Pull the code with git clone and run 'mvn clean install' in both directories.

The jar for the server is in directory: \9876\target\demo-0.0.1-SNAPSHOT.jar\
The jar for the client is in directory: \6789\target\game-0.0.1-SNAPSHOT.jar\

First run the server with 'java -jar demo-0.0.1-SNAPSHOT.jar'\
Then open two terminals and run 'java -jar game-0.0.1-SNAPSHOT.jar' in both.\
(this mimics the two players)

First the CLI aplication prompts the user for their name,
it adds a random string to it - generates a game id for the player.
Then it tells the user which mark they are playing - it's either 'X' or 'Y' - it marks their moves on the game board.
The aplication waits for two users to join.
It then prompts the user who's turn it is to make a move.

![image](https://user-images.githubusercontent.com/37778988/140234950-39a01a66-de84-4235-a8d7-9efca05dce66.png)

