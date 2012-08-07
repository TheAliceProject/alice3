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

package org.lgna.cheshire.simple;

/**
 * @author Dennis Cosgrove
 */
public class AstLiveRetargeter implements org.lgna.croquet.Retargeter {
	private java.util.Map< Object, Object > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private void addBody( org.lgna.project.ast.BlockStatement keyBlockStatement, org.lgna.project.ast.BlockStatement valueBlockStatement ) {
		edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "recursive retarget" );
		this.addKeyValuePair( keyBlockStatement, valueBlockStatement );
	}
	public void addKeyValuePair( Object key, Object value ) {
		this.map.put( key, value );
		if( key instanceof org.lgna.project.ast.AbstractStatementWithBody ) {
			org.lgna.project.ast.AbstractStatementWithBody keyStatementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)key;
			org.lgna.project.ast.AbstractStatementWithBody valueStatementWithBody = (org.lgna.project.ast.AbstractStatementWithBody)value;
			this.addBody( keyStatementWithBody.body.getValue(), valueStatementWithBody.body.getValue() );
			if( key instanceof org.lgna.project.ast.EachInStatement ) {
				org.lgna.project.ast.EachInStatement keyEachInStatement = (org.lgna.project.ast.EachInStatement)keyStatementWithBody;
				org.lgna.project.ast.EachInStatement valueEachInStatement = (org.lgna.project.ast.EachInStatement)valueStatementWithBody;
				this.map.put( keyEachInStatement.getItemProperty().getValue(), valueEachInStatement.getItemProperty().getValue() );
			}
		}
		if( key instanceof org.lgna.project.ast.ExpressionStatement ) {
			org.lgna.project.ast.ExpressionStatement keyExpressionStatement = (org.lgna.project.ast.ExpressionStatement)key;
			org.lgna.project.ast.ExpressionStatement valueExpressionStatement = (org.lgna.project.ast.ExpressionStatement)value;
			this.map.put( keyExpressionStatement.expression.getValue(), valueExpressionStatement.expression.getValue() );
		}
		if( key instanceof org.lgna.project.ast.UserCode ) {
			org.lgna.project.ast.UserCode keyUserCode = (org.lgna.project.ast.UserCode)key;
			org.lgna.project.ast.UserCode valueUserCode = (org.lgna.project.ast.UserCode)key;
			this.addBody( keyUserCode.getBodyProperty().getValue(), valueUserCode.getBodyProperty().getValue() );
		}
	}
	public <N> N retarget(N original) {
		if( original instanceof org.alice.ide.declarationseditor.DeclarationComposite ) {
			org.alice.ide.declarationseditor.DeclarationComposite< ?, ? > originalDeclarationComposite = (org.alice.ide.declarationseditor.DeclarationComposite< ?, ? >)original;
			org.lgna.project.ast.AbstractDeclaration originalDeclaration = originalDeclarationComposite.getDeclaration();
			org.lgna.project.ast.AbstractDeclaration possiblyRetargettedDeclaration = this.retarget( originalDeclaration ); 
			return (N)org.alice.ide.declarationseditor.DeclarationComposite.getInstance( possiblyRetargettedDeclaration );
		} else if( original instanceof org.alice.ide.ast.draganddrop.BlockStatementIndexPair ) {
			org.alice.ide.ast.draganddrop.BlockStatementIndexPair originalBlockStatementIndexPair = (org.alice.ide.ast.draganddrop.BlockStatementIndexPair)original;
			org.lgna.project.ast.BlockStatement originalBlockStatement = originalBlockStatementIndexPair.getBlockStatement();
			org.lgna.project.ast.BlockStatement replacementBlockStatement = (org.lgna.project.ast.BlockStatement)map.get( originalBlockStatement );
			if( replacementBlockStatement != null ) {
				return (N)new org.alice.ide.ast.draganddrop.BlockStatementIndexPair( replacementBlockStatement, originalBlockStatementIndexPair.getIndex() );
			} else {
				return original;
			}
		} else if( original instanceof org.alice.ide.instancefactory.InstanceFactory ) {
			return (N)org.alice.ide.instancefactory.InstanceFactoryUtilities.retarget( this, (org.alice.ide.instancefactory.InstanceFactory)original );
		} else {
			N rv = (N)map.get( original );
			if( rv != null ) {
				//pass
			} else {
				rv = original;
			}
			return rv;
		}
	}
}
