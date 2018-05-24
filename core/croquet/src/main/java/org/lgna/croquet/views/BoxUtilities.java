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

import edu.cmu.cs.dennisc.java.awt.DimensionUtilities;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * @author Dennis Cosgrove
 */
public class BoxUtilities {
	private static class Filler extends SwingComponentView<Box.Filler> {
		private final Dimension min;
		private final Dimension pref;
		private final Dimension max;

		public Filler( Dimension min, Dimension pref, Dimension max ) {
			this.min = min;
			this.pref = pref;
			this.max = max;
		}

		@Override
		protected Box.Filler createAwtComponent() {
			return new Box.Filler( this.min, this.pref, this.max );
		}
	}

	public static SwingComponentView<Box.Filler> createGlue() {
		return new Filler( new Dimension( 0, 0 ), new Dimension( 0, 0 ), new Dimension( Short.MAX_VALUE, Short.MAX_VALUE ) );
	}

	public static SwingComponentView<Box.Filler> createHorizontalGlue() {
		return new Filler( new Dimension( 0, 0 ), new Dimension( 0, 0 ), new Dimension( Short.MAX_VALUE, 0 ) );
	}

	public static SwingComponentView<Box.Filler> createVerticalGlue() {
		return new Filler( new Dimension( 0, 0 ), new Dimension( 0, 0 ), new Dimension( 0, Short.MAX_VALUE ) );
	}

	public static SwingComponentView<Box.Filler> createHorizontalSliver( int width ) {
		return new Filler( new Dimension( width, 0 ), new Dimension( width, 0 ), new Dimension( width, 0 ) );
	}

	public static SwingComponentView<Box.Filler> createVerticalSliver( int height ) {
		return new Filler( new Dimension( 0, height ), new Dimension( 0, height ), new Dimension( 0, height ) );
	}

	public static SwingComponentView<Box.Filler> createHorizontalStrut( int width ) {
		return new Filler( new Dimension( width, 0 ), new Dimension( width, 0 ), new Dimension( width, Short.MAX_VALUE ) );
	}

	public static SwingComponentView<Box.Filler> createVerticalStrut( int height ) {
		return new Filler( new Dimension( 0, height ), new Dimension( 0, height ), new Dimension( Short.MAX_VALUE, height ) );
	}

	public static SwingComponentView<Box.Filler> createRigidArea( Dimension size ) {
		return new Filler( size, size, size );
	}

	public static SwingComponentView<Box.Filler> createRigidArea( int width, int height ) {
		return createRigidArea( new Dimension( width, height ) );
	}

	//todo: use Short.MAX_VALUE instead of null?
	private static class ClampedComponent extends SwingComponentView<JComponent> {
		private Integer minimumPreferredWidth;
		private Integer minimumPreferredHeight;

		public ClampedComponent( AwtComponentView<?> component, Integer minimumPreferredWidth, Integer minimumPreferredHeight ) {
			this.minimumPreferredWidth = minimumPreferredWidth;
			this.minimumPreferredHeight = minimumPreferredHeight;
			this.internalAddComponent( component, BorderLayout.CENTER );
		}

		@Override
		protected JComponent createAwtComponent() {
			JPanel rv = new JPanel() {
				@Override
				public Dimension getPreferredSize() {
					Dimension rv = super.getPreferredSize();
					if( ClampedComponent.this.minimumPreferredWidth != null ) {
						rv = DimensionUtilities.constrainToMinimumWidth( rv, ClampedComponent.this.minimumPreferredWidth );
					}
					if( ClampedComponent.this.minimumPreferredHeight != null ) {
						rv = DimensionUtilities.constrainToMinimumHeight( rv, ClampedComponent.this.minimumPreferredHeight );
					}
					return rv;
				}
			};
			rv.setLayout( new BorderLayout() );
			return rv;
		}
	}

	public static SwingComponentView<?> createClampedToMinimumPreferredWidthComponent( AwtComponentView<?> component, int width ) {
		return new ClampedComponent( component, width, null );
	}

	public static SwingComponentView<?> createClampedToMinimumPreferredHeightComponent( AwtComponentView<?> component, int height ) {
		return new ClampedComponent( component, null, height );
	}

	public static SwingComponentView<?> createClampedToMinimumPreferredSizeComponent( AwtComponentView<?> component, int width, int height ) {
		return new ClampedComponent( component, width, height );
	}
}
