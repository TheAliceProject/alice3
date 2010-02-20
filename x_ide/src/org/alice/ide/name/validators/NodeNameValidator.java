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

package org.alice.ide.name.validators;

public abstract class NodeNameValidator extends org.alice.ide.name.NameValidator {
	private edu.cmu.cs.dennisc.alice.ast.Node node;
	public NodeNameValidator( edu.cmu.cs.dennisc.alice.ast.Node node ) {
		this.node = node;
	}
	public edu.cmu.cs.dennisc.alice.ast.Node getNode() {
		return this.node;
	}
	@Override
	public final boolean isNameValid( String name ) {
		if( name != null ) {
			final int N = name.length();
			if( N > 0 ) {
				char c0 = name.charAt( 0 );
				if( Character.isLetter( c0 ) || c0 == '_' ) {
					for( int i=1; i<N; i++ ) {
						char cI = name.charAt( i );
						if( Character.isLetterOrDigit( cI ) || cI== '_' ) {
							//pass
						} else {
							return false;
						}
					}
					return true;
				}
			}
		}
		return false;
	}
}
