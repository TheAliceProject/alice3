package org.alice.interact.debug;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.Tuple3;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;

public class DebugSphere extends Transformable{
	private Sphere sgPickPointSphere = new Sphere();
	private Visual sgPickPointSphereVisual = new Visual();
	
	
	public DebugSphere( )
	{
		SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
		sgFrontFacingAppearance.diffuseColor.setValue( Color4f.RED );
		sgFrontFacingAppearance.opacity.setValue( new Float(1.0) );
		
		this.sgPickPointSphereVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		this.sgPickPointSphere.radius.setValue(.1d);
		this.sgPickPointSphereVisual.geometries.setValue( new Geometry[] { this.sgPickPointSphere } );
		this.sgPickPointSphereVisual.setParent( this );
		
	}
	
	public void setLocalTranslation(Tuple3 position)
	{
		AffineMatrix4x4 transform = this.localTransformation.getValue();
		transform.translation.set(position);
		this.localTransformation.setValue(transform);
	}
	
}
