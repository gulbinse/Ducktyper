Grobe Lösungsansätze für GUI aus JavaFX-Komponenten

**1. Aufbau der GUI aus JavaFX-Komponenten**

<table><tr><th><b>GUI</b> <b>Aufbau</b></th><th valign="top"><b>Eingabefeld</b></th><th><b>Buttons</b></th><th valign="top"><b>Anmerkung</b></th><th><b>Layout</b></th></tr>
<tr><td><i>Initial Prompt</i></td><td>ja</td><td>Submit</td><td>- Farbe der Buttons richtig auswählen</td><td rowspan="6">“VBox” oder “GridPane”</td></tr>
<tr><td><i>Main Menu</i></td><td>nein</td><td><p>start game</p><p>profile setting</p><p>stats</p><p>exit</p></td><td>- Farbe der Buttons richtig auswählen</td></tr>
<tr><td><i>Game Window</i></td><td>ja</td><td><p>Profile Setting</p><p>Stats</p><p>exit</p></td><td><p>- Braucht Textanzeige</p><p>- zeigt #WPM, #Errors, und Top players</p></td></tr>
<tr><td><i>Result Window</i></td><td>nein</td><td><p>play again</p><p>exit</p></td><td>- “TableView” für die Anzeige der Top-Spieler, #WPM, und #Errors</td></tr>
<tr><td><i>Player Stats Window</i></td><td>nein</td><td><p>exit</p><p>reset stats</p></td><td>- “TableView” für die Anzeige der Spielrunde, durchnitliche WPM, #Errors, beste WPM, und durchnitliche Genauigkeit (Prozent)</td></tr>
<tr><td><i>Profile Setting Window</i> </td><td>ja (3)</td><td><p>save</p><p>cancel</p></td><td>- Farbe der Buttons richtig auswählen</td></tr>
</table>


**2. Übertragung des Spielzustands**

![A diagram of a game client

Description automatically generated](Aspose.Words.6ad002b6-f4f4-4091-a713-988e35d4d1f9.001.jpeg)


\- Wie wird der Spielzustand übertragen?

`  `- Netzwerkprotokoll: Verwenden von JSON über HTTP oder WebSocket für die Kommunikation zwischen Client und Server.

`  `- Serialisierung: Spielzustandsdaten werden in JSON-Format serialisiert und über das Netzwerk gesendet.

`  `- Deserialisierung: Empfangene JSON-Daten werden deserialisiert und in Java-Objekte umgewandelt.

**3. Implementierung der GUI Mockups**

***- Schritt 1: Strukturplanung***

`  `- Layout-Auswahl: Bestimmen, welche Layouts (z.B. “VBox”, “HBox”, “GridPane”) für welche GUI-Komponenten am besten geeignet sind.

`  `- Komponentenauswahl: Auswählen der benötigten JavaFX-Komponenten (z.B. “TextField”, “Button”, “Label”).

***- Schritt 2: Komponentenplatzierung***

`  `- Mockup-Umsetzung: Platzieren der Komponenten gemäß den Mockups in den gewählten Layouts.

`  `- Event-Handler: Implementieren von Event-Handlern für Benutzeraktionen (z.B. Knopf-Klicks).

***- Schritt 3: Datenbindung und Validierung***

`  `- Datenbindung: Verwenden von JavaFX-Properties und Bindings für die Echtzeit-Aktualisierung der GUI basierend auf dem Spielzustand.

`  `- Validierung: Implementieren von Eingabevalidierungen für die Benutzereingabe (z.B. Benutzernamen-Validierung).

\- ***Schritt 4: Integration und Testen***

`  `- Integration: Verbinden der GUI-Komponenten mit den Backend-Services für die Spielzustandsübertragung.

`  `- Testen: Durchführen von Funktionstests und Benutzerakzeptanztests, um sicherzustellen, dass die GUI wie erwartet funktioniert.


