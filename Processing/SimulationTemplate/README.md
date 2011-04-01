SimulationTemplate
==================

Dieses Template erzeugt eine frei bewegliche Simulation der Kuppel, 
die mit euren Processing-Inhalten bespielt werden kann.

HINWEIS: Vorneweg... man kann leider (noch?) nicht einfach ein bestehendes
Processing-Skript laden, sondern muss bestehende Sketches anpassen oder 
einfach neue Beispiele schreiben. Wie das geht, weiter unten...


Anleitung:
----------

Beim Öffnen zeigt das App zunächst einen Bildschirm und das flache 
Rendering aus dem ersten Beispiel.

Mit Hilfe von *SPACE* könnt ihr zwischen dem flachen Rendering und
der Kuppelsimulation umschalten.

Weitere Optionen:  
*e* schaltet zwischen den (aktiven) Examples durch...  
*g* schaltet das Grid (in der Kuppelsimulation) ein oder aus  
*p* ändert die Art und Weise wie das Beispiel in die Kuppel projeziert wird  


Neues Beispiel erstellen:
---------

Am besten einfach ein neues Tab erstellen und den Inhalt von Beispiel1 oder Beispiel2
kopieren und anpassen.

	public class BeispielX extends DomeSketch {

	  // Hier Variablen deklarieren
	  boolean MeinSketh = true
	
	  // Setup-Methode funktioniert wie in Processing
	  public void setup() {
	  }
	
	  // Draw-Methode
	  public void draw() {
	    /* Für alle Processing-Funktionen und Eigenschaften unbedingt p5. voran stellen! */
	    p5.beginDraw(); // Dieser Aufruf muss als erstes kommen!
	    p5.background(0);
	    runner += 0.1f; // Speed
	    x = x > p5.width ? -size : x + 1;
	    p5.noStroke();
	    p5.fill(255);
	    p5.rect(x, p5.height - size * 2, size, size);
	    p5.noFill();
	    p5.stroke(255);
	    p5.ellipse(p5.width / 2, p5.height / 2, x, x);
	    p5.endDraw(); // Dieser Aufruf als letztes!
	  }
	
	  /* Hier unbedingt den Klassennamen ändern! */
	  public BeispielX (PGraphics g) {
	    super(g);
	  }
	}
	
Zur Theorie: Innerhalb der Simulation wird ein PGraphics-Objekt erzeugt, 
welches ihr als Leinwand bespielt (mit p5.funktion()). Diese Leinwand (canvas)
wird dann später auf den Dome projeziert.


