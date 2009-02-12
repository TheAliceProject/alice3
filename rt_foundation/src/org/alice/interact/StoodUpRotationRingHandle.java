/**
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */
package org.alice.interact;

import org.alice.interact.RotationRingHandle.HandlePosition;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AngleInDegrees;
import edu.cmu.cs.dennisc.math.ForwardAndUpGuide;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.print.PrintUtilities;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.StandIn;
import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class StoodUpRotationRingHandle extends RotationRingHandle implements PropertyListener{

	protected Transformable standUpReference = new Transformable();
	
	public StoodUpRotationRingHandle()
	{
		super();
	}
	
	public StoodUpRotationRingHandle( Vector3 rotationAxis )
	{
		super( rotationAxis );
	}
	
	public StoodUpRotationRingHandle( Vector3 rotationAxis, HandlePosition handlePosition )
	{
		super(rotationAxis, handlePosition);
	}
	
	@Override
	public void setManipulatedObject( Transformable manipulatedObject ) {
		if (this.manipulatedObject != manipulatedObject)
		{
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.removePropertyListener( this );
			}
			super.setManipulatedObject( manipulatedObject );
			if (this.manipulatedObject != null)
			{
				this.manipulatedObject.localTransformation.addPropertyListener( this );
			}
		}
	}
	
	@Override
	public ReferenceFrame getReferenceFrame()
	{
		if (this.manipulatedObject != null)
		{
			this.standUpReference.setParent( this.manipulatedObject );
			this.standUpReference.localTransformation.setValue( AffineMatrix4x4.createIdentity() );
			this.standUpReference.setAxesOnlyToStandUp();
			return this.standUpReference;
		}
		return this;
	}
	
	@Override
	public void positionRelativeToObject( Composite object ) {
		this.setTransformation( this.getTransformationForAxis( this.rotationAxis ), this.getReferenceFrame() );
		this.setTranslationOnly( this.handleOffset, this.getReferenceFrame() );
	}

	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject( this.getManipulatedObject() );		
	}

	public void propertyChanging( PropertyEvent e ) {
		// TODO Auto-generated method stub
		
	}
}
