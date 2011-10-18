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

package org.alice.ide.croquet.models.ast;

/**
 * @author Dennis Cosgrove
 */
public class PropertyState extends org.alice.ide.croquet.models.StandardExpressionState {
	private static edu.cmu.cs.dennisc.map.MapToMap< org.lgna.croquet.Group, org.lgna.project.ast.JavaMethod, PropertyState > map = edu.cmu.cs.dennisc.map.MapToMap.newInstance();
	
	public static synchronized PropertyState getInstanceForSetter( org.lgna.croquet.Group group, org.lgna.project.ast.JavaMethod setter ) {
		PropertyState rv = map.get( group, setter );
		if( rv != null ) {
			//pass
		} else {
			rv = new PropertyState( group, setter );
			map.put( group, setter, rv );
		}
		return rv;
	}
	public static synchronized PropertyState getInstanceForGetter( org.lgna.croquet.Group group, org.lgna.project.ast.JavaMethod getter ) {
		return getInstanceForSetter( group, org.lgna.project.ast.AstUtilities.getSetterForGetter( getter ) );
	}
	private final org.lgna.project.ast.JavaMethod setter;
	private PropertyState( org.lgna.croquet.Group group, org.lgna.project.ast.JavaMethod setter ) {
		super( group, java.util.UUID.fromString( "f38ed248-1d68-43eb-b2c0-09ac62bd748e" ), null );
		this.setter = setter;
	}
	public org.lgna.project.ast.JavaMethod getSetter() {
		return this.setter;
	}
	private org.lgna.project.ast.JavaMethodParameter getParameter0() {
		return (org.lgna.project.ast.JavaMethodParameter)this.setter.getRequiredParameters().get( 0 );
	}
	@Override
	protected org.lgna.project.ast.AbstractType< ?, ?, ? > getType() {
		return this.getParameter0().getValueType();
	}
	@Override
	protected org.lgna.project.annotations.ValueDetails< ? > getValueDetails() {
		return this.getParameter0().getDetails();
	}
}
