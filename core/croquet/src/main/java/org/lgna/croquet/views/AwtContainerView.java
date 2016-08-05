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
public abstract class AwtContainerView<J extends java.awt.Container> extends AwtComponentView<J> {
	//	private java.awt.event.ContainerListener containerListener = new java.awt.event.ContainerListener() {
	//		public void componentAdded(java.awt.event.ContainerEvent e) {
	//			assert e.getContainer() == Component.this.getJComponent();
	//			java.awt.Component awtComponent = e.getChild();
	//			Component<?> child = Component.lookup( awtComponent );
	//			if( child != null ) {
	//				child.handleAddedTo( Component.this );
	//			} else {
	//				if( awtComponent instanceof javax.swing.plaf.UIResource ) {
	//					//pass
	//				} else {
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for child" );
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "    parent:", Component.this );
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChild:", awtComponent );
	//				}
	//			}
	//		}
	//		public void componentRemoved(java.awt.event.ContainerEvent e) {
	//			assert e.getContainer() == Component.this.getJComponent();
	//			java.awt.Component awtComponent = e.getChild();
	//			Component<?> child = Component.lookup( awtComponent );
	//			if( child != null ) {
	//				child.handleRemovedFrom( Component.this );
	//			} else {
	//				if( awtComponent instanceof javax.swing.plaf.UIResource ) {
	//					//pass
	//				} else {
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "no croquet component for child" );
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "    parent:", Component.this );
	//					edu.cmu.cs.dennisc.print.PrintUtilities.println( "    awtChild:", awtComponent );
	//				}
	//			}
	//		}
	//	};
	@Deprecated
	public AwtComponentView<?> getComponent( int i ) {
		return AwtComponentView.lookup( this.getAwtComponent().getComponent( i ) );
	}

	@Deprecated
	public AwtComponentView<?>[] getComponents() {
		java.awt.Component[] components = this.getAwtComponent().getComponents();
		final int N = components.length;
		AwtComponentView<?>[] rv = new AwtComponentView<?>[ N ];
		for( int i = 0; i < N; i++ ) {
			rv[ i ] = AwtComponentView.lookup( components[ i ] );
		}
		return rv;
	}

	public int getComponentZOrder( AwtComponentView<?> component ) {
		return this.getAwtComponent().getComponentZOrder( component.getAwtComponent() );
	}

	public void setComponentZOrder( AwtComponentView<?> component, int index ) {
		this.checkEventDispatchThread();
		this.getAwtComponent().setComponentZOrder( component.getAwtComponent(), index );
	}

	public int getComponentCount() {
		return getAwtComponent().getComponentCount();
	}

	public boolean isAncestorOf( AwtComponentView<?> other ) {
		return this.getAwtComponent().isAncestorOf( other.getAwtComponent() );
	}

	protected final void internalAddComponent( AwtComponentView<?> component ) {
		assert component != null : this;
		assert component != this : this;
		this.checkEventDispatchThread();
		this.checkTreeLock();
		this.getAwtComponent().add( component.getAwtComponent() );
	}

	protected final void internalAddComponent( AwtComponentView<?> component, Object constraints ) {
		assert component != null : this;
		assert component != this : this;
		this.checkEventDispatchThread();
		this.checkTreeLock();
		this.getAwtComponent().add( component.getAwtComponent(), constraints );
	}

	private void internalRemoveComponent( AwtComponentView<?> component, boolean isReleaseDesired ) {
		assert component != null : this;
		assert component != this : this;
		this.checkEventDispatchThread();
		this.checkTreeLock();
		this.getAwtComponent().remove( component.getAwtComponent() );
		//		if( component.getAwtComponent().isDisplayable() ) {
		//			component.handleUndisplayable();
		//		}
		if( isReleaseDesired ) {
			if( component instanceof AwtContainerView<?> ) {
				AwtContainerView<?> container = (AwtContainerView<?>)component;
				container.internalRemoveAllComponents( true );
			}
			component.release();
		}
	}

	private final void internalRemoveAllComponents( boolean isReleaseDesired ) {
		java.awt.Component[] awtComponents = this.getAwtComponent().getComponents();
		for( java.awt.Component awtComponent : awtComponents ) {
			if( awtComponent != null ) {
				AwtComponentView<?> component = lookup( awtComponent );
				this.internalRemoveComponent( component, isReleaseDesired );
			} else {
				edu.cmu.cs.dennisc.java.util.logging.Logger.warning( "encountered null component", this );
			}
		}
	}

	protected void internalRemoveComponent( AwtComponentView<?> component ) {
		this.internalRemoveComponent( component, false );
	}

	protected final void internalRemoveAllComponents() {
		this.internalRemoveAllComponents( false );
	}

	protected void internalForgetAndRemoveComponent( AwtComponentView<?> component ) {
		this.internalRemoveComponent( component, true );
		//		edu.cmu.cs.dennisc.java.awt.ForgetUtilities.forgetAndRemoveComponent( this.getAwtComponent(), component.getAwtComponent(), forgetObserver );
		//		this.repaint();
	}

	protected void internalForgetAndRemoveAllComponents() {
		this.internalRemoveAllComponents( true );
		//		edu.cmu.cs.dennisc.java.awt.ForgetUtilities.forgetAndRemoveAllComponents( this.getAwtComponent(), forgetObserver );
		//		this.repaint();
	}
}
