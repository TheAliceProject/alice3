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
public class Slider extends ViewController<javax.swing.JSlider, org.lgna.croquet.BoundedNumberState<?>> {
	public static enum Orientation {
		HORIZONTAL( javax.swing.SwingConstants.HORIZONTAL ),
		VERTICAL( javax.swing.SwingConstants.VERTICAL );
		private int internal;

		private Orientation( int internal ) {
			this.internal = internal;
		}

		private int getInternal() {
			return this.internal;
		}

		public static Orientation valueOf( int swingConstant ) {
			switch( swingConstant ) {
			case javax.swing.SwingConstants.HORIZONTAL:
				return HORIZONTAL;
			case javax.swing.SwingConstants.VERTICAL:
				return VERTICAL;
			default:
				return null;
			}
		}
	};

	public Slider( org.lgna.croquet.BoundedNumberState<?> model ) {
		super( model );
	}

	@Override
	protected javax.swing.JSlider createAwtComponent() {
		return new javax.swing.JSlider( this.getModel().getSwingModel().getBoundedRangeModel() );
	}

	public Orientation getOrientation() {
		return Orientation.valueOf( this.getAwtComponent().getOrientation() );
	}

	public void setOrientation( Orientation orientation ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setOrientation( orientation.getInternal() );
	}

	public boolean getInverted() {
		return this.getAwtComponent().getInverted();
	}

	public void setInverted( boolean inverted ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setInverted( inverted );
	}

	public int getMinorTickSpacing() {
		return this.getAwtComponent().getMinorTickSpacing();
	}

	public void setMinorTickSpacing( int minorTickSpacing ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setMinorTickSpacing( minorTickSpacing );
	}

	public int getMajorTickSpacing() {
		return this.getAwtComponent().getMajorTickSpacing();
	}

	public void setMajorTickSpacing( int majorTickSpacing ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setMajorTickSpacing( majorTickSpacing );
	}

	public boolean getSnapToTicks() {
		return this.getAwtComponent().getSnapToTicks();
	}

	public void setSnapToTicks( boolean snapToTicks ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setSnapToTicks( snapToTicks );
	}

	public boolean getPaintTicks() {
		return this.getAwtComponent().getPaintTicks();
	}

	public void setPaintTicks( boolean paintTicks ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setPaintTicks( paintTicks );
	}

	public boolean getPaintLabels() {
		return this.getAwtComponent().getPaintLabels();
	}

	public void setPaintLabels( boolean paintLabels ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setPaintLabels( paintLabels );
	}

	public java.util.Dictionary<Integer, javax.swing.JComponent> getLabelTable() {
		return this.getAwtComponent().getLabelTable();
	}

	public void setLabelTable( java.util.Dictionary<Integer, javax.swing.JComponent> labelTable ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setLabelTable( labelTable );
	}
}
