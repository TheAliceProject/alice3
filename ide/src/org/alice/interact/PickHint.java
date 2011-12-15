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
package org.alice.interact;

import java.util.BitSet;

import edu.cmu.cs.dennisc.scenegraph.Composite;
import edu.cmu.cs.dennisc.scenegraph.Transformable;



/**
 * @author David Culyba
 */
public class PickHint extends BitSet{

	public enum PickType {
		NOTHING(0),
		MOVEABLE_OBJECT(1),
		GROUND(2),
		THREE_D_HANDLE(3),
		CAMERA(4),
		LIGHT(5),
		TWO_D_HANDLE(6),
		MARKER(7),
		ORTHOGRAPHIC(8),
		PERSPECTIVE(9)
		;
		
		public int index;
		
		private PickType( int index )
		{
			this.index = index;
		}
		
		
	}
	
	
	public static final edu.cmu.cs.dennisc.scenegraph.Element.Key< PickHint > PICK_HINT_KEY = edu.cmu.cs.dennisc.scenegraph.Element.Key.createInstance( "PICK_HINT_KEY" ); 
	
	protected static final int NUM_TYPES = PickType.values().length;
	
	
	public static final PickHint NOTHING = new PickHint( PickType.NOTHING );
	public static final PickHint THREE_D_HANDLES = new PickHint( PickType.THREE_D_HANDLE );
	public static final PickHint GROUND = new PickHint( PickType.GROUND );
	public static final PickHint LIGHT = new PickHint( PickType.LIGHT );
	public static final PickHint CAMERA = new PickHint( PickType.CAMERA );
	public static final PickHint PERSPECTIVE_CAMERA = new PickHint( PickType.CAMERA, PickType.PERSPECTIVE );
	public static final PickHint ORTHOGRAPHIC_CAMERA = new PickHint( PickType.CAMERA, PickType.ORTHOGRAPHIC );
	public static final PickHint TWO_D_HANDLES = new PickHint( PickType.TWO_D_HANDLE );
	public static final PickHint MARKERS = new PickHint( PickType.MARKER );
	public static final PickHint EVERYTHING = createEverythingHint();
	
	public static final PickHint MOVEABLE_OBJECTS = new PickHint( PickType.MOVEABLE_OBJECT, PickType.MARKER );
	
	public static final PickHint ALL_HANDLES = new PickHint( PickType.TWO_D_HANDLE, PickType.THREE_D_HANDLE );
	public static final PickHint NON_INTERACTIVE = new PickHint( PickType.NOTHING, PickType.GROUND ,PickType.LIGHT, PickType.CAMERA);
	
	public PickHint()
	{
		super(NUM_TYPES);
	}
	
	public PickHint( PickType...pickTypes )
	{
		super(NUM_TYPES);
		for (PickType pickType : pickTypes)
		{
			this.set( pickType.index );
		}
	}
	
	public static PickHint createEverythingHint()
	{
		PickHint toReturn = new PickHint();
		for (int i=0; i<NUM_TYPES; i++)
		{
			toReturn.set( i );
		}
		return toReturn;
	}
	
	public boolean get( PickType pickType )
	{
		return this.get( pickType.index );
	}
	
	public Transformable getMatchingTransformable( Composite composite )
	{
		if (composite == null)
		{
			return null;
		}
		Object pickHintObject = composite.getBonusDataFor( PickHint.PICK_HINT_KEY );
		if (pickHintObject instanceof PickHint)
		{
			PickHint pickHint = (PickHint)pickHintObject;
			if (this.intersects( pickHint ) && composite instanceof Transformable)
			{
				return (Transformable)composite;
			}
		}
		return getMatchingTransformable( composite.getParent() );
	}
	
	@Override
	public String toString() {
		if (this.intersects(NOTHING))
		{
			return "pick:NOTHING";
		}
		if (this.intersects(MOVEABLE_OBJECTS))
		{
			return "pick:MOVEABLE_OBJECTS";
		}
		if (this.intersects(THREE_D_HANDLES))
		{
			return "pick:THREE_D_HANDLES";
		}
		if (this.intersects(GROUND))
		{
			return "pick:GROUND";
		}
		if (this.intersects(LIGHT))
		{
			return "pick:LIGHT";
		}
		if (this.intersects(CAMERA))
		{
			return "pick:CAMERA";
		}
		if (this.intersects(PERSPECTIVE_CAMERA))
		{
			return "pick:PERSPECTIVE_CAMERA";
		}
		if (this.intersects(ORTHOGRAPHIC_CAMERA))
		{
			return "pick:ORTHOGRAPHIC_CAMERA";
		}
		if (this.intersects(TWO_D_HANDLES))
		{
			return "pick:TWO_D_HANDLES";
		}
		return "pick:UNKNOWN";
	}
	
}
