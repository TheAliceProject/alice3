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
package org.lgna.project;

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

	private class Properties {
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
	
	private org.lgna.project.ast.NamedUserType programType = null;
	private java.util.Set< org.alice.virtualmachine.Resource > resources = edu.cmu.cs.dennisc.java.util.concurrent.Collections.newCopyOnWriteArraySet();
	private Properties properties = new Properties();
	public Project( org.lgna.project.ast.NamedUserType programType, java.util.Set< org.alice.virtualmachine.Resource > resources ) {
		this( programType );
		this.resources.addAll( resources );
	}
	public Project( org.lgna.project.ast.NamedUserType programType ) {
		this.programType = programType;
	}
	public org.lgna.project.ast.NamedUserType getProgramType() {
		return this.programType;
	}


	private static String IS_SCOPE_RESPECTED_KEY = Project.class.getName() + ".IS_SCOPE_RESPECTED";
	public boolean isScopeRespected() {
		return getBooleanProperty( IS_SCOPE_RESPECTED_KEY, true );
	}
	public void putScopeRespected( boolean isScopeRespected ) {
		putBooleanProperty( IS_SCOPE_RESPECTED_KEY, isScopeRespected );
	}
	
	public String getStringProperty( String key, String def ) {
		return this.properties.getString( key, def );
	}
	public boolean getBooleanProperty( String key, boolean def ) {
		return this.properties.getBoolean( key, def );
	}
	public int getIntegerProperty( String key, int def ) {
		return this.properties.getInteger( key, def );
	}
	public long getLongProperty( String key, long def ) {
		return this.properties.getLong( key, def );
	}
	public float getFloatProperty( String key, float def ) {
		return this.properties.getFloat( key, def );
	}
	public double getDoubleProperty( String key, double def ) {
		return this.properties.getDouble( key, def );
	}
	public byte[] getByteArrayProperty( String key, byte[] def ) {
		return this.properties.getByteArray( key, def );
	}
	public void putStringProperty( String key, String value ) {
		this.properties.putString( key, value );
	}
	public void putBooleanProperty( String key, boolean value ) {
		this.properties.putBoolean( key, value );
	}
	public void putIntegerProperty( String key, int value ) {
		this.properties.putInteger( key, value );
	}
	public void putLongProperty( String key, long value ) {
		this.properties.putLong( key, value );
	}
	public void putFloatProperty( String key, float value ) {
		this.properties.putFloat( key, value );
	}
	public void putDoubleProperty( String key, double value ) {
		this.properties.putDouble( key, value );
	}
	public void putByteArrayProperty( String key, byte[] value ) {
		this.properties.putByteArray( key, value );
	}
	
	public java.util.Set< String > getPropertyKeySet() {
		return this.properties.map.keySet();
	}
	public void readProperties( java.io.BufferedInputStream bis ) {
		this.properties.read( bis );
	}
	public void writeProperties( java.io.BufferedOutputStream bos ) throws java.io.IOException {
		this.properties.write( bos );
	}
	
	public void addResource( org.alice.virtualmachine.Resource resource ) {
		synchronized( this.resources ) {
			if( this.resources.contains( resource ) ) {
				//todo
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "already contains resource:", resource );
			} else {
				this.resources.add( resource );
			}
		}
	}
	public void removeResource( org.alice.virtualmachine.Resource resource ) {
		synchronized( this.resources ) {
			this.resources.remove( resource );
		}
	}
	public java.util.Set< org.alice.virtualmachine.Resource > getResources() {
		return this.resources;
	}
}
