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

import edu.cmu.cs.dennisc.codec.BinaryDecoder;
import edu.cmu.cs.dennisc.codec.BinaryEncoder;
import edu.cmu.cs.dennisc.java.util.Lists;
import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.edits.Edit;
import org.lgna.croquet.triggers.DocumentEventTrigger;
import org.lgna.croquet.views.AwtComponentView;
import org.lgna.croquet.views.ComponentManager;
import org.lgna.croquet.views.PasswordField;
import org.lgna.croquet.views.SubduedTextField;
import org.lgna.croquet.views.SwingComponentView;
import org.lgna.croquet.views.TextArea;
import org.lgna.croquet.views.TextComponent;
import org.lgna.croquet.views.TextField;

import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Dennis Cosgrove
 */
public abstract class StringState extends State<String> {
	private final class DocumentListener implements javax.swing.event.DocumentListener {
		private void handleUpdate( DocumentEvent e ) {
			if( this.ignoreCount == 0 ) {
				try {
					javax.swing.text.Document document = e.getDocument();
					String nextValue = document.getText( 0, document.getLength() );
					StringState.this.changeValueFromSwing( nextValue, DocumentEventTrigger.createUserInstance( e ).getUserActivity() );
				} catch( BadLocationException ble ) {
					throw new RuntimeException( ble );
				}
			} else {
				if( this.ignoreCount < 0 ) {
					Logger.severe( StringState.this );
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
		public void changedUpdate( DocumentEvent e ) {
			this.handleUpdate( e );
		}

		@Override
		public void insertUpdate( DocumentEvent e ) {
			this.handleUpdate( e );
		}

		@Override
		public void removeUpdate( DocumentEvent e ) {
			this.handleUpdate( e );
		}
	};

	private final DocumentListener documentListener = new DocumentListener();

	public static class SwingModel {
		private final List<TextComponent<?>> textComponents = Lists.newCopyOnWriteArrayList();
		private final javax.swing.text.Document document = new PlainDocument();

		private SwingModel() {
		}

		public void install( TextComponent<?> textComponent ) {
			this.textComponents.add( textComponent );
			textComponent.getAwtComponent().setDocument( this.document );
		}

		public void deinstall( TextComponent<?> textComponent ) {
			this.textComponents.remove( textComponent );
		}

		public Iterable<TextComponent<?>> getTextComponents() {
			return this.textComponents;
		}

		public javax.swing.text.Document getDocument() {
			return this.document;
		}
	}

	private final SwingModel swingModel = new SwingModel();

	private String textForBlankCondition;

	public StringState( Group group, UUID id, String initialValue ) {
		super( group, id, initialValue );
		try {
			this.swingModel.document.insertString( 0, initialValue, null );
		} catch( BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
		this.swingModel.document.addDocumentListener( this.documentListener );
	}

	@Override
	public List<List<PrepModel>> getPotentialPrepModelPaths( Edit edit ) {
		return Collections.emptyList();
	}

	@Override
	public void appendRepresentation( StringBuilder sb, String value ) {
		sb.append( value );
	}

	@Override
	public String decodeValue( BinaryDecoder binaryDecoder ) {
		return binaryDecoder.decodeString();
	}

	@Override
	public void encodeValue( BinaryEncoder binaryEncoder, String value ) {
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
			for( TextComponent<?> textComponent : this.swingModel.getTextComponents() ) {
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
		} catch( BadLocationException ble ) {
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
		for( SwingComponentView<?> component : ComponentManager.getComponents( this ) ) {
			if( component instanceof TextComponent<?> ) {
				TextComponent<?> textComponent = (TextComponent<?>)component;
				textComponent.updateTextForBlankCondition( this.textForBlankCondition );
			}
		}
	}

	@Override
	protected String getSwingValue() {
		try {
			return this.swingModel.document.getText( 0, this.swingModel.document.getLength() );
		} catch( BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public TextField createTextField() {
		return this.createTextField( null );
	}

	public PasswordField createPasswordField() {
		return this.createPasswordField( null );
	}

	public TextField createTextField( Operation operation ) {
		return new TextField( this, operation );
	}

	public PasswordField createPasswordField( Operation operation ) {
		return new PasswordField( this, operation );
	}

	public SubduedTextField createSubduedTextField() {
		return new SubduedTextField( this );
	}

	public TextArea createTextArea() {
		return new TextArea( this );
	}

	public void selectAll() {
		for( TextComponent<?> textComponent : this.swingModel.getTextComponents() ) {
			textComponent.selectAll();
		}
	}

	public void requestFocus() {
		for( AwtComponentView<?> component : ComponentManager.getComponents( this ) ) {
			//todo: find the most appropriate candidate?
			component.requestFocus();
		}
	}
}
