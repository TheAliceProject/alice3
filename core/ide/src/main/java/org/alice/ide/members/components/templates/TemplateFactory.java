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
package org.alice.ide.members.components.templates;

/**
 * @author Dennis Cosgrove
 */
public class TemplateFactory {
	private TemplateFactory() {
		throw new AssertionError();
	}

	private static java.util.Map<org.lgna.project.ast.AbstractMethod, org.lgna.croquet.views.DragComponent> mapMethodToProcedureInvocationTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.project.ast.AbstractMethod, org.lgna.croquet.views.DragComponent> mapMethodToFunctionInvocationTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.croquet.views.DragComponent> mapMethodToAccessorTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.croquet.views.DragComponent> mapMethodToAccessArrayAtIndexTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.croquet.views.DragComponent> mapMethodToArrayLengthTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private static java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.croquet.views.DragComponent> mapMethodToMutatorTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();
	private static java.util.Map<org.lgna.project.ast.AbstractField, org.lgna.croquet.views.DragComponent> mapMethodToMutateArrayAtIndexTemplate = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	public static org.lgna.croquet.views.DragComponent getProcedureInvocationTemplate( org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.croquet.views.DragComponent rv = mapMethodToProcedureInvocationTemplate.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = new org.alice.ide.members.components.templates.ProcedureInvocationTemplate( method );
			mapMethodToProcedureInvocationTemplate.put( method, rv );
		}
		return rv;
	}

	public static org.lgna.croquet.views.DragComponent getFunctionInvocationTemplate( org.lgna.project.ast.AbstractMethod method ) {
		org.lgna.croquet.views.DragComponent rv = mapMethodToFunctionInvocationTemplate.get( method );
		if( rv != null ) {
			//pass
		} else {
			rv = new org.alice.ide.members.components.templates.FunctionInvocationTemplate( method );
			mapMethodToFunctionInvocationTemplate.put( method, rv );
		}
		return rv;
	}

	public static org.lgna.croquet.views.DragComponent getMethodInvocationTemplate( org.lgna.project.ast.AbstractMethod method ) {
		if( method.isProcedure() ) {
			return getProcedureInvocationTemplate( method );
		} else {
			return getFunctionInvocationTemplate( method );
		}
	}

	public static org.lgna.croquet.views.DragComponent getAccessorTemplate( org.lgna.project.ast.AbstractField field ) {
		org.lgna.croquet.views.DragComponent rv = mapMethodToAccessorTemplate.get( field );
		if( rv != null ) {

		} else {
			rv = new org.alice.ide.members.components.templates.GetterTemplate( field );
			mapMethodToAccessorTemplate.put( field, rv );
		}
		return rv;
	}

	public static org.lgna.croquet.views.DragComponent getAccessArrayAtIndexTemplate( org.lgna.project.ast.AbstractField field ) {
		org.lgna.croquet.views.DragComponent rv = mapMethodToAccessArrayAtIndexTemplate.get( field );
		if( rv != null ) {

		} else {
			rv = new org.alice.ide.members.components.templates.AccessFieldArrayAtIndexTemplate( field );
			mapMethodToAccessArrayAtIndexTemplate.put( field, rv );
		}
		return rv;
	}

	public static org.lgna.croquet.views.DragComponent getArrayLengthTemplate( org.lgna.project.ast.AbstractField field ) {
		org.lgna.croquet.views.DragComponent rv = mapMethodToArrayLengthTemplate.get( field );
		if( rv != null ) {

		} else {
			rv = new org.alice.ide.members.components.templates.FieldArrayLengthTemplate( field );
			mapMethodToArrayLengthTemplate.put( field, rv );
		}
		return rv;
	}
}
