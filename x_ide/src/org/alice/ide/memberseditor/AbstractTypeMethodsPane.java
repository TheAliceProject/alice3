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
package org.alice.ide.memberseditor;

/**
 * @author Dennis Cosgrove
 */
abstract class AbstractTypeMethodsPane extends AbstractTypeMembersPane {
	public AbstractTypeMethodsPane( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super( type );
	}
	protected abstract javax.swing.JComponent createProcedureTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method );
	protected abstract javax.swing.JComponent createFunctionTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractMethod method );

	@Override
	protected java.awt.Component[] createTemplates( edu.cmu.cs.dennisc.alice.ast.AbstractMember member ) {
		javax.swing.JComponent component;
		if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractMethod ) {
			edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = (edu.cmu.cs.dennisc.alice.ast.AbstractMethod)member;
			if( method.getNextShorterInChain() != null ) {
				component = null;
			} else {
				if( method.isProcedure() ) {
					component = createProcedureTemplate( method );
				} else if( method.isFunction() ) {
					component = createFunctionTemplate( method );
				} else {
					component = null;
				}
			}
		} else {
			component = null;
		}
		java.awt.Component[] rv;
		if( component != null ) {
			//line.add( javax.swing.Box.createHorizontalStrut( INDENT ) );
			//if( member.isDeclaredInAlice() ) {
			if( getIDE().isEmphasizingClasses() ) {
				//pass
			} else {
				if( member instanceof edu.cmu.cs.dennisc.alice.ast.AbstractCode ) {
					edu.cmu.cs.dennisc.alice.ast.AbstractCode code = (edu.cmu.cs.dennisc.alice.ast.AbstractCode)member;
					if( code.isDeclaredInAlice() ) {
						edu.cmu.cs.dennisc.croquet.LineAxisPane line = new edu.cmu.cs.dennisc.croquet.LineAxisPane();
						edu.cmu.cs.dennisc.zoot.ZButton editButton = new edu.cmu.cs.dennisc.zoot.ZButton( new org.alice.ide.operations.ast.FocusCodeOperation( code ) );
						line.add( editButton );
						line.add( component );
						component = line;
					}
				}
			}
			rv = new java.awt.Component[] { component };
		} else {
			rv = null;
		}
		return rv;
	}
}
