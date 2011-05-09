/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import processing.core.PGraphics;
import visuals.DomeMesh;

/**
 *
 * @author mphasize
 */
public class Example3 extends DomeSketchImp {

	DomeMesh dome;

	public void setup() {
		dome = new DomeMesh(300, 300);
	}

	public void draw() {
		p5.beginDraw();
		//dome.draw(p5);
		p5.background(0);
		p5.endDraw();

	}

	public Example3(PGraphics g) {
		super(g);
	}
}
