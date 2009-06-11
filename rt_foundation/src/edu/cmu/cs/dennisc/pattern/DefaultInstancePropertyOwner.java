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

import edu.cmu.cs.dennisc.property.*;

/**
 * @author Dennis Cosgrove
 */

public abstract class DefaultInstancePropertyOwner extends AbstractElement implements InstancePropertyOwner, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable {
	private static java.util.HashMap< Class< ? extends edu.cmu.cs.dennisc.property.PropertyOwner >, java.util.List< Property< ? > > > s_classToPropertiesMap = new java.util.HashMap< Class< ? extends edu.cmu.cs.dennisc.property.PropertyOwner >, java.util.List< Property< ? > > >();
	private java.util.List< Property< ? > > m_properties = null;

	private java.util.List< edu.cmu.cs.dennisc.property.event.PropertyListener > m_propertyListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.property.event.PropertyListener >();
	private java.util.List< edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > > m_listPropertyListeners = new java.util.LinkedList< edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > >();

	public abstract boolean isComposedOfGetterAndSetterProperties();

	public void addPropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		m_propertyListeners.add( propertyListener );
	}
	public void removePropertyListener( edu.cmu.cs.dennisc.property.event.PropertyListener propertyListener ) {
		m_propertyListeners.remove( propertyListener );
	}
	public Iterable< edu.cmu.cs.dennisc.property.event.PropertyListener > accessPropertyListeners() {
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

	public void addListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > listPropertyListener ) {
		m_listPropertyListeners.add( listPropertyListener );
	}
	public void removeListPropertyListener( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > listPropertyListener ) {
		m_listPropertyListeners.remove( listPropertyListener );
	}
	public Iterable< edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > > accessListPropertyListeners() {
		return m_listPropertyListeners;
	}

	public void fireAdding( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.adding( e );
		}
	}
	public void fireAdded( edu.cmu.cs.dennisc.property.event.AddListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.added( e );
		}
	}
	public void fireClearing( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.clearing( e );
		}
	}
	public void fireCleared( edu.cmu.cs.dennisc.property.event.ClearListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.cleared( e );
		}
	}
	public void fireRemoving( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.removing( e );
		}
	}
	public void fireRemoved( edu.cmu.cs.dennisc.property.event.RemoveListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.removed( e );
		}
	}
	public void fireSetting( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.setting( e );
		}
	}
	public void fireSet( edu.cmu.cs.dennisc.property.event.SetListPropertyEvent e ) {
		for( edu.cmu.cs.dennisc.property.event.ListPropertyListener< ? > l : m_listPropertyListeners ) {
			l.set( e );
		}
	}

	public Property< ? > getPropertyNamed( String name ) {
		if( isComposedOfGetterAndSetterProperties() ) {
			for( Property< ? > property : getProperties() ) {
				if( property.getName().equals( name ) ) {
					return property;
				}
			}
			//throw new RuntimeException( "no property named: " + name );
			return null;
		} else {
			//todo: remove
			name = Character.toLowerCase( name.charAt( 0 ) ) + name.substring( 1 );
			try {
				java.lang.reflect.Field field = getClass().getField( name );
				return (Property< ? >)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, this );
			} catch (NoSuchFieldException nsfe) {
				return null;
			}
		}
	}
	public InstanceProperty< ? > getInstancePropertyNamed( String name ) {
		return (InstanceProperty< ? >)getPropertyNamed( name );
	}
	
	//	public Iterable< Property<?> > getProperties() {
	//		Class< ? extends edu.cmu.cs.dennisc.property.PropertyOwner > cls = getClass();
	//		java.util.List< Property<?> > rv = s_classToPropertiesMap.get( cls );
	//		if( rv == null ) {
	//			rv = new java.util.LinkedList< Property<?> >();
	//			for( java.lang.reflect.Field field : cls.getFields() ) {
	//				int modifiers = field.getModifiers();
	//				if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
	//					if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
	//						if( GetterSetterProperty.class.isAssignableFrom( field.getType() ) ) {
	//							rv.add( (Property)edu.cmu.cs.dennisc.reflect.ReflectionUtilities.get( field, null ) );
	//						}
	//					} else {
	//						if( InstanceProperty.class.isAssignableFrom( field.getType() ) ) {
	//							InstanceProperty instanceProperty = (InstanceProperty)edu.cmu.cs.dennisc.reflect.ReflectionUtilities.get( field, this );
	//							assert instanceProperty.getOwner() == this;
	//							rv.add( instanceProperty );
	//						}
	//					}
	//				}
	//			}
	//			s_classToPropertiesMap.put( cls, rv );
	//		}
	//		return rv;
	//	}
	public Iterable< Property< ? > > getProperties() {
		Class< ? extends edu.cmu.cs.dennisc.property.PropertyOwner > cls = getClass();
		if( isComposedOfGetterAndSetterProperties() ) {
			java.util.List< Property< ? > > rv = s_classToPropertiesMap.get( cls );
			if( rv == null ) {
				rv = new java.util.LinkedList< Property< ? > >();
				for( java.lang.reflect.Field field : cls.getFields() ) {
					int modifiers = field.getModifiers();
					if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
						if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
							if( GetterSetterProperty.class.isAssignableFrom( field.getType() ) ) {
								rv.add( (Property)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, null ) );
							}
						}
					}
				}
				s_classToPropertiesMap.put( cls, rv );
			}
			return rv;
		} else {
			if( m_properties == null ) {
				m_properties = new java.util.LinkedList< Property< ? > >();
				for( java.lang.reflect.Field field : cls.getFields() ) {
					int modifiers = field.getModifiers();
					if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
						if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
							//pass
						} else {
							if( InstanceProperty.class.isAssignableFrom( field.getType() ) ) {
								InstanceProperty instanceProperty = (InstanceProperty)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, this );
								assert instanceProperty.getOwner() == this;
								m_properties.add( instanceProperty );
							}
						}
					}
				}
			}
			return m_properties;
		}
	}
	public String lookupNameFor( InstanceProperty< ? > instanceProperty ) {
		for( java.lang.reflect.Field field : getClass().getFields() ) {
			if( Property.class.isAssignableFrom( field.getType() ) ) {
				int modifiers = field.getModifiers();
				if( java.lang.reflect.Modifier.isPublic( modifiers ) ) {
					if( java.lang.reflect.Modifier.isStatic( modifiers ) ) {
						//pass
					} else {
						if( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( field, this ) == instanceProperty ) {
							return field.getName();
						}
					}
				}
			}
		}
		return null;
	}

	private Object decodeObject( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, Class valueCls, java.util.Map< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable > map ) {
		Object rv;
		if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
			rv = binaryDecoder.decodeBinaryEncodableAndDecodable( valueCls );
		} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
			rv = binaryDecoder.decodeReferenceableBinaryEncodableAndDecodable( valueCls, map );
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
			rv = binaryDecoder.decodeEnum( (Class< ? extends Enum >)valueCls );
		} else {
			throw new RuntimeException( valueCls.getName() );
		}
		return rv;
	}

	public void decode( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder, java.util.Map< Integer, edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable > map ) {
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
					Class valueCls = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( valueClsName );
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
						java.util.Collection collection = (java.util.Collection)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.newInstance( valueCls );
						for( int i = 0; i < size; i++ ) {
							String componentTypeName = binaryDecoder.decodeString();
							Class< ? > componentType = edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getClassForName( componentTypeName );
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

	private void encodeObject( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, Object value, java.util.Map< edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable, Integer > map ) {
		if( value != null ) {
			Class< ? > valueCls = value.getClass();
			if( edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
				binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.BinaryEncodableAndDecodable)value );
			} else if( edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable.class.isAssignableFrom( valueCls ) ) {
				binaryEncoder.encode( (edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable)value, map );
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

	public void encode( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder, java.util.Map< edu.cmu.cs.dennisc.codec.ReferenceableBinaryEncodableAndDecodable, Integer > map ) {
		for( Property< ? > property : getProperties() ) {
			// todo?
			// if( property.isTransient() ) {
			// //pass
			// } else {
			binaryEncoder.encode( property.getName() );
			Object value = property.getValue( this );
			if( value != null ) {
				Class< ? > valueCls = value.getClass();
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
					java.util.Collection< ? > collection = (java.util.Collection< ? >)value;
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
	public final boolean equals( java.lang.Object obj ) {
		return super.equals( obj );
	}
	public boolean isEquivalentTo( Object other ) {
		if( this == other || super.equals( other ) ) {
			return true;
		} else {
			if( other instanceof DefaultInstancePropertyOwner ) {
				DefaultInstancePropertyOwner otherDIPO = (DefaultInstancePropertyOwner)other;
				int propertyCount = 0;
				for( Property thisProperty : this.getProperties() ) {
					String propertyName = thisProperty.getName();
					try {
						Property otherProperty = otherDIPO.getPropertyNamed( propertyName );
						if( otherProperty != null ) {
							Object thisValue = thisProperty.getValue( this );
							Object otherValue = otherProperty.getValue( otherDIPO );
							if( thisValue instanceof DefaultInstancePropertyOwner ) {
								if( ((DefaultInstancePropertyOwner)thisValue).isEquivalentTo( otherValue ) ) {
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
						propertyCount ++;
					} catch( Exception e ) {
						edu.cmu.cs.dennisc.print.PrintUtilities.println( "WARNING: exception in equivalence check:", e );
						e.printStackTrace();
						return false;
					}
				}
				for( Property otherProperty : otherDIPO.getProperties() ) {
					propertyCount --;
				}
				return propertyCount == 0;
			} else {
				return false;
			}
		}
	}
}
