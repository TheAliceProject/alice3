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

package org.alice.ide.ast.draganddrop.statement;

/**
 * @author Dennis Cosgrove
 */
public class LocalAssignmentTemplateDragModel extends StatementTemplateDragModel {
	private static java.util.Map< org.lgna.project.ast.UserLocal, LocalAssignmentTemplateDragModel > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public static synchronized LocalAssignmentTemplateDragModel getInstance( org.lgna.project.ast.UserLocal local ) {
		LocalAssignmentTemplateDragModel rv = map.get( local );
		if( rv != null ) {
			//pass
		} else {
			rv = new LocalAssignmentTemplateDragModel( local );
			map.put( local, rv );
		}
		return rv;
	}
	private org.lgna.project.ast.UserLocal local;
	private LocalAssignmentTemplateDragModel( org.lgna.project.ast.UserLocal local ) {
		super( java.util.UUID.fromString( "8fc94780-2193-4cb9-8db4-ef39e9ece075" ), org.lgna.project.ast.ExpressionStatement.class, org.alice.ide.ast.IncompleteAstUtilities.createIncompleteLocalAssignmentStatement( local ) );
		this.local = local;
	}
	@Override
	protected org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< LocalAssignmentTemplateDragModel > createResolver() {
		return new org.alice.ide.croquet.resolvers.NodeStaticGetInstanceKeyedResolver< LocalAssignmentTemplateDragModel >( this, org.lgna.project.ast.UserLocal.class, this.local );
	}
	@Override
	public org.lgna.croquet.Model getDropModel( org.lgna.croquet.history.DragStep step, org.alice.ide.ast.draganddrop.BlockStatementIndexPair blockStatementIndexPair ) {
		return new org.alice.ide.croquet.models.ast.cascade.statement.LocalAssignmentInsertCascade( blockStatementIndexPair, this.local );
	}
}
