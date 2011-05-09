/*
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */
package dome;

import domeapp.P5;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PVector;

/**
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 */

public class Dome implements PConstants {
	/* Projection Constants */

	public static final int AUTOMATIC = 0;
	public static final int DOMEMASTER = 1;
	public static final int PANORAMA_360 = 2;
	public static final int PANORAMA_180 = 3;
	/* Image for projection */
	private PImage texture = null;
	private int sourceProjection = AUTOMATIC;

	/* Draw Options */
	private boolean drawGrid = false;
	private boolean drawMeshes = false;
	private boolean drawPlane = false;
	private boolean drawRays = false;
	int mCount = 41;
	int lCount = 21;
	/* Internal Mathematics */
	private PVector vp1, vp2, vp3, vp4, p0;
	private PVector[][][] v;
	private float mu, la;
	private float k11, k12, k21, k22;
	protected float tr1, tr2;
	private float radius = 400f;
	private float distance = 520f;
	private float minc, linc, lstep, mstep;
	private float phi = PI / (float) mCount;
	// private float theta = PI / (float) mCount;
	private float a = radius * P5.sin(phi) * 2;

	public void draw() {
		if (texture != null) {
			P5.call.rectMode(CORNERS);

			la = 0;
			int l = 0;
			int m;
			int lp;
			int mp;
			k11 = P5.sq(P5.cos(tr1));
			k12 = P5.sq(P5.sin(tr1));
			k21 = P5.sq(P5.cos(tr2));
			k22 = P5.sq(P5.sin(tr2));

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

						P5.call.beginShape(POINTS);
						P5.call.strokeWeight(3);
						P5.call.stroke(127, 127, 0);
						P5.call.vertex(v[0][m][l].x, v[0][m][l].y, v[0][m][l].z);
						P5.call.stroke(63, 127, 0);
						P5.call.vertex(v[1][m][l].x, v[1][m][l].y, v[1][m][l].z);
						P5.call.stroke(0, 127, 63);
						P5.call.vertex(v[2][m][l].x, v[2][m][l].y, v[2][m][l].z);
						P5.call.stroke(0, 127, 127);
						P5.call.vertex(v[3][m][l].x, v[3][m][l].y, v[3][m][l].z);
						P5.call.endShape();
						P5.call.strokeWeight(2);
					}

					P5.call.stroke(255, 255, 96, 127);

					//////////////// draws the dome

					if (!drawGrid) {
						P5.call.noStroke();
					}

					float s1 = P5.sin(linc * l) * (radius + distance) / (P5.cos(linc * l) + distance / radius);
					float s2 = P5.sin(linc * (l + 1)) * (radius + distance) / (P5.cos(linc * (l + 1)) + distance / radius);
					float t1 = P5.map(s1, 0, ((radius + distance) * radius / distance), 0, (texture.height / 2f));
					float t2 = P5.map(s2, 0, ((radius + distance) * radius / distance), 0, (texture.height / 2f));

					if (sourceProjection == DOMEMASTER) {
						P5.call.beginShape(QUADS);
						P5.call.texture(texture);
						P5.call.tint(255, 192);
						P5.call.vertex(vp1.x, vp1.y, vp1.z, texture.width / 2f + P5.cos(mu) * t1, texture.height / 2f + P5.sin(mu) * t1);
						P5.call.vertex(vp2.x, vp2.y, vp2.z, texture.width / 2f + P5.cos(mu) * t2, texture.height / 2f + P5.sin(mu) * t2);
						P5.call.vertex(vp3.x, vp3.y, vp3.z, texture.width / 2f + P5.cos(mu + minc) * t2, texture.height / 2f + P5.sin(mu + minc) * t2);
						P5.call.vertex(vp4.x, vp4.y, vp4.z, texture.width / 2f + P5.cos(mu + minc) * t1, texture.height / 2f + P5.sin(mu + minc) * t1);
						P5.call.endShape(CLOSE);
					} else {
						P5.call.beginShape(QUADS);
						P5.call.texture(texture);
						P5.call.tint(255, 192);
						P5.call.vertex(vp1.x, vp1.y, vp1.z, mstep * m, lstep * l);
						P5.call.vertex(vp2.x, vp2.y, vp2.z, mstep * m, lstep * (l + 1));
						P5.call.vertex(vp3.x, vp3.y, vp3.z, mstep * (m + 1), lstep * (l + 1));
						P5.call.vertex(vp4.x, vp4.y, vp4.z, mstep * (m + 1), lstep * l);
						P5.call.endShape(CLOSE);
					}
					float s = 0;
					//////////////// draws the domemaster in plane
					if (drawPlane) {
						if (sourceProjection == DOMEMASTER) {
							P5.call.beginShape(QUADS);
							P5.call.texture(texture);
							P5.call.tint(255, 192);
							P5.call.vertex(s + P5.cos(mu) * s1, radius, s + P5.sin(mu) * s1 + radius, texture.width / 2f + P5.cos(mu) * t1, texture.height / 2f + P5.sin(mu) * t1);
							P5.call.vertex(s + P5.cos(mu) * s2, radius, s + P5.sin(mu) * s2 + radius, texture.width / 2f + P5.cos(mu) * t2, texture.height / 2f + P5.sin(mu) * t2);
							P5.call.vertex(s + P5.cos(mu + minc) * s2, radius, s + P5.sin(mu + minc) * s2 + radius, texture.width / 2f + P5.cos(mu + minc) * t2, texture.height / 2f + P5.sin(mu + minc) * t2);
							P5.call.vertex(s + P5.cos(mu + minc) * s1, radius, s + P5.sin(mu + minc) * s1 + radius, texture.width / 2f + P5.cos(mu + minc) * t1, texture.height / 2f + P5.sin(mu + minc) * t1);
							P5.call.endShape(CLOSE);
						} else {
							P5.call.beginShape(QUADS);
							P5.call.texture(texture);
							P5.call.tint(255, 192);
							P5.call.vertex(s + P5.cos(mu) * s1, radius, s + P5.sin(mu) * s1 + radius, mstep * m, lstep * l);
							P5.call.vertex(s + P5.cos(mu) * s2, radius, s + P5.sin(mu) * s2 + radius, mstep * m, lstep * (l + 1));
							P5.call.vertex(s + P5.cos(mu + minc) * s2, radius, s + P5.sin(mu + minc) * s2 + radius, mstep * (m + 1), lstep * (l + 1));
							P5.call.vertex(s + P5.cos(mu + minc) * s1, radius, s + P5.sin(mu + minc) * s1 + radius, mstep * (m + 1), lstep * l);
							P5.call.endShape(CLOSE);
						}
					}


					//////////////// draws the projection rays
					//stroke(255,63);
					if (drawRays) {
						int c = texture.get((int) (texture.width / 2f + P5.cos(mu) * t2), (int) (texture.height / 2f + P5.sin(mu) * t2));
						P5.call.stroke(c, 127);
						P5.call.beginShape(LINES);
						if (drawPlane) {
							P5.call.vertex(s + P5.cos(mu) * s2, radius, s + P5.sin(mu) * s2 + radius);
						} else {
							P5.call.vertex(vp2.x, vp2.y, vp2.z);
						}
						P5.call.vertex(p0.x, p0.y, p0.z);
						P5.call.endShape();
					}


					mu += minc;
					m++;
				}
				la += linc;
				l++;
			}
		}
	}

	private void setVectors() {
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

				v[0][m][l] = new PVector(P5.sin(la) * P5.cos(mu) * radius, P5.cos(la) * radius, (P5.sin(la) * P5.sin(mu) + 1) * radius);
				v[1][m][l] = new PVector(P5.cos(mu) * radius, -l * a + lCount / 2f * a, (P5.sin(mu) + 1) * radius);
				v[2][m][l] = new PVector(mCount * a / 2f - m * a, P5.cos(la) * radius, 2 * radius);
				v[3][m][l] = new PVector(mCount * a / 2f - m * a, -l * a + lCount / 2f * a, 2 * radius);
				mu += minc;
				m++;
			}
			la += linc;
			l++;
		}
	}

	private void initTexture() {
		if (sourceProjection == AUTOMATIC) {
			if (P5.abs(texture.width - texture.height) > 10f) {
				if (texture.width / (float) texture.height < 3) {
					sourceProjection = Dome.PANORAMA_360;
				} else {
					sourceProjection = Dome.PANORAMA_180;
				}

			} else {
				sourceProjection = Dome.DOMEMASTER;
			}
		}
		setVectors();
		minc = 1f / (float) (mCount - 1f) * TWO_PI;
		linc = 1f / (float) (lCount - 1f) * PI;
		if (sourceProjection != Dome.PANORAMA_360) {
			lstep = texture.height / (float) (lCount - 1f) * 2f;
		} else {
			lstep = texture.height / (float) (lCount - 1f);
		}
		mstep = texture.width / (float) (mCount - 1f);
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance > 0 ? distance : 0;
		if(!(p0 instanceof PVector)) {
			p0 = new PVector();
		}
		p0.set(0, -this.distance, radius);
	}

	public int getSourceProjection() {
		return sourceProjection;
	}

	public void setSourceProjection(int sourceProjection) {
		this.sourceProjection = sourceProjection;
	}

	public PImage getTexture() {
		return texture;
	}

	public void setTexture(PImage texture) {
		this.sourceProjection = AUTOMATIC;
		this.texture = texture;
	}

	public void setTexture(PImage texture, int projection) {
		this.sourceProjection = projection;
		this.texture = texture;
		this.initTexture();
	}

	public float getRadius() {
		return radius;
	}

	public void setRadius(float radius) {
		this.radius = radius;
	}

	public void toggleGrid() {
		this.drawGrid = !this.drawGrid;
	}
}
