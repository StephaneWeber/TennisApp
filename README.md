# TennisApp
Helping tweak profiles for the Tennis Clash game

IN DEV
*

TODO
* Refresh players list when updating maxLevel restriction
* Take into account players as possible items to upgrade

Resources:
* https://tennis-clash.fandom.com/wiki/Tennis_Clash_Wiki
* https://felix-halim.github.io/tennis-clash/

Configuration
-------------
This app exposes the following configurable properties (set in `application.yml`, environment variables or other Spring Boot property sources):

- application.csv.players (default: `./data/players.csv`)
  - Path to the players CSV file. Can be a classpath resource (prefix with `classpath:`) or an absolute/relative filesystem path.

- application.csv.ownedPlayers (default: `./data/owned_players.csv`)
  - Path to the owned players CSV file. Used to determine owned player levels.

- application.csv.gear (default: `./data/gear.csv`)
  - Path to the gear CSV file containing gear configurations.

- application.csv.ownedGear (default: `./data/owned_gear.csv`)
  - Path to the owned gear CSV file. Used to determine owned gear levels.

- application.defaults.minAgility (default: `90`)
  - Initial minimum agility filter used on the home page.

- application.defaults.minEndurance (default: `50`)
  - Initial minimum endurance filter used on the home page.

- application.defaults.minService (default: `40`)
  - Initial minimum service filter used on the home page.

- application.defaults.minForehand (default: `80`)
  - Initial minimum forehand filter used on the home page.

- application.defaults.minBackhand (default: `80`)
  - Initial minimum backhand filter used on the home page.

- application.defaults.minTotal (default: `350`)
  - Initial minimum total attribute score.

- application.defaults.upgradeAllowed (default: `0`)
  - How many upgrades are allowed by default when generating configurations.

- application.defaults.maxLevel (default: `15`)
  - Maximum player/gear level to use by default when generating configurations.

- application.defaults.pageSize (default: `100`)
  - The page size used for pagination of generated configurations on the home page.

Notes
-----
- Spring Boot relaxed binding means you can also set properties using environment variables or alternative naming (e.g. `APPLICATION_CSV_PLAYERS` or `application.csv.owned-players`).
- For tests, there's a test helper `TestCsvPropertiesFactory` to easily create `CsvProperties` objects pointing to temporary files.
