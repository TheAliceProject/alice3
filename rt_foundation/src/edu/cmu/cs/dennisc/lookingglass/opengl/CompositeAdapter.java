/*
 * Copyright (c) 2006-2009, Carnegie Mellon University. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 * 
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 
 * 3. Products derived from the software may not be called "Alice",
 *    nor may "Alice" appear in their name, without prior written
 *    permission of Carnegie Mellon University.
 * 
 * 4. All advertising materials mentioning features or use of this software
 *    must display the following acknowledgement:
 *    "This product includes software developed by Carnegie Mellon University"
 */

package edu.cmu.cs.dennisc.lookingglass.opengl;

/**
 * @author Dennis Cosgrove
 */
public abstract class CompositeAdapter< E extends edu.cmu.cs.dennisc.scenegraph.Composite > extends ComponentAdapter< E > {
	private java.util.Vector<ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component >> m_componentAdapters = new java.util.Vector<ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component >>();

	public Iterable< ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > > accessComponentAdapters() {
		return m_componentAdapters;
	}
	
	@Override
	public void accept( edu.cmu.cs.dennisc.pattern.Visitor visitor ) {
		super.accept( visitor );
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > child : m_componentAdapters ) {
				child.accept( visitor );
			}
		}
	}

	public void handleChildAdded( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter ) {
		synchronized( m_componentAdapters ) {
			m_componentAdapters.add( childAdapter );
		}
		// PrintUtilities.outln( getSceneAdapter(), " " );
	}
	public void handleChildRemoved( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter ) {
		synchronized( m_componentAdapters ) {
			m_componentAdapters.remove( childAdapter );
		}
	}

	public static void handleChildAdded( edu.cmu.cs.dennisc.scenegraph.event.ChildAddedEvent e ) {
		CompositeAdapter compositeAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		compositeAdapter.handleChildAdded( childAdapter );
	}
	public static void handleChildRemoved( edu.cmu.cs.dennisc.scenegraph.event.ChildRemovedEvent e ) {
		CompositeAdapter compositeAdapter = AdapterFactory.getAdapterFor( e.getTypedSource() );
		ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter = AdapterFactory.getAdapterFor( e.getChild() );
		compositeAdapter.handleChildRemoved( childAdapter );
	}

	//	Iterable<ComponentAdapter> accessChildAdapters() {
	//		synchronized( m_children ) {
	//			return m_children;
	//		}
	//	}

	@Override
	public void initialize( E sgE ) {
		super.initialize( sgE );
		Iterable< edu.cmu.cs.dennisc.scenegraph.Component > sgComponents = m_element.accessComponents();
		synchronized( sgComponents ) {
			for( edu.cmu.cs.dennisc.scenegraph.Component sgComponent : sgComponents ) {
				handleChildAdded( AdapterFactory.getAdapterFor( sgComponent ) );
			}
		}
	}

	@Override
	public void setup( RenderContext rc ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter : m_componentAdapters ) {
				childAdapter.setup( rc );
			}
		}
	}

	@Override
	public void renderGhost( RenderContext rc, GhostAdapter root ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter : m_componentAdapters ) {
				childAdapter.renderGhost( rc, root );
			}
		}
	}
	
	@Override
	public void renderOpaque( RenderContext rc ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter : m_componentAdapters ) {
				childAdapter.renderOpaque( rc );
			}
		}
	}
	@Override
	public void pick( PickContext pc, PickParameters pickParameters ) {
		synchronized( m_componentAdapters ) {
			for( ComponentAdapter<? extends edu.cmu.cs.dennisc.scenegraph.Component > childAdapter : m_componentAdapters ) {
				childAdapter.pick( pc, pickParameters );
			}
		}
	}
}
