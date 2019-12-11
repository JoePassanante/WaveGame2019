# WaveGame2019
Wave Game for SER225

## Setup

For eclipse:

- Unzip the file Wave_Game.zip
- Open Eclipse. Press File -> Import -> General -> Exisiting Projects into Workspace -> Next
- Next to "Select root directory", click "Browse..." and then select the folder that was contained in the downloaded zip file.
- Click "Finish" at the bottom.
- Now that the project is imported, double click the class called "GameClient" to open that file.
- With this class open, click the green "Run" button at the top of the screen.
- If a settings menu opens instead of the main game, do the following:
    - Press the small down arrow next to the "Run" button in order to open a dropdown menu
    - Mouse over "Run as" and select "Java Application"

For IDEA:

- Unzip the file Wave_Game.zip
- Open IDEA. Press File -> Open and then select the folder that was contained in the downloaded zip file.
- Click File -> Project Structure and then select a project SDK version that is 1.8 or higher

Information on how to play the game is located under the "?" option in the game menu.

Also check out the [Wiki](https://github.com/JoePassanante/WaveGame2019/wiki) for more information test cases and user stories.

## Project Structure

The project uses several design patterns to minimize redundant code and make adding new features easier:

**Proxy Class:** The GameClient class is a GameLevel that passes events and loop methods to the top of a Stack. The main
thread flushes swing events between loops, so you can expect them to come in one at a time.

**Game Loop:** The original game loop copied this tutorial: https://www.youtube.com/watch?v=1gir2R7G9ws

The tutorial relies on undefined behaviour in swing, as it runs tick methods in a seperate thread than it renders and
listens to input events, which can call GameLevel methods with unsynchronized side effects in two different threads. The
loop has been simplified and modified to tick, render, and listen to inputs in the swing Event Dispatch Thread using
SwingUtilities.

**State Stack:** The current GameLevel is stored on top of a Stack. Menus and game modes can be displayed by simply passing
them to getState().push, as opposed to using some global enumeration, which avoids lots of bugs and null checks. For
examples of how to use the state stack, see GameMode or the Waves and Walls classes.

**Random Object Factories:** Almost every class in the game had a different way of creating random objects when we got the
project. To make the code more predictable and less redundant, we added a RandomDifferentElement class to our own
Random implementation, that returns different random elements of a list. To create random new instances of different
classes, you can use method references to pass the constructors of other classes to its own constructor, for example
like in Waves.Spawn. This is very different than the implementations of the Factory pattern we are taught in school,
because it uses Java 8 features to avoid declaring a new class for every factory method. (Java 8 method references
basically just do that behind the scenes instead.) This saved many, many lines of code, so try to use the same pattern for
any randomly created objects.

**Performer:** Anything that can be seen or heard inherits from the Performer class. Reading image or sound files can be
easily accomplished using the Theme class. New themes and enemy textures can be added to the game without adding any
code, to see how look at the instances of Theme in Menu, and classes that inherit Enemy. Enemy textures just need to
be named after the class that displays them, for an example of how more textures can be used, see the RocketBoss class.
The Theme class will inherit assets from the superclasses of a Performer if one is mising.

**Copy Constructors:** There are no static variables in the project other than devMode. Single instances of the state
stack, player and entity lists, Random instance, and any other shared references are copied through the
GameLevel(GameLevel) constructor. This is so classes can change their behaviour without breaking other classes, and
so the rule of not using any static variables can continue being followed.

There were several enums and many public static variables when we got the project. All of them have been removed and replaced,
so try not to add any more. We considered both of these antipatterns as they increase the amount of effort it takes to extend
the game, rather than decrease. The better solutions are less obvious and rely somewhat on Java 8, but plenty of good examples can be found in the code.

## Bugs

Known Bugs as of 12/9/19:
- Sometimes in multiplayer levels begin ending very quickly, and player deaths start acting unpredictably.
- High Scores might not be saving and loading consistently.
