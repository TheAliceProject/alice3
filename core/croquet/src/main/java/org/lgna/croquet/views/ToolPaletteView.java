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

import edu.cmu.cs.dennisc.java.util.logging.Logger;
import org.lgna.croquet.ToolPaletteCoreComposite;
import org.lgna.croquet.event.ValueEvent;
import org.lgna.croquet.event.ValueListener;

import javax.swing.ButtonModel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

class ToolPaletteLayout implements LayoutManager {
	private final ButtonModel buttonModel;
	private Component pageStartComponent;
	private Component centerComponent;

	public ToolPaletteLayout( ButtonModel buttonModel ) {
		this.buttonModel = buttonModel;
	}

	@Override
	public Dimension minimumLayoutSize( Container parent ) {
		return new Dimension( 0, 0 );
	}

	@Override
	public Dimension preferredLayoutSize( Container parent ) {
		Dimension rv = new Dimension( 0, 0 );
		if( this.pageStartComponent != null ) {
			Dimension size = this.pageStartComponent.getPreferredSize();
			rv.width = Math.max( rv.width, size.width );
			rv.height += size.height;
		}
		if( this.centerComponent != null ) {
			if( this.buttonModel.isSelected() ) {
				Dimension size = this.centerComponent.getPreferredSize();
				rv.width = Math.max( rv.width, size.width );
				rv.height += size.height;
			}
		}
		return rv;
	}

	@Override
	public void layoutContainer( Container parent ) {
		Dimension parentSize = parent.getSize();
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
	public void addLayoutComponent( String name, Component comp ) {
		if( BorderLayout.PAGE_START.equals( name ) ) {
			this.pageStartComponent = comp;
		} else if( BorderLayout.CENTER.equals( name ) ) {
			this.centerComponent = comp;
		} else {
			Logger.severe( name, comp );
		}
	}

	@Override
	public void removeLayoutComponent( Component comp ) {
		if( this.pageStartComponent == comp ) {
			this.pageStartComponent = null;
		} else if( this.centerComponent == comp ) {
			this.centerComponent = null;
		} else {
			Logger.severe( comp );
		}
	}
}

/**
 * @author Dennis Cosgrove
 */
public final class ToolPaletteView extends Panel {
	private final ValueListener<Boolean> isCoreShowingListener = new ValueListener<Boolean>() {
		@Override
		public void valueChanged( ValueEvent<Boolean> e ) {
			ToolPaletteView.this.revalidateAndRepaint();
		}
	};

	private final ToolPaletteTitle title;

	public ToolPaletteView( ToolPaletteCoreComposite.OuterComposite composite ) {
		super( composite );
		this.title = new ToolPaletteTitle( composite.getIsExpandedState() );
		this.internalAddComponent( this.title, BorderLayout.PAGE_START );
		this.internalAddComponent( composite.getCoreComposite().getView(), BorderLayout.CENTER );
	}

	@Override
	protected LayoutManager createLayoutManager( JPanel jPanel ) {
		ToolPaletteCoreComposite.OuterComposite composite = (ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		return new ToolPaletteLayout( composite.getIsExpandedState().getImp().getSwingModel().getButtonModel() );
	}

	@Override
	protected void handleDisplayable() {
		ToolPaletteCoreComposite.OuterComposite composite = (ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		composite.getIsExpandedState().addAndInvokeNewSchoolValueListener( this.isCoreShowingListener );
		super.handleDisplayable();
	}

	public ToolPaletteTitle getTitle() {
		return this.title;
	}

	public CompositeView<?, ?> getCenterView() {
		ToolPaletteCoreComposite.OuterComposite composite = (ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		return composite.getCoreComposite().getView();
	}

	@Override
	protected void handleUndisplayable() {
		ToolPaletteCoreComposite.OuterComposite composite = (ToolPaletteCoreComposite.OuterComposite)this.getComposite();
		composite.getIsExpandedState().removeNewSchoolValueListener( this.isCoreShowingListener );
		super.handleUndisplayable();
	}

	@Override
	public void setBackgroundColor( Color color ) {
		super.setBackgroundColor( null );
		for( AwtComponentView<?> component : this.getComponents() ) {
			component.setBackgroundColor( color );
		}
	}
}
