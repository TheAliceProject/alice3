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
package org.alice.ide.name;

/**
 * @author Dennis Cosgrove
 */
public abstract class NameInputPane extends edu.cmu.cs.dennisc.croquet.RowsSpringPanel {
	private edu.cmu.cs.dennisc.croquet.StringState nameState = new edu.cmu.cs.dennisc.croquet.StringState(
			edu.cmu.cs.dennisc.croquet.Application.INHERIT_GROUP,
			java.util.UUID.fromString( "482c237a-c2b3-48dc-a8e2-380edfdfffe3" ),
			""
	);
	private edu.cmu.cs.dennisc.croquet.TextField textField;
//	private javax.swing.JTextField textField = new javax.swing.JTextField( 10 );

	public void setAndSelectNameText( String text ) {
		if( text != null ) {
			this.nameState.setState( text );
			this.getTextField().selectAll();
		}
	}

	protected abstract String getExplanationIfOkButtonShouldBeDisabled( String name );

	private edu.cmu.cs.dennisc.croquet.TextField getTextField() {
		if( this.textField != null ) {
			//pass
		} else {
			this.textField = this.nameState.createTextField();
		}
		return this.textField;
	}
	public String getNameText() {
		return this.nameState.getState();
	}
//	@Override
//	public boolean isOKButtonValid() {
//		return super.isOKButtonValid() && this.isNameAcceptable( this.textField.getText() );
//	}
	@Override
	protected java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> updateComponentRows(java.util.List<edu.cmu.cs.dennisc.croquet.Component<?>[]> rv) {
//		assert this.textField != null;
//		this.textField.getDocument().addDocumentListener( new javax.swing.event.DocumentListener() {
//			private void handleUpdate( javax.swing.event.DocumentEvent e ) {
//				javax.swing.text.Document document = e.getDocument();
//				try {
//					handleNameTextChange( document.getText( 0, document.getLength() ) );
//				} catch( javax.swing.text.BadLocationException ble ) {
//					throw new RuntimeException( ble );
//				}
//			}
//			public void changedUpdate( javax.swing.event.DocumentEvent e ) {
//				this.handleUpdate( e );
//			}
//			public void insertUpdate( javax.swing.event.DocumentEvent e ) {
//				this.handleUpdate( e );
//			}
//			public void removeUpdate( javax.swing.event.DocumentEvent e ) {
//				this.handleUpdate( e );
//			}
//		} );

		rv.add( 
				edu.cmu.cs.dennisc.croquet.SpringUtilities.createRow( 
						edu.cmu.cs.dennisc.croquet.SpringUtilities.createTrailingLabel( "name:" ),
						this.getTextField()
				) 
		);
		return rv;
	}	

}
