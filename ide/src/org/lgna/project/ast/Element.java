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
package org.lgna.project.ast;

/**
 * @author Dennis Cosgrove
 */
import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;
import edu.cmu.cs.dennisc.property.Property;

//todo: clean up
public abstract class Element implements InstancePropertyOwner, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable {
	private static final boolean IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS = true;

	private static java.util.HashMap<Class<? extends edu.cmu.cs.dennisc.property.PropertyOwner>, java.util.List<Property<?>>> s_classToPropertiesMap = new java.util.HashMap<Class<? extends edu.cmu.cs.dennisc.property.PropertyOwner>, java.util.List<Property<?>>>();
	private java.util.List<Property<?>> m_properties = null;

	private java.util.List<edu.cmu.cs.dennisc.property.event.PropertyListener> m_propertyListeners = new java.util.LinkedList<edu.cmu.cs.dennisc.property.event.PropertyListener>();
	private java.util.List<edu.cmu.cs.dennisc.property.event.ListPropertyListener<?>> m_listPropertyListeners = new java.util.LinkedList<edu.cmu.cs.dennisc.property.event.ListPropertyListener<?>>();

	public void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		m_propertyListeners.add( propertyListener );
	}

	public void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		m_propertyListeners.remove( propertyListener );
	}

	public Iterable<edu.cmu.cs.dennisc.property.event.PropertyListener> accessPropertyListeners() {
		return m_propertyListeners;
	}

	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : m_propertyListeners ) {
			propertyListener.propertyChanging( e );
		}
	}

	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : m_propertyListeners ) {
			propertyListener.propertyChanged( e );
		}
	}

	public void addListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> listPropertyListener ) {
		m_listPropertyListeners.add( listPropertyListener );
	}

	public void removeListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> listPropertyListener ) {
		m_listPropertyListeners.remove( listPropertyListener );
	}

	public Iterable<edu.cmu.cs.dennisc.property.event.ListPropertyListener<?>> accessListPropertyListeners() {
		return m_listPropertyListeners;
	}

	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.adding( e );
		}
	}

	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.added( e );
		}
	}

	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.clearing( e );
		}
	}

	public void fireCleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.cleared( e );
		}
	}

	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.removing( e );
		}
	}

	public void fireRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.removed( e );
		}
	}

	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.setting( e );
		}
	}

	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : m_listPropertyListeners ) {
			l.set( e );
		}
	}

	public Property<?> getPropertyNamed( String name ) {
		//todo: remove
		name = Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
		try {
			java.lang.reflect.Field field = getClass().getField( name );
			return (Property<?>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this );
		} catch( NoSuchFieldException nsfe ) {
			return null;
		}
	}

	public InstanceProperty<?> getInstancePropertyNamed( String name ) {
		return (InstanceProperty<?>)getPropertyNamed( name );
	}

	public java.util.List<Property<?>> getProperties() {
		Class<? extends edu.cmu.cs.dennisc.property.PropertyOwner> cls = getClass();
		if( m_properties == null ) {
			m_properties = new java.util.LinkedList<Property<?>>();
			for( java.lang.reflect.Field field : cls.getFields() ) {
				int modifiers = field.getModifiers();
				if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
					if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
						//pass
					} else {
						if( InstanceProperty.class.isAssignableFrom( field.getType() ) ) {
							InstanceProperty instanceProperty = (InstanceProperty)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this );
							assert instanceProperty.getOwner() == this;
							m_properties.add( instanceProperty );
						}
					}
				}
			}
		}
		return m_properties;
	}

	public String lookupNameFor( InstanceProperty<?> instanceProperty ) {
		for( java.lang.reflect.Field field : getClass().getFields() ) {
			if( Property.class.isAssignableFrom( field.getType() ) ) {
				int modifiers = field.getModifiers();
				if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
					if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
						//pass
					} else {
						if( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this ) == instanceProperty ) {
							return field.getName();
						}
					}
				}
			}
		}
		return null;
	}

	private Object decodeObject( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Class valueCls, java.util.Map<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable> map ) {
		Object rv;
		if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
			rv = binaryDecoder.decodeBinaryEncodableAndDecodable();
		} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
			rv = binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable( map );
		} else if( java.nio.ByteBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeByteBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.CharBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeCharBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.ShortBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeShortBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.IntBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeIntBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.LongBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeLongBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.FloatBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeFloatBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( java.nio.DoubleBuffer.class.isAssignableFrom( valueCls ) ) {
			rv = edu.cmu.cs.dennisc.codec.BufferUtilities.decodeDoubleBuffer( binaryDecoder, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
		} else if( Boolean.class == valueCls ) {
			rv = binaryDecoder.decodeBoolean();
		} else if( Byte.class == valueCls ) {
			rv = binaryDecoder.decodeByte();
		} else if( Character.class == valueCls ) {
			rv = binaryDecoder.decodeChar();
		} else if( Double.class == valueCls ) {
			rv = binaryDecoder.decodeDouble();
		} else if( Float.class == valueCls ) {
			rv = binaryDecoder.decodeFloat();
		} else if( Integer.class == valueCls ) {
			rv = binaryDecoder.decodeInt();
		} else if( Long.class == valueCls ) {
			rv = binaryDecoder.decodeLong();
		} else if( Short.class == valueCls ) {
			rv = binaryDecoder.decodeShort();
		} else if( String.class == valueCls ) {
			rv = binaryDecoder.decodeString();
		} else if( Enum.class.isAssignableFrom( valueCls ) ) {
			Enum e = binaryDecoder.decodeEnum();
			rv = e;
		} else {
			throw new RuntimeException( valueCls.getName() );
		}
		return rv;
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, java.util.Map<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable> map ) {
		while( true ) {
			String propertyName = binaryDecoder.decodeString();
			if( propertyName.length() > 0 ) {
				Property property = getPropertyNamed( propertyName );
				assert property != null;
				String valueClsName = binaryDecoder.decodeString();
				assert valueClsName != null;
				Object value;
				if( valueClsName.equals( "" ) ) {
					value = null;
				} else {
					Class valueCls = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( valueClsName );
					if( valueCls.isArray() ) {
						if( boolean[].class == valueCls ) {
							value = binaryDecoder.decodeBooleanArray();
						} else if( byte[].class == valueCls ) {
							value = binaryDecoder.decodeByteArray();
						} else if( char[].class == valueCls ) {
							value = binaryDecoder.decodeCharArray();
						} else if( double[].class == valueCls ) {
							value = binaryDecoder.decodeDoubleArray();
						} else if( float[].class == valueCls ) {
							value = binaryDecoder.decodeFloatArray();
						} else if( int[].class == valueCls ) {
							value = binaryDecoder.decodeIntArray();
						} else if( long[].class == valueCls ) {
							value = binaryDecoder.decodeLongArray();
						} else if( short[].class == valueCls ) {
							value = binaryDecoder.decodeShortArray();
						} else if( String[].class == valueCls ) {
							value = binaryDecoder.decodeStringArray();
						} else if( Enum[].class.isAssignableFrom( valueCls ) ) {
							value = binaryDecoder.decodeEnumArray( valueCls.getComponentType() );
						} else if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable[].class.isAssignableFrom( valueCls ) ) {
							value = binaryDecoder.decodeBinaryEncodableAndDecodableArray( valueCls.getComponentType() );
						} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom( valueCls ) ) {
							value = binaryDecoder.decodeReferenceableBinaryEncodableAndDecodableArray( valueCls.getComponentType(), map );
						} else {
							int length = binaryDecoder.decodeInt();
							value = java.lang.reflect.Array.newInstance( valueCls.getComponentType(), length );
							for( int i = 0; i < length; i++ ) {
								java.lang.reflect.Array.set( value, i, decodeObject( binaryDecoder, valueCls.getComponentType(), map ) );
							}
						}
					} else if( java.util.Collection.class.isAssignableFrom( valueCls ) ) {
						int size = binaryDecoder.decodeInt();
						java.util.Collection collection = (java.util.Collection)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.newInstance( valueCls );
						for( int i = 0; i < size; i++ ) {
							String componentTypeName = binaryDecoder.decodeString();
							Class<?> componentType = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( componentTypeName );
							collection.add( decodeObject( binaryDecoder, componentType, map ) );
						}
						value = null;
					} else {
						value = decodeObject( binaryDecoder, valueCls, map );
					}
				}
				property.setValue( this, value );
			} else {
				break;
			}
		}
	}

	private void encodeObject( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Object value, java.util.Map<edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable, Integer> map ) {
		if( value != null ) {
			Class<?> valueCls = value.getClass();

			if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
				binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable)value );
			} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
				binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable)value, map );
			} else if( java.nio.ByteBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.ByteBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.CharBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.CharBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.ShortBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.ShortBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.IntBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.IntBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.LongBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.LongBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.FloatBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.FloatBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( java.nio.DoubleBuffer.class.isAssignableFrom( valueCls ) ) {
				edu.cmu.cs.dennisc.codec.BufferUtilities.encode( binaryEncoder, (java.nio.DoubleBuffer)value, IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS );
			} else if( Boolean.class == valueCls ) {
				binaryEncoder.encode( (Boolean)value );
			} else if( Byte.class == valueCls ) {
				binaryEncoder.encode( (Byte)value );
			} else if( Character.class == valueCls ) {
				binaryEncoder.encode( (Character)value );
			} else if( Double.class == valueCls ) {
				binaryEncoder.encode( (Double)value );
			} else if( Float.class == valueCls ) {
				binaryEncoder.encode( (Float)value );
			} else if( Integer.class == valueCls ) {
				binaryEncoder.encode( (Integer)value );
			} else if( Long.class == valueCls ) {
				binaryEncoder.encode( (Long)value );
			} else if( Short.class == valueCls ) {
				binaryEncoder.encode( (Short)value );
			} else if( String.class == valueCls ) {
				binaryEncoder.encode( (String)value );
			} else if( Enum.class.isAssignableFrom( valueCls ) ) {
				binaryEncoder.encode( (Enum)value );
			} else {
				throw new RuntimeException( value.getClass().getName() + " " + value.toString() );
			}
		} else {
			binaryEncoder.encode( "" );
		}
	}

	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.util.Map<edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable, Integer> map ) {
		for( Property<?> property : getProperties() ) {
			// todo?
			// if( property.isTransient() ) {
			// //pass
			// } else {
			binaryEncoder.encode( property.getName() );
			Object value = property.getValue( this );
			if( value != null ) {
				Class<?> valueCls = value.getClass();
				binaryEncoder.encode( valueCls.getName() );
				if( valueCls.isArray() ) {
					if( boolean[].class == valueCls ) {
						binaryEncoder.encode( (boolean[])value );
					} else if( byte[].class == valueCls ) {
						binaryEncoder.encode( (byte[])value );
					} else if( char[].class == valueCls ) {
						binaryEncoder.encode( (char[])value );
					} else if( double[].class == valueCls ) {
						binaryEncoder.encode( (double[])value );
					} else if( float[].class == valueCls ) {
						binaryEncoder.encode( (float[])value );
					} else if( int[].class == valueCls ) {
						binaryEncoder.encode( (int[])value );
					} else if( long[].class == valueCls ) {
						binaryEncoder.encode( (long[])value );
					} else if( short[].class == valueCls ) {
						binaryEncoder.encode( (short[])value );
					} else if( String[].class == valueCls ) {
						binaryEncoder.encode( (String[])value );
					} else if( Enum[].class.isAssignableFrom( valueCls ) ) {
						binaryEncoder.encode( (Enum[])value );
					} else if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable[].class.isAssignableFrom( valueCls ) ) {
						binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable[])value );
					} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable[].class.isAssignableFrom( valueCls ) ) {
						binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable[])value, map );
					} else {
						int length = java.lang.reflect.Array.getLength( value );
						binaryEncoder.encode( length );
						for( int i = 0; i < length; i++ ) {
							encodeObject( binaryEncoder, java.lang.reflect.Array.get( value, i ), map );
						}
					}
				} else if( java.util.Collection.class.isAssignableFrom( valueCls ) ) {
					java.util.Collection<?> collection = (java.util.Collection<?>)value;
					int size = collection.size();
					binaryEncoder.encode( size );
					for( Object o : collection ) {
						encodeObject( binaryEncoder, o, map );
					}
				} else {
					encodeObject( binaryEncoder, value, map );
				}
			} else {
				binaryEncoder.encode( "" );
			}
		}
		binaryEncoder.encode( "" );
	}

	public boolean isEquivalentTo( Object other ) {
		if( ( this == other ) || super.equals( other ) ) {
			return true;
		} else {
			if( other instanceof Element ) {
				Element otherDIPO = (Element)other;
				int propertyCount = 0;
				for( Property thisProperty : this.getProperties() ) {
					String propertyName = thisProperty.getName();
					try {
						Property otherProperty = otherDIPO.getPropertyNamed( propertyName );
						if( otherProperty != null ) {
							Object thisValue = thisProperty.getValue( this );
							Object otherValue = otherProperty.getValue( otherDIPO );
							if( thisValue instanceof Element ) {
								if( ( (Element)thisValue ).isEquivalentTo( otherValue ) ) {
									//pass
								} else {
									return false;
								}
							} else {
								if( edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( thisValue, otherValue ) ) {
									//pass
								} else {
									return false;
								}
							}
						} else {
							return false;
						}
						propertyCount++;
					} catch( Exception e ) {
						edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( e, this, other );
						return false;
					}
				}
				for( Property otherProperty : otherDIPO.getProperties() ) {
					propertyCount--;
				}
				return propertyCount == 0;
			} else {
				return false;
			}
		}
	}
}
