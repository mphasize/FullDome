/*
 * Adaption of totally awesome domemod v.022 BETA by Dimitar Ruszev, 2010 (dimitar.ruszev @ gmail.com)
 * @author mphasize
 *
 * The interesting part is in the Beispiel1 oder Beispiel2... go there and check it out.
 */

import processing.opengl.*;
import oscP5.*;
import netP5.*;

InteractiveDome dome;
PGraphics canvas;
boolean previewMode = false;
ArrayList<DomeSketch> examples = new ArrayList<DomeSketch>();
DomeSketch example = null;
int exampleIndex = 0;
int[] projections = {
  Dome.DOMEMASTER, Dome.PANORAMA_180, Dome.PANORAMA_360
};
int projection = 0;
PFont font;
OscP5 osc;



void setup() {
  size(800, 600, OPENGL);
  //hint(PApplet.ENABLE_OPENGL_4X_SMOOTH);

  font = this.createFont("Verdana", 12, true);
  this.textFont(font, 12);

  /* Init sketching surface */
  canvas = createGraphics(400, 400, JAVA2D);
  canvas.beginDraw();
  canvas.endDraw();

  /* Load Examples 
   Make a copy of BeispielX, modify it, and add it to this list!
   */
   examples.add(new CursorExample(canvas));
  examples.add(new Beispiel1(canvas));
  examples.add(new Beispiel2(canvas));  
  
  /* Prepare first example */
  example = examples.get(exampleIndex);
  example.setup();

  /* Prepare Dome projection */
  dome = new InteractiveDome();
  dome.setRadius(400);
  dome.setTexture(canvas, projections[projection]);
  
  /* Prepare OSC Receiver */
  osc = new OscP5(this, 8000);
  
}


void draw() {
  background(50);
  example.draw();

  if (previewMode) {
    dome.handleMouseRotation(mouseX, mouseY);
    dome.draw();
  } 
  else {
    imageMode(CENTER);
    image(canvas, width / 2, height / 2);
  }
  drawInfos();
}

void drawInfos() {
  fill(255);
  String project = "";
  switch(projection) {
  case 0: 
    project = "DomeMaster"; 
    break;
  case 1: 
    project = "Panorama 180°"; 
    break;
  case 2: 
    project = "Panorama 360°"; 
    break;
  }
  String display = previewMode ? "Dome Preview" : "Sketch Preview";

  text("Example #" + exampleIndex +" (e)", 20, 32);
  text("Grid is " + (dome.hasGrid() ? "ON" : "OFF") + " (g)", 20, 48);  
  text("Display: " + display + " (SPACE)", 20, height - 36);
  if(previewMode) text("Projection: " + project + " (p)", 20, height-20);
}


void keyPressed() {
  /* reset parameters */
  if (key == 'r') {
    dome.resetPosition();
  }
  if (key == ' ') {
    previewMode = !previewMode;
  }
  if (key == 'g') {
    dome.toggleGrid();
  }
  if (key == 'p') {
    projection = projection < projections.length - 1 ? projection + 1 : 0;
    dome.setTexture(canvas, projections[projection]);
  }
  if (key == 'e') {
    exampleIndex = exampleIndex < examples.size() - 1 ? exampleIndex + 1 : 0;
    example = examples.get(exampleIndex);
    System.out.println("Switched to example " + exampleIndex);
    example.setup();
  }
  example.keyPressed(key, keyCode);  
  
}

void oscEvent(OscMessage msg) {
  example.oscEvent(msg);
}

