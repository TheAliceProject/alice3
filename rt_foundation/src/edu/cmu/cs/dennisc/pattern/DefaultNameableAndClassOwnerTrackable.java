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
package edu.cmu.cs.dennisc.pattern;

/**
 * @author Dennis Cosgrove
 */

public class DefaultNameableAndClassOwnerTrackable extends DefaultNameable implements ClassOwnerTrackable {
	private Class<?> m_clsOwner = null;
	public Class<?> getClassOwner() {
		return m_clsOwner;
	}
	public void setClassOwner( Class<?> clsOwner ) {
		m_clsOwner = clsOwner;
	}

	@Override
	public String toString() {
		String name = getName();
		if( m_clsOwner != null ) {
			StringBuffer sb = new StringBuffer();
			sb.append( m_clsOwner.getName() );
			sb.append( "." );
			if( name != null ) {
				sb.append( name );
			} else {
				sb.append( "???" );
			}
			return sb.toString();
		} else {
			return super.toString();
		}
	}
	
	public static void setNamesAndClassOwnersForPublicStaticFinalInstancesOwnedBy( Class<?> clsOwner ) {
		for( java.lang.reflect.Field field : edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getPublicStaticFinalFields( clsOwner, DefaultNameableAndClassOwnerTrackable.class ) ) {
			DefaultNameableAndClassOwnerTrackable instance = (DefaultNameableAndClassOwnerTrackable)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, null );
			instance.setName( field.getName() );
			instance.setClassOwner( clsOwner );
		}
	}
}
