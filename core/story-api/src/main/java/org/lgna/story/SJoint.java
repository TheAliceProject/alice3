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

package org.lgna.story;

import edu.cmu.cs.dennisc.map.MapToMap;
import org.lgna.project.annotations.MethodTemplate;
import org.lgna.story.implementation.JointImp;
import org.lgna.story.implementation.JointedModelImp;
import org.lgna.story.resources.JointArrayId;
import org.lgna.story.resources.JointId;

/**
 * @author Dennis Cosgrove
 */
public class SJoint extends SMovableTurnable {

	private static final MapToMap<SJointedModel, JointId, SJoint> mapToMap = MapToMap.newInstance();

	private static final MapToMap<SJointedModel, JointId[], SJoint[]> mapToArrayMap = MapToMap.newInstance();

	private static final MapToMap<SJointedModel, JointArrayId, SJoint[]> mapToArrayIdMap = MapToMap.newInstance();

	/* package-private */static SJoint getJoint( SJointedModel jointedModel, JointId jointId ) {
		return mapToMap.getInitializingIfAbsent( jointedModel, jointId, new MapToMap.Initializer<SJointedModel, JointId, SJoint>() {
			@Override
			public SJoint initialize( SJointedModel jointedModel, JointId jointId ) {
				JointedModelImp jointedModelImplementation = EmployeesOnly.getImplementation( jointedModel );
				return SJoint.getInstance( jointedModelImplementation, jointId );
			}
		} );
	}

	/* package-private */static SJoint[] getJointArray( SJointedModel jointedModel, JointId[] jointIdArray ) {
		return mapToArrayMap.getInitializingIfAbsent( jointedModel, jointIdArray, new MapToMap.Initializer<SJointedModel, JointId[], SJoint[]>() {
			@Override
			public SJoint[] initialize( SJointedModel jointedModel, JointId[] jointIdArray ) {
				SJoint[] jointArray = new SJoint[ jointIdArray.length ];
				for( int i = 0; i < jointIdArray.length; i++ ) {
					jointArray[ i ] = getJoint( jointedModel, jointIdArray[ i ] );
				}
				return jointArray;
			}
		} );
	}

	/* package-private */static SJoint[] getJointArray( SJointedModel jointedModel, JointArrayId jointArrayId ) {
		JointedModelImp jointedModelImplementation = EmployeesOnly.getImplementation( jointedModel );
		return getJointArray( jointedModel, jointedModelImplementation.getJointIdArray( jointArrayId ) );
	}

	private static SJoint getInstance( JointedModelImp jointedModelImplementation, JointId jointId ) {
		JointImp implementation = jointedModelImplementation.getJointImplementation( jointId );
		SJoint rv = implementation.getAbstraction();
		if( rv != null ) {
			//pass
		} else {
			rv = new SJoint( implementation );
			implementation.setAbstraction( rv );
		}
		return rv;
	}

	private final JointImp implementation;

	private SJoint( JointImp implementation ) {
		this.implementation = implementation;
	}

	@Override
			/* package-private */JointImp getImplementation() {
		return this.implementation;
	}

	public Boolean isPivotVisible() {
		return this.implementation.isPivotVisible();
	}

	public void setPivotVisible( Boolean isPivotVisible ) {
		this.implementation.setPivotVisible( isPivotVisible );
	}

	@MethodTemplate( )
	public Double getWidth() {
		return this.getImplementation().getSize().x;
	}

	@MethodTemplate( )
	public Double getHeight() {
		return this.getImplementation().getSize().y;
	}

	@MethodTemplate( )
	public Double getDepth() {
		return this.getImplementation().getSize().z;
	}

}
