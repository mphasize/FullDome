/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

import processing.core.PConstants;
import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
public class BarChart extends ChartImp implements PConstants {

	private PGraphics p5;
	private float barWidth;

	public BarChart(PGraphics g) {
		p5 = g;
	}

	public void draw() {
		p5.pushMatrix();
		p5.colorMode(HSB);
		p5.stroke(100, 0, 100);
		if (dirty) {
			calculatePercentages();
		}

		/* @todo Implement drawing code for bar chart */

		p5.colorMode(RGB);
		p5.popMatrix();
	}
}
