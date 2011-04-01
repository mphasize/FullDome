public class Integrator {

  final float DAMPING = 0.5f;
  final float ATTRACTION = 0.2f;
  float value;
  float vel;
  float accel;
  float force;
  float mass = 1;
  float damping = DAMPING;
  float attraction = ATTRACTION;
  boolean targeting;
  float target;

  public Integrator() {
  }

  public Integrator(float value) {
    this.value = value;
  }

  public Integrator(float value, float damping, float attraction) {
    this.value = value;
    this.damping = damping;
    this.attraction = attraction;
  }

  public void set(float v) {
    value = v;
  }

  public void update() {
    if (targeting) {
      force += attraction * (target - value);
    }

    accel = force / mass;
    vel = (vel + accel) * damping;
    value += vel;

    force = 0;
  }

  public void target(float t) {
    targeting = true;
    target = t;
  }

  public void noTarget() {
    targeting = false;
  }

  public float getValue() {
    return value;
  }
}

