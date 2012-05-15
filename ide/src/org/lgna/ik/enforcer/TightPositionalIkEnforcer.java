package org.lgna.ik.enforcer;

import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;

public class TightPositionalIkEnforcer extends IkEnforcer {
	
	public class Constraint {
		public boolean isLinear() {
			return true;//TODO
		}

		public void setEeDesiredPosition(Point3 translation) {
			// TODO Auto-generated method stub
			
		}

		public void setEeDesiredOrientation(OrthogonalMatrix3x3 orientation) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	public TightPositionalIkEnforcer(org.lgna.story.implementation.JointedModelImp<?, ?> jointedModelImp) {
		super(jointedModelImp);
	}

	public void enforceConstraints() {
		// TODO Auto-generated method stub
		
	}

	
}
