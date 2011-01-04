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
public abstract class Component<J extends java.awt.Component> extends ScreenElement {
	public interface ContainmentObserver {
		public void addedTo( Component< ? > parent );
		public void removedFrom( Component< ? > parent );
	}

	private static java.util.Map< java.awt.Component, Component< ? >> map = edu.cmu.cs.dennisc.java.util.Collections.newWeakHashMap();

	private static class InternalAwtContainerAdapter extends Container< java.awt.Container > {
		private java.awt.Container awtContainer;

		public InternalAwtContainerAdapter( java.awt.Container awtContainer ) {
			this.awtContainer = awtContainer;
			//this.awtParent = awtContainer.getParent();
			if( this.awtContainer instanceof javax.swing.plaf.UIResource ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "javax.swing.plaf.UIResource: ", awtContainer.getClass(), awtContainer.hashCode() );
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "creating adapter for: ", awtContainer.getClass(), awtContainer.hashCode() );
			}
		}
		@Override
		protected java.awt.Container createAwtComponent() {
			return this.awtContainer;
		}
	}

	private static class InternalAwtComponentAdapter extends Component< java.awt.Component > {
		private java.awt.Component awtComponent;

		public InternalAwtComponentAdapter( java.awt.Component awtComponent ) {
			this.awtComponent = awtComponent;
			//this.awtParent = awtComponent.getParent();
			if( this.awtComponent instanceof javax.swing.plaf.UIResource ) {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "javax.swing.plaf.UIResource: ", awtComponent.getClass(), awtComponent.hashCode() );
			} else {
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "creating adapter for: ", awtComponent.getClass(), awtComponent.hashCode() );
			}
		}
		@Override
		protected java.awt.Component createAwtComponent() {
			return this.awtComponent;
		}
	}

	//todo reduce visibility to /* package-private */
	public static Component< ? > lookup( java.awt.Component awtComponent ) {
		if( awtComponent != null ) {
			Component< ? > rv = Component.map.get( awtComponent );
			if( rv != null ) {
				//pass
			} else {
				if( awtComponent instanceof java.awt.Container ) {
					java.awt.Container awtContainer = (java.awt.Container)awtComponent;
					rv = new InternalAwtContainerAdapter( awtContainer );
				} else {
					rv = new InternalAwtComponentAdapter( awtComponent );
				}

				//note: trigger desired side effect of updating map
				rv.getAwtComponent();
				//

			}
			return rv;
		} else {
			return null;
		}
	}
		
	private java.awt.event.HierarchyListener hierarchyListener = new java.awt.event.HierarchyListener() {
		public void hierarchyChanged( java.awt.event.HierarchyEvent e ) {
			Component.this.handleHierarchyChanged( e );
		}
	};
