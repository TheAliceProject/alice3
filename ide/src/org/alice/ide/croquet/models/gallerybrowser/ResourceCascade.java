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
package org.alice.ide.croquet.models.gallerybrowser;

/**
 * @author Dennis Cosgrove
 * 
 */
public class ResourceCascade extends org.lgna.croquet.Cascade<org.lgna.project.ast.Expression> {
	private static edu.cmu.cs.dennisc.map.MapToMap<org.lgna.project.ast.AbstractType<?, ?, ?>, org.lgna.croquet.DropSite, ResourceCascade> mapToMap = edu.cmu.cs.dennisc.map.MapToMap.newInstance();

	public static ResourceCascade getInstance( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.croquet.DropSite dropSite ) {
		synchronized( mapToMap ) {
			ResourceCascade rv = mapToMap.get( type, dropSite );
			if( rv != null ) {
				//pass
			} else {
				rv = new ResourceCascade( type, dropSite );
				mapToMap.put( type, dropSite, rv );
			}
			return rv;
		}
	}

	private final org.lgna.croquet.DropSite dropSite;

	private ResourceCascade( org.lgna.project.ast.AbstractType<?, ?, ?> type, org.lgna.croquet.DropSite dropSite ) {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "53db430a-0d90-47d2-ad03-487e1dffb47d" ), org.lgna.project.ast.Expression.class, org.alice.ide.croquet.models.declaration.GalleryResourceBlank.getInstance( type ) );
		this.dropSite = dropSite;
	}

	@Override
	protected org.lgna.croquet.edits.Edit<? extends org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> completionStep, org.lgna.project.ast.Expression[] values ) {
		if( values[ 0 ] instanceof org.lgna.project.ast.FieldAccess ) {
			org.lgna.project.ast.FieldAccess fieldAccess = (org.lgna.project.ast.FieldAccess)values[ 0 ];
			org.lgna.project.ast.AbstractField argumentField = fieldAccess.field.getValue();
			org.alice.ide.croquet.models.declaration.ArgumentFieldSpecifiedManagedFieldDeclarationOperation.getInstance( argumentField, this.dropSite ).fire();
		} else if( values[ 0 ] instanceof org.lgna.project.ast.TypeExpression ) {
			org.alice.stageide.croquet.models.gallerybrowser.CreateFieldFromPersonResourceOperation.getInstance().fire();
		}
		//todo
		return null;
	}
}
