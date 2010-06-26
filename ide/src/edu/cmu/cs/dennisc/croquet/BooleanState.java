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
public /*final*/ class BooleanState extends Model< BooleanState > {
	public static interface ValueObserver {
		public void changing( boolean nextValue );
		public void changed( boolean nextValue );
	};
		
	private java.util.List< ValueObserver > valueObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.add( valueObserver );
	}
	public void addAndInvokeValueObserver(ValueObserver valueObserver) {
		this.addValueObserver(valueObserver);
		valueObserver.changed(this.getValue());
	}
	public void removeValueObserver( ValueObserver valueObserver ) {
		this.valueObservers.remove( valueObserver );
	}
	
	private javax.swing.ButtonModel createButtonModel() {
		return new javax.swing.JToggleButton.ToggleButtonModel();
	}
	private javax.swing.ButtonModel buttonModel = this.createButtonModel();
	private javax.swing.Action action = new javax.swing.AbstractAction() {
		public void actionPerformed(java.awt.event.ActionEvent e) {
		}
	};
	private java.awt.event.ItemListener itemListener = new java.awt.event.ItemListener() {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			Application application = Application.getSingleton();
			ModelContext<?> parentContext = application.getCurrentContext();
			BooleanStateContext childContext = parentContext.createBooleanStateContext( BooleanState.this, e, null );
			childContext.commitAndInvokeDo( new BooleanStateEdit( e ) );
		}
	};

	private boolean value;
	private String trueText = null;
	private String falseText = null;

	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState, String trueText, String falseText) {
		super(group, individualUUID);
		this.value = initialState;
		this.buttonModel.setSelected(initialState);
		this.buttonModel.addItemListener(this.itemListener);
		this.setTrueText(trueText);
		this.setFalseText(falseText);
	}

	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState, String trueAndFalseText) {
		this(group, individualUUID, initialState, trueAndFalseText, trueAndFalseText);
	}
	public BooleanState(Group group, java.util.UUID individualUUID, boolean initialState ) {
		this(group, individualUUID, initialState, null, null );
	}

	/*package-private*/ javax.swing.ButtonModel getButtonModel() {
		return this.buttonModel;
	}
	public Boolean getValue() {
		return this.buttonModel.isSelected();
	}
	public void setValue( boolean value ) {
		if( value != this.value ) {
			//this.buttonModel.removeItemListener(itemListener);

			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changing( value );
			}
			this.buttonModel.setSelected(value);
			this.value = value;
			for( ValueObserver valueObserver : this.valueObservers ) {
				valueObserver.changed( value );
			}

			//this.buttonModel.addItemListener(itemListener);
			this.updateName();
		}
	}

	public String getTrueText() {
		return this.trueText;
	}
	public void setTrueText(String trueText) {
		this.trueText = trueText;
		this.updateName();
	}

	public String getFalseText() {
		return this.falseText;
	}

	public void setFalseText(String falseText) {
		this.falseText = falseText;
		this.updateName();
	}


	private void updateName() {
		String name;
		if (this.getValue()) {
			name = this.trueText;
		} else {
			name = this.falseText;
		}
		this.action.putValue(javax.swing.Action.NAME, name);
	}
	
	/*package-private*/ < B extends AbstractButton<?,BooleanState> > B register( final B rv ) {
		Application.getSingleton().register( this );
		rv.setSwingButtonModel( this.buttonModel );
		rv.setAction( this.action );
		rv.addContainmentObserver( new Component.ContainmentObserver() {
			public void addedTo(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BooleanState.this.addComponent( rv );
			}
			public void removedFrom(edu.cmu.cs.dennisc.croquet.Component<?> parent) {
				BooleanState.this.removeComponent( rv );
			}
		} );
		return rv;
	}
	public RadioButton createRadioButton() {
		return register( new RadioButton( this ) );
	}
	public CheckBox createCheckBox() {
		return register( new CheckBox( this ) );
	}
	public CheckBoxMenuItem createCheckBoxMenuItem() {
		return register( new CheckBoxMenuItem( this ) );
	}
	public ToolPalette createToolPalette( Component<?> component ) {
		class ToolPaletteTitle extends AbstractButton<javax.swing.AbstractButton,BooleanState> {
			public ToolPaletteTitle( BooleanState booleanState) {
				super( booleanState );
			}
			@Override
			protected javax.swing.AbstractButton createAwtComponent() {
//					@Override
//					protected void paintComponent(java.awt.Graphics g) {
//						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
//						java.awt.Paint paint;
//						final double FACTOR;
//						if( this.getModel().isArmed() ) {
//							FACTOR = 1.3;
//						} else {
//							FACTOR = 1.15;
//						}
//						final double INVERSE_FACTOR = 1.0 / FACTOR;
//						java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, FACTOR, FACTOR );
//						java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, INVERSE_FACTOR, INVERSE_FACTOR );
//						paint = new java.awt.GradientPaint(0,0, colorA, 0, this.getHeight(), colorB );
//						g2.setPaint( paint );
//						g2.fill( g2.getClip() );
//
//						super.paintComponent(g);
//						
//						int height = this.getHeight();
//						final int SIZE = 6;
//						java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
//						path.moveTo( SIZE, 0 );
//						path.lineTo( -SIZE, SIZE );
//						path.lineTo( -SIZE, -SIZE );
//						path.closePath();
//						
//						int x = 18;
//						int y = height/2;
//						
//						java.awt.geom.AffineTransform m = g2.getTransform();
//						
//						
//						g.translate(x, y);
//						if( this.getModel().isSelected() || this.getModel().isPressed() ) {
//							g2.rotate( Math.PI/2 );
//						}
//						java.awt.Paint fillPaint = java.awt.Color.WHITE;
//						if( this.getModel().isSelected() ) {
//							//pass
//						} else {
//							if( this.getModel().isPressed() ) {
//								fillPaint = java.awt.Color.YELLOW.darker();
//							} else {
//								if( this.getModel().isArmed() ) {
//									fillPaint = java.awt.Color.YELLOW;
//								}
//							}
//						}
//						g2.setPaint( fillPaint );
//						g2.fill( path );
//						g2.setPaint( java.awt.Color.BLACK );
//						g2.draw( path );
//						
//						g2.setTransform( m );
//					}
//				};
				class ArrowIcon implements javax.swing.Icon {
					private int size;
					public ArrowIcon( int size ) {
						this.size = size;
					}
					public int getIconWidth() {
						return this.size;
					}
					public int getIconHeight() {
						return this.size;
					}
					public void paintIcon(java.awt.Component c, java.awt.Graphics g, int x, int y) {
						javax.swing.AbstractButton button = (javax.swing.AbstractButton)c;
						float x0 = x;
						float x1 = x + this.size - 1;
						float xC = ( x0 + x1 ) * 0.5f;
						float y0 = y;
						float y1 = y + this.size - 1;
						float yC = ( y0 + y1 ) * 0.5f;
						java.awt.geom.GeneralPath path = new java.awt.geom.GeneralPath();
						path.moveTo( x0, y0);
						if( button.getModel().isSelected() || button.getModel().isPressed() ) {
							path.lineTo( x1, y0 );
							path.lineTo( xC, y1 );
						} else {
							path.lineTo( x1, yC );
							path.lineTo( x0, y1 );
						}
						path.closePath();

						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						
//						g2.setColor( java.awt.Color.RED );
//						g2.fillRect( x, y, this.getIconWidth(), this.getIconHeight() );

						java.awt.Paint fillPaint = java.awt.Color.WHITE;
//						if( button.getModel().isSelected() ) {
//							//pass
//						} else {
							if( button.getModel().isPressed() ) {
								fillPaint = java.awt.Color.YELLOW.darker();
							} else {
								if( button.getModel().isRollover() ) {
									fillPaint = java.awt.Color.YELLOW;
								}
							}
//						}
						
						g2.setPaint( fillPaint );
						g2.fill( path );
						g2.setPaint( java.awt.Color.BLACK );
						g2.draw( path );
					}
				}
				javax.swing.JCheckBox rv = new javax.swing.JCheckBox() {
					@Override
					protected void paintComponent(java.awt.Graphics g) {
						java.awt.Graphics2D g2 = (java.awt.Graphics2D)g;
						java.awt.Paint paint;
						final double FACTOR;
						if( this.getModel().isArmed() ) {
							FACTOR = 1.3;
						} else {
							FACTOR = 1.15;
						}
						final double INVERSE_FACTOR = 1.0 / FACTOR;
						java.awt.Color colorA = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, FACTOR, FACTOR );
						java.awt.Color colorB = edu.cmu.cs.dennisc.java.awt.ColorUtilities.scaleHSB(this.getBackground(), 1.0, INVERSE_FACTOR, INVERSE_FACTOR );
						paint = new java.awt.GradientPaint(0,0, colorA, 0, this.getHeight(), colorB );
						g2.setPaint( paint );
						g2.fill( g2.getClip() );
						super.paintComponent(g);
					}
					@Override
					public void updateUI() {
						this.setUI( new javax.swing.plaf.basic.BasicButtonUI() );
					}
				};
				rv.setIcon( new ArrowIcon( 14 ) );
				//rv.setBorder( javax.swing.BorderFactory.createEmptyBorder( 4, 4, 4, 4 ) );
				rv.setOpaque( false );
				rv.setVerticalTextPosition( javax.swing.SwingConstants.CENTER );
				return rv;
			}
		}
		ToolPaletteTitle title = this.register( new ToolPaletteTitle( this ) );
		return new ToolPalette(title, component);
	}
}
