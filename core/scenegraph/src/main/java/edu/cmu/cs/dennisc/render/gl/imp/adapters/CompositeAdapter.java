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

package edu.cmu.cs.dennisc.render.gl.imp.adapters;

import edu.cmu.cs.dennisc.render.gl.imp.AdapterFactory;
import edu.cmu.cs.dennisc.render.gl.imp.PickContext;
import edu.cmu.cs.dennisc.render.gl.imp.PickParameters;
import edu.cmu.cs.dennisc.render.gl.imp.RenderContext;

/**
 * @author Dennis Cosgrove
 */
public abstract class CompositeAdapter<E extends edu.cmu.cs.dennisc.scenegraph.Composite> extends ComponentAdapter<E> {
	private java.util.Vector<ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component>> m_componentAdapters = new java.util.Vector<ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component>>();

	public Iterable<ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component>> accessComponentAdapters() {
		return m_componentAdapters;
	}

	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		super.accept( visitor );
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> child : m_componentAdapters ) {
				child.accept( visitor );
			}
		}
	}

	public void handleComponentAdded( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter ) {
		synchronized( m_componentAdapters ) {
			m_componentAdapters.add( childAdapter );
		}
		// PrintUtilities.outln( getSceneAdapter(), " " );
	}

	public void handleComponentRemoved( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter ) {
		synchronized( m_componentAdapters ) {
			m_componentAdapters.remove( childAdapter );
		}
	}

	public static void handleComponentAdded( edu.cmu.cs.dennisc.scenegraph.event.ComponentAddedEvent e ) {
		CompositeAdapter compositeAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		compositeAdapter.handleComponentAdded( childAdapter );
	}

	public static void handleComponentRemoved( edu.cmu.cs.dennisc.scenegraph.event.ComponentRemovedEvent e ) {
		CompositeAdapter compositeAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		compositeAdapter.handleComponentRemoved( childAdapter );
	}

	@Override
	public void initialize( E sgE ) {
		super.initialize( sgE );
		Iterable<edu.cmu.cs.dennisc.scenegraph.Component> sgComponents = m_element.getComponents();
		synchronized( sgComponents ) {
			for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgComponents ) {
				handleComponentAdded( AdapterFactory.getAdapterFor( sgComponent ) );
			}
		}
	}

	@Override
	public void setup( RenderContext rc ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter : m_componentAdapters ) {
				childAdapter.setup( rc );
			}
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter : m_componentAdapters ) {
				childAdapter.renderGhost( rc, root );
			}
		}
	}

	@Override
	public void renderOpaque( RenderContext rc ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter : m_componentAdapters ) {
				childAdapter.renderOpaque( rc );
			}
		}
	}

	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component> childAdapter : m_componentAdapters ) {
				childAdapter.pick( pc, pickParameters );
			}
		}
	}
}
