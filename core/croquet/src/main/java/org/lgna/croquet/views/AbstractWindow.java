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

package org.lgna.croquet.views;

/**
 * @author Dennis Cosgrove
 */
//todo: better name
public abstract class AbstractWindow<W extends java.awt.Window> extends ScreenElement {
	private static java.util.Map<java.awt.Component, AbstractWindow<?>> map = edu.cmu.cs.dennisc.java.util.Maps.newWeakHashMap();

	/* package-private */static AbstractWindow<?> lookup( java.awt.Component component ) {
		if( component != null ) {
			return AbstractWindow.map.get( component );
		} else {
			return null;
		}
	}

	private final W window;

	private final ContentPane contentPane;
	private final RootPane rootPane;

	public AbstractWindow( W window ) {
		this.window = window;
		AbstractWindow.map.put( window, this );
		this.contentPane = new ContentPane( this );
		this.rootPane = new RootPane( this );
	}

	@Override
	public final W getAwtComponent() {
		return this.window;
	}

	@Override
	public org.lgna.croquet.views.AbstractWindow<?> getRoot() {
		return this;
	}

	/* package-private */abstract java.awt.Container getAwtContentPane();

	/* package-private */abstract javax.swing.JRootPane getJRootPane();

	public ContentPane getContentPane() {
		return this.contentPane;
	}

	public RootPane getRootPane() {
		return this.rootPane;
	}

	public void addWindowListener( java.awt.event.WindowListener listener ) {
		this.window.addWindowListener( listener );
	}

	public void removeWindowListener( java.awt.event.WindowListener listener ) {
		this.window.removeWindowListener( listener );
	}

	public void addWindowStateListener( java.awt.event.WindowStateListener listener ) {
		this.window.addWindowStateListener( listener );
	}

	public void removeWindowStateListener( java.awt.event.WindowStateListener listener ) {
		this.window.removeWindowStateListener( listener );
	}

	public boolean isVisible() {
		return this.window.isVisible();
	}

	public void setVisible( boolean isVisible ) {
		this.window.setVisible( isVisible );
	}

	public int getX() {
		return this.window.getX();
	}

	public int getY() {
		return this.window.getY();
	}

	public java.awt.Point getLocation( ScreenElement asSeenBy ) {
		if( asSeenBy.getAwtComponent().isVisible() && this.isVisible() ) {
			java.awt.Point ptAsSeenBy = asSeenBy.getAwtComponent().getLocationOnScreen();
			java.awt.Point ptThis = this.getAwtComponent().getLocationOnScreen();
			return new java.awt.Point( ptThis.x - ptAsSeenBy.x, ptThis.y - ptAsSeenBy.y );
		} else {
			return null;
		}
	}

	public void setLocation( java.awt.Point location ) {
		this.window.setLocation( location );
	}

	public void setLocation( int x, int y ) {
		this.window.setLocation( x, y );
	}

	public boolean isLocationByPlatform() {
		return this.window.isLocationByPlatform();
	}

	public void setLocationByPlatform( boolean isLocationByPlatform ) {
		this.window.setLocationByPlatform( isLocationByPlatform );
	}

	public void setLocationRelativeTo( ScreenElement component ) {
		this.window.setLocationRelativeTo( component.getAwtComponent() );
	}

	public int getWidth() {
		return this.window.getWidth();
	}

	public int getHeight() {
		return this.window.getHeight();
	}

	public void setSize( java.awt.Dimension size ) {
		this.window.setSize( size );
	}

	public void setSize( int width, int height ) {
		this.window.setSize( width, height );
	}

	//	public java.awt.Rectangle getBounds() {
	//		return this.window.getBounds();
	//	}
	//	public java.awt.Rectangle getLocalBounds() {
	//		return new java.awt.Rectangle( 0, 0, this.getWidth(), this.getHeight() );
	//	}
	public java.awt.Rectangle getBounds( ScreenElement asSeenBy ) {
		java.awt.Point pt = this.getLocation( asSeenBy );
		if( pt != null ) {
			return new java.awt.Rectangle( pt.x, pt.y, this.getWidth(), this.getHeight() );
		} else {
			return null;
		}
	}

