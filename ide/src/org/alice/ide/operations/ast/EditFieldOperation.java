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
package org.alice.ide.operations.ast;

import edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice;

/**
 * @author Dennis Cosgrove
 */
public class EditFieldOperation extends AbstractEditFieldOperation {
	private edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field;
	public EditFieldOperation( edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInAlice field ) {
		super( edu.cmu.cs.dennisc.alice.Project.GROUP_UUID, "Edit " + field.getName() + "..." );
		this.field = field;
	}
	public void perform( edu.cmu.cs.dennisc.zoot.ActionContext actionContext ) {
		final java.util.Set< FieldDeclaredInAlice > referencedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		final java.util.Set< FieldDeclaredInAlice > reassignedFields = edu.cmu.cs.dennisc.java.util.Collections.newHashSet();
		org.alice.ide.IDE ide = org.alice.ide.IDE.getSingleton();
		edu.cmu.cs.dennisc.alice.Project project = ide.getProject();
		if( project != null ) {
			ide.ensureProjectCodeUpToDate();
			edu.cmu.cs.dennisc.alice.ast.AbstractType programType = project.getProgramType();
			edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess > crawler = new edu.cmu.cs.dennisc.pattern.IsInstanceCrawler< edu.cmu.cs.dennisc.alice.ast.FieldAccess >( edu.cmu.cs.dennisc.alice.ast.FieldAccess.class ) {
				@Override
				protected boolean isAcceptable( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
					return fieldAccess.field.getValue() == field;
				}
			};
			programType.crawl( crawler, true );
			java.util.List< edu.cmu.cs.dennisc.alice.ast.FieldAccess > fieldAccesses = crawler.getList();
			if( fieldAccesses.size() > 0 ) {
				referencedFields.add( this.field );
				for( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess : fieldAccesses ) {
					edu.cmu.cs.dennisc.alice.ast.Node parent = fieldAccess.getParent();
					if( parent instanceof edu.cmu.cs.dennisc.alice.ast.AssignmentExpression ) {
						edu.cmu.cs.dennisc.alice.ast.AssignmentExpression assignmentExpression = (edu.cmu.cs.dennisc.alice.ast.AssignmentExpression)parent;
						if( assignmentExpression.leftHandSide.getValue() == fieldAccess ) {
							reassignedFields.add( this.field );
							break;
						}
					}
				}
			}
			this.perform( actionContext, field, referencedFields, reassignedFields );
		} else {
			actionContext.cancel();
		}
	}
}
