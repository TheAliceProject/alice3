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
public abstract class CascadeMenuModel< FB > extends CascadeBlankOwner< FB, FB > {
	private class InternalBlank extends CascadeBlank< FB > {
		public InternalBlank() {
			super(java.util.UUID.fromString( "2f562397-a298-46da-bf8d-01a4bb86da3a" ) );
		}
		@Override
		protected java.util.List< org.lgna.croquet.CascadeBlankChild > updateChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< FB > blankNode ) {
			CascadeMenuModel.this.updateBlankChildren( rv, blankNode );
			return rv;
		}
	}
	private final InternalBlank blank = new InternalBlank();
	private String menuItemText;
	public CascadeMenuModel( java.util.UUID id ) {
		super( id );
		this.addBlank( this.blank );
	}
	@Override
	protected void localize() {
		super.localize();
		this.menuItemText = this.findDefaultLocalizedText();
	}
	protected abstract java.util.List< org.lgna.croquet.CascadeBlankChild > updateBlankChildren( java.util.List< org.lgna.croquet.CascadeBlankChild > rv, org.lgna.croquet.cascade.BlankNode< FB > blankNode );
	private org.lgna.croquet.cascade.AbstractItemNode< FB,FB,? > getSelectedFillInContext( org.lgna.croquet.cascade.ItemNode< ? super FB,? > itemNode ) {
		org.lgna.croquet.cascade.BlankNode< ? > blankNode = itemNode.getBlankStepAt( 0 );
		return blankNode.getSelectedFillInContext();
	}
	@Override
	public FB getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super FB,FB > itemNode ) {
		return this.getSelectedFillInContext( itemNode ).getTransientValue();
	}
	@Override
	public FB createValue( org.lgna.croquet.cascade.ItemNode< ? super FB,FB > itemNode, org.lgna.croquet.history.TransactionHistory transactionHistory ) {
		return this.getSelectedFillInContext( itemNode ).createValue( transactionHistory );
	}
	protected boolean isBackedByIconProxy() {
		return true;
	}
	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super FB,FB > itemNode ) {
		return new javax.swing.JLabel( this.menuItemText );
	}
	@Override
	public java.lang.String getMenuItemText( org.lgna.croquet.cascade.ItemNode<? super FB,FB> node ) {
		if( this.isBackedByIconProxy() ) {
			return super.getMenuItemText( node );
		} else {
			return this.menuItemText;
		}
	}
	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode<? super FB,FB> node ) {
		if( this.isBackedByIconProxy() ) {
			return super.getMenuItemIcon( node );
		} else {
			return null;
		}
	}
}
