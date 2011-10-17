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

package org.alice.ide.croquet.models.ast.cascade.keyed;

/**
 * @author Dennis Cosgrove
 */
public class JavaKeyedArgumentFillIn extends org.lgna.croquet.CascadeFillIn< org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > {
	private static java.util.Map< org.lgna.project.ast.JavaMethod, JavaKeyedArgumentFillIn > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static JavaKeyedArgumentFillIn getInstance( org.lgna.project.ast.JavaMethod value ) {
		synchronized( map ) {
			JavaKeyedArgumentFillIn rv = map.get( value );
			if( rv != null ) {
				//pass
			} else {
				rv = new JavaKeyedArgumentFillIn( value );
				map.put( value, rv );
			}
			return rv;
		}
	}
	private final org.lgna.project.ast.JavaKeyedArgument transientValue;
	private JavaKeyedArgumentFillIn( org.lgna.project.ast.JavaMethod keyMethod ) {
		super( java.util.UUID.fromString( "cc1fda14-598c-475f-afed-19cc7c5feb92" ) );
		org.lgna.project.ast.AbstractParameter parameter = null; //todo?
		org.lgna.project.ast.Expression expression = new org.alice.ide.ast.EmptyExpression( keyMethod.getReturnType() );
		this.transientValue = new org.lgna.project.ast.JavaKeyedArgument( parameter, expression, keyMethod );

		for( org.lgna.project.ast.AbstractParameter requiredParameter : keyMethod.getRequiredParameters() ) {
			this.addBlank( org.alice.ide.croquet.models.cascade.ParameterBlank.getInstance( requiredParameter ) );
		}
	}
	
	@Override
	public java.lang.String getMenuItemText( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > step ) {
		return this.transientValue.keyMethod.getValue().getName();
	}
	@Override
	public javax.swing.Icon getMenuItemIcon( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > step ) {
		return null;
	}

	@Override
	protected javax.swing.JComponent createMenuItemIconProxy( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > step ) {
		throw new AssertionError();
	}
	@Override
	public org.lgna.project.ast.JavaKeyedArgument createValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > step ) {
		return new org.lgna.project.ast.JavaKeyedArgument( 
				this.transientValue.parameter.getValue(), 
				this.createFromBlanks( step, org.lgna.project.ast.Expression.class )[ 0 ], 
				this.transientValue.keyMethod.getValue() 
		);
	}
	@Override
	public org.lgna.project.ast.JavaKeyedArgument getTransientValue( org.lgna.croquet.cascade.ItemNode< ? super org.lgna.project.ast.JavaKeyedArgument, org.lgna.project.ast.Expression > step ) {
		return this.transientValue;
	}
}
