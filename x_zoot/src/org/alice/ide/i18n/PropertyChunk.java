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

import org.alice.ide.ast.AbstractArgumentListPropertyPane;
import org.alice.ide.ast.ConstantPane;
import org.alice.ide.ast.DefaultListPropertyPane;
import org.alice.ide.ast.DefaultNodeListPropertyPane;
import org.alice.ide.ast.ExpressionListPropertyPane;
import org.alice.ide.ast.ExpressionPropertyPane;
import org.alice.ide.ast.InstancePropertyPane;
import org.alice.ide.ast.NodePropertyPane;
import org.alice.ide.ast.StatementListPropertyPane;
import org.alice.ide.ast.TypedDeclarationPane;
import org.alice.ide.ast.VariablePane;

/**
 * @author Dennis Cosgrove
 */
public class PropertyChunk extends Chunk {
	private String propertyName;
	private boolean isBonusSpecified;
	public PropertyChunk( String propertyName ) {
		this.isBonusSpecified = propertyName.startsWith( "_" ) && propertyName.endsWith( "_" );
		if( this.isBonusSpecified ) {
			this.propertyName = propertyName.substring( 1, propertyName.length()-1 );
		} else {
			this.propertyName = propertyName;
		}
	}
	public boolean isBonusSpecified() {
		return this.isBonusSpecified;
	}
	public String getPropertyName() {
		return this.propertyName;
	}
//	if( owner instanceof edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice ) {
//		rv = new VariablePane( (edu.cmu.cs.dennisc.alice.ast.VariableDeclaredInAlice)owner );
//	} else if( owner instanceof edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice ) {
//		rv = new ConstantPane( (edu.cmu.cs.dennisc.alice.ast.ConstantDeclaredInAlice)owner );
//	} else {
//		rv = new edu.cmu.cs.dennisc.moot.ZLabel( "should never happen" );
//	}
	@Override
	protected java.lang.StringBuffer updateRepr( java.lang.StringBuffer rv ) {
		rv.append( "propertyName=" );
		rv.append( this.propertyName );
		return rv;
	}
}
