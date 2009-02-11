/*
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

package org.alice.apis.moveandturn;

import edu.cmu.cs.dennisc.alice.annotations.*;

/**
 * @author Dennis Cosgrove
 */
@ClassTemplate(isFollowToSuperClassDesired = false)
public class Element extends edu.cmu.cs.dennisc.pattern.DefaultInstancePropertyOwner {
	public static final edu.cmu.cs.dennisc.property.GetterSetterProperty< Composite > NAME_PROPERTY = new edu.cmu.cs.dennisc.property.GetterSetterProperty< Composite >( Element.class, "Name" );

	private static final String ELEMENT_KEY = "ELEMENT_KEY";

	public static Element getElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		if( sgElement != null ) {
			return (Element)sgElement.getBonusDataFor( ELEMENT_KEY );
		} else {
			return null;
		}
	}
	protected void putElement( edu.cmu.cs.dennisc.scenegraph.Element sgElement ) {
		sgElement.putBonusDataFor( ELEMENT_KEY, this );
	}

	@MethodTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	@Override
	public boolean isComposedOfGetterAndSetterProperties() {
		return true;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[name=\"" + getName() + "\"]";
	}
}
