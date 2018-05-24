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
package org.lgna.story.resources.sims2;

import edu.cmu.cs.dennisc.java.lang.SystemUtilities;
import edu.cmu.cs.dennisc.java.util.Maps;
import edu.cmu.cs.dennisc.random.RandomUtilities;

import java.util.Map;

public abstract class IngredientManager<E> {
	private Map<Class<? extends E>, Class<? extends E>[]> mapInterfaceClsToImplementingClses = Maps.newHashMap();

	protected void add( Class<? extends E> interfaceCls, Class<? extends E>... implementingClses ) {
		this.mapInterfaceClsToImplementingClses.put( interfaceCls, implementingClses );
	}

	protected abstract Class<Class<? extends E>> getImplementingClassesComponentType();

	protected abstract Class<? extends E> getGenderedInterfaceClass( LifeStage lifeStage, Gender gender );

	public Class<? extends E>[] getImplementingClasses( LifeStage lifeStage, Gender gender ) {
		Class<? extends E> interfaceClsGendered = this.getGenderedInterfaceClass( lifeStage, gender );

		Class<? extends E>[] enumClsesGendered = this.mapInterfaceClsToImplementingClses.get( interfaceClsGendered );

		return SystemUtilities.createArray( this.getImplementingClassesComponentType(), enumClsesGendered );
	}

	public Class<? extends E> getRandomClass( LifeStage lifeStage, Gender gender ) {
		assert lifeStage != null;
		assert gender != null;
		Class<? extends E>[] clses = getImplementingClasses( lifeStage, gender );
		assert clses.length > 0 : lifeStage.toString() + gender.toString();
		return RandomUtilities.getRandomValueFrom( clses );
	}

	public E getRandomEnumConstant( LifeStage lifeStage, Gender gender ) {
		while( true ) {
			Class enumCls = getRandomClass( lifeStage, gender );
			if( enumCls.isEnum() ) {
				E rv = (E)RandomUtilities.getRandomEnumConstant( enumCls );
				if( rv != null ) {
					return rv;
				}
			}
		}
	}

	public boolean isApplicable( E e, LifeStage lifeStage, Gender gender ) {
		assert e != null;
		assert lifeStage != null;
		assert gender != null;
		Class<?> eCls = e.getClass();
		Class<? extends E> interfaceClsGendered = this.getGenderedInterfaceClass( lifeStage, gender );
		boolean rv = interfaceClsGendered.isAssignableFrom( eCls );
		//edu.cmu.cs.dennisc.print.PrintUtilities.println( rv, lifeStage, gender, eCls, interfaceClsGendered, interfaceClsUnisex );
		return rv;
	}

}
