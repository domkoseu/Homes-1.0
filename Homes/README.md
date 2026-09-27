# Homes-1.0

Java (Paper) port tveho Skript souboru pro system domovu (/homes, /sethome,
/delhome, /adminhomes) - GUI je postavene na Paper Dialog API misto
inventory-GUI z puvodniho Skriptu.

## Verze / kompatibilita

Cileno na **Paper 1.21.11** a **26.2**. Dialog API (`io.papermc.paper.dialog`,
`io.papermc.paper.registry.data.dialog.*`), ktere GUI pouziva, existuje od
Paper 1.21.7 a je porad soucasti Paperu i na verzi 26.x - pom.xml je nastaveny
na `1.21.11-R0.1-SNAPSHOT`, ale vysledny JAR (`Homes-1.0.jar`) bezi na obou
verzich beze zmeny, protoze plugin.yml deklaruje `api-version: '1.21'` a
zadna z pouzitych trid mezi tim nezmizela ani se nepresunula. Pokud by nejaka
budouci Paper verze tuto cast API zmenila, staci zvednout `paper.version` v
pom.xml a znovu zkompilovat - kod samotny by menit nemel.

## Build

```
mvn clean package
```

Vysledny JAR: `target/Homes-1.0.jar` - hod do `plugins/`.

## Prikazy

- `/homes` - otevre GUI se vsemi tvymi domovy (klik na prazdny slot ho rovnou
  nastavi, klik na existujici otevre Teleport/Delete/Close)
- `/homes <1-10>` - primo teleportuje (5s countdown, zrusi se pri pohybu)
- `/sethome <1-10>` - nastavi domov na aktualni pozici
- `/delhome [1-10]` - smaze domov (bez cisla ukaze pocet nastavenych)
- `/adminhomes <hrac>` - (permission `homes.admin`) zobrazi a umozni
  teleportovat se na domovy jineho hrace

Domovy 6-10 vyzaduji permission `homes.homes.<cislo>` (default: jen op,
presne jako v puvodnim skriptu).

## Poznamky k portu

- GUI z puvodnich "multi action dialog" bloku je 1:1 prevedene na
  `DialogType.multiAction(...)` s `ActionButton` + `DialogAction.customClick`
  callbacky - zadna globalni registrace dialogu neni potreba.
- Countdown teleport (`TeleportTask`) presne kopiruje logiku
  "loop 5 times -> wait 1s -> check moved" z puvodniho skriptu.
- Lokace se ukladaji do `plugins/Homes/homes.yml` (misto Skript promennych
  `{home<index>::<uuid>}`).
