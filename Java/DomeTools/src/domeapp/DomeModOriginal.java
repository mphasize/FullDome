package domeapp;

/**
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
import processing.core.*;
import java.io.*;
import dome.*;

public class DomeModOriginal extends PApplet {
	PVector vp1, vp2, vp3, vp4, p0;
	PVector[][][] v;
	Integrator phixi, phiyi, tr1i, tr2i;
	float phiz = 0f;
	float phizInc = TWO_PI / 18f;
	String guiText = "domemod v.021 BETA - Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com) \n\ncontrols:\n'i' = turn this help screen on/off\n\nmove mouse = rotate along x,y-axis [in roation mode];\nwarp around 4 different meshes \n- (hemisphere, cylinder and two planes) [in warp mode];\nleft click = change between these modes\n\n'w' = up; 's' = down; 'a' = left; 'd' = right;\n'g','z' = zooom in/out; 'h','j' = rotate along z-axis;\n'r' = reset transformations;\n\n'p','l' = move the projection point;\n'm','n','b','v' = turn meshes / grid / master plane / rays on/off;\n\n'q' turn status display on/off\n\n'e' show source image\n\n'x' quit program\n\nFor custom images, please put the material (*.jpg) in folder 'data'.\n(accepts both domemasters and panoramic images)\npress SPACE to cycle through";
	String[] images;
	int currentImage = 0;
	PImage tex;
	PImage[] timg;
	float tinc_x, tinc_y;
	boolean showSource = false;
	int mCount = 41;
	int lCount = 21;
	int m, l;
	float minc, linc, lstep, mstep;
	float laf, laf_1;
	float mu, la;
	float rad = 400f;
	float dis = 520f;
	float r_d = rad / dis;
	float disstep = 20f;
	float k11, k12, k21, k22;
//float surf_n, surf_c;
	float axesDia = 100f;
	private float tr1, tr2, lasttr1, lasttr2;
	private float phix, phiy, lastphix, lastphiy;
	private float xPos, yPos, zPos, xinc, yinc, zinc;
	private static final int ROTATE = 0;
	private static final int PROJECT_A = 1;
	private int inputType = ROTATE;
	private static final int INPUTCOUNT = 2;
	private float phi = PI / (float) mCount;
	private float theta = PI / (float) mCount;
	private float a = rad * sin(phi) * 2;
	private float h = rad * sin(theta) * 2;
	private float a_sq = sq(a);
//color c1, c2, cc;
	boolean drawMeshes = false;
	boolean drawRays = true;
	boolean drawGrid = true;
	boolean drawPlane = true;
	boolean displayGUI = true;
	boolean displayInfos = true;
	boolean dmInput = true;
	boolean fullInput = false;
	public final float MARGIN = 40;
	float[] surfs = new float[lCount];
	String texPath;
	PFont font, font2, font3;
	float leading = 14f;
	String s;
	File data_content;

	@Override
	public void setup() {
		P5.call = this;
		size(800, 600, OPENGL);
		phix = 0;
		phiy = 0;
		tr1 = 0;
		rectMode(CORNERS);
		phixi = new Integrator(0f, .5f, .1f);
		phiyi = new Integrator(0f, .5f, .1f);
		tr1i = new Integrator(0f, .5f, .1f);
		tr2i = new Integrator(0f, .5f, .1f);
		zinc = -50;
		zPos = -rad * 1.5f;
		xinc = 50;
		xPos = width / 2f;
		yinc = 50;
		yPos = height / 2f;


		p0 = new PVector(0, -dis, rad);

		//texPath = dataPath("") + "\\..\\input";
		//texPath = dataPath("") + "\\..\\input\\";
		texPath = dataPath("");
		data_content = new File(texPath);
		String[] file_content = data_content.list();
		for (int i = 0; i < file_content.length; i++) {
			String s = file_content[i];
			if (s.substring((s.length() - 3), s.length()).equals("jpg") || s.substring((s.length() - 3), s.length()).equals("png")) {
				if (images == null) {
					images = new String[1];
					images[0] = s;
				} else {
					images = append(images, s);
				}
			}
		}


		if (images != null) { // will be null if inaccessible
			println(images);
			timg = new PImage[images.length];
			for (int i = 0; i < images.length; i++) {
				timg[i] = loadImage(texPath + images[i]);
			}
		} else {
			println("No textures found!");
			exit();
		}

		initTex(timg[0]);
		font = loadFont("Consolas-18.vlw");
		font2 = loadFont("Consolas-14.vlw");
		font3 = loadFont("Consolas-10.vlw");
		textFont(font);
		ellipseMode(CENTER);
	}

	@Override
	public void draw() {
		background(13);
		//  noFill();
		//  stroke(255);
		//  rect(MARGIN,MARGIN,width-MARGIN,height-MARGIN);
		//stroke(255,0,0);
		noStroke();
		fill(255, 63);
		rect(0, 0, width, MARGIN);
		rect(0, height - MARGIN, width, height);
		rect(0, MARGIN, MARGIN, height - MARGIN);
		rect(width - MARGIN, MARGIN, width, height - MARGIN);
		noFill();

		handleMouseInput();

		pushMatrix();
		translate(xPos, yPos, zPos);
		rotateX(phix);
		rotateY(phiy);
		rotateZ(phiz);
		translate(0, 0, -rad);

		drawProjection1();
		popMatrix();
		pushMatrix();
		displayAxes();
		popMatrix();
		if (displayGUI) {
			displayGUI();
		}
		if (displayInfos) {
			displayInfos();
		}
		if (showSource) {
			image(tex, width * 3 / 4f, MARGIN, width / 4f - MARGIN, tex.height * (width / 4f - MARGIN) / tex.width);
		}

	}

	public void drawProjection1() {

		la = 0;
		int l = 0;
		int m;
		int lp;
		int mp;
		k11 = sq(cos(tr1));
		k12 = sq(sin(tr1));
		k21 = sq(cos(tr2));
		k22 = sq(sin(tr2));

		while (la < HALF_PI) {
			m = 0;
			mu = -HALF_PI;
			while (mu + HALF_PI <= TWO_PI) {

				lp = (l + 1);
				mp = (m + 1);


				vp1.set(k21 * (k11 * v[0][m][l].x + k12 * v[1][m][l].x) + k22 * (k11 * v[2][m][l].x + k12 * v[3][m][l].x),
						k21 * (k11 * v[0][m][l].y + k12 * v[1][m][l].y) + k22 * (k11 * v[2][m][l].y + k12 * v[3][m][l].y),
						k21 * (k11 * v[0][m][l].z + k12 * v[1][m][l].z) + k22 * (k11 * v[2][m][l].z + k12 * v[3][m][l].z));

				vp2.set(k21 * (k11 * v[0][m][lp].x + k12 * v[1][m][lp].x) + k22 * (k11 * v[2][m][lp].x + k12 * v[3][m][lp].x),
						k21 * (k11 * v[0][m][lp].y + k12 * v[1][m][lp].y) + k22 * (k11 * v[2][m][lp].y + k12 * v[3][m][lp].y),
						k21 * (k11 * v[0][m][lp].z + k12 * v[1][m][lp].z) + k22 * (k11 * v[2][m][lp].z + k12 * v[3][m][lp].z));

				vp3.set(k21 * (k11 * v[0][mp][lp].x + k12 * v[1][mp][lp].x) + k22 * (k11 * v[2][mp][lp].x + k12 * v[3][mp][lp].x),
						k21 * (k11 * v[0][mp][lp].y + k12 * v[1][mp][lp].y) + k22 * (k11 * v[2][mp][lp].y + k12 * v[3][mp][lp].y),
						k21 * (k11 * v[0][mp][lp].z + k12 * v[1][mp][lp].z) + k22 * (k11 * v[2][mp][lp].z + k12 * v[3][mp][lp].z));

				vp4.set(k21 * (k11 * v[0][mp][l].x + k12 * v[1][mp][l].x) + k22 * (k11 * v[2][mp][l].x + k12 * v[3][mp][l].x),
						k21 * (k11 * v[0][mp][l].y + k12 * v[1][mp][l].y) + k22 * (k11 * v[2][mp][l].y + k12 * v[3][mp][l].y),
						k21 * (k11 * v[0][mp][l].z + k12 * v[1][mp][l].z) + k22 * (k11 * v[2][mp][l].z + k12 * v[3][mp][l].z));


				if (drawMeshes) {

					beginShape(POINTS);
					strokeWeight(3);
					stroke(127, 127, 0);
					vertex(v[0][m][l].x, v[0][m][l].y, v[0][m][l].z);
					stroke(63, 127, 0);
					vertex(v[1][m][l].x, v[1][m][l].y, v[1][m][l].z);
					stroke(0, 127, 63);
					vertex(v[2][m][l].x, v[2][m][l].y, v[2][m][l].z);
					stroke(0, 127, 127);
					vertex(v[3][m][l].x, v[3][m][l].y, v[3][m][l].z);
					endShape();
					strokeWeight(2);
				}

				stroke(255, 255, 96, 127);

				//////////////// draws the dome

				if (!drawGrid) {
					noStroke();
				}
				float s1 = sin(linc * l) * (rad + dis) / (cos(linc * l) + dis / rad);
				float s2 = sin(linc * (l + 1)) * (rad + dis) / (cos(linc * (l + 1)) + dis / rad);
				float t1 = map(s1, 0, ((rad + dis) * rad / dis), 0, (tex.height / 2f));
				float t2 = map(s2, 0, ((rad + dis) * rad / dis), 0, (tex.height / 2f));

				if (dmInput) {
					beginShape(QUADS);
					texture(tex);
					tint(255, 192);
					vertex(vp1.x, vp1.y, vp1.z, tex.width / 2f + cos(mu) * t1, tex.height / 2f + sin(mu) * t1);
					vertex(vp2.x, vp2.y, vp2.z, tex.width / 2f + cos(mu) * t2, tex.height / 2f + sin(mu) * t2);
					vertex(vp3.x, vp3.y, vp3.z, tex.width / 2f + cos(mu + minc) * t2, tex.height / 2f + sin(mu + minc) * t2);
					vertex(vp4.x, vp4.y, vp4.z, tex.width / 2f + cos(mu + minc) * t1, tex.height / 2f + sin(mu + minc) * t1);
					endShape(CLOSE);
				} else {

					beginShape(QUADS);
					texture(tex);
					tint(255, 192);
					vertex(vp1.x, vp1.y, vp1.z, mstep * m, lstep * l);
					vertex(vp2.x, vp2.y, vp2.z, mstep * m, lstep * (l + 1));
					vertex(vp3.x, vp3.y, vp3.z, mstep * (m + 1), lstep * (l + 1));
					vertex(vp4.x, vp4.y, vp4.z, mstep * (m + 1), lstep * l);
					endShape(CLOSE);



				}
				float s = 0;
				//////////////// draws the domemaster in plane
				if (drawPlane) {
					if (dmInput) {
						beginShape(QUADS);
						texture(tex);
						tint(255, 192);
						vertex(s + cos(mu) * s1, rad, s + sin(mu) * s1 + rad, tex.width / 2f + cos(mu) * t1, tex.height / 2f + sin(mu) * t1);
						vertex(s + cos(mu) * s2, rad, s + sin(mu) * s2 + rad, tex.width / 2f + cos(mu) * t2, tex.height / 2f + sin(mu) * t2);
						vertex(s + cos(mu + minc) * s2, rad, s + sin(mu + minc) * s2 + rad, tex.width / 2f + cos(mu + minc) * t2, tex.height / 2f + sin(mu + minc) * t2);
						vertex(s + cos(mu + minc) * s1, rad, s + sin(mu + minc) * s1 + rad, tex.width / 2f + cos(mu + minc) * t1, tex.height / 2f + sin(mu + minc) * t1);
						endShape(CLOSE);
					} else {
						beginShape(QUADS);
						texture(tex);
						tint(255, 192);
						vertex(s + cos(mu) * s1, rad, s + sin(mu) * s1 + rad, mstep * m, lstep * l);
						vertex(s + cos(mu) * s2, rad, s + sin(mu) * s2 + rad, mstep * m, lstep * (l + 1));
						vertex(s + cos(mu + minc) * s2, rad, s + sin(mu + minc) * s2 + rad, mstep * (m + 1), lstep * (l + 1));
						vertex(s + cos(mu + minc) * s1, rad, s + sin(mu + minc) * s1 + rad, mstep * (m + 1), lstep * l);
						endShape(CLOSE);
					}
				}


				//////////////// draws the projection rays
				//stroke(255,63);
				if (drawRays) {
					int c = tex.get((int) (tex.width / 2f + cos(mu) * t2), (int) (tex.height / 2f + sin(mu) * t2));
					stroke(c, 127);
					beginShape(LINES);
					if (drawPlane) {
						vertex(s + cos(mu) * s2, rad, s + sin(mu) * s2 + rad);
					} else {
						vertex(vp2.x, vp2.y, vp2.z);
					}
					vertex(p0.x, p0.y, p0.z);
					endShape();
				}


				mu += minc;
				m++;
			}
			la += linc;
			l++;
		}

	}

	public void displayAxes() {

		translate(width - axesDia, height - axesDia, 0);
		textMode(MODEL);

		fill(255);

		rotateX(phix);
		text("y+", 3, axesDia / 2f - 3, 0);
		text("y-", 3, -axesDia / 2f + 8, 0);

		rotateY(phiy);

		text("z+", 3, 0, axesDia / 2f);
		text("z-", 3, 0, -axesDia / 2f);

		rotateZ(phiz);
		text("x+", axesDia / 2f - 18, -5, 0);
		text("x-", -axesDia / 2f - 3, -5, 0);

		stroke(255);
		beginShape(LINES);
		vertex(axesDia / 2f, 0, 0);
		vertex(-axesDia / 2f, 0, 0);
		endShape();
		beginShape(LINES);
		vertex(0, axesDia / 2f, 0);
		vertex(0, -axesDia / 2f, 0);
		endShape();
		beginShape(LINES);
		vertex(0, 0, axesDia / 2f);
		vertex(0, 0, -axesDia / 2f);
		endShape();
	}

	public void displayGUI() {
		fill(191);
		textFont(font2);
		text(guiText, MARGIN + 2, MARGIN - 5f);
	}

	public void displayInfos() {
		fill(255);
		textFont(font3);
		int lines = 7;
		s = "pos.x: " + xPos + ". pos.y: " + yPos + ". pos.z: " + zPos + '.';
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "rot.x: " + nf(degrees(phix), 3, 2) + "\u00b0 rot.y: " + nf(degrees(phiy), 3, 2) + "\u00b0 rot.z: " + nf(degrees(phiz), 3, 2) + '\u00b0';
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "radius: " + rad + ". distance to projection point: " + dis + '.';
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "grid is: " + onoff(drawGrid) + ". plane is: " + onoff(drawPlane) + ". rays are: " + onoff(drawRays) + ". meshes are: " + onoff(drawMeshes) + '.';
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "interaction mode is: ";
		if (inputType == 0) {
			s += "ROTATION.";
		} else {
			s += "WARPING.";
		}
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "mesh interpolation: " + nf(k21 * k11, 1, 4) + " | " + nf(k21 * k12, 1, 4) + " | " + nf(k22 * k11, 1, 4) + " | " + nf(k22 * k12, 1, 4) + '.';
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
		lines--;
		s = "input is: ";
		if (dmInput) {
			s += "DOMEMASTER. ";
		} else {
			if (fullInput) {
				s += "360\u00b0 (cropped) ";
			} else {
				s += "180\u00b0 ";
			}
			s += "PANORAMA. ";
		}
		text(s, MARGIN + 2, height - MARGIN + 7 - lines * leading);
	}

	public String onoff(boolean input) {
		if (input) {
			return ("ON");
		} else {
			return ("OFF");
		}

	}

	@Override
	public void keyPressed() {
		if (key == 'x') {
			exit();
		}
		if (key == 'e') {
			showSource = !showSource;
		}
		if (key == 'z') {
			zPos -= zinc;
		}
		if (key == 'g') {
			zPos += zinc;
		}
		if (key == 'w') {
			yPos -= yinc;
		}
		if (key == 's') {
			yPos += yinc;
		}
		if (key == 'a') {
			xPos -= xinc;
		}
		if (key == 'd') {
			xPos += xinc;
		}
		if (key == 'm') {
			drawMeshes = !drawMeshes;
		}
		if (key == 'n') {
			drawGrid = !drawGrid;
		}
		if (key == 'b') {
			drawPlane = !drawPlane;
		}
		if (key == 'v') {
			drawRays = !drawRays;
		}
		if (key == 'i') {
			displayGUI = !displayGUI;
		}
		if (key == 'q') {
			displayInfos = !displayInfos;
		}
		if (key == 'j') {
			phiz += phizInc;
		}
		if (key == 'h') {
			phiz -= phizInc;
		}
		if (key == 'p') {

			dis += disstep;
			r_d = rad / dis;
			p0.set(0, -dis, rad);
			println(dis);

		}
		if (key == 'l') {
			dis -= disstep;
			if (dis < 0) {
				dis = 0;
			}
			p0.set(0, -dis, rad);
			r_d = rad / dis;
			println(dis);
		}
		if (key == 'r') {
			xPos = width / 2f;
			yPos = height / 2f;
			zPos = -rad * 1.5f;
			phiz = 0f;
			dis = 520f;
		}

		if (key == ' ') {
			currentImage++;
			currentImage = currentImage % images.length;
			initTex(timg[currentImage]);
		}
	}

	public void initTex(String theImgName) {
		tex = null;
		fullInput = false;
		dmInput = true;
		tex = loadImage(theImgName);
		if (abs(tex.width - tex.height) > 10f) {
			dmInput = false;
			if (tex.width / (float) tex.height < 3) {
				fullInput = true;
			}

		}
		tinc_x = tex.width / (float) mCount;
		tinc_y = tex.height / (float) lCount * 2f;

		setVectors();
		minc = 1f / (float) (mCount - 1f) * TWO_PI;
		linc = 1f / (float) (lCount - 1f) * PI;
		if (!fullInput) {
			lstep = tex.height / (float) (lCount - 1f) * 2f;
		} else {
			lstep = tex.height / (float) (lCount - 1f);
		}
		mstep = tex.width / (float) (mCount - 1f);
	}

	public void initTex(PImage theImg) {
		tex = null;
		fullInput = false;
		dmInput = true;
		tex = theImg;
		if (abs(tex.width - tex.height) > 10f) {
			dmInput = false;
			if (tex.width / (float) tex.height < 3) {
				fullInput = true;
			}

		}
		tinc_x = tex.width / (float) mCount;
		tinc_y = tex.height / (float) lCount * 2f;

		setVectors();
		minc = 1f / (float) (mCount - 1f) * TWO_PI;
		linc = 1f / (float) (lCount - 1f) * PI;
		if (!fullInput) {
			lstep = tex.height / (float) (lCount - 1f) * 2f;
		} else {
			lstep = tex.height / (float) (lCount - 1f);
		}
		mstep = tex.width / (float) (mCount - 1f);
	}

	public void setVectors() {
		vp1 = new PVector();
		vp2 = new PVector();
		vp3 = new PVector();
		vp4 = new PVector();


		v = new PVector[4][mCount][lCount];
		minc = 1f / (float) (mCount - 1f) * TWO_PI;
		linc = 1f / (float) (lCount - 1f) * PI;
		la = 0;
		int l = 0;
		int m;

		while (la < PI + linc) {
			m = 0;
			mu = -HALF_PI;
			while (mu + HALF_PI < TWO_PI + minc) {

				v[0][m][l] = new PVector(sin(la) * cos(mu) * rad, cos(la) * rad, (sin(la) * sin(mu) + 1) * rad);
				v[1][m][l] = new PVector(cos(mu) * rad, -l * a + lCount / 2f * a, (sin(mu) + 1) * rad);
				v[2][m][l] = new PVector(mCount * a / 2f - m * a, cos(la) * rad, 2 * rad);
				v[3][m][l] = new PVector(mCount * a / 2f - m * a, -l * a + lCount / 2f * a, 2 * rad);
				mu += minc;
				m++;
			}
			la += linc;
			l++;
		}
//println(m);
//println(l);
	}

	public void drawAllMeshes(int l, int m) {
		for (int i = 0; i < v.length; i++) {
			beginShape(QUADS);
			vertex(v[i][m][l].x, v[i][m][l].y, v[i][m][l].z);
			vertex(v[i][m][l + 1].x, v[i][m][l + 1].y, v[i][m][l + 1].z);
			vertex(v[i][m + 1][l + 1].x, v[i][m + 1][l + 1].y, v[i][m + 1][l + 1].z);
			vertex(v[i][m + 1][l].x, v[i][m + 1][l].y, v[i][m + 1][l].z);
			endShape(CLOSE);
		}
	}

	private void handleMouseInput() {
		switch (inputType) {
			case ROTATE:
				lastphix = phix;
				lastphiy = phiy;
				phix = map(constrain(mouseY, 0 + MARGIN, height - MARGIN), 0 + MARGIN, height - MARGIN, TWO_PI, 0);
				phiy = map(constrain(mouseX, 0 + MARGIN, width - MARGIN), 0 + MARGIN, width - MARGIN, 0, TWO_PI);

				phixi.set(lastphix);
				phiyi.set(lastphiy);
				phixi.target(phix);
				phiyi.target(phiy);
				phixi.update();
				phix = phixi.getValue();
				phiyi.update();
				phiy = phiyi.getValue();

				tr1i.update();
				tr2i.update();
				tr1 = tr1i.getValue();
				tr2 = tr2i.getValue();
				break;
			case PROJECT_A:
				lasttr1 = tr1;
				lasttr2 = tr2;
				tr1 = map(constrain(mouseY, 0 + MARGIN, height - MARGIN), 0 + MARGIN, height - MARGIN, 0, HALF_PI);
				tr2 = map(constrain(mouseX, 0 + MARGIN, width - MARGIN), 0 + MARGIN, width - MARGIN, 0, HALF_PI);

				tr1i.set(lasttr1);
				tr2i.set(lasttr2);
				tr1i.target(tr1);
				tr2i.target(tr2);
				tr1i.update();
				tr1 = tr1i.getValue();
				tr2i.update();
				tr2 = tr2i.getValue();
				phixi.update();
				phix = phixi.getValue();
				phiyi.update();
				phiy = phiyi.getValue();
				break;
		}
	}

	@Override
	public void mousePressed() {
		inputType = (inputType + 1) % INPUTCOUNT;
		drawRays = false;
		drawPlane = false;
	}

	static public void main(String args[]) {
		PApplet.main(new String[]{"--bgcolor=#F0F0F0", "domeapp.DomeModOriginal"});
	}
}
