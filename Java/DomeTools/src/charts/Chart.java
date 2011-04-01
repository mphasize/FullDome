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
public interface Chart {

	public ArrayList<Node> getNodes();

	public void setNodes(ArrayList<Node> nodes);

	public void draw();

	public void addNode(Node node);

	public void setScale(float scale);

	public void setScale(float scaleV, float scaleH);
}
