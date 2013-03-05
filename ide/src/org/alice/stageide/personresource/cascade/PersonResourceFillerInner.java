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
package org.alice.stageide.personresource.cascade;

/**
 * @author Dennis Cosgrove
 */
public abstract class PersonResourceFillerInner extends org.alice.ide.cascade.fillerinners.ExpressionFillerInner {
	private final org.lgna.story.resources.sims2.LifeStage lifeStage;

	public PersonResourceFillerInner( Class<? extends org.lgna.story.resources.sims2.PersonResource> cls, org.lgna.story.resources.sims2.LifeStage lifeStage ) {
		super( cls );
		this.lifeStage = lifeStage;
	}

	@Override
	public void appendItems( java.util.List<org.lgna.croquet.CascadeBlankChild> items, org.lgna.project.annotations.ValueDetails<?> details, boolean isTop, org.lgna.project.ast.Expression prevExpression ) {
		org.lgna.croquet.CascadeFillIn<org.lgna.project.ast.InstanceCreation, Void> fillIn = null;
		if( prevExpression instanceof org.lgna.project.ast.InstanceCreation ) {
			org.lgna.project.ast.InstanceCreation instanceCreation = (org.lgna.project.ast.InstanceCreation)prevExpression;
			org.lgna.project.ast.AbstractType<?, ?, ?> type = instanceCreation.getType();
			if( type instanceof org.lgna.project.ast.JavaType ) {
				org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
				if( javaType.isAssignableTo( org.lgna.story.resources.sims2.PersonResource.class ) ) {
					fillIn = org.alice.stageide.personresource.PersonResourceComposite.getInstance().getPreviousResourceExpressionValueConverter().getFillIn();

				}
			}
		}
		if( fillIn != null ) {
			//pass
		} else {
			fillIn = org.alice.stageide.personresource.PersonResourceComposite.getInstance().getRandomPersonExpressionValueConverter( this.lifeStage ).getFillIn();
		}
		items.add( fillIn );
	}
}
