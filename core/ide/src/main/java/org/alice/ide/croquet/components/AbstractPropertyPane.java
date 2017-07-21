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
package org.alice.ide.croquet.components;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractPropertyPane<P extends edu.cmu.cs.dennisc.property.InstanceProperty<T>, T> extends org.lgna.croquet.views.Panel {
	private final org.alice.ide.x.AstI18nFactory factory;
	private final P property;
	private final int axis;
	private final edu.cmu.cs.dennisc.property.event.PropertyListener propertyAdapter = new edu.cmu.cs.dennisc.property.event.PropertyListener() {
		@Override
		public void propertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		};

		@Override
		public void propertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
			AbstractPropertyPane.this.refreshLater();
		};
	};

	public AbstractPropertyPane( org.alice.ide.x.AstI18nFactory factory, P property, int axis ) {
		assert property != null;
		this.factory = factory;
		this.property = property;
		this.axis = axis;
	}

	protected org.alice.ide.x.AstI18nFactory getFactory() {
		return this.factory;
	}

	public P getProperty() {
		return this.property;
	}

	protected int getBoxLayoutPad() {
		return 0;
	}

	@Override
	protected final java.awt.LayoutManager createLayoutManager( javax.swing.JPanel jPanel ) {
		assert ( this.axis == javax.swing.BoxLayout.LINE_AXIS ) || ( this.axis == javax.swing.BoxLayout.PAGE_AXIS );
		int pad = this.getBoxLayoutPad();
		if( pad > 0 ) {
			return new edu.cmu.cs.dennisc.javax.swing.layouts.PaddedBoxLayout( jPanel, this.axis, pad );
		} else {
			return new javax.swing.BoxLayout( jPanel, this.axis );
		}
	}

	public void addComponent( org.lgna.croquet.views.AwtComponentView<?> component ) {
		this.internalAddComponent( component );
	}

	@Override
	protected void handleDisplayable() {
		super.handleDisplayable();
		this.property.addPropertyListener( this.propertyAdapter );
	}

	@Override
	protected void handleUndisplayable() {
		this.property.removePropertyListener( this.propertyAdapter );
		super.handleUndisplayable();
	}

	@Override
	protected boolean isRefreshOnAddedToDesired() {
		return true;
	}
}
