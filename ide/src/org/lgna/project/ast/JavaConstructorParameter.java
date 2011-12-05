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

//todo: name
/**
 * @author Dennis Cosgrove
 */
public class JavaConstructorParameter extends JavaParameter {
	private static String getParameterNameFor( ConstructorReflectionProxy constructorReflectionProxy, int index ) {
		String rv = null;
		try {
			org.lgna.project.reflect.ClassInfo classInfo = org.lgna.project.reflect.ClassInfoManager.get( constructorReflectionProxy.getDeclaringClassReflectionProxy().getReification() );
			if( classInfo != null ) {
				org.lgna.project.reflect.ConstructorInfo constructorInfo = classInfo.lookupInfo( constructorReflectionProxy.getReification() );
				if( constructorInfo != null ) {
					String[] parameterNames = constructorInfo.getParameterNames();
					if( parameterNames != null ) {
						rv = parameterNames[ index ];
					}
				}
			}
		} catch( Throwable t ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t, constructorReflectionProxy, index );
		}
		return rv;
	}
	private JavaConstructor m_constructor;
	private int m_index;
	private String m_name;
	private JavaType m_valueType;
	/*package-private*/ JavaConstructorParameter( JavaConstructor constructor, int index, java.lang.annotation.Annotation[] annotations ) {
		super( annotations );
		m_constructor = constructor;
		m_index = index;
		ConstructorReflectionProxy constructorReflectionProxy = constructor.getConstructorReflectionProxy();
		m_name = getParameterNameFor( constructorReflectionProxy, m_index );
		m_valueType = JavaType.getInstance( constructorReflectionProxy.getParameterClassReflectionProxies()[ m_index ] );
		assert m_valueType != null;
	}
	
	public JavaConstructor getConstructor() {
		return m_constructor;
	}
	public int getIndex() {
		return m_index;
	}
	
	@Override
	public String getName() {
		return m_name;
	}
	@Override
	public AbstractType<?,?,?> getValueType() {
		return m_valueType;
	}

	@Override
	public boolean isEquivalentTo( Object other ) {
		if( other instanceof JavaConstructorParameter ) {
			JavaConstructorParameter otherPDIJC = (JavaConstructorParameter)other;
			return m_constructor.equals( otherPDIJC.m_constructor ) && m_index == otherPDIJC.m_index && edu.cmu.cs.dennisc.equivalence.EquivalenceUtilities.areEquivalent( m_name, otherPDIJC.m_name ) && m_valueType.equals( otherPDIJC.m_valueType );
		} else {
			return false;
		}
	}
}
