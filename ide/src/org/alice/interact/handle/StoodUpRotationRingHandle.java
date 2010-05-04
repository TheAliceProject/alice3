/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.interact.handle;

import org.alice.interact.MovementDirection;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.property.event.PropertyEvent;
import edu.cmu.cs.dennisc.property.event.PropertyListener;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
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
	
	public StoodUpRotationRingHandle( StoodUpRotationRingHandle handle )
	{
		this(handle.rotationAxisDirection, handle.handlePosition);
		this.initFromHandle( handle );
		this.handleOffset.set( handle.handleOffset );
	}
	
	public StoodUpRotationRingHandle( MovementDirection rotationAxisDirection )
	{
		super( rotationAxisDirection );
	}
	
	public StoodUpRotationRingHandle( MovementDirection rotationAxisDirection, HandlePosition handlePosition )
	{
		super(rotationAxisDirection, handlePosition);
	}
	
	@Override
	public StoodUpRotationRingHandle clone()
	{
		StoodUpRotationRingHandle newHandle = new StoodUpRotationRingHandle(this);
		return newHandle;
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
	public void positionRelativeToObject() {
		this.setTransformation( this.getTransformationForAxis( this.rotationAxis ), this.getReferenceFrame() );
		this.setTranslationOnly( this.handleOffset, this.getReferenceFrame() );
	}

	public void propertyChanged( PropertyEvent e ) {
		this.positionRelativeToObject();		
	}

	public void propertyChanging( PropertyEvent e ) {
		// TODO Auto-generated method stub
		
	}
}
