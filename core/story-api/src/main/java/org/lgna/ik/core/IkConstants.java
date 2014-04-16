package org.lgna.ik.core;

public class IkConstants {

	//for damped
	public static final double SVD_DAMPING_CONSTANT = .1;

	//for clamped and damped
	public static final boolean USE_ADAPTIVE_TIME = true;

	public static final double ADAPTIVE_TIME_MIN_DELTA_TIME_IS_FRACTION_OF_DESIRED_DELTA_TIME = .25;

	//optional, non-internal
	public static final double DESIRED_DELTA_TIME = 0.1;
	//	public static final double MAX_LINEAR_SPEED_FOR_EE = 0.1;
	public static final double MAX_LINEAR_SPEED_FOR_EE = Double.POSITIVE_INFINITY;
	//	public static final double MAX_ANGULAR_SPEED_FOR_EE = Math.PI / 10.0;
	public static final double MAX_ANGULAR_SPEED_FOR_EE = Double.POSITIVE_INFINITY;
	public static final double MAX_PSEUDO_INVERSE_ERROR_BEFORE_HALVING_DELTA_TIME = 1e-40; //not good, dependant on the situation...

	//make it natural-ish by moving it close to default pose(s)
	public static final boolean USE_NULLSPACE_TO_MOVE_CLOSE_TO_DEFAULT_POSE = true;

	public static final double NULLSPACE_DEFAULT_POSE_MOTION_SCALE = 1;

}
