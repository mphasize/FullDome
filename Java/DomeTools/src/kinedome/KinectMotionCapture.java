/**
 * Based on Osceleton Processing Example...
 *
 * This is the preparation work for mapping user gestures and pointing onto the dome.
 * You should be able to see the skeleton on a ground floor and the dome around it.
 *
 * @author mphasize
 */
package kinedome;

import java.util.Hashtable;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import visuals.DomeMesh;

/**
 * Based on Osceleton Processing Example
 * @author mphasize
 */
public class KinectMotionCapture extends PApplet implements PConstants {

	OscP5 oscP5;
	int ballSize = 10;
	Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();
	DomeMesh dome;
	PVector rArm;
	PVector lArm;
	float domeRotation = 0;
	float domeRotationTarget = 0;
	float beginRotation = 0;
	float beginHandR = 0;
	boolean gestureInProgress = false;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PApplet.main(new String[]{"kinedome.KinectMotionCapture"});
	}

	@Override
	public void setup() {
		size(screen.height * 4 / 3 / 2, screen.height / 2, OPENGL); //Keep 4/3 aspect ratio, since it matches the kinect's.
		oscP5 = new OscP5(this, "127.0.0.1", 7110);
		hint(ENABLE_OPENGL_4X_SMOOTH);
		noStroke();

		dome = new DomeMesh(width * 2, height * 2);
		lArm = new PVector();
		rArm = new PVector();
	}

	@Override
	public void draw() {
		background(0);

		translate(width / 2, height, -600);
		rotateY((mouseX / (float) width) * TWO_PI);
		rotateX(-PI + ((mouseY / (float) height) * TWO_PI));

		domeRotation += (domeRotationTarget - domeRotation) * 0.1f;

		rotateX(HALF_PI);
		pushMatrix();
		rotateZ(domeRotation);
		dome.draw(this.g);
		popMatrix();
		rotateX(-HALF_PI);

		translate(-width / 2, -height, 600);


		float grid = width / 12;
		for (int i = 0; i <= 12; i++) {
			line(i * grid, height, -300, i * grid, height, -900);
			line(0, height, -300 + (i * -50), width, height, -300 + (i * -50));
		}

		noStroke();
		fill(255);
		for (Skeleton s : skels.values()) {
			for (int i = 0; i < s.allCoords.length; i++) {
				float[] j = s.allCoords[i];
				if (i == 6) {
					if (j[1] < s.allCoords[3][1]) {
						float simplePointHeight = s.allCoords[3][1] - j[1];
						dome.setActive((int)(simplePointHeight*10));
						if (gestureInProgress) {
							float delta = beginHandR - j[0];
							domeRotationTarget = beginRotation + map(delta, 0,1, 0, TWO_PI);
						} else {
							beginHandR = j[0];
							beginRotation = domeRotation;
							gestureInProgress = true;
						}
					} else {
						dome.setActive(-1);
					}
				}
				pushMatrix();
				translate(j[0] * width, j[1] * height, -j[2] * 300);
				sphere(2 * ballSize / j[2]);
				popMatrix();
			}
			noFill();
			stroke(255);
			line(s.lFoot[0] * width, s.lFoot[1] * height, s.lFoot[2] * -300, s.lFoot[0] * width, height, s.lFoot[2] * -300);
			PVector handRechts = new PVector(s.rHand[0], s.rHand[1], s.rHand[2]);
			PVector elleRechts = new PVector(s.rElbow[0], s.rElbow[1], s.rElbow[2]);
			PVector richtung = PVector.sub(handRechts, elleRechts);
			pushMatrix();
			translate(elleRechts.x * width, elleRechts.y * height, elleRechts.z * -300);
			line(0, 0, 0, richtung.x * width * 5, richtung.y * height * 5, richtung.z * -300 * 5);
			popMatrix();
		}
	}

	/* incoming osc message are forwarded to the oscEvent method. */
