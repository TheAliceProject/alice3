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
		HANDLE(3),
		CAMERA(4),
		LIGHT(5),
		;
		
		public int index;
		
		private PickType( int index )
		{
			this.index = index;
		}
		
		
	}
	
	
	public static final String PICK_HINT_KEY = "PICK_HINT_KEY"; 
	
	protected static final int NUM_TYPES = PickType.values().length;
	
	
	public static final PickHint NOTHING = new PickHint( PickType.NOTHING );
	public static final PickHint MOVEABLE_OBJECTS = new PickHint( PickType.MOVEABLE_OBJECT );
	public static final PickHint HANDLES = new PickHint( PickType.HANDLE );
	public static final PickHint GROUND = new PickHint( PickType.GROUND );
	public static final PickHint LIGHT = new PickHint( PickType.LIGHT );
	public static final PickHint CAMERA = new PickHint( PickType.CAMERA );
	public static final PickHint EVERYTHING = createEverythingHint();
	
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
}
