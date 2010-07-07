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
public abstract class Container<J extends java.awt.Container> extends Component<J> {
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
	public Component<?> getComponent(int i) {
		return Component.lookup(this.getAwtComponent().getComponent(i));
	}

	@Deprecated
	public Component<?>[] getComponents() {
		java.awt.Component[] components = this.getAwtComponent().getComponents();
		final int N = components.length;
		Component<?>[] rv = new Component<?>[N];
		for (int i = 0; i < N; i++) {
			rv[i] = Component.lookup(components[i]);
		}
		return rv;
	}

	public int getComponentCount() {
		return getAwtComponent().getComponentCount();
	}

	public boolean isAncestorOf(Component<?> other) {
		return this.getAwtComponent().isAncestorOf(other.getAwtComponent());
	}

	protected void internalAddComponent(Component<?> component) {
		assert component != null;
		this.getAwtComponent().add(component.getAwtComponent());
	}
	protected void internalAddComponent(Component<?> component, Object constraints) {
		assert component != null;
		this.getAwtComponent().add(component.getAwtComponent(), constraints);
	}
	protected void internalRemoveComponent(Component<?> component) {
		assert component != null;
		this.getAwtComponent().remove(component.getAwtComponent());
	}

	protected final void internalRemoveAllComponents() {
		this.getAwtComponent().removeAll();
	}
	protected void internalForgetAndRemoveComponent( Component<?> component ) {
		if( component instanceof Container<?> ) {
			edu.cmu.cs.dennisc.java.awt.ForgetUtilities.forgetAndRemoveAllComponents( ((Container<?>)component).getAwtComponent() );
		}
		this.internalRemoveComponent( component );
		this.repaint();
	}
	protected void internalForgetAndRemoveAllComponents() {
		edu.cmu.cs.dennisc.java.awt.ForgetUtilities.forgetAndRemoveAllComponents( this.getAwtComponent() );
		this.repaint();
	}
}
