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

package org.lgna.story.implementation;

/**
 * @author Dennis Cosgrove
 */
public abstract class JointedModelImplementation extends SingleVisualModelImplementation {
	private final java.util.Map< org.lgna.story.resources.JointId, JointImplementation > map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	public JointedModelImplementation( edu.cmu.cs.dennisc.scenegraph.Visual sgVisual ) {
		super( sgVisual );
	}
	public JointImplementation getJointImplementation( org.lgna.story.resources.JointId jointId ) {
		synchronized( this.map ) {
			JointImplementation rv = this.map.get( jointId );
			if( rv != null ) {
				//pass
			} else {
				rv = this.createJointImplementation( jointId );
				this.map.put( jointId, rv );
			}
			return rv;
		}
	}
	protected abstract JointImplementation createJointImplementation( org.lgna.story.resources.JointId jointId );
	protected abstract org.lgna.story.resources.JointId[] getRootJointIds();
	
	private org.lgna.story.implementation.visualization.JointedModelVisualization visualization;
	private org.lgna.story.implementation.visualization.JointedModelVisualization getVisualization() {
		if( this.visualization != null ) {
			//pass
		} else {
			this.visualization = new org.lgna.story.implementation.visualization.JointedModelVisualization( this );
		}
		return this.visualization;
	}
	public void showVisualization() {
		this.getVisualization().setParent( this.getSgComposite() );
	}
	public void hideVisualization() {
		if( this.visualization != null ) {
			this.visualization.setParent( null );
		}
	}
	
	public static interface WalkObserver {
		public void pushJoint( JointImplementation joint );
		public void handleBone( JointImplementation parent, JointImplementation child );
		public void popJoint( JointImplementation joint );
	}
	
	private void walk( org.lgna.story.resources.JointId parentId, WalkObserver observer ) {
		JointImplementation parentImpl = this.getJointImplementation( parentId );
		observer.pushJoint( parentImpl );
		for( org.lgna.story.resources.JointId childId : parentId.getChildren() ) {
			observer.handleBone( parentImpl, this.getJointImplementation( childId ) );
		}
		observer.popJoint( parentImpl );
		for( org.lgna.story.resources.JointId childId : parentId.getChildren() ) {
			walk( childId, observer );
		}
	}
	public void walk( WalkObserver observer ) {
		for( org.lgna.story.resources.JointId root : this.getRootJointIds() ) {
			this.walk( root, observer );
		}
	}
}
