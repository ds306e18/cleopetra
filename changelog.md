# Changelog

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
