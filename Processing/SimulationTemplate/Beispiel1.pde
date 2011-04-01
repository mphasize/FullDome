public class Beispiel1 extends DomeSketch {

  private float runner = 0;
  private float x = 0;
  private float size = 20;

  public void setup() {
  }

  public void draw() {
    /* Use p5. prefix for everything that you would normally do inside processing */
    p5.beginDraw(); // This has to be used FIRST!
    p5.background(0);
    runner += 0.1f; // Speed
    x = x > p5.width ? -size : x + 1;
    p5.noStroke();
    p5.fill(255);
    p5.rect(x, p5.height - size * 2, size, size);
    p5.noFill();
    p5.stroke(255);
    p5.ellipse(p5.width / 2, p5.height / 2, x, x);
    p5.endDraw(); // This has to be used LAST!
  }

  /* Only change the name of the class to your class name here! */
  public Beispiel1(PGraphics g) {
    super(g);
  }
}

