public class InteractiveDome extends Dome {
  /* Manipulation constants */
  static final int ROTATION = 0;
  static final int PROJECTION = 1;
  PVector position;
  float radius = 400f;
  Integrator phixi, phiyi, tr1i, tr2i;
  float phix = 0, phiy = 0, phiz = 0, lastphix, lastphiy;
  float lasttr1, lasttr2;

  public InteractiveDome() {
    position = new PVector(width / 2, height / 2, -radius * 1.5f);
    phixi = new Integrator(0f, .5f, .1f);
    phiyi = new Integrator(0f, .5f, .1f);
    tr1i = new Integrator(0f, .5f, .1f);
    tr2i = new Integrator(0f, .5f, .1f);
  }


  public void draw() {
    pushMatrix();
    translate(position.x, position.y, position.z);
    rotateX(phix);
    rotateY(phiy);
    rotateZ(phiz);
    translate(0, 0, -radius);
    super.draw();
    popMatrix();
  }

  public void resetPosition() {
    position.set(width / 2, height / 2, -radius * 1.5f);
    phiz = 0f;
    this.setDistance(520f);
  }

  public void handleMouseRotation(float mouseX, float mouseY) {
    lastphix = phix;
    lastphiy = phiy;
    phix = map(constrain(mouseY, 0, height), 0, height, TWO_PI, 0);
    phiy = map(constrain(mouseX, 0, width), 0, width, 0, TWO_PI);
    phixi.set(lastphix);
    phiyi.set(lastphiy);
    phixi.target(phix);
    phiyi.target(phiy);
    phixi.update();
    phix = phixi.getValue();
    phiyi.update();
    phiy = phiyi.getValue();
    tr1i.update();
    tr2i.update();
    tr1 = tr1i.getValue();
    tr2 = tr2i.getValue();
  }

  public void handleMouseProjection(float mouseX, float mouseY) {
    lasttr1 = tr1;
    lasttr2 = tr2;
    tr1 = map(constrain(mouseY, 0, height), 0, height, 0, HALF_PI);
    tr2 = map(constrain(mouseX, 0, width), 0, width, 0, HALF_PI);
    tr1i.set(lasttr1);
    tr2i.set(lasttr2);
    tr1i.target(tr1);
    tr2i.target(tr2);
    tr1i.update();
    tr1 = tr1i.getValue();
    tr2i.update();
    tr2 = tr2i.getValue();
    phixi.update();
    phix = phixi.getValue();
    phiyi.update();
    phiy = phiyi.getValue();
  }
}

