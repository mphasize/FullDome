public class CursorExample extends DomeSketch {

  Cursor pointer;

  float jX = 0;
  float jY = 0;

  public void setup() {

    pointer = new Cursor(p5);
  }

  public void draw() {
    p5.beginDraw();
    p5.background(0);
    pointer.x(jX);
    pointer.y(jY);
    pointer.draw(p5);
    p5.endDraw();
  }

  public void keyPressed(char key, int keyCode) {
    if(key == CODED) {
      if(keyCode == UP) {
        println("up");
        pointer.up(1f);
      }
      if(keyCode == DOWN) {
        println("down");      
        pointer.down(1f);
      }
      if(keyCode == LEFT) {
        println("left");      
        pointer.left(1f);
      }
      if(keyCode == RIGHT) {
        println("right");
        pointer.right(1f);
      }
    }
  }

  void oscEvent(OscMessage msg) {
    if(msg.checkAddrPattern("/control/joystick") == true) {
      if(msg.checkTypetag("ff")) {
        jY = msg.get(0).floatValue();
        jX = msg.get(1).floatValue();
        println("Joystick: "+ jX +", " + jY);
        return;
      }
    }
  }

  /* Only change the name of the class to your class name here! */
  public CursorExample(PGraphics g) {
    super(g);
  }
}



class Cursor {
  PGraphics p5;
  float distance = 100;
  float angle = 0;
  float angleStep = PI / 180f;
  float distanceStep = angleStep;

  Cursor(PGraphics p5) {
    this.p5 = p5;
    this.distanceStep = p5.height / 180f;
  }

  void draw(PGraphics p5) {
    p5.pushMatrix();

    p5.noStroke();
    p5.fill(255);
    p5.translate(p5.width/2, p5.height/2);
    p5.rotate(angle);
    p5.ellipse(distance, 0, 20, 20);
    p5.noFill();
    p5.stroke(255);
    p5.line(distance, 0, distance+20, 0);

    p5.popMatrix();
  }

  void left(float amount) {
    angle += amount * map(distance, 0, p5.height/2, angleStep * 3, angleStep);
  }

  void right(float amount) {
    angle -= amount * map(distance, 0, p5.height/2, angleStep * 3, angleStep);
  }

  void up(float amount) {
    distance -= amount * distanceStep;
    if(distance < 0) distance = 0;
  }

  void down(float amount) {
    distance += amount * distanceStep;
    if(distance > p5.height/2) {
      distance = p5.height/2;
    }
    println(distanceStep);
  }

  void x(float amount) {
    angle -= amount * angleStep * map(distance, 0, p5.height/2, 3, 1);
  }

  void y(float amount) {
    distance -= amount * distanceStep;
    if(distance < 0) distance = 0;
    if(distance > p5.height/2) {
      distance = p5.height/2;
    }
  }
}

