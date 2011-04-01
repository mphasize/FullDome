/**
 * this project uses an fbo to write into cubemap txtures dynamically
 * use wasd or arrow cursors to navigate, use mouse to move the cursor
 * you can press the mouse to zoom in as well
 *
 * draw everything into the 'drawScene' function, instead of draw.
 *
 * Author: Christopher Warnow, 2010, ch.warnow@gmx.de
 */
import codeanticode.glgraphics.*;
import processing.opengl.*;
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import com.sun.opengl.util.*;

GLSLShader shader;
PGraphicsOpenGL pgl;
GL gl;
GLU glu;
GLUT glut;
PVector mouseP;
float globalZoom = 0;

PVector mousePos;
float mouseSpeed = .2;
float globalX = 0;
float globalZ = 0;
float destGlobalX = 0;
float destGlobalZ = 0;

void setup() {
  size(800, 600, OPENGL);
  mousePos = new PVector(width*.5, height*.5, 0);
  glut = new GLUT();
  glu = new GLU();
  pgl = (PGraphicsOpenGL) g;
  gl = pgl.gl;

  initCubeMap();

  noCursor();
}

/*
 * draw everything here
 * this scene is called 6 times for the cubemap 
 */
void drawScene() {

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
  for(int i=0;i<linesAmount;i++) {
    float ratio = (float)i/(linesAmount-1);
    line(0, 0, cos(ratio*TWO_PI) * 30, sin(ratio*TWO_PI) * 30);
  }
  noStroke();

  popMatrix();
}

void draw() {
  // main position
  globalX += (destGlobalX - globalX)*.25;
  globalZ += (destGlobalZ - globalZ)*.25;

  background(0, 0, 0);
  if(mousePressed) destGlobalZ += 3;

  mousePos.x += (mouseX - mousePos.x) * mouseSpeed;
  mousePos.y += (mouseY - mousePos.y) * mouseSpeed;
  mouseP = new PVector(-mousePos.x + width*.5, mousePos.y - height*.5, 0);

  // draw a sphere that reflects its environment (cubemap)
  drawCubeMap();
}

void keyPressed() {
  if(key == 'w' || keyCode == UP) {
    destGlobalZ+=3;
  }
  if(key == 's' || keyCode == DOWN) {
    destGlobalZ-=3;
  }

  if(key == 'a' || keyCode == LEFT) {
    destGlobalX-=3;
  }
  if(key == 'd' || keyCode == RIGHT) {
    destGlobalX+=3;
  }
}

