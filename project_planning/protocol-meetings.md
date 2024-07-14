# Protocol Meeting Documentation
Here are the following documentations of our meetings.
Each protocol contains the time, participants and contents of the meeting.
The protocols are written in german and all related to the project "Typeracer Game".

## 1-Session
Datum: 12.06.2024
Uhrzeit: 20:15 Uhr – 21:15 Uhr
Protokollführer: Alina
Teilnehmer: Adrian, Ali, Alina, Eric, Tobi

Thema 1: Ergebnisse der einzelnen Teilnehmer (siehe Notizen -> Discord -> #notizen)
Ergebnisse: - Projektstruktur Vorstellung
            - GUI Mockup und UML Diagramm vorgestellt
            - Featurevorschläge
            - Reviewvorschlag
            - Spiellogikvorstellung
            - Genauere Vorstellung bzgl. Client - Server Struktur

Thema 2: Weiterführende Besprechung
Diskussion: - Schnittschnellenbesprechung
            - Deadlinebesprechung
            - Featurebesprechung
Ergebnis:   - Schnittschnelle wird noch aufs UML Diagramm geupdated und nochmals im nächsten Meeting genauer besprochen
            - Deadline : Am besten bis zur Zwischenpräsentation die Basis-Spiellogik
            - Features : (siehe Notizen -> Discord -> #notizen)

Thema 3: Aufgabenverteilung
Aufgaben + Zuständiger: - Server (Adrian)
                        - GUI (Ali)
                        - Client (Alina)
                        - Spiellogik (Eric)
                        - Unterstützung Spiellogik + Unittests & Reviews (Tobi)
                        - Kommunikationsprotokoll (Adrian + Alina)

## Session-2
Datum: 27.06.2024
Uhrzeit: 14:15 Uhr – 16:15 Uhr
Protokollführer: Alina
Teilnehmer: Adrian, Ali, Alina, Eric, Tobi, Lucas

Thema 1: Updates der einzelnen Teilnehmer
Ergebnisse: - Lobby Feature hinzugefügt, Server Tests, Player ID (von Adrian)
            - GUI Startscreen, Spielscreen mit den jeweiligen Buttons (von Ali)
            - Duck-Design Vorschlag (von Tobi)

TOP 2 Weiterführende Besprechun
Diskussion: - ViewController
            - Feature Vorschlag : Character per minute
            - JoinLobbyRequest Message
            - ID + username lokal speichern
            - Weitere Screens für die GUI (Lobby Screen)

TOP 3	Was noch gemacht werden muss
Aufgaben: - ViewController (Methoden um die GUI und den Client zu verbinden)
          - Alles mergen

## Session-3
Datum: 02.07.2024
Uhrzeit: 19:00 - 20:00 Uhr
Protokollführer: Adrian
Teilnehmer: Adrian, Eric, Tobi

Diskussion: Schnittstelle zwischen Server und Game
Ergebnis: Fertige Einzelteile mergen, neuen Branch erstellen und dort Server und Game miteinander verknüpfen
Aufgaben: Unter gegenseitiger Absprache die Logiken zusammenführen

## Session-4
Datum: 03.07.2024
Uhrzeit: 19:00 - 20:00 Uhr
Protokollführer: Alina
Teilnehmer: Ali, Alina

Diskussion: Schnittstelle zwischen Client und GUI
Ergebnis: Bereits fast fertiger ViewController durch Ali
Aufgaben: Restlichen Methoden implementieren

## Session-5
Datum: 08.07.2024
Uhrzeit: 20:00 - 21:15 Uhr
Protokollführer: Eric
Teilnehmer: Adrian, Alina, Ali, Tobi, Eric

Diskussion: Fertigstellung der Client-/GUI-Seite
Ergebnis: Einigung über Kommunikationswege zwischen Client & GUI
Aufgaben:  Problemstellen gemeinsam in Pair Programming lösen 
           (GUI: Ali & Tobi, Client: Adrian & Alina, Generelles/Sonstiges: Eric)
Anschließende Programmiersessions mit Open End

## Session-6
Datum: 10.07.2024
Uhrzeit: 20:00 - 22:45 Uhr
Protokollführer: Eric
Teilnehmer: Adrian, Alina, Ali, Tobi, Eric

Diskussion: Zusammenführung von ViewController & Client; Probleme beheben und andere Seite verstehen
Ergebnis: Pair Programming Session -> Client kommuniziert meistens halbwegs mit dem Server
Aufgaben: Komplette Funktionalität vervollständigen, "Join Session" Button, Texteingabe richtig hinkriegen

## Session-7
Datum: 10.07.2024
Uhrzeit: 22:45 - 01:00 Uhr
Protokollführer: Eric
Teilnehmer: Alina, Adrian, Eric

Diskussion: Client soll richtig mit Server kommunizieren
Ergebnis: Pair Programming Session -> Race Condition gefunden und kleinere Bugs behoben
Aufgaben: Weiter machen mit restlicher Funktionalität 

## Session-8
Eric — 11.07.2024 14:50
Datum: 11.07.2024
Uhrzeit: 14:00 - 16:00 Uhr
Von der Besprechung:

Wichtig Aufgaben mit Aufgabenverteilung:
- Join Lobby & Create Lobby Button mit Funktion (Ali, Eric)
- ID in Lobby anzeigen mit Funktion (Eric)
- Readybutton mit Funktionalität (Zu beachten: Erst ins Spiel starten wenn alle ready (GameStateNotification mit RUNNING empfangen)) (Ali)
- Races anzeigen; Spieler mit Name, WPM, Accuracy anzeigen (+ Aktualisierung (Bindings/Properties/Listener)) (Adrian, Eric)
- Text anzeigen (Tobi, Eric)
- Einzutippenden Text formatieren (z.B. Falsche Buchstaben unterstrichen) (Alina, Tobi)
- Gamescreen verlassen wenn Spiel vorbei (Eric)
- README erstellen (Ali, Alina)
- Namen im CHANGELOG hinzufügen (Eric)
- Video für Präsentation erstellen (Alina)

Mögliche Umsetzungen falls Zeit zur Verfügung steht:
- Mit Enter-Taste joinen können (Eric)
- Spielerliste in Lobby anzeigen (Adrian)
- Design updaten (Tobi)
- WPM stoppen wenn Spieler fertig
- Leaved Players in GUI richtig anzeigen
- Play-Again-Funktionalität richtig implementieren (Daten & Spielanzeigen zurücksetzen, Funktionalität in Lobby)
- Top-Player-Liste anzeigen
- Wenn Error reinkommt, Alert anzeigen
- Saubere Javadocs
- TODOs entfernen
- Klassen aufräumen
- JAR erstellen
- UML-Diagramme etc. aktualisieren
- Discord Protokolle, UI-Mockups etc. in Git hochladen
- Tobis Hintergrund einbinden
- Min-Height & Min-Width für GUI-Fenster (Tobi)
- Error anzeigen bei nichtexistenter SessionID
- Ready wieder zurücknehmen können
- Beim Eintippfeld immer nur einen Character anzeigen o.ä. 
- Ggf. Statistiken, Einstellungen etc. rausschmeißen

## Session-9
Datum: 12.07.2024
Uhrzeit: 00:00 - 01:30
Protokollführer: Tobi
Teilnehmer: Alina, Tobi

Gemeinsame Arbeit an korrekter Darstellung von Text und farblicher Repräsentation
Ergebnis: Text wird angezeigt aber nicht geupdated
Aufgaben: Das halt anders machen iwie

## Session-10
Datum: 12.07.2024
Uhrzeit: 20:00 - 01:00 Uhr
Protokollführer: Eric
Teilnehmer: Adrian, Eric

Diskussion: Wie Anzeige der Werte & des Fortschritts jedes Spielers umsetzen? Pair Programming Session
Ergebnis: Alle Werte & Racetrack werden richtig angezeigt (Racetrack sogar ganz hübsch), kleinere Bugs behoben
Aufgaben: Rest fertigstellen
