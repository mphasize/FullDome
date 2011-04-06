/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package visuals;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PVector;

/**
 *
 * @author mphasize
 */
public class Pyramid {

	PVector[] points = new PVector[5];
	PVector tip = new PVector(.5f, .5f, 1);
	PVector translation = new PVector();

	public Pyramid() {
		points[0] = new PVector(1, 1, 0);
		points[1] = new PVector(1, 0, 0);
		points[2] = new PVector(0, 0, 0);
		points[3] = new PVector(0, 1, 0);
		points[4] = new PVector(1, 1, 0);
	}

	public void draw(PGraphics p5) {
		p5.pushMatrix();
		p5.translate(translation.x, translation.y, translation.z);
		p5.scale(100);
		p5.noStroke();
		p5.fill(0);
		p5.beginShape(PApplet.QUAD);
		for (int i = 0; i < 4; i++) {
			p5.vertex(points[i].x, points[i].y, points[i].z);
		}
		p5.endShape();
		for (int i = 0; i < 4; i++) {
			p5.beginShape();
			p5.fill(0);
			p5.vertex(points[i].x, points[i].y, points[i].z);
			p5.vertex(points[i + 1].x, points[i + 1].y, points[i + 1].z);
			p5.fill(255);
			p5.vertex(tip.x, tip.y, tip.z);
			p5.endShape(p5.CLOSE);
		}

		p5.popMatrix();
	}

	public PVector getTranslation() {
		return translation;
	}

	public void setTranslation(PVector translation) {
		this.translation = translation;
	}

	public void setHeight(float h) {
		tip.z = h;
	}

	
}
