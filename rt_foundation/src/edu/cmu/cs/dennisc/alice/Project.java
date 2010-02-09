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
package edu.cmu.cs.dennisc.alice;

import org.alice.virtualmachine.Resource;

/**
 * @author Dennis Cosgrove
 */
public class Project {
	private static int readInt( java.io.BufferedInputStream bis ) throws java.io.IOException {
		byte[] lengthArray = new byte[ 4 ];
		bis.read( lengthArray );
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( lengthArray );
		return buffer.getInt();
	}
	private static String readString( java.io.BufferedInputStream bis ) throws java.io.IOException {
		int length = readInt( bis );
		byte[] stringArray = new byte[ length ];
		bis.read( stringArray );
		return new String( stringArray );
	}
	private static void writeInt( java.io.BufferedOutputStream bos, int i ) throws java.io.IOException {
		byte[] array = new byte[ 4 ];
		java.nio.ByteBuffer buffer = java.nio.ByteBuffer.wrap( array );
		buffer.putInt( i );
		bos.write( array );
	}
	private static void writeString( java.io.BufferedOutputStream bos, String s ) throws java.io.IOException {
		writeInt( bos, s.length() );
		bos.write( s.getBytes() );
	}

	public class Properties {
		private java.util.Map< String, String > map = new java.util.HashMap< String, String >();
		public void read( java.io.BufferedInputStream bis ) {
			if( bis != null ) {
				try {
					String version = readString( bis );
					int count = readInt( bis );
					for( int i=0; i<count; i++ ) {
						String key = readString( bis );
						String value = readString( bis );
						this.map.put( key, value );
					}
				} catch( Throwable t ) {
					this.map.clear();
					t.printStackTrace();
				}
			}
		}
		public void write( java.io.BufferedOutputStream bos ) throws java.io.IOException {
			String version = Version.getCurrentVersionText();
			writeString( bos, version );
			synchronized( this.map ) {
				writeInt( bos, this.map.size() );
				for( String key : this.map.keySet() ) {
					String value = this.map.get( key );
					writeString( bos, key );
					writeString( bos, value );
				}
			}
			bos.flush();
		}
		
		public String getString( String key, String def ) {
			String rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = value;
			} else {
				rv = def; 
			}
			return rv;
		}
		public boolean getBoolean( String key, boolean def ) {
			boolean rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = Boolean.valueOf( value );
			} else {
				rv = def; 
			}
			return rv;
		}
		public int getInteger( String key, int def ) {
			int rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = Integer.valueOf( value );
			} else {
				rv = def; 
			}
			return rv;
		}
		public long getLong( String key, long def ) {
			long rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = Long.valueOf( value );
			} else {
				rv = def; 
			}
			return rv;
		}
		public float getFloat( String key, float def ) {
			float rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = Float.valueOf( value );
			} else {
				rv = def; 
			}
			return rv;
		}
		public double getDouble( String key, double def ) {
			double rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = Double.valueOf( value );
			} else {
				rv = def; 
			}
			return rv;
		}
		public byte[] getByteArray( String key, byte[] def ) {
			byte[] rv;
			String value = this.map.get( key );
			if( value != null ) {
				rv = value.getBytes();
			} else {
				rv = def; 
			}
			return rv;
		}
		public void putString( String key, String value ) {
			this.map.put( key, value );
		}
		public void putBoolean( String key, boolean value ) {
			this.map.put( key, Boolean.toString( value ) );
		}
		public void putInteger( String key, int value ) {
			this.map.put( key, Integer.toString( value ) );
		}
		public void putLong( String key, long value ) {
			this.map.put( key, Long.toString( value ) );
		}
		public void putFloat( String key, float value ) {
			this.map.put( key, Float.toString( value ) );
		}
		public void putDouble( String key, double value ) {
			this.map.put( key, Double.toString( value ) );
		}
		public void putByteArray( String key, byte[] value ) {
			this.map.put( key, new String( value ) );
		}
	}
	
	private java.util.Set< Resource > resources = java.util.Collections.synchronizedSet( new java.util.HashSet< Resource >() );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType programType = null;
	private Properties properties = new Properties();
	public Project( edu.cmu.cs.dennisc.alice.ast.AbstractType programType, java.util.Set< Resource > resources ) {
		this( programType );
		synchronized( this.resources ) {
			this.resources.addAll( resources );
		}
	}
	public Project( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		setProgramType( programType );
	}
	public edu.cmu.cs.dennisc.alice.ast.AbstractType getProgramType() {
		return this.programType;
	}
	/*public*/private void setProgramType( edu.cmu.cs.dennisc.alice.ast.AbstractType programType ) {
		this.programType = programType;
	}
	public Properties getProperties() {
		return this.properties;
	}
	
	public void addResource( Resource resource ) {
		synchronized( this.resources ) {
			this.resources.add( resource );
		}
	}
	public void deleteResource( Resource resource ) {
		synchronized( this.resources ) {
			this.resources.remove( resource );
		}
	}
	public java.util.Set< Resource > getResources() {
		return this.resources;
	}
//	/*public*/private void setProperties( Properties properties ) {
//		if( properties != null ) {
//			//pass
//		} else {
//			properties = new Properties();
//		}
//		this.properties = properties;
//	}
}
