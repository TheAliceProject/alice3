/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Thing extends Model {
	static {
		edu.cmu.cs.dennisc.lookingglass.opengl.AdapterFactory.register( Thing.class, ThingAdapter.class );
	}

	private native void initialize( Object o, Object o2 );

	private native void unload();

	public void synchronizedUnload()
	{
		synchronized( renderLock ) {
			unload();
		}
	}

	private native void setTexture( Object o );

	public void synchronizedSetTexture( Object o ) {
		synchronized( renderLock ) {
			setTexture( o );
		}
	}

	public Thing( Object o, Object o2 ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		synchronized( renderLock ) {
			initialize( o, o2 );
		}
	}
}
