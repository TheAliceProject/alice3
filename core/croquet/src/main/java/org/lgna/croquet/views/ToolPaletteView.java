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
package org.lgna.croquet.views;

class ToolPaletteLayout implements java.awt.LayoutManager {
	private final javax.swing.ButtonModel buttonModel;
	private java.awt.Component pageStartComponent;
	private java.awt.Component centerComponent;

	public ToolPaletteLayout( javax.swing.ButtonModel buttonModel ) {
		this.buttonModel = buttonModel;
	}

	@Override
	public java.awt.Dimension minimumLayoutSize( java.awt.Container parent ) {
		return new java.awt.Dimension( 0, 0 );
	}

	@Override
	public java.awt.Dimension preferredLayoutSize( java.awt.Container parent ) {
		java.awt.Dimension rv = new java.awt.Dimension( 0, 0 );
		if( this.pageStartComponent != null ) {
			java.awt.Dimension size = this.pageStartComponent.getPreferredSize();
			rv.width = Math.max( rv.width, size.width );
			rv.height += size.height;
		}
		if( this.centerComponent != null ) {
			if( this.buttonModel.isSelected() ) {
				java.awt.Dimension size = this.centerComponent.getPreferredSize();
				rv.width = Math.max( rv.width, size.width );
				rv.height += size.height;
			}
		}
		return rv;
	}

	@Override
	public void layoutContainer( java.awt.Container parent ) {
		java.awt.Dimension parentSize = parent.getSize();
		int y = 0;
		if( this.pageStartComponent != null ) {
			int height = this.pageStartComponent.getPreferredSize().height;
			this.pageStartComponent.setBounds( 0, y, parentSize.width, height );
			y += height;
		}
		if( this.centerComponent != null ) {
			if( this.buttonModel.isSelected() ) {
				this.centerComponent.setBounds( 0, y, parentSize.width, parentSize.height - y );
			} else {
				this.centerComponent.setBounds( 0, y, 0, 0 );
			}
		}
	}

	@Override
	public void addLayoutComponent( String name, java.awt.Component comp ) {
		if( java.awt.BorderLayout.PAGE_START.equals( name ) ) {
			this.pageStartComponent = comp;
		} else if( java.awt.BorderLayout.CENTER.equals( name ) ) {
			this.centerComponent = comp;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( name, comp );
		}
	}

	@Override
	public void removeLayoutComponent( java.awt.Component comp ) {
		if( this.pageStartComponent == comp ) {
			this.pageStartComponent = null;
		} else if( this.centerComponent == comp ) {
			this.centerComponent = null;
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( comp );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public final class ToolPaletteView extends Panel {
	private final org.lgna.croquet.event.ValueListener<Boolean> isCoreShowingListener = new org.lgna.croquet.event.ValueListener<Boolean>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<Boolean> e ) {
			ToolPaletteView.this.revalidateAndRepaint();
		}
	};

	private final ToolPaletteTitle title;

	public ToolPaletteView( org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite composite ) {
		super( composite );
		this.title = new ToolPaletteTitle( composite.getIsExpandedState() );
		this.internalAddComponent( this.title, java.awt.BorderLayout.PAGE_START );
		this.internalAddComponent( composite.getCoreComposite().getView(), java.awt.BorderLayout.CENTER );
	}

	@Override
	protected java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite composite = (org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		return new ToolPaletteLayout( composite.getIsExpandedState().getImp().getSwingModel().getButtonModel() );
	}

	@Override
	protected void handleDisplayable() {
		org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite composite = (org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		composite.getIsExpandedState().addAndInvokeNewSchoolValueListener( this.isCoreShowingListener );
		super.handleDisplayable();
	}

	public ToolPaletteTitle getTitle() {
		return this.title;
	}

	public CompositeView<?, ?> getCenterView() {
		org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite composite = (org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		return composite.getCoreComposite().getView();
	}

	@Override
	protected void handleUndisplayable() {
		org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite composite = (org.lgna.croquet.ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		composite.getIsExpandedState().removeNewSchoolValueListener( this.isCoreShowingListener );
		super.handleUndisplayable();
	}

	@Override
	public void setBackgroundColor( java.awt.Color color ) {
		super.setBackgroundColor( null );
		for( AwtComponentView<?> component : this.getComponents() ) {
			component.setBackgroundColor( color );
		}
	}
}
