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
package org.alice.ide.common;

/**
 * @author Dennis Cosgrove
 */
public class DeclarationNameLabel extends edu.cmu.cs.dennisc.croquet.Label {
	private edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration;

	private class NamePropertyAdapter implements edu.cmu.cs.dennisc.property.event.PropertyListener {
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		}
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			DeclarationNameLabel.this.updateText();
		}
	}

	private NamePropertyAdapter namePropertyAdapter = new NamePropertyAdapter();

	public DeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration ) {
		this.declaration = declaration;
		this.updateText();
		this.setForegroundColor( java.awt.Color.BLACK );
	}
	public DeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration declaration, float fontScaleFactor ) {
		this( declaration );
		this.scaleFont( fontScaleFactor );
	}
	@Override
	protected void adding() {
		super.adding();
		if( this.declaration != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.declaration.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.addPropertyListener( this.namePropertyAdapter );
			}
		}
	}
	
	@Override
	protected void removed() {
		if( this.declaration != null ) {
			edu.cmu.cs.dennisc.property.StringProperty nameProperty = this.declaration.getNamePropertyIfItExists();
			if( nameProperty != null ) {
				nameProperty.removePropertyListener( this.namePropertyAdapter );
			}
		}
		super.removed();
	}
	protected edu.cmu.cs.dennisc.alice.ast.AbstractDeclaration getDeclaration() {
		return this.declaration;
	}
	
	protected String getNameText() {
		return this.declaration.getName();
	}
	
	protected String getTextForNullName() {
		return org.alice.ide.IDE.getSingleton().getTextForNull();
	}
	protected final String getTextForBlankName() {
		return "<unset>";
	}
	private void updateText() {
		String text;
		if( this.declaration != null ) {
			text = getNameText();
		} else {
			text = null;
		}
		if( text != null ) {
			//pass
		} else {
			text = this.getTextForNullName();
		}
		if( text.length() > 0 ) {
			//pass
		} else {
			text = this.getTextForBlankName();
		}
		this.setText( text );
		this.repaint();
	}
}
