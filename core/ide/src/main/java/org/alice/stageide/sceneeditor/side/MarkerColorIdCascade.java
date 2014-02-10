/**
 * Copyright (c) 2006-2012, Carnegie Mellon University. All rights reserved.
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
package org.alice.stageide.sceneeditor.side;

/**
 * @author Dennis Cosgrove
 */
public final class MarkerColorIdCascade extends org.lgna.croquet.ImmutableCascade<org.lgna.project.ast.Expression> {
	private static edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.UserField, MarkerColorIdCascade> map = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	public static MarkerColorIdCascade getInstance( org.lgna.project.ast.UserField field ) {
		return map.getInitializingIfAbsent( field, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.UserField, MarkerColorIdCascade>() {
			public MarkerColorIdCascade initialize( org.lgna.project.ast.UserField key ) {
				return new MarkerColorIdCascade( key );
			}
		} );
	}

	private final org.lgna.project.ast.UserField field;

	private MarkerColorIdCascade( org.lgna.project.ast.UserField field ) {
		super( org.lgna.croquet.Application.PROJECT_GROUP, java.util.UUID.fromString( "1f6171eb-193b-46a9-a49a-22bacab341de" ), org.lgna.project.ast.Expression.class, org.alice.ide.croquet.models.cascade.ExpressionBlank.createBlanks( org.lgna.story.Color.class ) );
		this.field = field;
	}

	@Override
	protected org.lgna.croquet.edits.AbstractEdit<? extends org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> createEdit( org.lgna.croquet.history.CompletionStep<org.lgna.croquet.Cascade<org.lgna.project.ast.Expression>> completionStep, org.lgna.project.ast.Expression[] values ) {
		assert values.length == 1;
		return new org.alice.stageide.sceneeditor.side.edits.MarkerColorIdEdit( completionStep, this.field, values[ 0 ] );
	}
}
