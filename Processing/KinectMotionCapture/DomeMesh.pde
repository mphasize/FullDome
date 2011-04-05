/**
 *
 * @author mphasize
 */
public class DomeMesh {

	int segments = 5;
	RingMesh[] rings = new RingMesh[segments];

	public DomeMesh(float radius, float height) {
		float angle = PApplet.HALF_PI / (float) segments;
		//float h = radius * PApplet.sin(angle);
		for (int i = 0; i < segments; i++) {
			float h = (radius * (PApplet.sin(angle * (i + 1)) - PApplet.sin(angle * i)));
			rings[i] = new RingMesh(radius * PApplet.cos(angle * i), radius * PApplet.cos(angle * (i + 1)), h);
		}
	}

	public void draw(PApplet p5) {
		p5.pushMatrix();
		for (int i = 0; i < rings.length; i++) {
			rings[i].draw(p5);
			p5.translate(0, 0, rings[i].getHeight());
		}
		p5.popMatrix();
	}
}

