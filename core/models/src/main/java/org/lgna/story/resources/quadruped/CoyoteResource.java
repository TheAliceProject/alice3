/*
 * Alice 3 End User License Agreement
 *
 * Copyright (c) 2006-2015, Carnegie Mellon University. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Products derived from the software may not be called "Alice", nor may "Alice" appear in their name, without prior written permission of Carnegie Mellon University.
 *
 * 4. All advertising materials mentioning features or use of this software must display the following acknowledgement: "This product includes software developed by Carnegie Mellon University"
 *
 * 5. The gallery of art assets and animations provided with this software is contributed by Electronic Arts Inc. and may be used for personal, non-commercial, and academic use only. Redistributions of any program source code that utilizes The Sims 2 Assets must also retain the copyright notice, list of conditions and the disclaimer contained in The Alice 3.0 Art Gallery License.
 *
 * DISCLAIMER:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND.  ANY AND ALL EXPRESS, STATUTORY OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY,  FITNESS FOR A PARTICULAR PURPOSE, TITLE, AND NON-INFRINGEMENT ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHORS, COPYRIGHT OWNERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING FROM OR OTHERWISE RELATING TO THE USE OF OR OTHER DEALINGS WITH THE SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package org.lgna.story.resources.quadruped;
import org.lgna.project.annotations.FieldTemplate;
import org.lgna.project.annotations.Visibility;
import org.lgna.story.SQuadruped;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.implementation.QuadrupedImp;
import org.lgna.story.resources.ImplementationAndVisualType;
import org.lgna.story.resources.JointId;
import org.lgna.story.resources.JointedModelResource;
import org.lgna.story.resources.QuadrupedResource;

public enum CoyoteResource implements QuadrupedResource {
	INDIGO,
	BROWN;

@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LOWER_LIP = new JointId( MOUTH, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId LEFT_EAR_TIP = new JointId( LEFT_EAR, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId RIGHT_EAR_TIP = new JointId( RIGHT_EAR, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.PRIME_TIME, methodNameHint="getTongue")
	public static final JointId TONGUE_0 = new JointId( HEAD, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId TONGUE_1 = new JointId( TONGUE_0, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId TONGUE_2 = new JointId( TONGUE_1, CoyoteResource.class );
@FieldTemplate(visibility = Visibility.COMPLETELY_HIDDEN)
	public static final JointId TONGUE_3 = new JointId( TONGUE_2, CoyoteResource.class );

	public static final JointId[] TONGUE_ARRAY = { TONGUE_0, TONGUE_1, TONGUE_2, TONGUE_3 };

	@FieldTemplate( visibility = Visibility.COMPLETELY_HIDDEN )
	public static final JointId[] TAIL_ARRAY = { TAIL_0, TAIL_1, TAIL_2, TAIL_3 };
	@Override
	public JointId[] getTailArray() {
		return CoyoteResource.TAIL_ARRAY;
	}

	private final ImplementationAndVisualType resourceType;
	CoyoteResource() {
		this( ImplementationAndVisualType.ALICE );
	}

	CoyoteResource( ImplementationAndVisualType resourceType ) {
		this.resourceType = resourceType;
	}

	public JointId[] getRootJointIds() {
		return QuadrupedResource.JOINT_ID_ROOTS;
	}

	@Override
	public JointedModelImp.JointImplementationAndVisualDataFactory<JointedModelResource> getImplementationAndVisualFactory() {
		return this.resourceType.getFactory( this );
	}
	@Override
	public QuadrupedImp createImplementation( SQuadruped abstraction ) {
		return new QuadrupedImp( abstraction, this.resourceType.getFactory( this ) );
	}
}
