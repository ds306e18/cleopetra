# Changelog

#### Unreleased changes:
- Implemented custom config file reader/writer. #78 - NicEastvillage
- With this new technology comes: 
    - Support for up to 32 bots per team (previously dependant on the selected 'rlbot.cfg')
    - User no longer has to select a 'rlbot.cfg'


#### Version 1.3 - 9. April 2019
- Support for Psyonix bots. #13 - NicEastvillage
- Completely reworked how bots are added to teams. Bots are now stored in a bot collection, from which you can pick bots to add to a team. - NicEastvillage
- The addition of the bot collection includes the following features:
    - No more copy-pasting then the same bots appear multiple times
    - A single bot config, or a whole folder of bots can loaded and added to the bot collection
    - If the RLBotPack is present, it will be loaded automatically on startup
    - Psyonix bots also come preloaded (they are located at `<user>/.cleopetra/psyonix_bots/`)
    - Info about bots are now displayed in a pop-up and must be loaded from the bot's config
        - a "Show files" button will open a file explorer showing the bot's config file
    - Psyonix bots and RLBot bots have different icons

#### Version 1.2.1 - 4. March 2019
- Added ability to swap team colors in a match. #49 - NicEastvillage
- Fixed a bug where changing the score of a match without affecting the outcome would reset subsequent matches. #53 - NicEastvillage
- Made some settings UI disable when the tournament start as those cannot be changed anymore. #15 - NicEastvillage
- Values are now read from a bot's config file when it is selected in participant settings. #4 - jeroen11dijk
- The Ini4j library is now used for config files. - jeroen11dijk
- Added a new logo and updated title image. - NicEastvillage

#### Version 1.2.0 - 31. Jan 2019
- Double elimination. #9 - NicEastvillage
    - Supports any number of teams and byes
    - Supports bracket reset (an extra grand final match if the lower-bracket winner wins against upper-bracket winner)
- Round robin and swiss now uses tiebreaker to rank all teams before transferring teams to the next stage. - NicEastvillage
- Expanded, separated, and reworked functionality of ConfigFileEditor. #3 - cogitantium
- Matches are now displayed slightly bigger. - NicEastvillage
- Matches now has an identifier and pending matches will display which matches they depend on. E.g. "Winner of X" vs "Loser of Y". - NicEastvillage
- Fixed a bug that occurred when matches with to-be-determined players were double-clicked. #51 - NicEastvillage

#### Version 1.1.2 - 23. Jan 2019
- Hotfix: Fixed seeding being backwards in some seeding options. - NicEastvillage

#### Version 1.1.1 - 23. Jan 2019
- Added a manual seeding option, that allow the user to give multiple teams the same seed. #33 - NicEastvillage
- Tie breakers are now enums, which fixes serialization of tie breakers. #32 - NicEastvillage
- Loading a tournament with played single-elimination matches is now possible, because deseralizing bypasses some behaviour. #35 - NicEastvillage

#### Version 1.1 - 8. Jan 2019
- Teams are now serialized by index, which means teams and bots can be edited after saving and loading a tournament. #2 #25 - NicEastvillage
- The content on settings tabs are now centered and scales less weirdly. #23 - NicEastvillage
- Added seeding options. #10 - NicEastvillage
    - Seed by order in participant list: Normal seeding
    - No seeding: The teams are placed directly into the bracket/groups
    - Random seeding: Shuffles the teams
    - The swiss algorithm is currently unaffected as it doesn't consider seeding at all right now.

#### Version 1.0.1 - 29. Dec 2018
- Added a `run.bat` for quickly running CleoPetra without having to install IntelliJ or Gradle. #19 #21 - NicEastvillage and tarehart
- Running unit tests no longer creates junk in main folder. #17 #22 - cogitantium

#### Version 1.0.0 - 21. Dec 2018
**Features:**
- Tournament Formats:
    - Single elimination
    - Swiss system
    - Round robin
- Multiple stages in one tournament, where the best teams are automatically transferred to the next
- Auto-start a match with the rlbot framework
    - Or if just auto-modify the rlbot.cfg without starting the framework
    - All other rlbot settings (like game mode and mutators) won't be affected
- Ability to change a match results later in the stage, if you entered something wrong
- Can resets subsequent matches, if you change the outcome of a match
- Saving and loading tournaments
- Seeding of participants
- Different tiebreaker methods
- Double click a match to quickly edit scores
- Click a bot in bracket overview to see details about that bot
