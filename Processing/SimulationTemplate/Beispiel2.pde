public class Beispiel2 extends DomeSketch {
  
  int number_of_rects = 12;
  float runner;

  public void setup() {
  }

  public void draw() {
    /* Use p5. prefix for everything that you would normally do inside processing */    
    runner += 0.01f; // Rotation speed
    p5.beginDraw();  // This has to be used FIRST!
    p5.rectMode(CENTER);    
    p5.background(0);
    p5.fill(100);
    p5.stroke(255);
    p5.translate(p5.width/2, p5.height/2);
    for(int i = 0; i < number_of_rects; i++) {
      p5.pushMatrix();
      p5.rotate(sin(runner) + i * TWO_PI/number_of_rects);
      p5.translate(150,0);      
      p5.rect(0,0, 50, 20);
      p5.popMatrix();
    }
    p5.endDraw();  // This has to be used LAST!
  }

  /* Only change the name of the class to your class name here! */
  public Beispiel2(PGraphics g) {
    super(g);
  }
}

