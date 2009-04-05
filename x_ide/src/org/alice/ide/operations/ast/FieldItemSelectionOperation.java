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
public class FieldItemSelectionOperation extends org.alice.ide.operations.AbstractItemSelectionOperation< edu.cmu.cs.dennisc.alice.ast.AbstractField > {
	private edu.cmu.cs.dennisc.alice.ast.AbstractField nextField;
	private edu.cmu.cs.dennisc.alice.ast.AbstractField prevField;
	public FieldItemSelectionOperation( javax.swing.ComboBoxModel comboBoxModel ) {
		super( comboBoxModel );
	}
	public void performSelectionChange( zoot.ItemSelectionContext< edu.cmu.cs.dennisc.alice.ast.AbstractField > singleSelectionContext ) {
//		if( singleSelectionContext.isPreviousSelectionValid() ) {
			this.prevField = singleSelectionContext.getPreviousSelection();
//		} else {
//			//todo?
//		}
		this.nextField = singleSelectionContext.getNextSelection();
		this.redo();
		singleSelectionContext.commit();
	}
	
	public void redo() {
		getIDE().setFieldSelection( this.nextField );
	}
	public void undo() {
		getIDE().setFieldSelection( this.prevField );
	}
}
