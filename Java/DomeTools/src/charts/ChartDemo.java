/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

import java.util.ArrayList;
import processing.core.PApplet;
import processing.core.PConstants;

/**
 *
 * @author mphasize
 */
public class ChartDemo extends PApplet implements PConstants {

	private ArrayList<Node> nodes = new ArrayList<Node>();
	private PieChart pie;
	private BarChart bars;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PApplet.main(new String[]{"charts.ChartDemo"});
	}

	@Override
	public void setup() {
		size(800, 600, JAVA2D);
		smooth();

		nodes.add(new Node(13, "Hunde"));
		nodes.add(new Node(24, "Katzen"));
		nodes.add(new Node(37, "Hühner"));
		nodes.add(new Node(51, "Schweine"));
		nodes.add(new Node(7, "Rinder"));

		pie = new PieChart(this.g);
		pie.setNodes(nodes);
		pie.setPosition(width/2, height/2);
		pie.setScale(200);
		pie.setColorHue(125);

		bars = new BarChart(this.g);
		bars.setNodes(nodes);
		bars.setPosition(width/2, height/2);
		bars.setScale(400, 200);
		bars.setColorHue(125);
	}

	@Override
	public void draw() {
		background(0);
		pie.draw();
		this.noLoop();
	}
}
