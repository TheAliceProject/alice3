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
package edu.cmu.cs.dennisc.pattern;

/**
 * @author Dennis Cosgrove
 */

public class DefaultReleasable implements Releasable {
	private java.util.Vector<edu.cmu.cs.dennisc.pattern.event.ReleaseListener> m_releaseListeners = new java.util.Vector<edu.cmu.cs.dennisc.pattern.event.ReleaseListener>();
	
	protected void actuallyRelease() {
	}
	
	public final void release() {
		if( m_releaseListeners.size() > 0 ) {
			edu.cmu.cs.dennisc.pattern.event.ReleaseListener[] releaseListeners = new edu.cmu.cs.dennisc.pattern.event.ReleaseListener[ m_releaseListeners.size() ];
			m_releaseListeners.toArray( releaseListeners );
			
			edu.cmu.cs.dennisc.pattern.event.ReleaseEvent e = new edu.cmu.cs.dennisc.pattern.event.ReleaseEvent( this );
			for( edu.cmu.cs.dennisc.pattern.event.ReleaseListener releaseListener : releaseListeners ) {
				releaseListener.releasing( e );
			}
			
			actuallyRelease();
			
			for( edu.cmu.cs.dennisc.pattern.event.ReleaseListener releaseListener : releaseListeners ) {
				releaseListener.released( e );
			}
		}
	}

	public void addReleaseListener( edu.cmu.cs.dennisc.pattern.event.ReleaseListener releaseListener ) {
		m_releaseListeners.addElement( releaseListener );
	}
	public void removeReleaseListener( edu.cmu.cs.dennisc.pattern.event.ReleaseListener releaseListener ) {
		m_releaseListeners.removeElement( releaseListener );
	}
	public Iterable< edu.cmu.cs.dennisc.pattern.event.ReleaseListener > accessReleaseListeners() {
		return m_releaseListeners;
	}
}
