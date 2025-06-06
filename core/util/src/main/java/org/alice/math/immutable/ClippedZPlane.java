package org.alice.math.immutable;

import java.awt.Rectangle;

public record ClippedZPlane(double halfWidth, double halfHeight) {
  private static final double DEFAULT_HALF_HEIGHT = 0.1;
  public static final ClippedZPlane DEFAULT = ClippedZPlane.createWithHalfHeight(DEFAULT_HALF_HEIGHT);

  public static ClippedZPlane createWithHeight(double height) {
    return ClippedZPlane.createWithHalfHeight(height / 2.0);
  }

  public static ClippedZPlane createWithHalfHeight(double halfHeight) {
    return new ClippedZPlane(Double.NaN, halfHeight);
  }

  public ClippedZPlane withHeight(double height) {
    return new ClippedZPlane(halfWidth, height / 2.0);
  }

  public ClippedZPlane completeFrom(Rectangle viewport) {
    return completeFrom(viewport.getWidth() / viewport.getHeight());
  }

  public ClippedZPlane completeFrom(FixedRectangle viewport) {
    return completeFrom((double) viewport.width() / viewport.height());
  }

  private ClippedZPlane completeFrom(double aspectRatio) {
    if (!Double.isNaN(halfWidth) && !Double.isNaN(halfHeight)) {
      return this;
    }
    if (!Double.isNaN(halfWidth)) {
      return new ClippedZPlane(halfWidth,  halfWidth / aspectRatio);
    }
    if (!Double.isNaN(halfHeight)) {
      return new ClippedZPlane(halfHeight * aspectRatio, halfHeight);
    }
    return new ClippedZPlane(ClippedZPlane.DEFAULT_HALF_HEIGHT * aspectRatio, DEFAULT_HALF_HEIGHT);
  }

  public double getWidth() {
    return halfWidth * 2.0;
  }

  public double getHeight() {
    return halfHeight * 2.0;
  }

  public double getXMinimum() {
    return -halfWidth;
  }

  public double getXMaximum() {
    return halfWidth;
  }

  public double getYMinimum() {
    return -halfHeight;
  }

  public double getYMaximum() {
    return halfHeight;
  }

}