// Here you can easily see the format of the OSC messages sent. For each user, the joints are named with
// the joint named followed by user ID (head0, neck0 .... r_foot0; head1, neck1.....)
	void oscEvent(OscMessage msg) {
		//msg.print();

		if (msg.checkAddrPattern("/joint") && msg.checkTypetag("sifff")) {
			// We have received joint coordinates, let's find out which skeleton/joint and save the values ;)
			Integer id = msg.get(1).intValue();
			Skeleton s = skels.get(id);
			if (s == null) {
				s = new Skeleton(id, this);
				skels.put(id, s);
			}
			if (msg.get(0).stringValue().equals("head")) {
				s.head[0] = msg.get(2).floatValue();
				s.head[1] = msg.get(3).floatValue();
				s.head[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("neck")) {
				s.neck[0] = msg.get(2).floatValue();
				s.neck[1] = msg.get(3).floatValue();
				s.neck[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_collar")) {
				s.rCollar[0] = msg.get(2).floatValue();
				s.rCollar[1] = msg.get(3).floatValue();
				s.rCollar[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_shoulder")) {
				s.rShoulder[0] = msg.get(2).floatValue();
				s.rShoulder[1] = msg.get(3).floatValue();
				s.rShoulder[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_elbow")) {
				s.rElbow[0] = msg.get(2).floatValue();
				s.rElbow[1] = msg.get(3).floatValue();
				s.rElbow[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_wrist")) {
				s.rWrist[0] = msg.get(2).floatValue();
				s.rWrist[1] = msg.get(3).floatValue();
				s.rWrist[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_hand")) {
				s.rHand[0] = msg.get(2).floatValue();
				s.rHand[1] = msg.get(3).floatValue();
				s.rHand[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_finger")) {
				s.rFinger[0] = msg.get(2).floatValue();
				s.rFinger[1] = msg.get(3).floatValue();
				s.rFinger[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_collar")) {
				s.lCollar[0] = msg.get(2).floatValue();
				s.lCollar[1] = msg.get(3).floatValue();
				s.lCollar[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_shoulder")) {
				s.lShoulder[0] = msg.get(2).floatValue();
				s.lShoulder[1] = msg.get(3).floatValue();
				s.lShoulder[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_elbow")) {
				s.lElbow[0] = msg.get(2).floatValue();
				s.lElbow[1] = msg.get(3).floatValue();
				s.lElbow[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_wrist")) {
				s.lWrist[0] = msg.get(2).floatValue();
				s.lWrist[1] = msg.get(3).floatValue();
				s.lWrist[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_hand")) {
				s.lHand[0] = msg.get(2).floatValue();
				s.lHand[1] = msg.get(3).floatValue();
				s.lHand[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_finger")) {
				s.lFinger[0] = msg.get(2).floatValue();
				s.lFinger[1] = msg.get(3).floatValue();
				s.lFinger[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("torso")) {
				s.torso[0] = msg.get(2).floatValue();
				s.torso[1] = msg.get(3).floatValue();
				s.torso[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_hip")) {
				s.rHip[0] = msg.get(2).floatValue();
				s.rHip[1] = msg.get(3).floatValue();
				s.rHip[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_knee")) {
				s.rKnee[0] = msg.get(2).floatValue();
				s.rKnee[1] = msg.get(3).floatValue();
				s.rKnee[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_ankle")) {
				s.rAnkle[0] = msg.get(2).floatValue();
				s.rAnkle[1] = msg.get(3).floatValue();
				s.rAnkle[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("r_foot")) {
				s.rFoot[0] = msg.get(2).floatValue();
				s.rFoot[1] = msg.get(3).floatValue();
				s.rFoot[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_hip")) {
				s.lHip[0] = msg.get(2).floatValue();
				s.lHip[1] = msg.get(3).floatValue();
				s.lHip[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_knee")) {
				s.lKnee[0] = msg.get(2).floatValue();
				s.lKnee[1] = msg.get(3).floatValue();
				s.lKnee[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_ankle")) {
				s.lAnkle[0] = msg.get(2).floatValue();
				s.lAnkle[1] = msg.get(3).floatValue();
				s.lAnkle[2] = msg.get(4).floatValue();
			} else if (msg.get(0).stringValue().equals("l_foot")) {
				s.lFoot[0] = msg.get(2).floatValue();
				s.lFoot[1] = msg.get(3).floatValue();
				s.lFoot[2] = msg.get(4).floatValue();
			}
		} else if (msg.checkAddrPattern("/new_user") && msg.checkTypetag("i")) {
			// A new user is in front of the kinect... Tell him to do the calibration pose!
			println("New user with ID = " + msg.get(0).intValue());
		} else if (msg.checkAddrPattern("/new_skel") && msg.checkTypetag("i")) {
			//New skeleton calibrated! Lets create it!
			Integer id = msg.get(0).intValue();
			Skeleton s = new Skeleton(id, this);
			skels.put(id, s);
		} else if (msg.checkAddrPattern("/lost_user") && msg.checkTypetag("i")) {
			//Lost user/skeleton
			Integer id = msg.get(0).intValue();
			println("Lost user " + id);
			skels.remove(id);
		}
	}
}
