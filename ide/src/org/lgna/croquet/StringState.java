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
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public class StringState extends State<String> {
	private javax.swing.text.Document document;
	private javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			try {
				javax.swing.text.Document document = e.getDocument();
				String nextValue = document.getText( 0, document.getLength() );
				String prevValue = StringState.this.previousValue;
				boolean isAdjusting = false;
				fireChanging( prevValue, nextValue, isAdjusting );
				org.lgna.croquet.history.TransactionManager.handleDocumentEvent( StringState.this, e, prevValue, nextValue );
				StringState.this.previousValue = nextValue;
				fireChanged( prevValue, nextValue, isAdjusting );
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}
		public void changedUpdate(javax.swing.event.DocumentEvent e) {
			this.handleUpdate(e);
		}
		public void insertUpdate(javax.swing.event.DocumentEvent e) {
			this.handleUpdate(e);
		}
		public void removeUpdate(javax.swing.event.DocumentEvent e) {
			this.handleUpdate(e);
		}
	};

	private String previousValue;
	public StringState(Group group, java.util.UUID id, String initialState) {
		super(group, id);
		this.previousValue = initialState;
		this.document = new javax.swing.text.PlainDocument();
		try {
			this.document.insertString(0, initialState, null);
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
		this.document.addDocumentListener( this.documentListener );
	}

	@Override
	protected void localize() {
	}

	@Override
	public String getValue() {
		try {
			return this.document.getText( 0, this.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	public void setValue( String value ) {
		try {
			this.document.remove( 0, this.document.getLength() );
			this.document.insertString( 0, value, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	
	@Override
	protected java.lang.StringBuilder updateTutorialStepText( java.lang.StringBuilder rv, org.lgna.croquet.history.Step< ? > step, org.lgna.croquet.edits.Edit< ? > edit, org.lgna.croquet.UserInformation userInformation ) {
		if( edit instanceof org.lgna.croquet.edits.StringStateEdit ) {
			org.lgna.croquet.edits.StringStateEdit stringStateEdit = (org.lgna.croquet.edits.StringStateEdit)edit;
			rv.append( "Enter <strong>" );
			rv.append( stringStateEdit.getNextValue() );
			rv.append( "</strong>" );
		} else {
			rv = super.updateTutorialStepText( rv, step, edit, userInformation );
		}
		return rv;
	}

	public javax.swing.text.Document getDocument() {
		return this.document;
	}
	public org.lgna.croquet.components.TextField createTextField() {
		return new org.lgna.croquet.components.TextField( this );
	}
	public org.lgna.croquet.components.TextArea createTextArea() {
		return new org.lgna.croquet.components.TextArea( this );
	}
}
