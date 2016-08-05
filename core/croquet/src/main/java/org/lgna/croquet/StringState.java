/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package org.lgna.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class StringState extends SimpleValueState<String> {
	private final class DocumentListener implements javax.swing.event.DocumentListener {
		private void handleUpdate( javax.swing.event.DocumentEvent e ) {
			if( this.ignoreCount == 0 ) {
				try {
					javax.swing.text.Document document = e.getDocument();
					String nextValue = document.getText( 0, document.getLength() );
					StringState.this.changeValueFromSwing( nextValue, IsAdjusting.FALSE, org.lgna.croquet.triggers.DocumentEventTrigger.createUserInstance( e ) );
				} catch( javax.swing.text.BadLocationException ble ) {
					throw new RuntimeException( ble );
				}
			} else {
				if( this.ignoreCount < 0 ) {
					edu.cmu.cs.dennisc.java.util.logging.Logger.severe( StringState.this );
				}
			}
		}

		private int ignoreCount = 0;

		public void pushIgnore() {
			this.ignoreCount++;
		}

		public void popIgnore() {
			this.ignoreCount--;
		}

		@Override
		public void changedUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}

		@Override
		public void insertUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}

		@Override
		public void removeUpdate( javax.swing.event.DocumentEvent e ) {
			this.handleUpdate( e );
		}
	};

	private final DocumentListener documentListener = new DocumentListener();

	public static class SwingModel {
		private final java.util.List<org.lgna.croquet.views.TextComponent<?>> textComponents = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
		private final javax.swing.text.Document document = new javax.swing.text.PlainDocument();

		private SwingModel() {
		}

		public void install( org.lgna.croquet.views.TextComponent<?> textComponent ) {
			this.textComponents.add( textComponent );
			textComponent.getAwtComponent().setDocument( this.document );
		}

		public void deinstall( org.lgna.croquet.views.TextComponent<?> textComponent ) {
			this.textComponents.remove( textComponent );
		}

		public Iterable<org.lgna.croquet.views.TextComponent<?>> getTextComponents() {
			return this.textComponents;
		}

		public javax.swing.text.Document getDocument() {
			return this.document;
		}
	}

	private final SwingModel swingModel = new SwingModel();

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
	public java.util.List<java.util.List<PrepModel>> getPotentialPrepModelPaths( org.lgna.croquet.edits.Edit edit ) {
		return java.util.Collections.emptyList();
	}

	@Override
	public void appendRepresentation( StringBuilder sb, String value ) {
		sb.append( value );
	}

	@Override
	public Class<String> getItemClass() {
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
			for( org.lgna.croquet.views.TextComponent<?> textComponent : this.swingModel.getTextComponents() ) {
				textComponent.getAwtComponent().setEnabled( this.isEnabled );
			}
		}
	}

	public SwingModel getSwingModel() {
		return this.swingModel;
	}

	@Override
	protected void setSwingValue( String nextValue ) {
		this.documentListener.pushIgnore();
		try {
			this.swingModel.document.remove( 0, this.swingModel.document.getLength() );
			this.swingModel.document.insertString( 0, nextValue, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		} finally {
			this.documentListener.popIgnore();
		}
	}

	@Override
	protected void localize() {
		this.textForBlankCondition = this.findLocalizedText( "textForBlankCondition" );
	}

	public String getTextForBlankCondition() {
		return this.textForBlankCondition;
	}

	public void setTextForBlankCondition( String textForBlankCondition ) {
		this.textForBlankCondition = textForBlankCondition;
		for( org.lgna.croquet.views.SwingComponentView<?> component : org.lgna.croquet.views.ComponentManager.getComponents( this ) ) {
			if( component instanceof org.lgna.croquet.views.TextComponent<?> ) {
				org.lgna.croquet.views.TextComponent<?> textComponent = (org.lgna.croquet.views.TextComponent<?>)component;
				textComponent.updateTextForBlankCondition( this.textForBlankCondition );
			}
		}
	}

	//	@Override
	//	protected void commitStateEdit( String prevValue, String nextValue, boolean isAdjusting, org.lgna.croquet.triggers.Trigger trigger ) {
	//		org.lgna.croquet.history.TransactionManager.handleDocumentEvent( StringState.this, trigger, prevValue, nextValue );
	//	}
	@Override
	protected String getSwingValue() {
		try {
			return this.swingModel.document.getText( 0, this.swingModel.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public org.lgna.croquet.views.TextField createTextField() {
		return this.createTextField( null );
	}

	public org.lgna.croquet.views.PasswordField createPasswordField() {
		return this.createPasswordField( null );
	}

	public org.lgna.croquet.views.TextField createTextField( Operation operation ) {
		return new org.lgna.croquet.views.TextField( this, operation );
	}

	public org.lgna.croquet.views.PasswordField createPasswordField( Operation operation ) {
		return new org.lgna.croquet.views.PasswordField( this, operation );
	}

	public org.lgna.croquet.views.SubduedTextField createSubduedTextField() {
		return new org.lgna.croquet.views.SubduedTextField( this );
	}

	public org.lgna.croquet.views.TextArea createTextArea() {
		return new org.lgna.croquet.views.TextArea( this );
	}

	public void selectAll() {
		for( org.lgna.croquet.views.TextComponent<?> textComponent : this.swingModel.getTextComponents() ) {
			textComponent.selectAll();
		}
	}

	public void requestFocus() {
		for( org.lgna.croquet.views.AwtComponentView<?> component : org.lgna.croquet.views.ComponentManager.getComponents( this ) ) {
			//todo: find the most appropriate candidate?
			component.requestFocus();
		}
	}
}
