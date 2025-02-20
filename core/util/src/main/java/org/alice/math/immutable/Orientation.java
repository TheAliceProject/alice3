package org.alice.math.immutable;


public interface Orientation {
    boolean isNaN();
    boolean isIdentity();
    boolean isAlignedWith(Orientation other);
    OrthogonalMatrix3x3 asMatrix3x3();
    UnitQuaternion asUnitQuaternion();
    AxisRotation asAxisRotation();
    EulerAngles asEulerAngles();
    // This one was not used
    ForwardAndUpGuide asForwardAndUpGuide();

    // Temporary use during transition to immutable Records
    @Deprecated(forRemoval = true)
    edu.cmu.cs.dennisc.math.Orientation mutable();
}
