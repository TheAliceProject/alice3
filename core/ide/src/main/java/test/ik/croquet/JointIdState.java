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

package test.ik.croquet;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointIdState extends org.lgna.croquet.CustomItemStateWithInternalBlank<org.lgna.story.resources.JointId> {
	private org.lgna.story.resources.JointId value;

	public JointIdState( java.util.UUID id ) {
		super( org.lgna.croquet.Application.DOCUMENT_UI_GROUP, id, null, test.ik.croquet.codecs.JointIdCodec.SINGLETON );
	}

	@Override
	protected org.lgna.story.resources.JointId getSwingValue() {
		return value;
	}

	@Override
	protected void setSwingValue( org.lgna.story.resources.JointId nextValue ) {
		//		this.setSwingValue( nextValue );
		this.value = nextValue;
	}

	private static void fillIn( java.util.List<org.lgna.croquet.CascadeBlankChild> rv, org.lgna.story.resources.JointId id ) {
		rv.add( JointIdFillIn.getInstance( id ) );
		//TODO: Make this work for resource classes that have different joints based on different resources
		//(this only happens when the resource class declares an array and different resources have a variable number of joint in that array, like railroad track pieces)
		for( org.lgna.story.resources.JointId childId : id.getDeclaredChildren( null ) ) {
			fillIn( rv, childId );
		}
	}

	@Override
	protected void updateBlankChildren( java.util.List<org.lgna.croquet.CascadeBlankChild> blankChildren, org.lgna.croquet.imp.cascade.BlankNode<org.lgna.story.resources.JointId> blankNode ) {
		org.lgna.story.resources.JointId[] rootIds = org.lgna.story.resources.BipedResource.JOINT_ID_ROOTS;
		for( org.lgna.story.resources.JointId rootId : rootIds ) {
			fillIn( blankChildren, rootId );
			blankChildren.add( org.lgna.croquet.CascadeLineSeparator.getInstance() );
		}
	}
}
