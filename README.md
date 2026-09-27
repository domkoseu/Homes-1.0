
<img width="726" height="332" alt="Snímek obrazovky 2026-09-27 020755" src="https://github.com/user-attachments/assets/12a26560-d821-4fc3-b8ec-5bb32877b0e5" />
<img width="579" height="250" alt="Snímek obrazovky 2026-09-27 104448" src="https://github.com/user-attachments/assets/395d6e86-ce68-4343-877f-f3ea7c1d7bf9" />

# Homes-1.0
Java (Paper) port of your Skript homes system (/homes, /sethome, /delhome,
/adminhomes) - the GUI is built on Paper's Dialog API instead of the
inventory-based GUI from the original Skript.

## Version / compatibility

Works on **Paper 1.21.7 through 26.2** (and forks like Purpur/Folia). The
Dialog API (`io.papermc.paper.dialog`, `io.papermc.paper.registry.data.dialog.*`)
that the GUI uses was added in Paper 1.21.7 and is still part of Paper on the
26.x line - `pom.xml` builds against `1.21.11-R0.1-SNAPSHOT`, but the
resulting JAR (`Homes-1.0.jar`) runs unchanged on any server in that range,
since `plugin.yml` declares `api-version: '1.21'` and none of the classes
used have moved or been removed in between. If a future Paper release ever
changes this part of the API, just bump `paper.version` in `pom.xml` and
recompile - the code itself shouldn't need to change.

Not compatible with 1.21.6 and older (Dialog API doesn't exist there yet).

## Build

(If you're running this from IntelliJ's Maven "Command Line" field, just type
`clean package` - IntelliJ already prepends `mvn` itself.)

Output JAR: `target/Homes-1.0.jar` - drop it into `plugins/`.

## Commands

- `/homes` - opens a GUI with all your homes (click an empty slot to set it
  right there, click an existing one to Teleport / Delete / Close)
- `/homes <1-10>` - teleports directly (5s countdown, cancels if you move)
- `/sethome <1-10>` - sets a home at your current position
- `/delhome [1-10]` - deletes a home (no number shows how many you have set)
- `/adminhomes <player>` - (permission `homes.admin`) view and teleport to
  another player's homes

Homes 6-10 require the permission `homes.homes.<number>` (default: op only,
same as the original script).

## Porting notes

- The GUI from the original "multi action dialog" blocks is a 1:1 port to
  `DialogType.multiAction(...)` with `ActionButton` + `DialogAction.customClick`
  callbacks - no global dialog registration needed.
- The countdown teleport (`TeleportTask`) mirrors the original
  "loop 5 times -> wait 1s -> check moved" logic exactly.
- Locations are stored in `plugins/Homes/homes.yml` (instead of the Skript
  variables `{home<index>::<uuid>}`).
