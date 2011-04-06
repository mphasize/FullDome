/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import charts.Node;
import charts.PieChart;
import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
public class Example1 extends DomeSketchImp {

	private PieChart pie;

	public Example1(PGraphics g) {
		super(g);
	}

	public void setup() {
		pie = new PieChart(p5);
		pie.addNode(new Node(15, "Sketches"));
		pie.addNode(new Node(77, "Applications"));
		pie.addNode(new Node(25, "Wars"));
		pie.addNode(new Node(16, "Hipps"));

		pie.setPosition(p5.width / 2, p5.height / 2);
		pie.setScale(200);
		pie.setColorHue(125);
	}

	public void draw() {
		p5.beginDraw();
		p5.background(0);
		pie.draw();
		p5.endDraw();
	}
}
