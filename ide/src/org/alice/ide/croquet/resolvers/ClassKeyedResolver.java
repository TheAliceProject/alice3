package org.alice.ide.croquet.resolvers;

public class ClassKeyedResolver<T> extends edu.cmu.cs.dennisc.croquet.KeyedResolver< T > {
	private Class<?> cls;
	public ClassKeyedResolver( T instance, Class<?> cls ) {
		super( instance );
		this.cls = cls;
	}
	public ClassKeyedResolver( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		super( binaryDecoder );
	}
	@Override
	protected Class<?>[] getParameterTypes() {
		return new Class[] { Class.class };
	}
	@Override
	protected Object[] decodeArguments( edu.cmu.cs.dennisc.codec.BinaryDecoder binaryDecoder ) {
		String clsName = binaryDecoder.decodeString();
		Class<?> cls = edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getClassForName( clsName );
		return new Object[] { cls };
	}
	@Override
	protected void encodeArguments( edu.cmu.cs.dennisc.codec.BinaryEncoder binaryEncoder ) {
		binaryEncoder.encode( this.cls.getName() );
	}
}
