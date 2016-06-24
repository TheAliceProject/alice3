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

package org.alice.stageide.custom;

/**
 * @author Dennis Cosgrove
 */
public class AudioResourceExpressionState extends org.alice.ide.croquet.models.StandardExpressionState {
	private static class SingletonHolder {
		private static AudioResourceExpressionState instance = new AudioResourceExpressionState();
	}

	public static AudioResourceExpressionState getInstance() {
		return SingletonHolder.instance;
	}

	private AudioResourceExpressionState() {
		super( org.lgna.croquet.Application.INHERIT_GROUP, java.util.UUID.fromString( "cb9c681f-3486-4be2-bdf3-f3ba8d663e3b" ), null );
	}

	@Override
	protected org.lgna.project.annotations.ValueDetails<?> getValueDetails() {
		return null;
	}

	@Override
	protected org.lgna.project.ast.AbstractType<?, ?, ?> getType() {
		return org.lgna.project.ast.JavaType.getInstance( org.lgna.common.resources.AudioResource.class );
	}

	public org.lgna.common.resources.AudioResource getAudioResource() {
		org.lgna.project.ast.Expression expression = this.getValue();
		if( expression instanceof org.lgna.project.ast.ResourceExpression ) {
			org.lgna.project.ast.ResourceExpression resourceExpression = (org.lgna.project.ast.ResourceExpression)expression;
			org.lgna.common.Resource resource = resourceExpression.resource.getValue();
			if( resource instanceof org.lgna.common.resources.AudioResource ) {
				return (org.lgna.common.resources.AudioResource)resource;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}
}
