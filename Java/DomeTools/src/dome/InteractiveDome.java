/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dome;

import domeapp.P5;
import processing.core.PVector;

/**
 *
 * @author mphasize
 */
public class InteractiveDome extends Dome {
	/* Manipulation constants */

	public static final int ROTATION = 0;
	public static final int PROJECTION = 1;
	private PVector position;
	private float radius = 400f;
	private Integrator phixi, phiyi, tr1i, tr2i;
	private float phix = 0, phiy = 0, phiz = 0, lastphix, lastphiy;
	private float lasttr1, lasttr2;

	public InteractiveDome() {
		position = new PVector(P5.call.width / 2, P5.call.height / 2, -radius * 1.5f);
		phixi = new Integrator(0f, .5f, .1f);
		phiyi = new Integrator(0f, .5f, .1f);
		tr1i = new Integrator(0f, .5f, .1f);
		tr2i = new Integrator(0f, .5f, .1f);
	}

	@Override
	public void draw() {
		P5.call.pushMatrix();
		P5.call.translate(position.x, position.y, position.z);
		P5.call.rotateX(phix);
		P5.call.rotateY(phiy);
		P5.call.rotateZ(phiz);
		P5.call.translate(0, 0, -radius);
		super.draw();
		P5.call.popMatrix();
	}

	public void resetPosition() {
		position.set(P5.call.width / 2, P5.call.height / 2, -radius * 1.5f);
		phiz = 0f;
		this.setDistance(520f);
	}

	public void handleMouseRotation(float mouseX, float mouseY) {
		lastphix = phix;
		lastphiy = phiy;
		phix = P5.map(P5.constrain(mouseY, 0, P5.call.height), 0, P5.call.height, TWO_PI, 0);
		phiy = P5.map(P5.constrain(mouseX, 0, P5.call.width), 0, P5.call.width, 0, TWO_PI);
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
		tr1 = P5.map(P5.constrain(mouseY, 0, P5.call.height), 0, P5.call.height, 0, HALF_PI);
		tr2 = P5.map(P5.constrain(mouseX, 0, P5.call.width), 0, P5.call.width, 0, HALF_PI);
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
