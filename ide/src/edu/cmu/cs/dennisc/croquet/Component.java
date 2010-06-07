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
public abstract class Component<J extends java.awt.Component> implements TrackableShape {
	public interface ContainmentObserver {
		public void addedTo( Component<?> parent );
		public void removedFrom( Component<?> parent );
	}
	private static java.util.Map<java.awt.Component, Component<?>> map = edu.cmu.cs.dennisc.java.util.Collections.newWeakHashMap();
	private static class InternalAwtContainerAdapter extends Container< java.awt.Container > {
		private java.awt.Container awtContainer;
		public InternalAwtContainerAdapter( java.awt.Container awtContainer ) {
			this.awtContainer = awtContainer;
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "creating adapter for: ", awtContainer.getClass(), awtContainer.hashCode() );
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
			edu.cmu.cs.dennisc.print.PrintUtilities.println( "creating adapter for: ", awtComponent.getClass(), awtComponent.hashCode() );
		}
		@Override
		protected java.awt.Component createAwtComponent() {
			return this.awtComponent;
		}
	}

	//todo reduce visibility to /* package-private */
	public static Component<?> lookup(java.awt.Component awtComponent) {
		if (awtComponent != null) {
			Component<?> rv = Component.map.get(awtComponent);
			if( rv != null ) {
				//pass
			} else {
				if (awtComponent instanceof java.awt.Container) {
					java.awt.Container awtContainer = (java.awt.Container) awtComponent;
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
		public void hierarchyChanged(java.awt.event.HierarchyEvent e) {
			long flags = e.getChangeFlags();
			if( ( flags & java.awt.event.HierarchyEvent.PARENT_CHANGED ) != 0 ) {
				java.awt.Component awtComponent = e.getComponent();
				java.awt.Component awtChanged = e.getChanged();
				java.awt.Container awtParent = e.getChangedParent();

				assert awtComponent == Component.this.getAwtComponent();
				
				if( awtComponent == awtChanged ) {
					if( awtParent != Component.this.awtParent ) {
						Component.this.handleParentChange( awtParent );
					} else {
						//Thread.dumpStack();
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "investigate: hierarchyChanged seems to not be actually changing the parent" );
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "    flags:", flags );
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "    this:", this );
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChanged:", awtChanged.getClass().getName(), awtChanged );
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtParent:", awtParent.getClass().getName(), awtParent.getLayout() );
					}
				}
			}
		}
	};

	private java.awt.Container awtParent;
	private void handleParentChange( java.awt.Container awtParent ) {
		if( this.awtParent != null ) {
			Component<?> parent = Component.lookup( this.awtParent );
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
			Component<?> parent = Component.lookup( this.awtParent );
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
	private java.util.List< ContainmentObserver > containmentObservers = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArrayList();
	public void addContainmentObserver( ContainmentObserver containmentObserver ) {
		this.containmentObservers.add( containmentObserver );
	}
	public void removeContainmentObserver( ContainmentObserver containmentObserver ) {
		this.containmentObservers.remove( containmentObserver );
	}

	protected void handleAddedTo( Component<?> parent ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "added:", this.hashCode(), parent != null ? parent.hashCode() : 0 );
		for( ContainmentObserver containmentObserver : this.containmentObservers ) {
			containmentObserver.addedTo(parent);
		}
	}
	protected void handleRemovedFrom( Component<?> parent ) {
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( "removed:", this.hashCode(), parent != null ? parent.hashCode() : 0 );
		for( ContainmentObserver containmentObserver : this.containmentObservers ) {
			containmentObserver.removedFrom(parent);
		}
	}
	
	private J awtComponent;
	protected abstract J createAwtComponent();

	///*package-private*/ final J getAwtComponent() {
	// todo: reduce visibility to /*package-private*/
	public final J getAwtComponent() {
		if (this.awtComponent != null) {
			// pass
		} else {
			this.awtComponent = this.createAwtComponent();
			this.awtComponent.addHierarchyListener( this.hierarchyListener );
			//this.jComponent.addContainerListener( this.containerListener );
			this.awtComponent.setName( this.getClass().getName() );
			Component.map.put(this.awtComponent, this);
		}
		return this.awtComponent;
	}
	
	public void release() {
		if( this.awtComponent != null ) {
			//this.jComponent.removeContainerListener( this.containerListener );
			this.awtComponent.removeHierarchyListener( this.hierarchyListener );
			Component.map.remove(this.awtComponent);
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
		if( font != null ) {
			this.getAwtComponent().setFont( font );
		} else {
			throw new NullPointerException();
		}
	}
	
	public final void scaleFont(float scaleFactor) {
		this.setFont( edu.cmu.cs.dennisc.java.awt.FontUtilities.scaleFont(this.getAwtComponent(), scaleFactor) );
	}
	public final void setFontSize(float fontSize) {
		this.setFont( this.getFont().deriveFont(fontSize) );
	}
	public final void changeFont(edu.cmu.cs.dennisc.java.awt.font.TextAttribute<?>... textAttributes) {
		this.setFont( edu.cmu.cs.dennisc.java.awt.FontUtilities.deriveFont(this.getAwtComponent(), textAttributes ) );
	}

	/*package-private*/ boolean isEnabled() {
		return this.getAwtComponent().isEnabled();
	}
	/*package-private*/ void setEnabled(boolean isEnabled) {
		this.getAwtComponent().setEnabled(isEnabled);
	}

	public java.awt.Color getForegroundColor() {
		return this.getAwtComponent().getForeground();
	}

	public void setForegroundColor(java.awt.Color color) {
		this.getAwtComponent().setForeground(color);
	}

	public java.awt.Color getBackgroundColor() {
		return this.getAwtComponent().getBackground();
	}

	public void setBackgroundColor(java.awt.Color color) {
		this.getAwtComponent().setBackground(color);
	}

	public boolean isVisible() {
		return this.getAwtComponent().isVisible();
	}

	public void setVisible(boolean isVisible) {
		this.getAwtComponent().setVisible(isVisible);
	}

	public boolean isOpaque() {
		return this.getAwtComponent().isOpaque();
	}


	public boolean getIgnoreRepaint() {
		return this.getAwtComponent().getIgnoreRepaint();
	}

	public void setIgnoreRepaint(boolean ignoreRepaint) {
		this.getAwtComponent().setIgnoreRepaint(ignoreRepaint);
	}


	public java.awt.Cursor getCursor() {
		return this.getAwtComponent().getCursor();
	}

	public void setCursor(java.awt.Cursor cursor) {
		this.getAwtComponent().setCursor(cursor);
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

	public java.awt.Point getLocationOnScreen() {
		return this.getAwtComponent().getLocationOnScreen();
	}

	public java.awt.Rectangle getLocalBounds() {
		return javax.swing.SwingUtilities.getLocalBounds( this.getAwtComponent() );
	}
	public java.awt.Rectangle getBounds() {
		return this.getAwtComponent().getBounds();
	}
	public java.awt.Rectangle getBounds( Component< ? > asSeenBy ) {
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
	public final java.awt.Rectangle getVisibleRectangle( Component< ? > destination ) {
		return this.convertRectangle( this.getVisibleRectangle(), destination );
	}

	/*package-private*/ static java.awt.Rectangle inset( java.awt.Rectangle rv, java.awt.Insets insets ) {
		if( insets != null ) {
			rv.x -= insets.left;
			rv.y -= insets.top;
			rv.width += insets.left + insets.right;
			rv.height += insets.top + insets.bottom;
		}
		return rv;
	}
	public java.awt.Shape getShape( Component< ? > asSeenBy, java.awt.Insets insets ) {
		java.awt.Rectangle rv = this.getBounds( asSeenBy );
		return inset( rv, insets );
	}
	public java.awt.Shape getVisibleShape( Component< ? > asSeenBy, java.awt.Insets insets ) {
		java.awt.Rectangle rv = this.getVisibleRectangle( asSeenBy );
		return inset( rv, insets );
	}

	@Deprecated
	public java.awt.Point convertPoint(java.awt.Point pt, java.awt.Component asSeenBy) {
		return javax.swing.SwingUtilities.convertPoint(this.getAwtComponent(), pt, asSeenBy);
	}

	public java.awt.Point convertPoint(java.awt.Point pt, Component<?> asSeenBy) {
		return javax.swing.SwingUtilities.convertPoint(this.getAwtComponent(), pt, asSeenBy.getAwtComponent());
	}

	@Deprecated
	public java.awt.Rectangle convertRectangle(java.awt.Rectangle rectangle, java.awt.Component asSeenBy) {
		return javax.swing.SwingUtilities.convertRectangle(this.getAwtComponent(), rectangle, asSeenBy);
	}

	public java.awt.Rectangle convertRectangle(java.awt.Rectangle rectangle, Component<?> asSeenBy) {
		return javax.swing.SwingUtilities.convertRectangle(this.getAwtComponent(), rectangle, asSeenBy.getAwtComponent());
	}

	@Deprecated
	public java.awt.event.MouseEvent convertMouseEvent(java.awt.event.MouseEvent e, java.awt.Component asSeenBy) {
		return edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(this.getAwtComponent(), e, asSeenBy);
	}

	public java.awt.event.MouseEvent convertMouseEvent(java.awt.event.MouseEvent e, Component<?> asSeenBy) {
		return edu.cmu.cs.dennisc.javax.swing.SwingUtilities.convertMouseEvent(this.getAwtComponent(), e, asSeenBy.getAwtComponent());
	}


	@Deprecated
	public Container<?> getParent() {
		return (Container<?>)Component.lookup(this.getAwtComponent().getParent());
	}

	@Deprecated
	public AbstractWindow<?> getRoot() {
		return AbstractWindow.lookup(javax.swing.SwingUtilities.getRoot(this.getAwtComponent()));
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
	public void addKeyListener(java.awt.event.KeyListener listener) {
		this.getAwtComponent().addKeyListener(listener);
	}

	@Deprecated
	public void removeKeyListener(java.awt.event.KeyListener listener) {
		this.getAwtComponent().removeKeyListener(listener);
	}

	@Deprecated
	public void addMouseListener(java.awt.event.MouseListener listener) {
		this.getAwtComponent().addMouseListener(listener);
	}

	@Deprecated
	public void removeMouseListener(java.awt.event.MouseListener listener) {
		this.getAwtComponent().removeMouseListener(listener);
	}

	@Deprecated
	public void addMouseMotionListener(java.awt.event.MouseMotionListener listener) {
		this.getAwtComponent().addMouseMotionListener(listener);
	}

	@Deprecated
	public void removeMouseMotionListener(java.awt.event.MouseMotionListener listener) {
		this.getAwtComponent().removeMouseMotionListener(listener);
	}

	@Deprecated
	public void addMouseWheelListener(java.awt.event.MouseWheelListener listener) {
		this.getAwtComponent().addMouseWheelListener(listener);
	}

	@Deprecated
	public void removeMouseWheelListener(java.awt.event.MouseWheelListener listener) {
		this.getAwtComponent().removeMouseWheelListener(listener);
	}

	@Deprecated
	public void setPreferredSize(java.awt.Dimension preferredSize) {
		this.getAwtComponent().setPreferredSize(preferredSize);
	}
		
	@Override
	public String toString() {
		return this.getAwtComponent().getClass().toString();
	}
}
