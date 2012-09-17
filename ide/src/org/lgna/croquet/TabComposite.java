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
public abstract class TabComposite<V extends org.lgna.croquet.components.View<?, ?>> extends AbstractComposite<V> {
	private String titleText;
	private javax.swing.Icon titleIcon;

	private org.lgna.croquet.BooleanState booleanState;
	//todo: remove
	private org.lgna.croquet.components.BooleanStateButton<?> button = null;

	public TabComposite( java.util.UUID id ) {
		super( id );
	}

	public java.util.UUID getTabId() {
		return this.getMigrationId();
	}

	public abstract boolean isCloseable();

	public org.lgna.croquet.components.ScrollPane createScrollPane() {
		org.lgna.croquet.components.ScrollPane rv = new org.lgna.croquet.components.ScrollPane();
		rv.setBorder( javax.swing.BorderFactory.createEmptyBorder() );
		rv.setBothScrollBarIncrements( 12, 24 );
		return rv;
	}

	@Override
	protected void localize() {
		super.localize();
		this.setTitleText( this.findDefaultLocalizedText() );
	}

	public String getTitleText() {
		return this.titleText;
	}

	protected void setTitleText( String titleText ) {
		this.titleText = titleText;
		this.updateTitleText();
	}

	public javax.swing.Icon getTitleIcon() {
		return this.titleIcon;
	}

	public void setTitleIcon( javax.swing.Icon titleIcon ) {
		this.titleIcon = titleIcon;
		this.updateTitleIcon();
	}

	private void updateTitleText() {
		if( this.button != null ) {
			this.booleanState.setTextForBothTrueAndFalse( this.getTitleText() );
		}
	}

	private void updateTitleIcon() {
		if( this.button != null ) {
			this.button.getAwtComponent().setIcon( this.getTitleIcon() );
		}
	}

	@Override
	public void appendUserRepr( StringBuilder userRepr ) {
		userRepr.append( this.getTitleText() );
	}

	@Override
	protected final void appendRepr( StringBuilder repr ) {
		this.appendUserRepr( repr );
	}

	@Override
	protected String createRepr() {
		StringBuilder sb = new StringBuilder();
		this.appendRepr( sb );
		return sb.toString();
	}

	public void customizeTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.BooleanStateButton<?> button ) {
		this.initializeIfNecessary();
		this.booleanState = booleanState;
		this.button = button;
		this.updateTitleText();
		this.updateTitleIcon();
	}

	public void releaseTitleComponent( org.lgna.croquet.BooleanState booleanState, org.lgna.croquet.components.BooleanStateButton<?> button ) {
	}
}
