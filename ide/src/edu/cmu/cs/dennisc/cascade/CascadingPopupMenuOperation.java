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
package edu.cmu.cs.dennisc.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class CascadingPopupMenuOperation extends edu.cmu.cs.dennisc.croquet.PopupMenuOperation {
	public CascadingPopupMenuOperation( java.util.UUID id ) {
		super( id );
	}
	
	private static class MenuModel extends edu.cmu.cs.dennisc.croquet.MenuModel {
		private FillIn<?> fillIn;
		public MenuModel( FillIn<?> fillIn ) {
			super( java.util.UUID.fromString( "6f3f3350-dc15-4dca-a650-f88ea484da14" ) );
			this.fillIn = fillIn;
		}
		@Override
		protected void handleMenuSelected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
			super.handleMenuSelected( e, menu );
			this.fillIn.handleMenuSelected( e, menu );
		}
		@Override
		protected void handleMenuDeselected( javax.swing.event.MenuEvent e, edu.cmu.cs.dennisc.croquet.Menu< edu.cmu.cs.dennisc.croquet.MenuModel > menu ) {
			this.fillIn.handleMenuDeselected( e, menu );
			super.handleMenuDeselected( e, menu );
		}
	}
	private MenuModel createMenuModel( FillIn< ? > fillIn ) {
		return new MenuModel( fillIn );
	}
	protected abstract edu.cmu.cs.dennisc.croquet.Operation<?> createItemOperation( FillIn< ? > fillIn );

	public edu.cmu.cs.dennisc.croquet.Model createCroquetModel( FillIn< ? > fillIn, boolean isLast ) {
		if( fillIn instanceof SeparatorFillIn ) {
			SeparatorFillIn separatorFillIn = (SeparatorFillIn)fillIn;
			String name = separatorFillIn.getName();
			javax.swing.Icon icon = separatorFillIn.getIcon();
			if( name != null || icon != null ) {
				return new edu.cmu.cs.dennisc.croquet.MenuSeparatorModel( name, icon );
			} else {
				return null;
			}
		} else {
			if( isLast ) {
				return this.createItemOperation( fillIn );
			} else {
				return this.createMenuModel( fillIn );
			}
		}
	}
	
	public abstract edu.cmu.cs.dennisc.croquet.Edit<  ? extends edu.cmu.cs.dennisc.croquet.ActionOperation > createEdit( Object value, edu.cmu.cs.dennisc.croquet.ActionOperationContext context );
	
	
	protected abstract edu.cmu.cs.dennisc.cascade.Blank getCascadeBlank();
	@Override
	protected void handlePopupMenuCreation( edu.cmu.cs.dennisc.croquet.PopupMenu popupMenu ) {
		super.handlePopupMenuCreation( popupMenu );
		edu.cmu.cs.dennisc.cascade.Blank blank = this.getCascadeBlank();
		blank.setCascadingPopupMenuOperation( this );
		java.util.List< edu.cmu.cs.dennisc.cascade.Node > children = blank.getChildren();
		for( edu.cmu.cs.dennisc.cascade.Node child : children ) {
			edu.cmu.cs.dennisc.cascade.FillIn<?> fillIn = (edu.cmu.cs.dennisc.cascade.FillIn<?>)child;
			edu.cmu.cs.dennisc.croquet.Application.addMenuElement( popupMenu, fillIn.getCroquetModel() );
		}
	}
}