	@Override
	public java.awt.Shape getShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( this.getBounds( asSeenBy ), insets );
	}

	@Override
	public java.awt.Shape getVisibleShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
		return this.getShape( asSeenBy, insets );
	}

	public boolean isAlwaysOnTopSupported() {
		return this.getAwtComponent().isAlwaysOnTopSupported();
	}

	public boolean isAlwaysOnTop() {
		return this.getAwtComponent().isAlwaysOnTop();
	}

	public void setAlwaysOnTop( boolean isAlwaysOnTop ) {
		this.getAwtComponent().setAlwaysOnTop( isAlwaysOnTop );
	}

	@Override
	public ScrollPane getScrollPaneAncestor() {
		return null;
	}

	@Override
	public boolean isInView() {
		return this.isVisible();
	}

	public TrackableShape getCloseButtonTrackableShape() {
		return new TrackableShape() {
			@Override
			public java.awt.Shape getShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
				java.awt.Rectangle bounds = AbstractWindow.this.getBounds( asSeenBy );
				if( bounds != null ) {
					bounds.height = bounds.height - AbstractWindow.this.getRootPane().getHeight();
					bounds.height -= 8;
					return edu.cmu.cs.dennisc.java.awt.RectangleUtilities.inset( bounds, insets );
				} else {
					return null;
				}
			}

			@Override
			public java.awt.Shape getVisibleShape( ScreenElement asSeenBy, java.awt.Insets insets ) {
				return this.getShape( asSeenBy, insets );
			}

			@Override
			public ScrollPane getScrollPaneAncestor() {
				return null;
			}

			@Override
			public boolean isInView() {
				return AbstractWindow.this.isInView();
			}

			@Override
			public void addComponentListener( java.awt.event.ComponentListener listener ) {
				AbstractWindow.this.addComponentListener( listener );
			}

			@Override
			public void removeComponentListener( java.awt.event.ComponentListener listener ) {
				AbstractWindow.this.removeComponentListener( listener );
			}

			@Override
			public void addHierarchyBoundsListener( java.awt.event.HierarchyBoundsListener listener ) {
				AbstractWindow.this.addHierarchyBoundsListener( listener );
			}

			@Override
			public void removeHierarchyBoundsListener( java.awt.event.HierarchyBoundsListener listener ) {
				AbstractWindow.this.removeHierarchyBoundsListener( listener );
			}
		};
	}

	public void pack() {
		this.getAwtComponent().pack();
	}

	private static Button lookupButton( javax.swing.JButton jButton ) {
		AwtComponentView<?> component = AwtComponentView.lookup( jButton );
		if( component instanceof Button ) {
			Button button = (Button)component;
			return button;
		} else {
			return null;
		}
	}

	public Button getDefaultButton() {
		return lookupButton( this.getJRootPane().getDefaultButton() );
	}

	public void setDefaultButton( Button button ) {
		this.getJRootPane().setDefaultButton( button.getAwtComponent() );
	}

	private edu.cmu.cs.dennisc.java.util.StackDataStructure<javax.swing.JButton> defaultJButtonStack;

	public void pushDefaultButton( Button button ) {
		if( this.defaultJButtonStack != null ) {
			//pass
		} else {
			this.defaultJButtonStack = edu.cmu.cs.dennisc.java.util.Stacks.newStack();
		}
		this.defaultJButtonStack.push( this.getJRootPane().getDefaultButton() );
		this.setDefaultButton( button );
	}

	public Button popDefaultButton() {
		Button rv;
		if( this.defaultJButtonStack != null ) {
			if( this.defaultJButtonStack.isEmpty() ) {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.defaultJButtonStack );
				rv = null;
			} else {
				javax.swing.JButton jButton = this.defaultJButtonStack.pop();
				this.getJRootPane().setDefaultButton( jButton );
				rv = lookupButton( jButton );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.warning( this.defaultJButtonStack );
			rv = null;
		}
		return rv;
	}

	private org.lgna.croquet.MenuBarComposite menuBarComposite;
	private org.lgna.croquet.ToolBarComposite toolBarComposite;
	private org.lgna.croquet.Composite<?> mainComposite;

	public org.lgna.croquet.Composite<?> getMainComposite() {
		return this.mainComposite;
	}

	public void setToolBarComposite( org.lgna.croquet.ToolBarComposite toolBarComposite ) {
		if( this.toolBarComposite != toolBarComposite ) {
			synchronized( this.getAwtComponent().getTreeLock() ) {
				if( this.toolBarComposite != null ) {
					this.getContentPane().removeComponent( this.toolBarComposite.getView() );
					this.toolBarComposite.handlePostDeactivation();
				}
				this.toolBarComposite = toolBarComposite;
				if( this.toolBarComposite != null ) {
					this.toolBarComposite.handlePreActivation();
					this.getContentPane().addPageStartComponent( this.toolBarComposite.getView() );
				}
			}
			this.getContentPane().revalidateAndRepaint();
		}
	}

	public void setMainComposite( org.lgna.croquet.Composite<?> mainComposite ) {
		if( this.mainComposite != mainComposite ) {
			synchronized( this.getAwtComponent().getTreeLock() ) {
				if( this.mainComposite != null ) {
					this.getContentPane().removeComponent( this.mainComposite.getView() );
					this.mainComposite.handlePostDeactivation();
				}
				this.mainComposite = mainComposite;
				if( this.mainComposite != null ) {
					this.mainComposite.handlePreActivation();
					this.getContentPane().addCenterComponent( this.mainComposite.getView() );
				}
			}
			this.getContentPane().revalidateAndRepaint();
		}
	}

	public org.lgna.croquet.MenuBarComposite getMenuBarComposite() {
		return this.menuBarComposite;
	}

	protected abstract void setJMenuBar( javax.swing.JMenuBar jMenuBar );

	public void setMenuBarComposite( org.lgna.croquet.MenuBarComposite menuBarComposite ) {
		if( this.menuBarComposite != menuBarComposite ) {
			synchronized( this.getAwtComponent().getTreeLock() ) {
				if( this.menuBarComposite != null ) {
					this.menuBarComposite.handlePostDeactivation();
				}
				this.menuBarComposite = menuBarComposite;
				javax.swing.JMenuBar jMenuBar;
				if( this.menuBarComposite != null ) {
					this.menuBarComposite.handlePreActivation();
					jMenuBar = menuBarComposite.getView().getAwtComponent();
				} else {
					jMenuBar = null;
				}
				this.setJMenuBar( jMenuBar );
			}

		}
	}
}
