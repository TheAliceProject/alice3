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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public enum FieldModifierFinalVolatileOrNeither {
//1.6
//	FINAL( javax.lang.model.element.Modifier.FINAL ),
//	VOLATILE( javax.lang.model.element.Modifier.VOLATILE ),
//	NEITHER();
//	private javax.lang.model.element.Modifier[] m_modifiers;
//	FieldModifierFinalVolatileOrNeither( javax.lang.model.element.Modifier... modifiers ) {
//		m_modifiers = modifiers;
//	}
//	public java.util.Set< javax.lang.model.element.Modifier > updateModifiers( java.util.Set< javax.lang.model.element.Modifier > rv ) {
//		for( javax.lang.model.element.Modifier modifier : m_modifiers ) {
//			rv.add( modifier );
//		}
//		return rv;
//	}
	FINAL( java.lang.reflect.Modifier.FINAL ),
	VOLATILE( java.lang.reflect.Modifier.VOLATILE ),
	NEITHER();
	private int[] m_modifiers;
	FieldModifierFinalVolatileOrNeither( int... modifiers ) {
		m_modifiers = modifiers;
	}
	public java.util.Set< Integer > updateModifiers( java.util.Set< Integer > rv ) {
		for( int modifier : m_modifiers ) {
			rv.add( modifier );
		}
		return rv;
	}
	
	
	//todo: rename
	public static FieldModifierFinalVolatileOrNeither get( int modifiers ) {
		if( java.lang.reflect.Modifier.isFinal( modifiers ) ) {
			return FieldModifierFinalVolatileOrNeither.FINAL;
		} else if( java.lang.reflect.Modifier.isVolatile( modifiers ) ) {
			return FieldModifierFinalVolatileOrNeither.VOLATILE;
		} else {
			return FieldModifierFinalVolatileOrNeither.NEITHER;
		}
	}
}
