/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kinedome;

import processing.core.PApplet;

/**
 *
 * OSCeleton Example
 */
public class Skeleton {
	int id; //here we store the skeleton's ID as assigned by OpenNI and sent through OSC.
	PApplet p5;
	int scale = 50;

	// We just use this class as a structure to store the joint coordinates sent by OSC.
	// The format is {x, y, z}, where x and y are in the [0.0, 1.0] interval,
	// and z is in the [0.0, 7.0] interval.
	float head[] = new float[3];
	float neck[] = new float[3];
	float rCollar[] = new float[3];
	float rShoulder[] = new float[3];
	float rElbow[] = new float[3];
	float rWrist[] = new float[3];
	float rHand[] = new float[3];
	float rFinger[] = new float[3];
	float lCollar[] = new float[3];
	float lShoulder[] = new float[3];
	float lElbow[] = new float[3];
	float lWrist[] = new float[3];
	float lHand[] = new float[3];
	float lFinger[] = new float[3];
	float torso[] = new float[3];
	float rHip[] = new float[3];
	float rKnee[] = new float[3];
	float rAnkle[] = new float[3];
	float rFoot[] = new float[3];
	float lHip[] = new float[3];
	float lKnee[] = new float[3];
	float lAnkle[] = new float[3];
	float lFoot[] = new float[3];
	float[] allCoords[] = {head, neck, rCollar, rShoulder, rElbow, rWrist,
		rHand, rFinger, lCollar, lShoulder, lElbow, lWrist,
		lHand, lFinger, torso, rHip, lHip};


	Skeleton(int id, PApplet p5) {
		this.id = id;
		this.p5 = p5;
	}

	public void drawStickfigure() {
		p5.stroke(255);
		p5.noFill();
		line(head, neck);
		line(neck, rCollar);
		line(neck, lCollar);
		line(lCollar, lShoulder);
		line(lShoulder, lElbow);
		line(lElbow, lWrist);
		line(lWrist, lHand);
		line(rCollar, rShoulder);
		line(rShoulder, rElbow);
		line(rElbow, rWrist);
		line(rWrist, rHand);
		line(lShoulder, torso);
		line(rShoulder, torso);
	}

	private void line(float[] from, float[] to) {
		p5.line(from[0] * scale, from[1] * scale, -from[2] * scale, to[0] * scale, to[1] * scale, -to[2] * scale);
	}
}
