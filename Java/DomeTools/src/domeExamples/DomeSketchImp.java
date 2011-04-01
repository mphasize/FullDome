/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import processing.core.PGraphics;

/**
 *
 * @author mphasize
 */
abstract public class DomeSketchImp implements DomeSketch {

	PGraphics p5;

	public DomeSketchImp(PGraphics g) {
		this.init(g);
	}

	public void init(PGraphics g) {
		p5 = g;
	}
}
