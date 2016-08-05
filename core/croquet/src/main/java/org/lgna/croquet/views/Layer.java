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
public final class Layer {
	private final LayeredPane layeredPane;
	private final Integer id;
	private SwingComponentView<?> component;
	private final java.awt.event.ComponentListener componentListener = new java.awt.event.ComponentListener() {
		@Override
		public void componentShown( java.awt.event.ComponentEvent e ) {
		}

		@Override
		public void componentHidden( java.awt.event.ComponentEvent e ) {
		}

		@Override
		public void componentMoved( java.awt.event.ComponentEvent e ) {
		}

		@Override
		public void componentResized( java.awt.event.ComponentEvent e ) {
			Layer.this.updateComponentSize();
		}
	};

	/* package-private */Layer( LayeredPane layeredPane, Integer id ) {
		this.layeredPane = layeredPane;
		this.id = id;
	}

	private void updateComponentSize() {
		if( this.component != null ) {
			this.component.getAwtComponent().setSize( this.layeredPane.getSize() );
		}
	}

	public SwingComponentView<?> getComponent() {
		return this.component;
	}

	public void setComponent( SwingComponentView<?> component ) {
		if( this.component != component ) {
			javax.swing.JLayeredPane jLayeredPane = this.layeredPane.getAwtComponent();
			if( this.component != null ) {
				jLayeredPane.removeComponentListener( this.componentListener );
				jLayeredPane.remove( this.component.getAwtComponent() );
			}
			this.component = component;
			if( this.component != null ) {
				this.updateComponentSize();
				jLayeredPane.add( this.component.getAwtComponent(), this.id );
				jLayeredPane.addComponentListener( this.componentListener );
			}
			jLayeredPane.repaint();
		}
	}

	public boolean isBelowDefaultLayer() {
		return this.id < javax.swing.JLayeredPane.DEFAULT_LAYER;
	}

	public boolean isAboveDefaultLayer() {
		return this.id > javax.swing.JLayeredPane.DEFAULT_LAYER;
	}

	public boolean isBelowPaletteLayer() {
		return this.id < javax.swing.JLayeredPane.PALETTE_LAYER;
	}

	public boolean isAbovePaletteLayer() {
		return this.id > javax.swing.JLayeredPane.PALETTE_LAYER;
	}

	public boolean isBelowModalLayer() {
		return this.id < javax.swing.JLayeredPane.MODAL_LAYER;
	}

	public boolean isAboveModalLayer() {
		return this.id > javax.swing.JLayeredPane.MODAL_LAYER;
	}

	public boolean isBelowPopupLayer() {
		return this.id < javax.swing.JLayeredPane.POPUP_LAYER;
	}

	public boolean isAbovePopupLayer() {
		return this.id > javax.swing.JLayeredPane.POPUP_LAYER;
	}

	public boolean isBelowDragLayer() {
		return this.id < javax.swing.JLayeredPane.DRAG_LAYER;
	}

	public boolean isAboveDragLayer() {
		return this.id > javax.swing.JLayeredPane.DRAG_LAYER;
	}
}
