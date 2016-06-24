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

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSplitPane<SC extends org.lgna.croquet.AbstractSplitComposite<?>> extends CompositeView<javax.swing.JSplitPane, SC> {
	private static final java.awt.Dimension MINIMUM_SIZE = new java.awt.Dimension( 24, 24 );
	private final int orientation;

	protected AbstractSplitPane( SC splitComposite, int orientation ) {
		super( splitComposite );
		this.orientation = orientation;
	}

	protected abstract javax.swing.JSplitPane createJSplitPane( int orientation );

	@Override
	protected final javax.swing.JSplitPane createAwtComponent() {
		javax.swing.JSplitPane rv = this.createJSplitPane( this.orientation );
		rv.getLeftComponent().setMinimumSize( MINIMUM_SIZE );
		rv.getRightComponent().setMinimumSize( MINIMUM_SIZE );
		return rv;
	}

	public void addDividerLocationChangeListener( java.beans.PropertyChangeListener changeListener ) {
		this.getAwtComponent().addPropertyChangeListener( "dividerLocation", changeListener );
	}

	public void removeDividerLocationChangeListener( java.beans.PropertyChangeListener changeListener ) {
		this.getAwtComponent().removePropertyChangeListener( "dividerLocation", changeListener );
	}

	//	this.getAwtComponent().setOrientation( orientation );
	//	if( splitComposite != null ) {
	//		org.lgna.croquet.Composite< ? > leadingComposite = splitComposite.getLeadingComposite();
	//		org.lgna.croquet.Composite< ? > trailingComposite = splitComposite.getTrailingComposite();
	//		this.setLeadingComponent( leadingComposite != null ? leadingComposite.getView() : null );
	//		this.setTrailingComponent( trailingComposite != null ? trailingComposite.getView() : null );
	//	}
	//	public Component<?> getLeadingComponent() {
	//		return Component.lookup( this.getAwtComponent().getLeftComponent() );
	//	}
	//	public Component<?> getTailingComponent() {
	//		return Component.lookup( this.getAwtComponent().getRightComponent() );
	//	}
	//	protected abstract void internalSetLeadingComponent( JComponent<?> component );
	//	protected abstract void internalSetTrailingComponent( JComponent<?> component );
	//	public void setLeadingComponent( JComponent<?> component ) {
	//		javax.swing.JComponent jComponent;
	//		if( component != null ) {
	//			jComponent = component.getAwtComponent();
	//			jComponent.setMinimumSize( MINIMUM_SIZE );
	//		} else {
	//			jComponent = null;
	//		}
	//		if( this.getAwtComponent().getLeftComponent() != jComponent ) {
	//			this.internalSetLeadingComponent( component );
	//			this.revalidateAndRepaint();
	//		}
	//	}
	//	public void setTrailingComponent( JComponent<?> component ) {
	//		javax.swing.JComponent jComponent;
	//		if( component != null ) {
	//			jComponent = component.getAwtComponent();
	//			jComponent.setMinimumSize( MINIMUM_SIZE );
	//		} else {
	//			jComponent = null;
	//		}
	//		if( this.getAwtComponent().getRightComponent() != jComponent ) {
	//			this.internalSetTrailingComponent( component );
	//			this.revalidateAndRepaint();
	//		}
	//	}

	public double getResizeWeight() {
		return this.getAwtComponent().getResizeWeight();
	}

	public void setResizeWeight( double resizeWeight ) {
		this.getAwtComponent().setResizeWeight( resizeWeight );
	}

	public int getDividerSize() {
		return this.getAwtComponent().getDividerSize();
	}

	public void setDividerSize( int dividerSize ) {
		this.getAwtComponent().setDividerSize( dividerSize );
	}

	public int getDividerLocation() {
		return this.getAwtComponent().getDividerLocation();
	}

	public void setDividerLocation( int dividerLocation ) {
		this.getAwtComponent().setDividerLocation( dividerLocation );
	}

	public void setDividerProportionalLocation( double proportionalLocation ) {
		this.getAwtComponent().setDividerLocation( proportionalLocation );
	}
}
