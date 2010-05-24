/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may 
 *    "Alice" appear in their name, without prior written permission of 
 *    Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must
 *    display the following acknowledgement: "This product includes software 
 *    developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is 
 *    contributed by Electronic Arts Inc. and may be used for personal, 
 *    non-commercial, and academic use only. Redistributions of any program 
 *    source code that utilizes The Sims 2 Assets must also retain the copyright
 *    notice, list of conditions and the disclaimer contained in 
 *    The Alice 3.0 Art Gallery License.
 * 
 * DISCLAIMER:
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  
 * ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT 
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A 
 * PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES 
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; 
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND 
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT 
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO 
 * THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.alice.ide.operations.ast;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractRenameNodeOperation extends edu.cmu.cs.dennisc.croquet.InputDialogOperation {
	private org.alice.ide.name.RenamePane renameNodePane;
	public AbstractRenameNodeOperation( edu.cmu.cs.dennisc.croquet.Group group, java.util.UUID individualId, String name ) {
		super( group, individualId, name );
		this.renameNodePane = new org.alice.ide.name.RenamePane();
	}
	@Override
	protected edu.cmu.cs.dennisc.croquet.Component<?> prologue(edu.cmu.cs.dennisc.croquet.Context context) {
		this.renameNodePane.setAndSelectNameText( this.getNameProperty().getValue() );
		return this.renameNodePane;
	}
	@Override
	protected void epilogue(edu.cmu.cs.dennisc.croquet.Context context, boolean isOk) {
		if( isOk ) {
			final String nextValue = this.renameNodePane.getNameText();
			final edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.getNameProperty();
			final String prevValue = nameProperty.getValue();
			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
				@Override
				public void doOrRedo( boolean isDo ) {
					nameProperty.setValue( nextValue );
				}
				@Override
				public void undo() {
					nameProperty.setValue( prevValue );
				}
				@Override
				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
					rv.append( "rename: " );
					rv.append( prevValue );
					rv.append( " ===> " );
					rv.append( nextValue );
					return rv;
				}
			} );
		} else {
			context.cancel();
		}
	}
	
	@Override
	protected java.awt.Dimension getDesiredDialogSize(edu.cmu.cs.dennisc.croquet.Dialog dialog) {
		return new java.awt.Dimension( 300, 150 );
	}
	protected abstract edu.cmu.cs.dennisc.property.StringProperty getNameProperty();
	protected abstract org.alice.ide.name.validators.NodeNameValidator getNodeNameValidator();
	
	@Override
	protected String getExplanationIfOkButtonShouldBeDisabled() {
		org.alice.ide.name.validators.NodeNameValidator nodeNameValidator = this.getNodeNameValidator();
		String rv = nodeNameValidator.getExplanationIfOkButtonShouldBeDisabled( this.renameNodePane.getNameText() );
		if( rv != null ) {
			return rv;
		} else {
			return super.getExplanationIfOkButtonShouldBeDisabled();
		}
	}
//	protected final void perform( edu.cmu.cs.dennisc.croquet.Context context, java.awt.event.ActionEvent e, edu.cmu.cs.dennisc.croquet.AbstractButton< ? > button ) {
//		org.alice.ide.name.RenamePane renameNodePane = new org.alice.ide.name.RenamePane( nodeNameValidator );
//		renameNodePane.setAndSelectNameText( nameProperty.getValue() );
//		final String nextValue = renameNodePane.showInJDialog( button );
//		if( nextValue != null && nextValue.length() > 0 ) {
//			final String prevValue = nameProperty.getValue();
//			context.commitAndInvokeDo( new org.alice.ide.ToDoEdit( context ) {
//				@Override
//				public void doOrRedo( boolean isDo ) {
//					nameProperty.setValue( nextValue );
//				}
//				@Override
//				public void undo() {
//					nameProperty.setValue( prevValue );
//				}
//				@Override
//				protected StringBuffer updatePresentation(StringBuffer rv, java.util.Locale locale) {
//					rv.append( "rename: " );
//					rv.append( prevValue );
//					rv.append( " ===> " );
//					rv.append( nextValue );
//					return rv;
//				}
//			} );
//		} else {
//			context.cancel();
//		}
//	}
}
