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
package org.alice.ide.croquet.models.ast.rename;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractRenameNodeOperation extends org.lgna.croquet.InputDialogOperation<Void> {
	public AbstractRenameNodeOperation( org.lgna.croquet.Group group, java.util.UUID individualId ) {
		super( group, individualId );
	}
	@Override
	protected org.alice.ide.name.RenamePane prologue(org.lgna.croquet.history.InputDialogOperationStep<Void> step) {
		org.alice.ide.name.RenamePane renamePane = new org.alice.ide.name.RenamePane();
		renamePane.setAndSelectNameText( this.getNameProperty().getValue() );
		return renamePane;
	}
	@Override
	protected void epilogue(org.lgna.croquet.history.InputDialogOperationStep<Void> step, boolean isOk) {
		if( isOk ) {
			org.alice.ide.name.RenamePane renamePane = step.getMainPanel();
			final String nextValue = renamePane.getNameText();
			final edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.getNameProperty();
			final String prevValue = nameProperty.getValue();
			step.commitAndInvokeDo( new org.alice.ide.ToDoEdit( step ) {
				@Override
				protected final void doOrRedoInternal( boolean isDo ) {
					nameProperty.setValue( nextValue );
				}
				@Override
				protected final void undoInternal() {
					nameProperty.setValue( prevValue );
				}
				@Override
				protected StringBuilder updatePresentation( StringBuilder rv, java.util.Locale locale ) {
					rv.append( "rename: " );
					rv.append( prevValue );
					rv.append( " ===> " );
					rv.append( nextValue );
					return rv;
				}
			} );
		} else {
			step.cancel();
		}
	}
	
	@Override
	protected void modifyPackedDialogSizeIfDesired( org.lgna.croquet.components.Dialog dialog ) {
		dialog.setSize( 300, 150 );
	}
	protected abstract edu.cmu.cs.dennisc.property.StringProperty getNameProperty();
	protected abstract org.alice.ide.name.validators.NodeNameValidator getNodeNameValidator();
	
	@Override
	protected String getInternalExplanation( org.lgna.croquet.history.InputDialogOperationStep<Void> step ) {
		org.alice.ide.name.RenamePane renamePane = step.getMainPanel();
		org.alice.ide.name.validators.NodeNameValidator nodeNameValidator = this.getNodeNameValidator();
		String rv = nodeNameValidator.getExplanationIfOkButtonShouldBeDisabled( renamePane.getNameText() );
		if( rv != null ) {
			return rv;
		} else {
			return super.getInternalExplanation( step );
		}
	}
}
