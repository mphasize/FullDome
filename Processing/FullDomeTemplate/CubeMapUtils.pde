/**
 * this project uses an fbo to write into cubemap txtures dynamically
 * use wasd or arrow cursors to navigate, use mouse to move the cursor
 * you can press the mouse to zoom in as well
 *
 * draw everything into the 'drawScene' function, instead of draw.
 *
 * Author: Christopher Warnow, 2010, ch.warnow@gmx.de
 */
int[] fbo = { 
  0       };
int[] rbo = { 
  0       };
int[] envMapTextureID = { 
  0       };

float[] cameraPos = {
  0.0f, 0.0f, -.00001f, 1.0f };

int envMapSize = 1024;

void initCubeMap() {
  // init cubemap textures
  gl.glGenTextures(1, envMapTextureID, 0);
  gl.glBindTexture(GL.GL_TEXTURE_CUBE_MAP, envMapTextureID[0]);
  gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_S, GL.GL_CLAMP_TO_EDGE);
  gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_T, GL.GL_CLAMP_TO_EDGE);
  gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_WRAP_R, GL.GL_CLAMP_TO_EDGE);
  gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MIN_FILTER, GL.GL_NEAREST);
  gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL.GL_TEXTURE_MAG_FILTER, GL.GL_NEAREST);

  for (int i = GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X; i < GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X+6; i++)
  {
    gl.glTexImage2D(i, 0, GL.GL_RGBA8, envMapSize, envMapSize, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, null);
  }

  // smoothing
  gl.glEnable (GL.GL_POLYGON_SMOOTH);
  gl.glEnable (GL.GL_LINE_SMOOTH);
  gl.glEnable (GL.GL_BLEND);
  gl.glHint (GL.GL_LINE_SMOOTH_HINT, GL.GL_DONT_CARE);
  gl.glHint (GL.GL_POLYGON_SMOOTH_HINT, GL.GL_DONT_CARE);

  // Loading reflection shader.
  shader = new GLSLShader(this, "cubemapvert.glsl", "cubemapfrag.glsl");
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
  glut.glutSolidSphere(height*0.03, 50, 50);

  shader.stop();
}

// Called to regenerate the envmap
void regenerateEnvMap()
{ 
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



