/*
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
package domeapp;

import dome.Dome;
import dome.InteractiveDome;
import domeExamples.DomeSketch;
import domeExamples.Example1;
import domeExamples.Example2;
import domeExamples.Example3;
import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PFont;
import processing.core.PGraphics;

/**
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
public class DomeTester extends PApplet implements PConstants {

	private InteractiveDome dome;
	private PGraphics canvas;
	private boolean previewMode = false;
	private ArrayList<DomeSketch> examples = new ArrayList<DomeSketch>();
	private DomeSketch example = null;
	private int exampleIndex = 0;
	private int[] projections = {Dome.DOMEMASTER, Dome.PANORAMA_180, Dome.PANORAMA_360};
	private int projection = 0;
	private PFont font;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PApplet.main(new String[]{"domeapp.DomeTester"});
	}

	@Override
	public void setup() {
		P5.call = this;
		size(1024, 768, OPENGL);
		hint(PApplet.ENABLE_OPENGL_4X_SMOOTH);

		font = this.createFont("Verdana", 12, true);
		this.textFont(font, 12);

		/* Init sketching surface */
		canvas = createGraphics(800, 300, JAVA2D);
		canvas.beginDraw();
		canvas.endDraw();

		/* Load Examples */
		examples.add(new Example3(canvas));
		examples.add(new Example2(canvas));
		examples.add(new Example1(canvas));
		/* Prepare first example */
		example = examples.get(exampleIndex);
		example.setup();

		/* Prepare Dome projection */
		dome = new InteractiveDome();
		dome.setRadius(400);
		dome.setTexture(canvas, projections[projection]);
	}

	@Override
	public void draw() {
		background(50);
		example.draw();

		if (previewMode) {
			dome.handleMouseRotation(mouseX, mouseY);
			dome.draw();
		} else {
			imageMode(CENTER);
			image(canvas, width / 2, height / 2);
		}
		drawInfos();
	}

	public void drawInfos() {
		fill(255);
		String project = "";
		switch(projection) {
			case 0: project = "DomeMaster"; break;
			case 1: project = "Panorama 180°"; break;
			case 2: project = "Panorama 360°"; break;
		}
		String display = previewMode ? "Dome Preview" : "Sketch Preview";

		text("Example #" + exampleIndex, 20, 32);
		text("Display: " + display, 20, height - 36);
		if(previewMode) text("Projection: " + project, 20, height-20);

	}

	@Override
	public void keyPressed() {
		/* reset parameters */
		if (key == 'r') {
			dome.resetPosition();
			example.setup();
		}
		if (key == ' ') {
			previewMode = !previewMode;
		}
		if (key == 'g') {
			dome.toggleGrid();
		}
		if (key == 'p') {
			projection = projection < projections.length - 1 ? projection + 1 : 0;
			dome.setTexture(canvas, projections[projection]);
		}
		if (key == 'e') {
			exampleIndex = exampleIndex < examples.size() - 1 ? exampleIndex + 1 : 0;
			example = examples.get(exampleIndex);
			System.out.println("Switched to example " + exampleIndex);
			example.setup();
		}
	}
}
