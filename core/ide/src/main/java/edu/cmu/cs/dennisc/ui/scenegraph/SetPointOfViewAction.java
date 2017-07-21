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
package edu.cmu.cs.dennisc.ui.scenegraph;

/**
 * @author Dennis Cosgrove
 */
public class SetPointOfViewAction implements edu.cmu.cs.dennisc.pattern.Action {
	private edu.cmu.cs.dennisc.animation.Animator m_animator;
	private edu.cmu.cs.dennisc.scenegraph.Transformable m_sgSubject;
	private edu.cmu.cs.dennisc.scenegraph.ReferenceFrame m_sgAsSeenBy;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_undoPOV;
	private edu.cmu.cs.dennisc.math.AffineMatrix4x4 m_redoPOV;

	public SetPointOfViewAction( edu.cmu.cs.dennisc.animation.Animator animator, edu.cmu.cs.dennisc.scenegraph.Transformable sgSubject, edu.cmu.cs.dennisc.scenegraph.ReferenceFrame sgAsSeenBy, edu.cmu.cs.dennisc.math.AffineMatrix4x4 undoPOV, edu.cmu.cs.dennisc.math.AffineMatrix4x4 redoPOV ) {
		m_animator = animator;
		m_sgSubject = sgSubject;
		m_sgAsSeenBy = sgAsSeenBy;
		m_undoPOV = undoPOV;
		m_redoPOV = redoPOV;
	}

	@Override
	public void run() {
		assert m_undoPOV == null;
		m_undoPOV = m_sgSubject.getTransformation( m_sgAsSeenBy );
		redo();
	}

	private void animate( edu.cmu.cs.dennisc.math.AffineMatrix4x4 pov ) {
		if( m_animator != null ) {
			m_animator.invokeAndWait_ThrowRuntimeExceptionsIfNecessary( new edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation( m_sgSubject, m_sgAsSeenBy, edu.cmu.cs.dennisc.animation.affine.PointOfViewAnimation.USE_EXISTING_VALUE_AT_RUN_TIME, pov ), null );
		}
	}

	@Override
	public void undo() {
		animate( m_undoPOV );
	}

	@Override
	public void redo() {
		animate( m_redoPOV );
	}
}
