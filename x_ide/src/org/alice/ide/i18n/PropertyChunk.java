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
package org.alice.ide.i18n;

/**
 * @author Dennis Cosgrove
 */
public class PropertyChunk extends Chunk {
	private String propertyName;
	private int underscoreCount;
	public PropertyChunk( String propertyName ) {
		
		if( propertyName.startsWith( "__" ) && propertyName.endsWith( "__" ) ) {
			this.underscoreCount = 2;
		} else if( propertyName.startsWith( "_" ) && propertyName.endsWith( "_" ) ) {
			this.underscoreCount = 1;
		} else {
			this.underscoreCount = 0;
		}
		this.propertyName = propertyName.substring( this.underscoreCount, propertyName.length()-this.underscoreCount );
	}
	public int getUnderscoreCount() {
		return this.underscoreCount;
	}
	public String getPropertyName() {
		return this.propertyName;
	}
//	if( owner instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
//		rv = new VariablePane( (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)owner );
//	} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
//		rv = new ConstantPane( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)owner );
//	} else {
//		rv = zoot.ZLabel.acquire( "should never happen" );
//	}
	@Override
	protected StringBuffer updateRepr( StringBuffer rv ) {
		rv.append( "propertyName=" );
		rv.append( this.propertyName );
		return rv;
	}
}
