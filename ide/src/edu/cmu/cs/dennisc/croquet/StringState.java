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
package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public final class StringState extends Model< StringState > {
	public static interface ValueObserver {
		public void changed( String nextValue );
	};
	private java.util.List< ValueObserver > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void removeValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	private void fireValueChanged( String nextValue ) {
		for( ValueObserver valueObserver : this.valueObservers ) {
			valueObserver.changed( nextValue );
		}
	}
	
	private javax.swing.text.Document document;
	private javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
		private void handleUpdate(javax.swing.event.DocumentEvent e) {
			Application application = Application.getSingleton();
			ModelContext<?> context = application.getCurrentContext();
			StringStateContext stringStateContext;
			if( context instanceof StringStateContext ) {
				stringStateContext = (StringStateContext)context;
			} else {
				stringStateContext = context.createStringStateContext( StringState.this );
			}
			stringStateContext.handleDocumentEvent( e );
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

	public StringState(Group group, java.util.UUID individualUUID, String initialState) {
		super(group, individualUUID);
		this.document = new javax.swing.text.PlainDocument();
		try {
			this.document.insertString(0, initialState, null);
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
		this.document.addDocumentListener( this.documentListener );
	}

	public String getState() {
		try {
			return this.document.getText( 0, this.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}
	public void setState( String value ) {
		try {
			this.document.remove( 0, this.document.getLength() );
			this.document.insertString( 0, value, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	private < T extends TextComponent<?> > T register( final T rv ) {
		Application.getSingleton().register( this );
		rv.setDocument(this.document);
		rv.addContainmentObserver( new Component.ContainmentObserver() {
			public void addedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				StringState.this.addComponent( rv );
			}
			public void removedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				StringState.this.removeComponent( rv );
			}
		} );
		rv.setMargin( new java.awt.Insets( 4, 4, 2, 2 ) );
		rv.setBackgroundColor( new java.awt.Color( 255, 255, 221 ) );
		return rv;
	}
	public TextField createTextField() {
		return register( new TextField( this ) );
	}
	public TextArea createTextArea() {
		return register( new TextArea( this ) );
	}
}
