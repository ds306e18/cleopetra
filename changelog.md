# Changelog

#### Version 1.7.5 - January 2021
- Updated expected bot pack path and only load bot pack on request from user. - NicEastvillage
- Bot collection can contain bots with identical names. - NicEastvillage

#### Version 1.7.4 - January 2021
- New installer that no longer requires users to have Java,
and creates file associations so users can open rlts files directly. - tarehart

#### Version 1.7.3 - August 2020
- Now supports RLBotGUIX python installations. - NicEastvillage
- Example overlay supports best-of-X series. - NicEastvillage

#### Version 1.7.2
- Added support three new match options. - NicEastvillage
    - Enable rendering
    - Enable state setting
    - Auto save replays
- Fixed a bug where round robin stats were not updated correctly due to deserialization error. - NicEastvillage
- CleoPetra is now way smarter about where file choosers starts. - NicEastvillage

#### Version 1.7.1
- Fixed bug that prevented overlay to be written to current_match.json. - NicEastvillage
- Fixed bug where overlay path text field was not updated when loading a saved tournament. #103 - NicEastvillage

#### Version 1.7 - 26. April 2020
- Added option to use RLBotPack Python installation if available (default: true). - NicEastvillage
- Added option for using random standard map. - Darxeal
- Added series (best of X) functionality. #56 - NicEastvillage
    - A default series length can be set for each stage, but length of individual series can also be changed once the bracket is generated.
- Updated the data exposed to overlays (or other programs) in the `current_match.json`. - NicEastvillage
    - Exposed data includes team names, all bot details, and scores of current series
    - Added functionality to choose where the `current_match.json` is located in the RLBotSettingsTab
    - Clicking "modify config" will now also update the `current_match.json`

#### Version 1.6 - 17. October 2019
- Now writes overlay data to a json next to the cleopetra.jar. This can be disabled in RLBotSettings tab. - NicEastvillage
- Improved the RLBot runner process. - NicEastvillage
    - The RLBot console window (RLBot runner) now does not need to be shut down after each match. #88
    - CleoPetra issues the RLBot runner to start and stop new matches through socket communication.
    - RLBot.exe is not shut down between each match, which means:
        - Skyborg's overlay will work properly.
        - Rendering and bot percentages does not have to be toggled each match.
- Improved how round robin decides the top teams of the stage. #92 - NicEastvillage
- Added button for auto-naming teams based on its bots. #79 - NicEastvillage

#### Version 1.5.1 - 3. October 2019
- Added seeding for first round of Swiss. - NicEastvillage
- Bodged the Swiss round generation algorithm to never make rounds with missing matches. Instead
  it will accept a few rematches and warn the user. "Fixes" #5. - NicEastvillage

#### Version 1.5 - 1. October 2019
- Clean up of unit tests. - NicEastvillage
- Team stats such as wins, loses, goals scored, and goals conceded are now tracked per stage. #8 - NicEastvillage
- Added scoreboards to each group in round robin and updated look of all scoreboards. #1 #12 - NicEastvillage
- Removed Tiebreaker from UI because people should and do use goal-diff every time. - NicEastvillage

#### Version 1.4 - 21. August 2019
- Implemented custom config file reader/writer. - NicEastvillage
- With this new technology comes:
    - Support for up to 32 bots per team (previously dependant on the selected 'rlbot.cfg'). #78
    - The user no longer has to select a 'rlbot.cfg'
    - Ability to set game mode, map and mutators under the RLBot Settings tab

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
