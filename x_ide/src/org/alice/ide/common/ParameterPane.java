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
package org.alice.ide.common;

import edu.cmu.cs.dennisc.alice.ast.NodeListProperty;
import edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class ParameterPane extends AccessiblePane {
	private NodeListProperty< ParameterDeclaredInAlice > parametersProperty;
	private ParameterDeclaredInAlice parameter;

	public ParameterPane( NodeListProperty< ParameterDeclaredInAlice > parametersProperty, ParameterDeclaredInAlice parameter ) {
		this.parametersProperty = parametersProperty;
		this.parameter = parameter;
		this.add( new org.alice.ide.common.NodeNameLabel( this.parameter ) );
		this.setBackground( org.alice.ide.IDE.getColorForASTClass( edu.cmu.cs.dennisc.alice.ast.ParameterAccess.class ) );
		final org.alice.ide.operations.ast.RenameParameterOperation renameParameterOperation = new org.alice.ide.operations.ast.RenameParameterOperation( this.parameter );
		
		if( this.parametersProperty != null ) {
			final org.alice.ide.operations.ast.DeleteParameterOperation deleteParameterOperation = new org.alice.ide.operations.ast.DeleteParameterOperation( this.parametersProperty, this.parameter );
			final org.alice.ide.operations.ast.ForwardShiftCodeParameterOperation forwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.ForwardShiftCodeParameterOperation( this.parametersProperty, this.parameter );
			final org.alice.ide.operations.ast.BackwardShiftCodeParameterOperation backwardShiftCodeParameterOperation = new org.alice.ide.operations.ast.BackwardShiftCodeParameterOperation( this.parametersProperty, this.parameter );
			this.setPopupOperation( new zoot.AbstractPopupActionOperation() {
				@Override
				protected java.util.List< zoot.Operation > getOperations() {
					java.util.List< zoot.Operation > rv = new java.util.LinkedList< zoot.Operation >();
					rv.add( renameParameterOperation );
					if( forwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						rv.add( forwardShiftCodeParameterOperation );
					}
					if( backwardShiftCodeParameterOperation.isIndexAppropriate() ) {
						rv.add( backwardShiftCodeParameterOperation );
					}
					rv.add( zoot.ZManager.MENU_SEPARATOR );
					rv.add( deleteParameterOperation );
					return rv;
				}
			} );
		} else {
			this.setPopupOperation( new zoot.DefaultPopupActionOperation( renameParameterOperation ) );
		}
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getExpressionType() {
		return parameter.getValueType();
	}
	@Override
	public edu.cmu.cs.dennisc.alice.ast.Expression createExpression() {
		return new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( this.parameter );
	}
}
