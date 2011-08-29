package org.lgna.story.implementation.sims2;

import org.lgna.story.implementation.sims2.JointImplementation;
import org.lgna.story.implementation.VehicleImplementation;
import org.lgna.story.resources.JointId;

public class SimsVehicleImplementation extends VehicleImplementation {
	
	private final edu.cmu.cs.dennisc.nebulous.Model nebModel;
	
	public SimsVehicleImplementation( org.lgna.story.Vehicle abstraction, String vehicleName)
	{
		super( abstraction, new edu.cmu.cs.dennisc.scenegraph.Visual() );
		try{
			this.nebModel = new edu.cmu.cs.dennisc.nebulous.Model( vehicleName );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			throw new RuntimeException( lre );
		}
		this.getSgVisuals()[ 0 ].geometries.setValue( new edu.cmu.cs.dennisc.scenegraph.Geometry[] { this.nebModel } );
	}
	
	public SimsVehicleImplementation( org.lgna.story.Vehicle abstraction, String vehicleName, String textureName)
	{
		this(abstraction, vehicleName);
		if (textureName != null)
		{
			setTexture(textureName);
		}
	}
	
	public void setTexture( String textureName ) {
		this.nebModel.setTexture(textureName);
	}
	
	@Override
	protected JointImplementation createJointImplementation(JointId jointId) {
		return new JointImplementation( this, new NebulousJoint( this.nebModel, jointId ) );
	}

}
