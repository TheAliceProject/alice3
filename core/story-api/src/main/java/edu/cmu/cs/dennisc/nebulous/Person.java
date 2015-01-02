/*
 * Copyright (c) 2006-2010, Carnegie Mellon University. All rights reserved.
 */

package edu.cmu.cs.dennisc.nebulous;

/**
 * @author Dennis Cosgrove
 */
public class Person extends Model {
	static {
		edu.cmu.cs.dennisc.render.gl.imp.adapters.AdapterFactory.register( Person.class, PersonAdapter.class );
	}

	private final Object o;

	public Person( Object o ) throws edu.cmu.cs.dennisc.eula.LicenseRejectedException {
		this.o = o;
		synchronized( renderLock ) {
			initialize( o );
		}
	}

	private native void initialize( Object o );

	private native void unload();

	public void synchronizedUnload() {
		synchronized( renderLock ) {
			unload();
		}
	}

	private native void setGender( Object o );

	public void synchronizedSetGender( Object o ) {
		synchronized( renderLock ) {
			setGender( o );
		}
	}

	private native void setOutfit( Object o );

	public void synchronizedSetOutfit( Object o ) {
		synchronized( renderLock ) {
			setOutfit( o );
		}
	}

	private native void setSkinTone( Object o );

	public void synchronizedSetSkinTone( Object o ) {
		synchronized( renderLock ) {
			setSkinTone( o );
		}
	}

	private native void setObesityLevel( Object o );

	public void synchronizedSetObesityLevel( Object o ) {
		synchronized( renderLock ) {
			setObesityLevel( o );
		}
	}

	private native void setEyeColor( Object o );

	public void synchronizedSetEyeColor( Object o ) {
		synchronized( renderLock ) {
			setEyeColor( o );
		}
	}

	private native void setHair( Object o );

	public void synchronizedSetHair( Object o ) {
		synchronized( renderLock ) {
			setHair( o );
		}
	}

	private native void setFace( Object o );

	public void synchronizedSetFace( Object o ) {
		synchronized( renderLock ) {
			setFace( o );
		}
	}

	private native void setAll( Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair, Object face );

	public void synchronizedSetAll( Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair, Object face ) {
		synchronized( renderLock ) {
			setAll( gender, outfit, skinTone, obesityLevel, eyeColor, hair, face );
		}
	}

	public void synchronizedSetAll( Object gender, Object outfit, Object skinTone, Object obesityLevel, Object eyeColor, Object hair )
	{
		synchronizedSetAll( gender, outfit, skinTone, obesityLevel, eyeColor, hair, "" );
	}

	@Override
	protected void appendRepr( StringBuilder sb ) {
		super.appendRepr( sb );
		sb.append( ";" );
		sb.append( this.o != null ? this.o.getClass().getName() : null );
		sb.append( "." );
		sb.append( this.o );
	}
}
