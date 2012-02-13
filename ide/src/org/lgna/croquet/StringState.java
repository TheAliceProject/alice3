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
public abstract class StringState extends State< String > {
	public class SwingModel {
		private final javax.swing.text.Document document = new javax.swing.text.PlainDocument();
		private SwingModel() {
		}
		public javax.swing.text.Document getDocument() {
			return this.document;
		}
	}

	private final SwingModel swingModel = new SwingModel();
	private final javax.swing.event.DocumentListener documentListener = new javax.swing.event.DocumentListener() {
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			try {
				javax.swing.text.Document document = e.getDocument();
				String nextValue = document.getText( 0, document.getLength() );
				boolean isAdjusting = false;
				StringState.this.changeValueFromSwing( nextValue, isAdjusting, new org.lgna.croquet.triggers.DocumentEventTrigger( e ) );
			} catch( javax.swing.text.BadLocationException ble ) {
				throw new RuntimeException( ble );
			}
		}
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}
		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}
		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}
	};

	private String textForBlankCondition;
	public StringState( Group group, java.util.UUID id, String initialValue ) {
		super( group, id, initialValue );
		try {
			this.swingModel.document.insertString( 0, initialValue, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
		this.swingModel.document.addDocumentListener( this.documentListener );
	}

	@Override
	public Iterable< ? extends PrepModel > getPotentialRootPrepModels() {
		return java.util.Collections.emptyList();
	}

	@Override
	public StringBuilder appendRepresentation( StringBuilder rv, String value, java.util.Locale locale ) {
		rv.append( value );
		return rv;
	}

	@Override
	public Class< String > getItemClass() {
		return String.class;
	}
	@Override
	public String decodeValue( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeString();
	}
	@Override
	public void encodeValue( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, String value ) {
		binaryEncoder.encode( value );
	}

	private boolean isEnabled = true;
	@Override
	public boolean isEnabled() {
		return this.isEnabled;
	}
	@Override
	public void setEnabled( boolean isEnabled ) {
		if( this.isEnabled != isEnabled ) {
			this.isEnabled = isEnabled;
			for( org.lgna.croquet.components.JComponent< ? > component : this.getComponents() ) {
				component.getAwtComponent().setEnabled( this.isEnabled );
			}
		}
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected void updateSwingModel( String nextValue ) {
		this.swingModel.document.removeDocumentListener( this.documentListener );
		try {
			this.swingModel.document.remove( 0, this.swingModel.document.getLength() );
			this.swingModel.document.insertString( 0, nextValue, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		} finally {
			this.swingModel.document.addDocumentListener( this.documentListener );
		}
	}

	@Override
	protected void localize() {
		this.textForBlankCondition = this.getLocalizedText( "textForBlankCondition" );
	}
	
	public String getTextForBlankCondition() {
		return this.textForBlankCondition;
	}

//	@Override
//	protected void commitStateEdit( String prevValue, String nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
//		org.lgna.croquet.history.TransactionManager.handleDocumentEvent( StringState.this, trigger, prevValue, nextValue );
//	}
	@Override
	protected String getActualValue() {
		try {
			return this.swingModel.document.getText( 0, this.swingModel.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public org.lgna.croquet.components.TextField createTextField() {
		return new org.lgna.croquet.components.TextField( this );
	}
	public org.lgna.croquet.components.PasswordField createPasswordField() {
		return new org.lgna.croquet.components.PasswordField( this );
	}
	public org.lgna.croquet.components.TextArea createTextArea() {
		return new org.lgna.croquet.components.TextArea( this );
	}
}
