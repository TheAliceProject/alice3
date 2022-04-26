package edu.cmu.cs.dennisc.render.gl.imp;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.util.ImmModeSink;

//import static com.jogamp.opengl.GL2.GL_QUAD_STRIP;

public class CurveRenderer {
  private static final int STACK_COUNT = 50;
  private static final int SLICE_COUNT = 50;
  private static final int SIDE_COUNT = 32;
  private static final int RING_COUNT = 16;
  private static final float TAU = 2f * FloatUtil.PI;

  public record CirclePortion(float centerS, float centerT, float portion) {
  }

  /**
   * Customized version of drawSphere with inverting image handling
   */
  public void drawSphere(Context c, double radius) {
    GL2 gl = c.gl;
    double rho, drho, theta, dtheta;
    double x, y, z;
    double s, t, ds, dt;
    int i, j, imin, imax;

    drho = Math.PI / STACK_COUNT;
    dtheta = (Math.PI * 2) / SLICE_COUNT;

    if (!c.isTextureEnabled()) {
      gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glNormal3d(0.0, 0.0, 1.0);
      gl.glVertex3d(0.0, 0.0, radius);
      for (j = 0; j <= SLICE_COUNT; j++) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = -Math.sin(theta) * Math.sin(drho);
        y = Math.cos(theta) * Math.sin(drho);
        z = Math.cos(drho);
        gl.glNormal3d(x, y, z);
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
    }

    ds = 1.0 / SLICE_COUNT;
    dt = 1.0 / STACK_COUNT;
    t = 0.0;
    if (c.isTextureEnabled()) {
      imin = 0;
      imax = STACK_COUNT;
    } else {
      imin = 1;
      imax = STACK_COUNT - 1;
    }

    for (i = imax; i >= imin; i--) {
      rho = i * drho;
      gl.glBegin(ImmModeSink.GL_QUAD_STRIP);
      s = 1.0;
      for (j = SLICE_COUNT; j >= 0; j--) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = Math.sin(theta) * Math.sin(rho);
        y = -Math.cos(rho);
        z = Math.cos(theta) * Math.sin(rho);
        gl.glNormal3d(x, y, z);
        if (c.isTextureEnabled()) {
          gl.glTexCoord2d(s, t);
        }
        gl.glVertex3d(x * radius, y * radius, z * radius);
        x = Math.sin(theta) * Math.sin(rho + drho);
        y = -Math.cos(rho + drho);
        z = Math.cos(theta) * Math.sin(rho + drho);
        gl.glNormal3d(x, y, z);
        if (c.isTextureEnabled()) {
          gl.glTexCoord2d(s, t - dt);
        }
        s -= ds;
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
      t += dt;
    }

    if (!c.isTextureEnabled()) {
      gl.glBegin(GL.GL_TRIANGLE_FAN);
      gl.glNormal3d(0.0, 0.0, -1.0);
      gl.glVertex3d(0.0, 0.0, -radius);
      rho = Math.PI - drho;
      s = 1.0;
      for (j = SLICE_COUNT; j >= 0; j--) {
        theta = (j == SLICE_COUNT) ? 0.0 : j * dtheta;
        x = -Math.sin(theta) * Math.sin(rho);
        y = Math.cos(theta) * Math.sin(rho);
        z = Math.cos(rho);
        gl.glNormal3d(x, y, z);
        s -= ds;
        gl.glVertex3d(x * radius, y * radius, z * radius);
      }
      gl.glEnd();
    }
  }

  public void drawTorus(Context c, Double majorRadius, Double minorRadius) {
    for (int i = RING_COUNT - 1; i >= 0; i--) {
      c.gl.glBegin(ImmModeSink.GL_QUAD_STRIP);
      for (int j = 0; j < SIDE_COUNT + 1; j++) {
        for (int k = 1; k >= 0; k--) {
          double s, t, x, y, z, u, v;
          double nx, ny, nz;
          double sinRings, cosRings, sinSides, cosSides, dist;

          s = (i + k) % RING_COUNT + 0.5;
          t = j % (SIDE_COUNT + 1);
          sinRings = Math.sin((s * TAU) / RING_COUNT);
          cosRings = -Math.cos((s * TAU) / RING_COUNT);
          sinSides = Math.sin((t * TAU) / SIDE_COUNT);
          cosSides = Math.cos((t * TAU) / SIDE_COUNT);
          dist = majorRadius + minorRadius * cosRings;

          x = -sinSides * dist;
          y = minorRadius * sinRings;
          z = dist * cosSides;
          u = -t / SIDE_COUNT;
          v = (i + k) / (float) RING_COUNT;
          nx = sinSides * cosRings;
          ny = sinRings;
          nz = cosSides * cosRings;
          normal3d(c.gl, nx, ny, nz);
          if (c.isTextureEnabled()) {
            c.gl.glTexCoord2d(u, v);
          }
          c.gl.glVertex3d(x, y, z);
        }
      }
      c.gl.glEnd();
    }
  }

