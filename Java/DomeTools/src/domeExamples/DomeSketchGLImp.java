/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import codeanticode.glgraphics.GLGraphicsOffScreen;
import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
abstract public class DomeSketchGLImp implements DomeSketchGL {

	PGraphics p5;

	public DomeSketchGLImp(GLGraphicsOffScreen g) {
		this.init(g);
	}

	public void init(GLGraphicsOffScreen g) {
		p5 = g;
	}
}
