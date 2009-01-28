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
package sceneeditor;

import edu.cmu.cs.dennisc.color.Color4f;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.property.event.AddListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent;
import edu.cmu.cs.dennisc.property.event.SetListPropertyEvent;
import edu.cmu.cs.dennisc.scenegraph.AsSeenBy;
import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Geometry;
import edu.cmu.cs.dennisc.scenegraph.ReferenceFrame;
import edu.cmu.cs.dennisc.scenegraph.SingleAppearance;
import edu.cmu.cs.dennisc.scenegraph.Sphere;
import edu.cmu.cs.dennisc.scenegraph.Torus;
import edu.cmu.cs.dennisc.scenegraph.Transformable;
import edu.cmu.cs.dennisc.scenegraph.Visual;
import edu.cmu.cs.dennisc.scenegraph.bound.BoundUtilities;
import edu.cmu.cs.dennisc.scenegraph.bound.CumulativeBound;

/**
 * @author David Culyba
 */
public abstract class ManipulationHandle extends Transformable {

	protected Visual sgVisual = new Visual();
	protected SingleAppearance sgFrontFacingAppearance = new SingleAppearance();
	protected Transformable manipulatedObject;
	
	/**
	 * @param manipulatedObject the manipulatedObject to set
	 */
	public void setManipulatedObject( Transformable manipulatedObject ) {
		if (this.manipulatedObject != manipulatedObject)
		{
			this.manipulatedObject = manipulatedObject;
			this.setParent( manipulatedObject );
			this.positionRelativeToObject( this.manipulatedObject );
			this.resizeRelativeToObject( this.manipulatedObject );
		}
	}

	public ManipulationHandle( )
	{
		sgFrontFacingAppearance.diffuseColor.setValue( Color4f.YELLOW );
		sgVisual.frontFacingAppearance.setValue( sgFrontFacingAppearance );
		sgVisual.setParent( this );
		this.setParent( null );
		this.putBonusDataFor( PickHint.PICK_HINT_KEY, PickHint.HANDLES );
	}

	public Visual getSGVisual() {
		return sgVisual;
	}
	public SingleAppearance getSGFrontFacingAppearance() {
		return sgFrontFacingAppearance;
	}
	
	public Transformable getManipulatedObject()
	{
		return (Transformable)this.getParent();
	}
	
	@Override
	public void setName( String name ) {
		super.setName( name );
		sgVisual.setName( name + ".sgVisual" );
		sgFrontFacingAppearance.setName( name + ".sgFrontFacingAppearance" );
	}
	
	public ReferenceFrame getReferenceFrame()
	{
		return this.getManipulatedObject();
	}
	
	abstract public void positionRelativeToObject( Composite object );
	abstract public void resizeRelativeToObject( Composite object );
	
	
	public void show()
	{
	}
	
	public void hide()
	{
		
	}

}
