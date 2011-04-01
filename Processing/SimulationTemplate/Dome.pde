/*
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
  private float a = radius * sin(phi) * 2;

  public void draw() {
    if (texture != null) {
      rectMode(CORNERS);

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

          float s1 = sin(linc * l) * (radius + distance) / (cos(linc * l) + distance / radius);
          float s2 = sin(linc * (l + 1)) * (radius + distance) / (cos(linc * (l + 1)) + distance / radius);
          float t1 = map(s1, 0, ((radius + distance) * radius / distance), 0, (texture.height / 2f));
          float t2 = map(s2, 0, ((radius + distance) * radius / distance), 0, (texture.height / 2f));

          if (sourceProjection == DOMEMASTER) {
            beginShape(QUADS);
            texture(texture);
            tint(255, 192);
            vertex(vp1.x, vp1.y, vp1.z, texture.width / 2f + cos(mu) * t1, texture.height / 2f + sin(mu) * t1);
            vertex(vp2.x, vp2.y, vp2.z, texture.width / 2f + cos(mu) * t2, texture.height / 2f + sin(mu) * t2);
            vertex(vp3.x, vp3.y, vp3.z, texture.width / 2f + cos(mu + minc) * t2, texture.height / 2f + sin(mu + minc) * t2);
            vertex(vp4.x, vp4.y, vp4.z, texture.width / 2f + cos(mu + minc) * t1, texture.height / 2f + sin(mu + minc) * t1);
            endShape(CLOSE);
          } 
          else {
            beginShape(QUADS);
            texture(texture);
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
            if (sourceProjection == DOMEMASTER) {
              beginShape(QUADS);
              texture(texture);
              tint(255, 192);
              vertex(s + cos(mu) * s1, radius, s + sin(mu) * s1 + radius, texture.width / 2f + cos(mu) * t1, texture.height / 2f + sin(mu) * t1);
              vertex(s + cos(mu) * s2, radius, s + sin(mu) * s2 + radius, texture.width / 2f + cos(mu) * t2, texture.height / 2f + sin(mu) * t2);
              vertex(s + cos(mu + minc) * s2, radius, s + sin(mu + minc) * s2 + radius, texture.width / 2f + cos(mu + minc) * t2, texture.height / 2f + sin(mu + minc) * t2);
              vertex(s + cos(mu + minc) * s1, radius, s + sin(mu + minc) * s1 + radius, texture.width / 2f + cos(mu + minc) * t1, texture.height / 2f + sin(mu + minc) * t1);
              endShape(CLOSE);
            } 
            else {
              beginShape(QUADS);
              texture(texture);
              tint(255, 192);
              vertex(s + cos(mu) * s1, radius, s + sin(mu) * s1 + radius, mstep * m, lstep * l);
              vertex(s + cos(mu) * s2, radius, s + sin(mu) * s2 + radius, mstep * m, lstep * (l + 1));
              vertex(s + cos(mu + minc) * s2, radius, s + sin(mu + minc) * s2 + radius, mstep * (m + 1), lstep * (l + 1));
              vertex(s + cos(mu + minc) * s1, radius, s + sin(mu + minc) * s1 + radius, mstep * (m + 1), lstep * l);
              endShape(CLOSE);
            }
          }


          //////////////// draws the projection rays
          //stroke(255,63);
          if (drawRays) {
            int c = texture.get((int) (texture.width / 2f + cos(mu) * t2), (int) (texture.height / 2f + sin(mu) * t2));
            stroke(c, 127);
            beginShape(LINES);
            if (drawPlane) {
              vertex(s + cos(mu) * s2, radius, s + sin(mu) * s2 + radius);
            } 
            else {
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

        v[0][m][l] = new PVector(sin(la) * cos(mu) * radius, cos(la) * radius, (sin(la) * sin(mu) + 1) * radius);
        v[1][m][l] = new PVector(cos(mu) * radius, -l * a + lCount / 2f * a, (sin(mu) + 1) * radius);
        v[2][m][l] = new PVector(mCount * a / 2f - m * a, cos(la) * radius, 2 * radius);
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
      if (abs(texture.width - texture.height) > 10f) {
        if (texture.width / (float) texture.height < 3) {
          sourceProjection = Dome.PANORAMA_360;
        } 
        else {
          sourceProjection = Dome.PANORAMA_180;
        }
      } 
      else {
        sourceProjection = Dome.DOMEMASTER;
      }
    }
    setVectors();
    minc = 1f / (float) (mCount - 1f) * TWO_PI;
    linc = 1f / (float) (lCount - 1f) * PI;
    if (sourceProjection != Dome.PANORAMA_360) {
      lstep = texture.height / (float) (lCount - 1f) * 2f;
    } 
    else {
      lstep = texture.height / (float) (lCount - 1f);
    }
    mstep = texture.width / (float) (mCount - 1f);
  }

  public float getDistance() {
    return distance;
  }

  public void setDistance(float distance) {
    this.distance = distance > 0 ? distance : 0;
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

  public boolean hasGrid() {
    return this.drawGrid;
  }
}

