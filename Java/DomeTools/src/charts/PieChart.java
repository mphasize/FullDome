/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
public class PieChart extends ChartImp implements PConstants {

	PGraphics p5;

	public PieChart(PGraphics g) {
		p5 = g;
	}

	public void draw() {
		p5.pushMatrix();
		p5.colorMode(HSB);
		p5.stroke(100, 0, 100);
		if (dirty) {
			this.calculatePercentages();
		}
		float r = 0;
		p5.translate(posX, posY);
		p5.rotate(-HALF_PI);
		for (float p : percentages) {
			float nodearc = p * TWO_PI;
			p5.fill(this.color, 255, PApplet.map(p, 0, 1, 50, 255));
			p5.arc(0, 0, scaleV, scaleH, r, r + nodearc);
			r += nodearc;
		}
		p5.colorMode(RGB);
		p5.popMatrix();
	}
}
