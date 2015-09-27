/*******************************************************************************
 * Copyright (c) 2006, 2015, Carnegie Mellon University. All rights reserved.
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
 *******************************************************************************/
package edu.cmu.cs.dennisc.pattern;

import edu.cmu.cs.dennisc.property.InstanceProperty;
import edu.cmu.cs.dennisc.property.InstancePropertyOwner;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractInstancePropertyOwner extends AbstractNameable implements InstancePropertyOwner, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable {
	private static final boolean IS_NATIVE_BYTE_ORDER_REQUIRED_FOR_BUFFERS = true;

	public void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		this.propertyListeners.add( propertyListener );
	}

	public void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		this.propertyListeners.remove( propertyListener );
	}

	public java.util.Collection<edu.cmu.cs.dennisc.property.event.PropertyListener> getPropertyListeners() {
		return java.util.Collections.unmodifiableCollection( this.propertyListeners );
	}

	@Override
	public void firePropertyChanging( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : this.propertyListeners ) {
			propertyListener.propertyChanging( e );
		}
	}

	@Override
	public void firePropertyChanged( edu.cmu.cs.dennisc.property.event.PropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener : this.propertyListeners ) {
			propertyListener.propertyChanged( e );
		}
	}

	public void addListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> listPropertyListener ) {
		this.listPropertyListeners.add( listPropertyListener );
	}

	public void removeListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> listPropertyListener ) {
		this.listPropertyListeners.remove( listPropertyListener );
	}

	public java.util.Collection<edu.cmu.cs.dennisc.property.event.ListPropertyListener<?>> getListPropertyListeners() {
		return java.util.Collections.unmodifiableCollection( this.listPropertyListeners );
	}

	@Override
	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.adding( e );
		}
	}

	@Override
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.added( e );
		}
	}

	@Override
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.clearing( e );
		}
	}

	@Override
	public void fireCleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.cleared( e );
		}
	}

	@Override
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.removing( e );
		}
	}

	@Override
	public void fireRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.removed( e );
		}
	}

	@Override
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.setting( e );
		}
	}

	@Override
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener<?> l : this.listPropertyListeners ) {
			l.set( e );
		}
	}

	@Override
	public InstanceProperty<?> getPropertyNamed( String name ) {
		//todo: remove
		name = Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
		try {
			java.lang.reflect.Field field = getClass().getField( name );
			return (InstanceProperty<?>)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this );
		} catch( NoSuchFieldException nsfe ) {
			return null;
		}
	}

	@Override
	public java.util.List<InstanceProperty<?>> getProperties() {
		if( this.properties == null ) {
			Class<? extends edu.cmu.cs.dennisc.property.InstancePropertyOwner> cls = getClass();
			this.properties = new java.util.LinkedList<InstanceProperty<?>>();
			for( java.lang.reflect.Field field : cls.getFields() ) {
				int modifiers = field.getModifiers();
				if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
					if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
						//pass
					} else {
						if( InstanceProperty.class.isAssignableFrom( field.getType() ) ) {
							InstanceProperty instanceProperty = (InstanceProperty)edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.get( field, this );
							assert instanceProperty.getOwner() == this;
							this.properties.add( instanceProperty );
						}
					}
				}
			}
		}
		return this.properties;
	}

	@Override
	public String lookupNameFor( InstanceProperty<?> instanceProperty ) {
		for( java.lang.reflect.Field field : getClass().getFields() ) {
			if( InstanceProperty.class.isAssignableFrom( field.getType() ) ) {
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

	@Override
	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, java.util.Map<Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable> map ) {
		while( true ) {
			String propertyName = binaryDecoder.decodeString();
			if( propertyName.length() > 0 ) {
				InstanceProperty property = getPropertyNamed( propertyName );
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
				property.setValue( value );
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

	@Override
	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.util.Map<edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable, Integer> map ) {
		for( InstanceProperty<?> property : getProperties() ) {
			// todo?
			// if( property.isTransient() ) {
			// //pass
			// } else {
			binaryEncoder.encode( property.getName() );
			Object value = property.getValue();
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

	@Override
	public final boolean equals( Object obj ) {
		return super.equals( obj );
	}

	public boolean isEquivalentTo( Object other ) {
		if( ( this == other ) || super.equals( other ) ) {
			return true;
		} else {
			if( other instanceof AbstractInstancePropertyOwner ) {
				AbstractInstancePropertyOwner otherDIPO = (AbstractInstancePropertyOwner)other;
				int propertyCount = 0;
				for( InstanceProperty thisProperty : this.getProperties() ) {
					String propertyName = thisProperty.getName();
					try {
						InstanceProperty otherProperty = otherDIPO.getPropertyNamed( propertyName );
						if( otherProperty != null ) {
							Object thisValue = thisProperty.getValue();
							Object otherValue = otherProperty.getValue();
							if( thisValue instanceof AbstractInstancePropertyOwner ) {
								if( ( (AbstractInstancePropertyOwner)thisValue ).isEquivalentTo( otherValue ) ) {
									//pass
								} else {
									return false;
								}
							} else {
								if( edu.cmu.cs.dennisc.java.util.Objects.equals( thisValue, otherValue ) ) {
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
				for( InstanceProperty otherProperty : otherDIPO.getProperties() ) {
					propertyCount--;
				}
				return propertyCount == 0;
			} else {
				return false;
			}
		}
	}

	private java.util.List<InstanceProperty<?>> properties = null;
	private final java.util.List<edu.cmu.cs.dennisc.property.event.PropertyListener> propertyListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final java.util.List<edu.cmu.cs.dennisc.property.event.ListPropertyListener<?>> listPropertyListeners = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
}
