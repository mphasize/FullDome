/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import codeanticode.glgraphics.GLGraphicsOffScreen;

/**
 *
 * @author mphasize
 */
public class GLExample1 extends DomeSketchGLImp {

	private float runner = 0;
	private float x = 0;
	private float size = 20;

	public GLExample1(GLGraphicsOffScreen g) {
		super(g);
	}

	public void setup() {
	}

	public void draw() {
		p5.beginDraw();
		p5.background(0);
		runner += 0.1f;
		x = x > p5.width ? -size : x + 1;
		p5.noStroke();
		p5.fill(255);
		p5.rect(x, p5.height - size * 2, size, size);
		p5.noFill();
		p5.stroke(255);
		p5.ellipse(p5.width / 2, p5.height / 2, x, x);
		p5.endDraw();
	}

}
