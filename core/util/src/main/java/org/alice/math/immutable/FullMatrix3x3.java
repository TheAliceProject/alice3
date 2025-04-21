package org.alice.math.immutable;

import edu.cmu.cs.dennisc.codec.BinaryEncoder;

public record FullMatrix3x3(Vector3 right, Vector3 up, Vector3 backward) implements Matrix3x3 {
  //<editor-fold desc="Accessors">
  @Override
  public Vector3 getRight() {
    return right;
  }

  @Override
  public Vector3 getUp() {
    return up;
  }

  @Override
  public Vector3 getBackward() {
    return backward;
  }
  //</editor-fold>

  @Override
  public void encode(BinaryEncoder binaryEncoder) {
    binaryEncoder.encodeRecord(this);
  }
}
