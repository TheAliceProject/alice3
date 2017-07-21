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
package org.alice.ide.operations.ast;

import org.lgna.project.ast.NodeListProperty;
import org.lgna.project.ast.UserParameter;

/**
 * @author Dennis Cosgrove
 */
public class DeleteParameterOperation extends AbstractCodeParameterOperation {
	public DeleteParameterOperation( NodeListProperty<UserParameter> parametersProperty, org.lgna.project.ast.UserParameter parameter ) {
		super( java.util.UUID.fromString( "853fb6a3-ea7b-4575-93d6-547f687a7033" ), parametersProperty, parameter );
		this.setName( "Delete" );
	}

	@Override
	protected void perform( org.lgna.croquet.history.CompletionStep<?> step ) {
		final org.lgna.project.ast.UserMethod method = edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( this.getCode(), org.lgna.project.ast.UserMethod.class );
		final int index = method.requiredParameters.indexOf( this.getParameter() );
		if( ( method != null ) && ( index >= 0 ) ) {
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ParameterAccess> crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler<org.lgna.project.ast.ParameterAccess>( org.lgna.project.ast.ParameterAccess.class ) {
				@Override
				protected boolean isAcceptable( org.lgna.project.ast.ParameterAccess parameterAccess ) {
					return parameterAccess.parameter.getValue() == getParameter();
				}
			};
			method.crawl( crawler, org.lgna.project.ast.CrawlPolicy.EXCLUDE_REFERENCES_ENTIRELY );
			java.util.List<org.lgna.project.ast.ParameterAccess> parameterAccesses = crawler.getList();
			final int N_ACCESSES = parameterAccesses.size();

			java.util.List<org.lgna.project.ast.MethodInvocation> methodInvocations = org.alice.ide.IDE.getActiveInstance().getMethodInvocations( method );
			final int N_INVOCATIONS = methodInvocations.size();
			if( N_ACCESSES > 0 ) {
				StringBuffer sb = new StringBuffer();
				sb.append( "<html><body>There " );
				if( N_ACCESSES == 1 ) {
					sb.append( "is 1 access" );
				} else {
					sb.append( "are " );
					sb.append( N_ACCESSES );
					sb.append( " accesses" );
				}
				sb.append( " to this parameter.<br>You must remove the " );
				if( N_ACCESSES == 1 ) {
					sb.append( "access" );
				} else {
					sb.append( "accesses" );
				}
				sb.append( " before you may delete the parameter.<br>Canceling.</body></html>" );
				new edu.cmu.cs.dennisc.javax.swing.option.OkDialog.Builder( sb.toString() )
						.buildAndShow();
				step.cancel();
			} else {
				if( N_INVOCATIONS > 0 ) {
					String codeText;
					if( method.isProcedure() ) {
						codeText = "procedure";
					} else {
						codeText = "function";
					}
					StringBuffer sb = new StringBuffer();
					sb.append( "<html><body>There " );
					if( N_INVOCATIONS == 1 ) {
						sb.append( "is 1 invocation" );
					} else {
						sb.append( "are " );
						sb.append( N_INVOCATIONS );
						sb.append( " invocations" );
					}
					sb.append( " to this " );
					sb.append( codeText );
					sb.append( " in your program.<br>Deleting this parameter will also delete the arguments from those " );
					if( N_INVOCATIONS == 1 ) {
						sb.append( "invocation" );
					} else {
						sb.append( "invocations" );
					}
					sb.append( "<br>Would you like to continue with the deletion?</body></html>" );
					edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult result = new edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelDialog.Builder( sb.toString() )
							.title( "Delete Parameter" )
							.buildAndShow();
					if( result == edu.cmu.cs.dennisc.javax.swing.option.YesNoCancelResult.YES ) {
						//pass
					} else {
						step.cancel();
					}
				}
			}
			if( step.isCanceled() ) {
				//pass
			} else {
				step.commitAndInvokeDo( new org.alice.ide.croquet.edits.ast.DeleteParameterEdit( step, this.getCode(), this.getParameter() ) );
			}
		} else {
			edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "cancel" );
			step.cancel();
		}
	}
}
