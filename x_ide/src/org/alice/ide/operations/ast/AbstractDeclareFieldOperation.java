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
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractDeclareFieldOperation extends org.alice.ide.operations.AbstractActionOperation {
	protected abstract edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getOwnerType();
	protected abstract edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createField( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType );
	public AbstractDeclareFieldOperation() {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID );
	}
	public final void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType = this.getOwnerType();
		edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field = this.createField( ownerType );
		if( field != null ) {
			class Edit extends edu.cmu.cs.dennisc.zoot.AbstractEdit {
				private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType;
				private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
				private int index;
				
				public Edit( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType, edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field, int index ) {
					this.ownerType = ownerType;
					this.field = field;
					this.index = index;
				}
				@Override
				public void doOrRedo( boolean isDo ) {
					this.ownerType.fields.add( this.index, this.field );
				}
				@Override
				public void undo() {
					if( this.ownerType.fields.get( this.index ) == this.field ) {
						this.ownerType.fields.remove( this.index );
					} else {
						throw new javax.swing.undo.CannotUndoException();
					}
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "declare:" );
					edu.cmu.cs.dennisc.alice.ast.Node.safeAppendRepr(rv, field, locale);
					return rv;
				}
			}
			int index = ownerType.fields.size();
			actionContext.commitAndInvokeDo( new Edit( ownerType, field, index ) );
		} else {
			actionContext.cancel();
		}
	}
}
