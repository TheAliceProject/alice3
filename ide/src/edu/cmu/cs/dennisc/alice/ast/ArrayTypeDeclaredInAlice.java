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

package edu.cmu.cs.dennisc.alice.ast;

/**
 * @author Dennis Cosgrove
 */
public class ArrayTypeDeclaredInAlice extends AbstractType {
	private static edu.cmu.cs.dennisc.map.MapToMap< AbstractTypeDeclaredInAlice<?>, Integer, ArrayTypeDeclaredInAlice > s_map = new edu.cmu.cs.dennisc.map.MapToMap< AbstractTypeDeclaredInAlice<?>, Integer, ArrayTypeDeclaredInAlice >();
	public static ArrayTypeDeclaredInAlice get( AbstractTypeDeclaredInAlice<?> leafType, int dimensionCount ) {
		ArrayTypeDeclaredInAlice rv = s_map.get( leafType, dimensionCount );
		if( rv != null ) {
			//pass
		} else {
			rv = new ArrayTypeDeclaredInAlice( leafType, dimensionCount );
			s_map.put( leafType, dimensionCount, rv );
		}
		return rv;
	}

	private AbstractTypeDeclaredInAlice<?> m_leafType;
	private int m_dimensionCount;
	
	private ArrayTypeDeclaredInAlice( AbstractTypeDeclaredInAlice<?> leafType, int dimensionCount ) {
		m_leafType = leafType;
		m_dimensionCount = dimensionCount;
	}
	
	public AbstractTypeDeclaredInAlice<?> getLeafType() {
		return m_leafType;
	}
	public int getDimensionCount() {
		return m_dimensionCount;
	}
	
	@Override
	public String getName() {
		StringBuffer sb = new StringBuffer();
		sb.append( m_leafType.getName() );
		for( int i=0; i<m_dimensionCount; i++ ) {
			sb.append( "[]" );
		}
		return sb.toString();
	}
	@Override
	public edu.cmu.cs.dennisc.property.StringProperty getNamePropertyIfItExists() {
		//todo?
		return null;
	}
	
	@Override
	public boolean isArray() {
		return true;
	}
	@Override
	public AbstractType<?,?,?> getComponentType() {
		if( m_dimensionCount == 1 ) {
			return m_leafType;
		} else {
			return ArrayTypeDeclaredInAlice.get( m_leafType, m_dimensionCount-1 );
		}
	}
	@Override
	public boolean isDeclaredInAlice() {
		return true;
	}
	@Override
	public AbstractPackage getPackage() {
		//todo?
		return m_leafType.getPackage();
	}
	@Override
	public AbstractType<?,?,?> getSuperType() {
		AbstractType<?,?,?> superType = m_leafType.getSuperType();
		if( superType instanceof AbstractTypeDeclaredInAlice<?> ) {
			return ArrayTypeDeclaredInAlice.get( ((AbstractTypeDeclaredInAlice<?>)superType), m_dimensionCount );
		} else {
			assert superType instanceof TypeDeclaredInJava;
			return TypeDeclaredInJava.get( edu.cmu.cs.dennisc.java.lang.reflect.ReflectionUtilities.getArrayClass( superType.getClass(), m_dimensionCount ) );
		}
		//todo: investigate
	}
	@Override
	public boolean isFollowToSuperClassDesired() {
		//todo?
		return m_leafType.isFollowToSuperClassDesired();
	}
	@Override
	public boolean isConsumptionBySubClassDesired() {
		//todo?
		return m_leafType.isConsumptionBySubClassDesired();
	}
	

	@Override
	public java.util.ArrayList< ? extends AbstractConstructor > getDeclaredConstructors() {
		//todo
		return new java.util.ArrayList< AbstractConstructor >();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractField > getDeclaredFields() {
		//todo
		return new java.util.ArrayList< AbstractField >();
	}
	@Override
	public java.util.ArrayList< ? extends AbstractMethod > getDeclaredMethods() {
		//todo
		return new java.util.ArrayList< AbstractMethod >();
	}
	@Override
	public Access getAccess() {
		return m_leafType.getAccess();
	}
	@Override
	public boolean isInterface() {
		//todo?
		return m_leafType.isInterface();
	}
	@Override
	public boolean isAbstract() {
		//todo?
		return m_leafType.isAbstract();
	}
	@Override
	public boolean isFinal() {
		//todo?
		return m_leafType.isFinal();
	}
	@Override
	public boolean isStatic() {
		//todo?
		return m_leafType.isStatic();
	}
	@Override
	public boolean isStrictFloatingPoint() {
		//todo?
		return m_leafType.isStrictFloatingPoint();
	}
	
	@Override
	public AbstractType getArrayType() {
		return ArrayTypeDeclaredInAlice.get( m_leafType, m_dimensionCount+1 );
	}
}
