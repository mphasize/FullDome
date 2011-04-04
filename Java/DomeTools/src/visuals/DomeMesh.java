/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visuals;

import processing.core.PApplet;

/**
 *
 * @author mphasize
 */
public class DomeMesh {

	int segments = 5;
	RingMesh[] rings = new RingMesh[segments];

	public DomeMesh(float radius, float height) {
		for (int i = 0; i < segments; i++) {
			rings[i] = new RingMesh((radius/segments) * i,(radius/segments) * (i+1) , height / segments);
		}
	}

	public void draw(PApplet p5) {
		p5.pushMatrix();
		for(int i = 0; i < rings.length; i++) {
			rings[i].draw(p5);
			p5.translate(0, 0, rings[i].getHeight());
		}
		p5.popMatrix();
	}
}
