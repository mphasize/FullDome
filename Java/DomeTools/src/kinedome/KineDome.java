/**
 * this project uses an fbo to write into cubemap txtures dynamically
 * use wasd or arrow cursors to navigate, use mouse to move the cursor
 * you can press the mouse to zoom in as well
 *
 * draw everything into the 'drawScene' function, instead of draw.
 *
 * Author: Christopher Warnow, 2010, ch.warnow@gmx.de
 */
package kinedome;

import codeanticode.glgraphics.GLSLShader;
import com.sun.opengl.util.GLUT;
import java.util.Hashtable;
import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import oscP5.OscMessage;
import oscP5.OscP5;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

/**
 *
 * @author mphasize
 */
public class KineDome extends PApplet implements PConstants {

	GLSLShader shader;
	PGraphicsOpenGL pgl;
	GL gl;
	GLU glu;
	GLUT glut;
	PVector mouseP;
	float globalZoom = 0;
	PVector mousePos;
	float mouseSpeed = .2f;
	float globalX = 0;
	float globalZ = 0;
	float destGlobalX = 0;
	float destGlobalZ = 0;
	int[] fbo = {
		0};
	int[] rbo = {
		0};
	int[] envMapTextureID = {
		0};
	float[] cameraPos = {
		0.0f, 0.0f, -.00001f, 1.0f};
	int envMapSize = 1024;
	Hashtable<Integer, Skeleton> skels = new Hashtable<Integer, Skeleton>();
	OscP5 oscP5;
	int ballSize = 5;
	boolean domemaster = true;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		PApplet.main(new String[]{"kinedome.KineDome"});
	}

	@Override
	public void setup() {
		size(800, 600, OPENGL);
		mousePos = new PVector(width * .5f, height * .5f, 0);
		glut = new GLUT();
		glu = new GLU();
		pgl = (PGraphicsOpenGL) g;
		gl = pgl.gl;
		initCubeMap();
		noCursor();
		oscP5 = new OscP5(this, "127.0.0.1", 7110);
	}

	/*
	 * draw everything here
	 * this scene is called 6 times for the cubemap
	 */
	public void drawScene() {

		pushMatrix();
		translate(globalX, 0, globalZ);

		// cursorBox
		fill(255, 200, 50);
		pushMatrix();
		translate(mouseP.x, mouseP.y, mouseP.z);
		box(5);
		popMatrix();

		// some test lines
		stroke(255);
		int linesAmount = 20;
		for (int i = 0; i < linesAmount; i++) {
			float ratio = (float) i / (linesAmount - 1);
			line(0, 0, cos(ratio * TWO_PI) * 30, sin(ratio * TWO_PI) * 30);
		}
		noStroke();


		pushMatrix();
		rotateX(PI/2f);

		for (Skeleton s : skels.values()) {
			//s.drawStickfigure();
			for (float[] j : s.allCoords) {
				pushMatrix();
				translate(j[0] * 300, j[1] * 300, -j[2] * 100);
				sphere(ballSize);
				popMatrix();
			}

		}
		popMatrix();


		popMatrix();


	}

	@Override
	public void draw() {
		// main position
		globalX += (destGlobalX - globalX) * .25;
		globalZ += (destGlobalZ - globalZ) * .25;

		background(0, 0, 0);
		if (mousePressed) {
			destGlobalZ += 3;
		}

		mousePos.x += (mouseX - mousePos.x) * mouseSpeed;
		mousePos.y += (mouseY - mousePos.y) * mouseSpeed;
		mouseP = new PVector(-mousePos.x + width * .5f, mousePos.y - height * .5f, 0);

		// draw a sphere that reflects its environment (cubemap)
		drawCubeMap();
	}

	@Override
	public void keyPressed() {
		if (key == 'w' || keyCode == UP) {
			destGlobalZ += 3;
		}
		if (key == 's' || keyCode == DOWN) {
			destGlobalZ -= 3;
		}

		if (key == 'a' || keyCode == LEFT) {
			destGlobalX -= 3;
		}
		if (key == 'd' || keyCode == RIGHT) {
			destGlobalX += 3;
		}
		if (key == ' ') {
			this.domemaster = !this.domemaster;
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
			} /* else if (msg.get(0).stringValue().equals("r_knee")) {
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
			} */ else if (msg.get(0).stringValue().equals("l_hip")) {
				s.lHip[0] = msg.get(2).floatValue();
				s.lHip[1] = msg.get(3).floatValue();
				s.lHip[2] = msg.get(4).floatValue();
			} /* else if (msg.get(0).stringValue().equals("l_knee")) {
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
			} */
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

	void initCubeMap() {
		// init cubemap textures
		gl.glGenTextures(1, envMapTextureID, 0);
		gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, envMapTextureID[0]);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_R, GL.GL_CLAMP_TO_EDGE);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
		gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);

		for (int i = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X; i < GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 6; i++) {
			gl.glTexImage2D(i, 0, GL.GL_RGBA8, envMapSize, envMapSize, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
		}

		// smoothing
		gl.glEnable(GL.GL_POLYGON_SMOOTH);
		gl.glEnable(GL.GL_LINE_SMOOTH);
		gl.glEnable(GL.GL_BLEND);
		gl.glHint(GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
		gl.glHint(GL.GL_POLYGON_SMOOTH_HINT, GL.GL_DONT_CARE);

		// Loading reflection shader.
		shader = new GLSLShader(this, "data/cubemapvert.glsl", "data/cubemapfrag.glsl");
	}

	void drawCubeMap() {
		regenerateEnvMap();
		drawDomeMaster();
	}

	void drawDomeMaster() {
		// draw the ellipse
		ortho(-width * .025f, width * .025f, -height * .025f, height * .025f,
				-1500, 1500);

		camera(0, 0, 150, // <- NOTE: this is dynamic and changes the
				// perspective, must test and try more
				0, 0, 0, 0, 1, 0);

		shader.start();

		// draw sphere
		gl.glColor3f(1.0f, 1.0f, 1.0f);
		glut.glutSolidSphere(height * 0.03, 50, 50);

		shader.stop();
	}

// Called to regenerate the envmap
	void regenerateEnvMap() {
		// init fbo
		gl.glGenFramebuffersEXT(1, fbo, 0);
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);
		// Attach one of the faces of the Cubemap texture to this FBO
		gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT,
				GL.GL_COLOR_ATTACHMENT0_EXT, GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X,
				envMapTextureID[0], 0);

		gl.glGenRenderbuffersEXT(1, rbo, 0);
		gl.glBindRenderbufferEXT(GL.GL_RENDERBUFFER_EXT, rbo[0]);
		gl.glRenderbufferStorageEXT(GL.GL_RENDERBUFFER_EXT,
				GL.GL_DEPTH_COMPONENT24, envMapSize, envMapSize);

		// Attach depth buffer to FBO
		gl.glFramebufferRenderbufferEXT(GL.GL_FRAMEBUFFER_EXT,
				GL.GL_DEPTH_ATTACHMENT_EXT, GL.GL_RENDERBUFFER_EXT, rbo[0]);

		// generate 6 views from origin(0, 0, 0)
		gl.glMatrixMode(GL.GL_PROJECTION);
		gl.glLoadIdentity();
		glu.gluPerspective(90.0f, 1.0f, 1.0f, 1025.0f);
		gl.glViewport(0, 0, envMapSize, envMapSize);
		// gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, fbo[0]);

		for (int i = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X; i < GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X + 6; i++) {

			gl.glMatrixMode(GL.GL_MODELVIEW);
			gl.glLoadIdentity();
			switch (i) {
				case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X:
					// +X
					glu.gluLookAt(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
							0.0f);
					break;
				case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X:
					// -X
					glu.gluLookAt(0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, -1.0f,
							0.0f);
					break;
				case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y:
					// +Y
					glu.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f,
							1.0f);
					break;
				case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y:
					// -Y
					glu.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
							-1.0f);
					break;
				case GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z:
					// +Z
					glu.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, -1.0f,
							0.0f);
					break;
				case GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z:
					// -Z
					glu.gluLookAt(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, -1.0f,
							0.0f);
					break;
				default:
					assert (false);
					break;
			}
			gl.glFramebufferTexture2DEXT(GL.GL_FRAMEBUFFER_EXT,
					GL.GL_COLOR_ATTACHMENT0_EXT, i, envMapTextureID[0], 0);
			// Clear the window with current clearing color
			gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
			// Draw objects in the scene
			drawScene();
		}

		// Delete resources
		gl.glDeleteRenderbuffersEXT(1, rbo, 0);
		// Bind 0, which means render to back buffer, as a result, fb is unbound
		gl.glBindFramebufferEXT(GL.GL_FRAMEBUFFER_EXT, 0);
		gl.glDeleteFramebuffersEXT(1, fbo, 0);

		// restore view
		gl.glMatrixMode(GL.GL_MODELVIEW);
		gl.glLoadIdentity();
		glu.gluLookAt(cameraPos[0], cameraPos[1], cameraPos[2], 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f, 0.0f);
		gl.glViewport(0, 0, width, height);
	}
}
