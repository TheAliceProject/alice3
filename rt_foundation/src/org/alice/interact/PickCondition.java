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

import org.alice.apis.moveandturn.Element;

import edu.cmu.cs.dennisc.scenegraph.Transformable;

/**
 * @author David Culyba
 */
public class PickCondition {
	
	
	
	protected boolean isNot = false;
	protected PickHint pickHint; 
	protected PickCondition nextCondition = null;
	
	public PickCondition( PickHint pickHint )
	{
		this.pickHint = pickHint;
	}
	
	public PickCondition( PickHint pickHint, PickCondition previousCondition )
	{
		this( pickHint );
		previousCondition.setNextCondition( this );
	}
	
	public void setNextCondition( PickCondition nextCondition )
	{
		this.nextCondition = nextCondition;
	}
	
	public static PickHint getPickType( edu.cmu.cs.dennisc.lookingglass.PickResult pickObject )
	{
		boolean isNull = pickObject == null || pickObject.getGeometry() == null || pickObject.getVisual() == null;
		if (isNull)
		{
			return PickHint.NOTHING;
		}
		else
		{
			return getPickType( pickObject.getVisual() );
		}
	}
	
	public static PickHint getPickType( edu.cmu.cs.dennisc.scenegraph.Component pickedObject )
	{
		if (pickedObject == null)
		{
			return PickHint.NOTHING;
		}
		else
		{
			Object bonusData = pickedObject.getBonusDataFor( PickHint.PICK_HINT_KEY );
			if (bonusData instanceof PickHint)
			{
				return (PickHint)bonusData;
			}
			else
			{
				return getPickType(pickedObject.getParent());
			}
		}
	}
	
	public boolean evaluateObject(edu.cmu.cs.dennisc.lookingglass.PickResult pickObject)
	{
		boolean result = this.pickHint.intersects( getPickType( pickObject ) );
		if (isNot)
		{
			result = !result;
		}
		return result;
	}
	
	public boolean evalutateChain(edu.cmu.cs.dennisc.lookingglass.PickResult pickObject)
	{
		if ( this.nextCondition == null )
		{
			return this.evaluateObject( pickObject );
		}
		else
		{
			return this.evaluateObject( pickObject ) && this.nextCondition.evalutateChain( pickObject ) ;
		}
	}
	
}
