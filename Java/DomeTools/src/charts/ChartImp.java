/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

import java.util.ArrayList;

/**
 *
 * @author mphasize
 */
public abstract class ChartImp implements Chart {

	ArrayList<Node> nodes = new ArrayList<Node>();
	float[] percentages;
	float max = 0;
	float total = 0;
	boolean dirty = true;
	int color = 0;
	float scaleV = 100;
	float scaleH = 100;
	float posX = 100;
	float posY = 100;

	public void addNode(Node node) {
		nodes.add(node);
		if (node.getNumber() > max) {
			max = node.getNumber();
		}
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
		this.dirty = true;
	}

	public void setScale(float scale) {
		this.scaleH = scale;
		this.scaleV = scale;
	}

	public void setScale(float scaleV, float scaleH) {
		this.scaleV = scaleV;
		this.scaleH = scaleH;
	}

	protected void calculatePercentages() {
		percentages = new float[nodes.size()];
		total = 0;
		for (Node n : nodes) {
			total += n.getNumber();
		}
		for (int i = 0; i < nodes.size(); i++) {
			percentages[i] = nodes.get(i).getNumber() / total;
			System.out.println(percentages[i]);
		}
		dirty = false;
	}

	public int getColorHue() {
		return color;
	}

	public void setColorHue(int color) {
		this.color = color;
	}

	public void setPosition(float x, float y) {
		this.posX = x;
		this.posY = y;
	}

	public float[] getPosition() {
		float[] ret = new float[2];
		ret[0] = posX;
		ret[1] = posY;
		return ret;
	}
}
