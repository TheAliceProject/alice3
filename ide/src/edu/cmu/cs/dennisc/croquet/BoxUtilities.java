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

package edu.cmu.cs.dennisc.croquet;

/**
 * @author Dennis Cosgrove
 */
public class BoxUtilities {
	public static Component< javax.swing.Box.Filler > createGlue() {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( Short.MAX_VALUE, Short.MAX_VALUE ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createHorizontalGlue() {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( Short.MAX_VALUE, 0 ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createVerticalGlue() {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 0, 0 ), new java.awt.Dimension( 0, Short.MAX_VALUE ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createHorizontalSliver( final int width ) {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( width, 0 ), new java.awt.Dimension( width, 0 ), new java.awt.Dimension( width, 0 ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createVerticalSliver( final int height ) {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( 0, height ), new java.awt.Dimension( 0, height ), new java.awt.Dimension( 0, height ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createHorizontalStrut( final int width ) {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( width, 0 ), new java.awt.Dimension( width, 0 ), new java.awt.Dimension( width, Short.MAX_VALUE ) );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createVerticalStrut( final int height ) {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( new java.awt.Dimension( 0, height ), new java.awt.Dimension( 0, height ), new java.awt.Dimension( Short.MAX_VALUE, height ) );
			}
		};
	}

	public static Component< javax.swing.Box.Filler > createRigidArea( final java.awt.Dimension size ) {
		return new Component< javax.swing.Box.Filler >() {
			@Override
			protected javax.swing.Box.Filler createJComponent() {
				return new javax.swing.Box.Filler( size, size, size );
			}
		};
	}
	public static Component< javax.swing.Box.Filler > createRigidArea( int width, int height ) {
		return createRigidArea( new java.awt.Dimension( width, height ) );
	}
	
	//todo: use Short.MAX_VALUE instead of null?
	private static class ClampedComponent extends Component< javax.swing.JComponent > {
		private Component<?> component;
		private Integer minimumPreferredWidth;
		private Integer minimumPreferredHeight;
		public ClampedComponent( Component<?> component, Integer minimumPreferredWidth, Integer minimumPreferredHeight ) {
			this.component = component;
			this.minimumPreferredWidth = minimumPreferredWidth;
			this.minimumPreferredHeight = minimumPreferredHeight;
		}
		@Override
		protected javax.swing.JComponent createJComponent() {
			javax.swing.JPanel rv = new javax.swing.JPanel() {
				@Override
				public java.awt.Dimension getPreferredSize() {
					java.awt.Dimension rv = super.getPreferredSize();
//					edu.cmu.cs.dennisc.print.PrintUtilities.println();
//					edu.cmu.cs.dennisc.print.PrintUtilities.println();
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( rv );
					if( ClampedComponent.this.minimumPreferredWidth != null ) {
						rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumWidth( rv, ClampedComponent.this.minimumPreferredWidth );
					}
					if( ClampedComponent.this.minimumPreferredHeight != null ) {
						rv = edu.cmu.cs.dennisc.java.awt.DimensionUtilties.constrainToMinimumHeight( rv, ClampedComponent.this.minimumPreferredHeight );
					}
//					edu.cmu.cs.dennisc.print.PrintUtilities.println( rv );
//					edu.cmu.cs.dennisc.print.PrintUtilities.println();
//					edu.cmu.cs.dennisc.print.PrintUtilities.println();
					return rv;
				}
			};
			rv.setLayout( new java.awt.BorderLayout() );
			rv.add( this.component.getJComponent(), java.awt.BorderLayout.CENTER );
			return rv;
		}
	}
	public static Component< ? > createClampedToMinimumPreferredWidthComponent( Component<?> component, int width ) {
		return new ClampedComponent( component, width, null );
	}
	public static Component< ? > createClampedToMinimumPreferredHeightComponent( Component<?> component, int height ) {
		return new ClampedComponent( component, null, height );
	}
	public static Component< ? > createClampedToMinimumPreferredSizeComponent( Component<?> component, int width, int height ) {
		return new ClampedComponent( component, width, height );
	}
}
