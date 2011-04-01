/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package charts;

/**
 *
 * @author mphasize
 */
public class Node {

	float number;
	String title;
	String description;

	public Node(float _number, String _title) {
		this(_number, _title, null);
	}


	public Node(float _number, String _title, String _description) {
		this.number = _number;
		if (_title != null) {
			this.title = _title;
		} else {
			this.title = "";
		}
		if (_description != null) {
			this.description = _description;
		} else {
			this.description = "";
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getNumber() {
		return number;
	}

	public void setNumber(float number) {
		this.number = number;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
