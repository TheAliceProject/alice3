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

package edu.cmu.cs.dennisc.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public abstract class Composite extends Component {
	private java.util.List< Component > m_children = new java.util.LinkedList< Component >();
	private java.util.List< edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener > m_childrenListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener >();

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		super.accept( visitor );
		for( Component child : m_children ) {
			child.accept( visitor );
		}
	}

	@Override
	protected void actuallyRelease() {
		super.actuallyRelease();
		for( Component child : m_children ) {
			child.release();
		}
	}

	public boolean isAncestorOf( Component component ) {
		if( component == null ) {
			return false;
		} else {
			return component.isDescendantOf( this );
		}
	}

	public boolean isSceneOf( Composite other ) {
		return false;
	}
	public boolean isParentOf( Composite other ) {
		return this == other.getParent();
	}
	public boolean isLocalOf( Composite other ) {
		return this == other;
	}

	protected void fireChildAdded( Component child ) {
		assert child != this;
		//System.err.println( "fireChildAdded: " + Thread.currentThread() );
		synchronized( m_children ) {
			m_children.add( child );
		}
		edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent( this, child );
		synchronized( m_childrenListeners ) {
			for( edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener childrenListener : m_childrenListeners ) {
				childrenListener.componentAdded( e );
			}
		}
		//todo: investigate
		//for( edu.cmu.cs.dennisc.scenegraph.event.ChildrenListener childrenListener : m_childrenListeners ) {
		//	childrenListener.childAdded( e );
		//}
	}
	protected void fireChildRemoved( Component child ) {
		//System.err.println( "fireChildRemoved: " + Thread.currentThread() );
		synchronized( m_children ) {
			m_children.remove( child );
		}
		edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent e = new edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent( this, child );
		synchronized( m_childrenListeners ) {
			for( edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener childrenListener : m_childrenListeners ) {
				childrenListener.componentRemoved( e );
			}
		}
		//todo: investigate
		//for( edu.cmu.cs.dennisc.scenegraph.event.ChildrenListener childrenListener : m_childrenListeners ) {
		//	childrenListener.childRemoved( e );
		//}
	}

	public void addComponent( Component component ) {
		assert component != this;
		component.setParent( this );
	}
	public void removeComponent( Component component ) {
		if( component.getParent() == this ) {
			component.setParent( null );
		} else {
			throw new RuntimeException();
		}
	}

	public int getComponentCount() {
		return m_children.size();
	}
	public int getIndexOfComponent( Component component ) {
		return m_children.indexOf( component );
	}
	public Component getComponentAt( int i ) {
		return m_children.get( i );
	}
	public Component[] getComponents( Component[] rv ) {
		synchronized( m_children ) {
			return m_children.toArray( rv );
		}
	}
	public Component[] getComponents() {
		return getComponents( new Component[ getComponentCount() ] );
	}
	public Iterable< Component > accessComponents() {
		return m_children;
	}

	public void addChildrenListener( edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener childrenListener ) {
		//System.err.println( "addChildrenListener: " + Thread.currentThread() );
		synchronized( m_childrenListeners ) {
			m_childrenListeners.add( childrenListener );
		}
	}
	public void removeChildrenListener( edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener childrenListener ) {
		//System.err.println( "removeChildrenListener: " + Thread.currentThread() );
		synchronized( m_childrenListeners ) {
			m_childrenListeners.remove( childrenListener );
		}
	}
	public Iterable< edu.cmu.cs.dennisc.scenegraph.event.ComponentsListener > getChildrenListenerIterable() {
		return m_childrenListeners;
	}

	@Override
	protected void fireAbsoluteTransformationChange() {
		super.fireAbsoluteTransformationChange();
		synchronized( m_children ) {
			for( Component child : m_children ) {
				child.fireAbsoluteTransformationChange();
			}
		}
	}
	@Override
	protected void fireHierarchyChanged() {
		super.fireHierarchyChanged();
		synchronized( m_children ) {
			for( Component child : m_children ) {
				child.fireHierarchyChanged();
			}
		}
	}

	@Override
	public Element newCopy() {
		Composite rv = (Composite)super.newCopy();
		synchronized( m_children ) {
			for( Component component : m_children ) {
				Component rvComponent = (Component)component.newCopy();
				rvComponent.setParent( rv );
			}
		}
		return rv;
	}
	
}
