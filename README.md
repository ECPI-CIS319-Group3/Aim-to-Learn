# CIS319 Java Case Study - "Aim to Learn"

## Directory Info

Directory | Info
-|-
[`/AimToLearn`](/AimToLearn) | IntelliJ IDEA project folder, containing Java source files ([`/src/java`](/AimToLearn/src/java/aimtolearn)) and resource files ([`/src/resources`](/AimToLearn/src/resources/aimtolearn))
[`/wireframes`](/wireframes) | Contains [draw.io](https://www.draw.io/) XML files and images
[`/docs`](/docs) | Contains MS Word and Visio documents for this project
[`/assets`](/assets) | Contains misc. assets relating to the project, including [piskel](http://www.piskelapp.com/) files

## Game Info

*Aim to Learn* is an educational shooter that is somewhat modelled after [Space Invaders](https://en.wikipedia.org/wiki/Space_Invaders
). The player is posed a question from one of our three subjects, and various possible answers fall down from above. The player must shoot the correct answer while avoiding falling answers and avoiding shooting the incorrect answer.

### Basic "How to Play"

The player, shown as a ship, is moved with <kbd>&larr;</kbd> and <kbd>&rarr;</kbd> or the <kbd>A</kbd> and <kbd>D</kbd> keys. Shots are fired by pressing <kbd>&uarr;</kbd> or the <kbd>W</kbd> key.

### Technical Information

* Questions are loaded from a [JSON file](/AimToLearn/src/resources/aimtolearn/QnA.json) using Google's [GSON](https://github.com/google/gson) library.
* Each screen is contained within its own `JPanel` and those are swapped between on-the-fly by using `JFrame.setContentPane(...)`. See [Game.java](/AimToLearn/src/java/aimtolearn/Game.java#L70).
* The game ([currently](/AimToLearn/src/java/aimtolearn/Constants.java#L18)) runs at 100 updates per second, which runs on a second thread (see [GameLoop.java](/AimToLearn/src/java/aimtolearn/GameLoop.java)). This updates whatever is currently being shown on the screen using each screen's `tick()` method ([example](/AimToLearn/src/java/aimtolearn/screens/ShipScreen.java#L83)).
