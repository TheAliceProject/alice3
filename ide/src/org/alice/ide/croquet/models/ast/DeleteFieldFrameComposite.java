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
package org.alice.ide.croquet.models.ast;

import org.lgna.croquet.OperationInputDialogCoreComposite;
import org.lgna.croquet.PlainStringValue;
import org.lgna.croquet.StringValue;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.UserField;

/**
 * @author Matt May
 */
public class DeleteFieldFrameComposite extends OperationInputDialogCoreComposite<DeleteFieldFrameView> {

	private UserField field;
	private StringValue unableToDelete = createStringValue( this.createKey( "unableToDelete" ) );
	private StringValue because = createStringValue( this.createKey( "because" ) );
	private StringValue singularAccessReference = createStringValue( this.createKey( "singularAccessReference" ) );
	private StringValue pluralAccessReference = createStringValue( this.createKey( "pluralAccessReference" ) );
	private StringValue mustRemove = createStringValue( this.createKey( "mustRemove" ) );
	private StringValue singularThisReference = createStringValue( this.createKey( "singularThisReference" ) );
	private StringValue pluralTheseReferences = createStringValue( this.createKey( "pluralTheseReferences" ) );
	private StringValue ifYouWantToDelete = createStringValue( this.createKey( "ifYouWantToDelete" ) );
	private PlainStringValue bleh = createStringValue( createKey( "bleh" ) );
	//	private DeleteFindComposite searchFrame;
	private Integer refCount;

	public DeleteFieldFrameComposite( UserField field ) {
		super( java.util.UUID.fromString( "e6ba357c-6490-4e88-a406-ba6567a4cc71" ), null );
		this.field = field;
		java.util.List<org.lgna.project.ast.FieldAccess> references = org.alice.ide.IDE.getActiveInstance().getFieldAccesses( field );
		refCount = references.size();
		buildString();
		//		searchFrame = new DeleteFindComposite( field );
	}

	//	public static DeleteFieldFrameComposite getDialog( UserField field ) {
	//		return new DeleteFieldFrameComposite( field );
	//	}

	@Override
	protected DeleteFieldFrameView createView() {
		return new DeleteFieldFrameView( this );
	}

	public UserField getField() {
		return this.field;
	}

	public Integer getRefCount() {
		return this.refCount;
	}

	public String buildString() {
		initializeIfNecessary();
		String rv = "";
		int count = getRefCount();
		rv += unableToDelete.getText();
		rv += getField().name.getValue();
		rv += because.getText();
		if( count == 1 ) {
			rv += singularAccessReference.getText();
		} else {
			rv += count + " ";
			rv += pluralAccessReference.getText();
		}
		rv += " " + mustRemove.getText();
		if( count == 1 ) {
			rv += singularThisReference.getText();
		} else {
			rv += pluralTheseReferences.getText();
		}
		rv += " " + ifYouWantToDelete.getText();
		rv += getField().name.getValue();
		rv += "\" .";
		bleh.setText( rv );
		return rv;
	}

	public PlainStringValue getBleh() {
		return this.bleh;
	}

	//	public AbstractFindComposite getSearchFrame() {
	//		return this.searchFrame;
	//	}

	@Override
	protected Edit createEdit( CompletionStep<?> completionStep ) {
		//		searchFrame.getBooleanState().setValueTransactionlessly( true );
		return null;
	}

	@Override
	protected org.lgna.croquet.AbstractSeverityStatusComposite.Status getStatusPreRejectorCheck( CompletionStep<?> step ) {
		return null;
	}

}
