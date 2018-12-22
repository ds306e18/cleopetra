# CleoPetra
CleoPetra aims to create a custom tournament runner for the RLBot-community that help host, manage and run a tournament. The system supports multiple formats such as single elimination, Swiss-system and round robin. It also supports multiple stages and the seeding of participants, allowing for a easy transfer of the best participants within stages.
Apart from these features the system also adds support specifically for the [RLBot-framework](https://github.com/RLBot/RLBot). It can modify the `rlbot.cfg` file based on the bots of a match and can also start a match using the RLBot-framework.

#### Prerequisites
- Java 8 JDK installed.
- Optional: RLBot-framework installed.

#### Quick Start

If you just want to use the tournament runner, do the following:

1. Download or clone the repository
2. Run the `run.bat`. Startup might take awhile the first time. The command prompt will say `75% executing` in the command prompt, just ignore that and keep the command prompt open.

Alternatively you can run `gradle fatJar` in a command prompt. This will build a jar file that can be run anywhere. Find the jar in `build/libs/`.

## Setup IntelliJ project
For using the IDE-environment to compile, debug and build artifacts, the following setup must be carried out. The project is running with Gradle and this will be used to build and run both the tests and the project.

1. Install [IntelliJ](https://www.jetbrains.com/idea/). The community edition is free.
2. Clone repository to your computer.
3. Start IntelliJ, click open and choose the repository folder *tournamentsystem*.
4. When the project has been loaded a pop-up message will appear in the lower right corner og the screen. Click the *Import Gradle Project*.
5. Keep the default settings and press *OK* and wait for the import to finish.
6. The project is now imported and can be run within IntelliJ through Gradle.

#### Run application and tests.
The above setup is required in order to run the application using Gradle. The following functionality is accessed using the Gradle panel found in the right side of IntelliJ.

To **run the application** use the task: ``tournamentsystem -> Tasks -> application -> run``.<br>
To **run the tests** use the task: ``tournamentsystem -> Tasks -> verification -> test``.<br>
To **build an executable jar** use the task: ``tournamentsystem -> Task -> other -> fatJar``. The executable jar will now be located ``\tournamentsystem\build\libs\tournamentsystem.jar``.<br>