  // Inspired by GLUquadricImpl.drawDisk() for the limited case of GLU_OUTSIDE, GLU_FILL, and having normals.
  // The texture application has been flipped in T so images appear forward
  public void drawDisk(Context c, double innerRadius, double outerRadius, CirclePortion textureCircle) {
    c.gl.getGL2().glNormal3f(0.0f, 0.0f, +1.0f);

    float da = TAU / SLICE_COUNT;
    float dr = (float) (outerRadius - innerRadius);
    final float dtc = (float) (2.0 * outerRadius) / textureCircle.portion;
    float sa, ca;
    float r1 = (float) innerRadius;
    final float r2 = r1 + dr;
    c.gl.getGL2().glBegin(ImmModeSink.GL_QUAD_STRIP);
    for (var s = 0; s <= SLICE_COUNT; s++) {
      float a = s == SLICE_COUNT ? 0.0f : s * da;
      sa = (float) Math.sin(a);
      ca = (float) Math.cos(a);
      if (c.isTextureEnabled()) {
        c.gl.getGL2().glTexCoord2f(textureCircle.centerS + sa * r2 / dtc, textureCircle.centerT - ca * r2 / dtc);
      }
      c.gl.getGL2().glVertex2f(r2 * sa, r2 * ca);
      if (c.isTextureEnabled()) {
        c.gl.getGL2().glTexCoord2f(textureCircle.centerS + sa * r1 / dtc, textureCircle.centerT - ca * r1 / dtc);
      }
      c.gl.getGL2().glVertex2f(r1 * sa, r1 * ca);
    }
    c.gl.getGL2().glEnd();
  }

  public void drawCylinderSide(Context c, double baseRadius, double topRadius, double height,
                               float textureTmin, float textureTmax) {
    final float da = TAU / SLICE_COUNT;
    final float dr = (float) ((baseRadius - topRadius) / STACK_COUNT);
    final float dz = (float) (height / STACK_COUNT);
    final float nz = (float) ((topRadius - baseRadius) / height);
    final float ds = 1.0f / SLICE_COUNT;
    final float dt = (textureTmax - textureTmin) / STACK_COUNT;

    float t = textureTmin;
    float z = 0.0f;
    float r = (float) topRadius;
    float x, y;

    for (int j = 0; j < STACK_COUNT; j++) {
      float s = 0.0f;
      c.gl.getGL2().glBegin(ImmModeSink.GL_QUAD_STRIP);
      for (int i = 0; i <= SLICE_COUNT; i++) {
        if (i == SLICE_COUNT) {
          x = (float) Math.sin(0.0f);
          y = (float) Math.cos(0.0f);
        } else {
          x = (float) Math.sin((i * da));
          y = (float) Math.cos((i * da));
        }
        normal3d(c.gl, x, y, nz);
        if (c.isTextureEnabled()) {
          c.gl.getGL2().glTexCoord2f(s, t);
        }
        c.gl.getGL2().glVertex3f(x * r, y * r, z);
        normal3d(c.gl, x, y, nz);
        if (c.isTextureEnabled()) {
          c.gl.getGL2().glTexCoord2f(s, t + dt);
        }
        c.gl.getGL2().glVertex3f(x * (r + dr), y * (r + dr), z + dz);
        s += ds;
      }
      c.gl.getGL2().glEnd();
      r += dr;
      t += dt;
      z += dz;
    }
  }

  private void normal3d(final GL gl, double x, double y, double z) {
    double mag = Math.sqrt(x * x + y * y + z * z);
    if (mag > 0.00001F) {
      x /= mag;
      y /= mag;
      z /= mag;
    }
    gl.getGL2().glNormal3d(x, y, z);
  }

}