//	private static boolean isWarningAlreadyPrinted = false;
//	private static StringBuilder append( StringBuilder rv, String s, java.awt.Component awtComponent ) {
//		rv.append( "[" );
//		rv.append( s );
//		if( awtComponent != null ) {
//			rv.append( awtComponent.getClass().getName() );
//			rv.append( " " );
//			rv.append( awtComponent.hashCode() );
//		} else {
//			rv.append( "null 0" );
//		}
//		rv.append( "]\n" );
//		return rv;
//	}
//	private java.awt.event.HierarchyListener hierarchyListener = new java.awt.event.HierarchyListener() {
//		public void hierarchyChanged( java.awt.event.HierarchyEvent e ) {
//			java.awt.Component awtComponent = e.getComponent();
//			java.awt.Component awtChanged = e.getChanged();
//			java.awt.Container awtParent = e.getChangedParent();
////			System.err.println( "hierarchyChanged: " + e );
//			long flags = e.getChangeFlags();
//			if( (flags & java.awt.event.HierarchyEvent.PARENT_CHANGED) != 0 ) {
//
//				assert awtComponent == Component.this.getAwtComponent();
//
//				if( awtComponent == awtChanged ) {
//					if( awtParent != Component.this.awtParent ) {
//						Component.this.handleParentChange( awtParent );
//					} else {
//						if( isWarningAlreadyPrinted ) {
//							//pass
//						} else {
//							//Thread.dumpStack();
//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "investigate: hierarchyChanged seems to not be actually changing the parent" );
//							//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    flags:", flags );
//							//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
//							//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChanged:", awtChanged.getClass().getName(), awtChanged );
//							//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.hashCode(), awtParent.getClass().getName(), awtParent.getLayout() );
//							isWarningAlreadyPrinted = true;
//						}
//					}
//				}
//			}
//			if( ( flags & java.awt.event.HierarchyEvent.DISPLAYABILITY_CHANGED ) != 0 ) {
//				Component.this.handleDisplayabilityChanged( e );
////				StringBuilder sb = new StringBuilder();
////				sb.append( awtComponent == awtChanged );
////				sb.append( "\n" );
////				append( sb, "awtComponent:", awtComponent );
////				append( sb, "awtChanged:", awtChanged );
////				append( sb, "awtParent:", awtParent );
////				//append( sb, "Component.this.awtParent:", Component.this.awtParent );
////				sb.append( "\n" );
////				System.err.println( sb.toString() );
////				assert awtChanged == awtComponent;
////				assert awtParent != null;
////				if( awtChanged.isDisplayable() ) {
////					if( Component.this.awtParent == null ) {
////						assert Component.this.awtParent == null;
////						Component.this.handleAddedTo( map.get( awtParent ) );
////						Component.this.awtParent = awtParent;
////					}
////				} else {
////					if( Component.this.awtParent != null ) {
////						assert Component.this.awtParent != null;
////						assert Component.this.awtParent == awtParent;
////						Component.this.handleRemovedFrom( map.get( awtParent ) );
////						Component.this.awtParent = null;
////					}
////				}
//			}
//		}
//	};
//	private java.awt.Container awtParent;
//	private void handleParentChange( java.awt.Container awtParent ) {
//		if( this.awtParent != null ) {
//			Component< ? > parent = Component.lookup( this.awtParent );
//			if( parent != null ) {
//				//pass
//			} else {
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for parent" );
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.getClass().getName(), awtParent.getLayout() );
//			}
//			this.handleRemovedFrom( parent );
//		} else {
//			assert awtParent != null;
//		}
//		this.awtParent = awtParent;
//		if( this.awtParent != null ) {
//			Component< ? > parent = Component.lookup( this.awtParent );
//			if( parent != null ) {
//				//pass
//			} else {
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for parent" );
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
//				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.getClass().getName(), awtParent.getLayout() );
//			}
//			this.handleAddedTo( parent );
//		}
//	}

	protected void handleDisplayable() {
	}
	protected void handleUndisplayable() {
	}
	
	private boolean isDisplayableState = false;
	private void trackDisplayability() {
		if( this.awtComponent.isDisplayable() ) {
			if( this.isDisplayableState ) {
				//pass
			} else {
				this.handleDisplayable();
				this.isDisplayableState = true;
			}
		} else {
			if( this.isDisplayableState ) {
				this.handleUndisplayable();
				this.isDisplayableState = false;
			} else {
				//pass
			}
		}
	}
	
	protected void handleAddedTo( Component< ? > parent ) {
	}
	protected void handleRemovedFrom( Component< ? > parent ) {
	}
	
	private java.awt.Container awtParent;
	private void handleParentChange( java.awt.Container awtParent ) {
		if( this.awtParent != null ) {
			Component< ? > parent = Component.lookup( this.awtParent );
			if( parent != null ) {
				//pass
			} else {
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for parent" );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.getClass().getName(), awtParent.getLayout() );
			}
			this.handleRemovedFrom( parent );
		} else {
			assert awtParent != null;
		}
		this.awtParent = awtParent;
		if( this.awtParent != null ) {
			Component< ? > parent = Component.lookup( this.awtParent );
			if( parent != null ) {
				//pass
			} else {
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for parent" );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
				//				edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.getClass().getName(), awtParent.getLayout() );
			}
			this.handleAddedTo( parent );
		}
	}
	
	private static boolean isWarningAlreadyPrinted = false;
	protected final void handleHierarchyChanged( java.awt.event.HierarchyEvent e ) {
		//assert e.getComponent() == this.awtComponent : this;
		java.awt.Component awtComponent = e.getComponent();
		java.awt.Component awtChanged = e.getChanged();
		java.awt.Container awtParent = e.getChangedParent();
		long flags = e.getChangeFlags();
		if( ( flags & java.awt.event.HierarchyEvent.DISPLAYABILITY_CHANGED ) != 0 ) {
			if( e.getComponent() == this.awtComponent ) {
				this.trackDisplayability();
			} else {
				edu.cmu.cs.dennisc.print.PrintUtilities.println( "handleDisplayabilityChanged:", this.awtComponent.hashCode(), this.awtComponent.isDisplayable() );
			}
		}

		if( (flags & java.awt.event.HierarchyEvent.PARENT_CHANGED) != 0 ) {

			assert awtComponent == Component.this.getAwtComponent();

			if( awtComponent == awtChanged ) {
				if( awtParent != Component.this.awtParent ) {
					Component.this.handleParentChange( awtParent );
				} else {
					if( isWarningAlreadyPrinted ) {
						//pass
					} else {
						//Thread.dumpStack();
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "investigate: hierarchyChanged seems to not be actually changing the parent" );
						//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    flags:", flags );
						//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
						//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChanged:", awtChanged.getClass().getName(), awtChanged );
						//							edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.hashCode(), awtParent.getClass().getName(), awtParent.getLayout() );
						isWarningAlreadyPrinted = true;
					}
				}
			}
		}
		
	}

	private J awtComponent;

	protected abstract J createAwtComponent();

	// todo: reduce visibility to /*package-private*/
	@Override
	public final J getAwtComponent() {
		if( this.awtComponent != null ) {
			// pass
		} else {
			this.awtComponent = this.createAwtComponent();
			this.trackDisplayability();
			this.awtComponent.addHierarchyListener( this.hierarchyListener );
			this.awtComponent.setName( this.getClass().getName() );
			if( this.awtComponent instanceof javax.swing.JComponent ) {
				((javax.swing.JComponent)this.awtComponent).setComponentOrientation(  java.awt.ComponentOrientation.getOrientation( javax.swing.JComponent.getDefaultLocale() ) );
			}
			Component.map.put( this.awtComponent, this );
		}
		return this.awtComponent;
	}

	protected void release() {
		if( this.awtComponent != null ) {
			System.err.println( "release: " + this.hashCode() );
			Thread.dumpStack();
			this.awtComponent.removeHierarchyListener( this.hierarchyListener );
			this.trackDisplayability();
			Component.map.remove( this.awtComponent );
			this.awtComponent = null;
		}
	}

	public java.util.Locale getLocale() {
		return this.getAwtComponent().getLocale();
	}

	public java.awt.Font getFont() {
		return this.getAwtComponent().getFont();
	}

	public void setFont( java.awt.Font font ) {
//		if( font != null ) {
			this.getAwtComponent().setFont( font );
//		} else {
//			throw new NullPointerException();
//		}
	}

	public final void scaleFont( float scaleFactor ) {
		this.setFont( edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont( this.getAwtComponent(), scaleFactor ) );
	}
	public final void setFontSize( float fontSize ) {
		this.setFont( this.getFont().deriveFont( fontSize ) );
	}
	public final void changeFont( edu.cmu.cs.dennisc.java.awt.font.TextAttribute< ? >... textAttributes ) {
		this.setFont( edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont( this.getAwtComponent(), textAttributes ) );
	}

	protected java.awt.Dimension constrainPreferredSizeIfNecessary( java.awt.Dimension rv ) {
		if( minimumPreferredWidth != null ) {
			rv.width = Math.max( rv.width, minimumPreferredWidth );
		}
		if( maximumPreferredWidth != null ) {
			rv.width = Math.min( rv.width, maximumPreferredWidth );
		}
		if( minimumPreferredHeight != null ) {
			rv.height = Math.max( rv.height, minimumPreferredHeight );
		}
		if( maximumPreferredHeight != null ) {
			rv.height = Math.min( rv.height, maximumPreferredHeight );
		}
		return rv;
	}
	private Integer minimumPreferredWidth = null;
	private Integer maximumPreferredWidth = null;
	private Integer minimumPreferredHeight = null;
	private Integer maximumPreferredHeight = null;
	
	public final Integer getMaximumPreferredWidth() {
		return this.maximumPreferredWidth;
	}
	public final void setMaximumPreferredWidth( Integer maximumPreferredWidth ) {
		this.maximumPreferredWidth = maximumPreferredWidth;
	}
	public final Integer getMinimumPreferredWidth() {
		return this.minimumPreferredWidth;
	}
	public final void setMinimumPreferredWidth( Integer minimumPreferredWidth ) {
		this.minimumPreferredWidth = minimumPreferredWidth;
	}
	public final Integer getMaximumPreferredHeight() {
		return this.maximumPreferredHeight;
	}
	public final void setMaximumPreferredHeight( Integer maximumPreferredHeight ) {
		this.maximumPreferredHeight = maximumPreferredHeight;
	}
	public final Integer getMinimumPreferredHeight() {
		return this.minimumPreferredHeight;
	}
	public final void setMinimumPreferredHeight( Integer minimumPreferredHeight ) {
		this.minimumPreferredHeight = minimumPreferredHeight;
	}
	
	private boolean isMaximumSizeClampedToPreferredSize = false;
	public boolean isMaximumSizeClampedToPreferredSize() { 
		return this.isMaximumSizeClampedToPreferredSize;
	}
	public void setMaximumSizeClampedToPreferredSize(boolean isMaximumSizeClampedToPreferredSize) {
		this.isMaximumSizeClampedToPreferredSize = isMaximumSizeClampedToPreferredSize;
	}

	/*package-private*/boolean isEnabled() {
		return this.getAwtComponent().isEnabled();
	}
	/*package-private*/void setEnabled( boolean isEnabled ) {
		this.getAwtComponent().setEnabled( isEnabled );
	}

	public java.awt.Color getForegroundColor() {
		return this.getAwtComponent().getForeground();
	}

	public void setForegroundColor( java.awt.Color color ) {
		this.getAwtComponent().setForeground( color );
	}

	public java.awt.Color getBackgroundColor() {
		return this.getAwtComponent().getBackground();
	}

	public void setBackgroundColor( java.awt.Color color ) {
		this.getAwtComponent().setBackground( color );
	}

	public boolean isVisible() {
		return this.getAwtComponent().isVisible();
	}

	public void setVisible( boolean isVisible ) {
		this.getAwtComponent().setVisible( isVisible );
	}

	public boolean isOpaque() {
		return this.getAwtComponent().isOpaque();
	}

	public boolean getIgnoreRepaint() {
		return this.getAwtComponent().getIgnoreRepaint();
	}

	public void setIgnoreRepaint( boolean ignoreRepaint ) {
		this.getAwtComponent().setIgnoreRepaint( ignoreRepaint );
	}

	public java.awt.Cursor getCursor() {
		return this.getAwtComponent().getCursor();
	}
	public void setCursor( java.awt.Cursor cursor ) {
		this.getAwtComponent().setCursor( cursor );
	}

	public float getAlignmentX() {
		return this.getAwtComponent().getAlignmentX();
	}
	public float getAlignmentY() {
		return this.getAwtComponent().getAlignmentY();
	}

	public int getX() {
		return this.getAwtComponent().getX();
	}
	public int getY() {
		return this.getAwtComponent().getY();
	}

	public final void setLocation( java.awt.Point pt ) {
		this.setLocation( pt.x, pt.y );
	}
	public void setLocation( int x, int y ) {
		this.getAwtComponent().setLocation( x, y );
	}
	public void setLocation( java.awt.Point pt, ScreenElement asSeenBy ) {
		this.getAwtComponent().setLocation( asSeenBy.convertPoint( pt, this.getParent() ) );
	}
	public final void setLocation( int x, int y, ScreenElement asSeenBy ) {
		this.setLocation( new java.awt.Point( x, y ), asSeenBy );
	}
	
	public java.awt.Point getLocationOnScreen() {
		return this.getAwtComponent().getLocationOnScreen();
	}

	public java.awt.Rectangle getLocalBounds() {
		return javax.swing.SwingUtilities.getLocalBounds( this.getAwtComponent() );
	}
	public java.awt.Rectangle getBounds() {
		return this.getAwtComponent().getBounds();
	}
	public java.awt.Rectangle getBounds( ScreenElement asSeenBy ) {
		Container< ? > parent = this.getParent();
		if( parent != null ) {
			return parent.convertRectangle( this.getBounds(), asSeenBy );
		} else {
			return null;
		}
	}

	public java.awt.Rectangle getVisibleRectangle() {
		return this.getBounds();
	}
	public final java.awt.Rectangle getVisibleRectangle( ScreenElement asSeenBy ) {
		return this.convertRectangle( this.getVisibleRectangle(), asSeenBy );
	}

	public java.awt.Shape getShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		java.awt.Rectangle rv = this.getBounds( asSeenBy );
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( rv, insets );
	}
	public java.awt.Shape getVisibleShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		java.awt.Rectangle rv = this.getVisibleRectangle( asSeenBy );
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( rv, insets );
	}

	public boolean isInView() {
		java.awt.Rectangle visibleRect = this.getVisibleRectangle();
		java.awt.Dimension size = this.getAwtComponent().getSize();
		return visibleRect.width == size.width || visibleRect.height == size.height;
	}

	public <E extends Container< ? >> E getFirstAncestorAssignableTo( Class< E > cls ) {
		Container< ? > parent = this.getParent();
		if( parent != null ) {
			if( cls.isAssignableFrom( parent.getClass() ) ) {
				return cls.cast( parent );
			} else {
				return parent.getFirstAncestorAssignableTo( cls );
			}
		} else {
			return null;
		}
	}

	public ScrollPane getScrollPaneAncestor() {
		return this.getFirstAncestorAssignableTo( ScrollPane.class );
	}
	public java.awt.Rectangle convertRectangle( java.awt.Rectangle rectangle, ScreenElement destination ) {
		return javax.swing.SwingUtilities.convertRectangle( this.getAwtComponent(), rectangle, destination.getAwtComponent() );
	}
	public java.awt.event.MouseEvent convertMouseEvent( java.awt.event.MouseEvent e, ScreenElement destination ) {
		return edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent( this.getAwtComponent(), e, destination.getAwtComponent() );
	}

	@Deprecated
	public Container< ? > getParent() {
		return (Container< ? >)Component.lookup( this.getAwtComponent().getParent() );
	}

	@Deprecated
	public AbstractWindow< ? > getRoot() {
		return AbstractWindow.lookup( javax.swing.SwingUtilities.getRoot( this.getAwtComponent() ) );
	}

	public int getWidth() {
		return this.getAwtComponent().getWidth();
	}

	public int getHeight() {
		return this.getAwtComponent().getHeight();
	}

	public void repaint() {
		this.getAwtComponent().repaint();
	}

	public void requestFocus() {
		this.getAwtComponent().requestFocus();
	}

	@Deprecated
	public void addHierarchyListener( java.awt.event.HierarchyListener listener ) {
		this.getAwtComponent().addHierarchyListener( listener );
	}

	@Deprecated
	public void removeHierarchyListener( java.awt.event.HierarchyListener listener ) {
		this.getAwtComponent().removeHierarchyListener( listener );
	}

	@Deprecated
	public void addKeyListener( java.awt.event.KeyListener listener ) {
		this.getAwtComponent().addKeyListener( listener );
	}

	@Deprecated
	public void removeKeyListener( java.awt.event.KeyListener listener ) {
		this.getAwtComponent().removeKeyListener( listener );
	}

	@Deprecated
	public void addMouseListener( java.awt.event.MouseListener listener ) {
		this.getAwtComponent().addMouseListener( listener );
	}

	@Deprecated
	public void removeMouseListener( java.awt.event.MouseListener listener ) {
		this.getAwtComponent().removeMouseListener( listener );
	}

	@Deprecated
	public void addMouseMotionListener( java.awt.event.MouseMotionListener listener ) {
		this.getAwtComponent().addMouseMotionListener( listener );
	}

	@Deprecated
	public void removeMouseMotionListener( java.awt.event.MouseMotionListener listener ) {
		this.getAwtComponent().removeMouseMotionListener( listener );
	}

	@Deprecated
	public void addMouseWheelListener( java.awt.event.MouseWheelListener listener ) {
		this.getAwtComponent().addMouseWheelListener( listener );
	}

	@Deprecated
	public void removeMouseWheelListener( java.awt.event.MouseWheelListener listener ) {
		this.getAwtComponent().removeMouseWheelListener( listener );
	}

	@Deprecated
	public void setPreferredSize( java.awt.Dimension preferredSize ) {
		this.getAwtComponent().setPreferredSize( preferredSize );
	}

	@Deprecated
	public void makeStandOut() {
		edu.cmu.cs.dennisc.java.awt.ComponentUtilities.makeStandOut( this.getAwtComponent() );
	}

	@Override
	public String toString() {
		return this.getAwtComponent().getClass().toString();
	}
}
