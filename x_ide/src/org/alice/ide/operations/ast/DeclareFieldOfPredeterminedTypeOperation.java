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
public class DeclareFieldOfPredeterminedTypeOperation extends AbstractNonGalleryDeclareFieldOperation {
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType;
	private edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType;
	public DeclareFieldOfPredeterminedTypeOperation( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType ) {
		this.ownerType = ownerType;
		this.valueType = valueType;
		this.putValue( javax.swing.Action.SMALL_ICON, new org.alice.ide.common.TypeIcon( this.valueType ) );
		this.putValue( javax.swing.Action.NAME, "Declare New Instance..." );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice getOwnerType() {
		return this.ownerType;
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice createField(edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType) {
		if( this.getIDE().isDeclareFieldOfPredeterminedTypeSupported( ownerType ) ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
			org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane createFieldPane = new org.alice.ide.createdeclarationpanes.CreateFieldFromGalleryPane( ownerType, typeInJava.getClassReflectionProxy().getReification() );
			return createFieldPane.showInJDialog( this.getIDE(), "Create New Instance", true );
		} else {
			return null;
		}
	}
}
