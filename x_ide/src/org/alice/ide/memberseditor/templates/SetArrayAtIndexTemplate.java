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
package org.alice.ide.memberseditor.templates;

/**
 * @author Dennis Cosgrove
 */
public class SetArrayAtIndexTemplate extends ExpressionStatementTemplate {
	private edu.cmu.cs.dennisc.alice.ast.AbstractField field;
	public SetArrayAtIndexTemplate( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		this.field = field;
		if( this.field instanceof edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice ) {
			edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice fieldInAlice = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice)this.field;
			this.setPopupOperation( new FieldPopupOperation( fieldInAlice ) );
		}
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createIncompleteExpression() {
		return new edu.cmu.cs.dennisc.alice.ast.AssignmentExpression( 
				this.field.getDesiredValueType().getComponentType(), 
				new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( 
						field.getValueType(), 
						org.alice.ide.ast.NodeUtilities.createIncompleteFieldAccess( this.field ), 
						new org.alice.ide.ast.EmptyExpression( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE ) 
				), 
				edu.cmu.cs.dennisc.alice.ast.AssignmentExpression.Operator.ASSIGN, 
				new org.alice.ide.ast.EmptyExpression( this.field.getDesiredValueType().getComponentType() )
		);
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType[] getBlankExpressionTypes() {
		return new edu.cmu.cs.dennisc.alice.ast.AbstractType[] { edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.INTEGER_OBJECT_TYPE, this.field.getDesiredValueType().getComponentType() };
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.Expression createExpression( edu.cmu.cs.dennisc.alice.ast.Expression... expressions ) {
		edu.cmu.cs.dennisc.alice.ast.AssignmentExpression rv = new edu.cmu.cs.dennisc.alice.ast.AssignmentExpression(
				field.getValueType().getComponentType(), 
				new edu.cmu.cs.dennisc.alice.ast.ArrayAccess( 
						field.getValueType(), 
						org.alice.ide.ast.NodeUtilities.createFieldAccess( 
								getIDE().createInstanceExpression(), 
								field ), 
						expressions[ 0 ]
				), 
				edu.cmu.cs.dennisc.alice.ast.AssignmentExpression.Operator.ASSIGN, 
				expressions[ 1 ]
		);
		return rv;
	}
}
