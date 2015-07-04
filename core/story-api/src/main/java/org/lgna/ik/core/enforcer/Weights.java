package org.lgna.ik.core.enforcer;

import java.util.HashMap;
import java.util.Map;

import org.lgna.story.resources.JointId;

public class Weights {

	public void setDefaultJointWeight( double defaultJointWeight ) {
		this.defaultJointWeight = defaultJointWeight;
	}

	public void setJointWeight( JointId jointId, double weight ) {
		jointWeights.put( jointId, weight );
	}

	public double getEffectiveJointWeight( JointId jointId ) {
		if( jointWeights.containsKey( jointId ) ) {
			return jointWeights.get( jointId );
		} else {
			return defaultJointWeight;
		}
	}

	Map<JointId, Double> jointWeights = new HashMap<JointId, Double>();
	double defaultJointWeight = 1.0;

}
