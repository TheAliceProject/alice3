package org.lgna.story.implementation.sims2;

import org.lgna.story.implementation.sims2.JointImplementation;
import org.lgna.story.implementation.VehicleImplementation;
import org.lgna.story.resources.JointId;

public class SimsJointedModelImplementation extends org.lgna.story.implementation.JointedModelImplementation {
	
	private final org.lgna.story.Model abstraction;
	private final edu.cmu.cs.dennisc.nebulous.Model nebModel;
	
	public SimsJointedModelImplementation( org.lgna.story.Model abstraction, String modelName, String textureName, org.lgna.story.resources.JointId[] rootJointIds )
	{
		super( new edu.cmu.cs.dennisc.scenegraph.Visual(), rootJointIds );
		try{
			this.nebModel = new edu.cmu.cs.dennisc.nebulous.Model( modelName, textureName );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
		}
		this.abstraction = abstraction;
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.nebModel } );
	}
	
	@Override
	public org.lgna.story.Model getAbstraction() {
		return this.abstraction;
	}
	
	@Override
	protected JointImplementation createJointImplementation(JointId jointId) {
		return new JointImplementation( this, new NebulousJoint( this.nebModel, jointId ) );
	}

}
