/*
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
package domeapp;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import visuals.DomeMesh;
import visuals.Pyramid;
import visuals.RingMesh;

/**
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
public class MeshTest extends PApplet implements PConstants {

	private RingMesh mesh1;
	private RingMesh mesh2;
	private RingMesh mesh3;
	private float runner, runner2;
	private DomeMesh dome;
	private Pyramid pyramid;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PApplet.main(new String[]{"domeapp.MeshTest"});
	}

	@Override
	public void setup() {
		size(1024, 768, OPENGL);
		//hint(PApplet.ENABLE_OPENGL_4X_SMOOTH);

		mesh1 = new RingMesh(200, 100, 50);
		mesh1.setPosition(new PVector(width / 2, height / 2, 100));
		mesh2 = new RingMesh(100, 50, 20);
		mesh2.setPosition(new PVector(width / 2, height / 2, 150));
		mesh3 = new RingMesh(50, 0, 10);
		mesh3.setPosition(new PVector(width / 2, height / 2, 300));

		dome = new DomeMesh(300, 150);
		pyramid = new Pyramid();

	}

	@Override
	public void draw() {
		background(50);
		runner += 0.01f;
		runner2 += 0.01f;
		translate(width / 2, height / 2, 0);


		rotateY(runner2);
		rotateZ(runner);

		dome.draw(this.g);

		pyramid.draw(g);


		//mesh1.draw(this);
		translate(0, 0, 50);
		//mesh2.draw(this);
		translate(0, 0, 20);
		//mesh3.draw(this);
	}

	@Override
	public void keyPressed() {
		/* reset parameters */
		if (key == 'r') {
		}
	}
}
