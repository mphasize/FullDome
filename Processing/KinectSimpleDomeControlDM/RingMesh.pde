
/**
 *
 * @author mphasize
 */
public class RingMesh {

  int segments = 18;
  float[][] points;
  float radius1;
  float radius2;
  float length;
  PVector position;
  PVector rotation;

  public RingMesh(float r1, float r2, float height) {
    this.position = new PVector();
    this.rotation = new PVector();
    this.radius1 = r1;
    this.radius2 = r2;
    this.length = height;
    points = new float[this.segments * 2][3];
    this.construct();
  }

  public void construct() {
    float r = 0;
    float angle = PApplet.TWO_PI / (segments-1);
    // Bottom ring points
    for (int i = 0; i < points.length; i += 2) {
      points[i] = new float[3];
      points[i][0] = PApplet.sin(r) * this.radius1;
      points[i][1] = PApplet.cos(r) * this.radius1;
      points[i][2] = 0;
      r += angle;
    }
    r = 0;
    // Top ring points
    for (int i = 1; i < points.length; i += 2) {
      points[i] = new float[3];
      points[i][0] = PApplet.sin(r) * this.radius2;
      points[i][1] = PApplet.cos(r) * this.radius2;
      points[i][2] = this.length;
      r += angle;
    }
  }

  public void draw(PApplet p5) {
    p5.beginShape(PApplet.TRIANGLE_STRIP);
    for(int i = 0; i < points.length; i++) {
      p5.vertex(points[i][0], points[i][1], points[i][2]);
    }
    p5.endShape();
    p5.fill(100);
    p5.beginShape(PApplet.QUADS);
    p5.vertex(points[0][0], points[0][1], points[0][2]);
    p5.vertex(points[1][0], points[1][1], points[1][2]);    
    p5.vertex(points[3][0], points[3][1], points[3][2]);    
    p5.vertex(points[2][0], points[2][1], points[2][2]);
    p5.endShape();
    p5.noFill();
  }

  public PVector getPosition() {
    return position;
  }

  public void setPosition(PVector position) {
    this.position = position;
  }

  public PVector getRotation() {
    return rotation;
  }

  public void setRotation(PVector rotation) {
    this.rotation = rotation;
  }

  public float getHeight() {
    return this.length;
  }
}

