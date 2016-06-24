/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */
package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Thing extends Model {
	static {
		edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.register( Thing.class, ThingAdapter.class );
	}

	private final Object o;
	private final Object o2;

	public Thing( Object o, Object o2 ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		this.o = o;
		this.o2 = o2;
		synchronized( renderLock ) {
			try {
				this.initialize( o, o2 );
			} catch( RuntimeException re ) {
				System.err.println( this );
				throw re;
			}
		}
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

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( ";" );
		sb.append( this.o != null ? this.o.getClass().getName() : null );
		sb.append( "." );
		sb.append( this.o );
		sb.append( ";" );
		sb.append( this.o2 != null ? this.o2.getClass().getName() : null );
		sb.append( "." );
		sb.append( this.o2 );
	}
}
