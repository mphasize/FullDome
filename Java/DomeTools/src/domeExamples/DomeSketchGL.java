/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package domeExamples;

import codeanticode.glgraphics.GLGraphicsOffScreen;
import processing.core.PConstants;

/**
 *
 * @author mphasize
 */
public interface DomeSketchGL extends PConstants {

	public void init(GLGraphicsOffScreen g);

	public void setup();

	public void draw();
}
