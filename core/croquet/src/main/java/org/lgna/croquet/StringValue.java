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
public abstract class StringValue extends AbstractElement {
	private String originalLocalizedText;
	private final javax.swing.text.AbstractDocument document;

	public StringValue( java.util.UUID id, javax.swing.text.AbstractDocument document ) {
		super( id );
		this.document = document;
	}

	@Override
	protected void localize() {
		this.originalLocalizedText = this.findDefaultLocalizedText();
		this.setText( this.modifiedLocalizedTextIfAppropriate( this.originalLocalizedText ) );
	}

	protected String modifiedLocalizedTextIfAppropriate( String originalLocalizedText ) {
		return originalLocalizedText;
	}

	public String getOriginalLocalizedText() {
		this.initializeIfNecessary();
		return this.originalLocalizedText;
	}

	public javax.swing.text.AbstractDocument getDocument() {
		return this.document;
	}

	public String getText() {
		try {
			return this.document.getText( 0, this.document.getLength() );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( ble );
		}
	}

	public final void setText( String text ) {
		try {
			this.document.replace( 0, this.document.getLength(), text, null );
		} catch( javax.swing.text.BadLocationException ble ) {
			throw new RuntimeException( text, ble );
		}
	}

	public org.lgna.croquet.views.AbstractLabel createLabel( float fontScalar, edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return new org.lgna.croquet.views.DocumentLabel( this.document, fontScalar, textAttributes );
	}

	public org.lgna.croquet.views.AbstractLabel createLabel( edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes ) {
		return this.createLabel( 1.0f, textAttributes );
	}
}
