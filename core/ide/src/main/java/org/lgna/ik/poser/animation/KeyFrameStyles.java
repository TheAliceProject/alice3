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
package org.lgna.ik.poser.animation;

import org.lgna.story.AnimationStyle;

/**
 * @author Matt May
 */
public enum KeyFrameStyles {
	ARRIVE_AND_EXIT_ABRUPTLY( false, false ),
	ARRIVE_ABRUPTLY_AND_EXIT_GENTLY( false, true ),
	ARRIVE_GENTLY_AND_EXIT_ABRUPTLY( true, false ),
	ARRIVE_AND_EXIT_GENTLY( true, true );
	private boolean isSlowInDesired;
	private boolean isSlowOutDesired;

	KeyFrameStyles( boolean isSlowInDesired, boolean isSlowOutDesired ) {
		this.isSlowInDesired = isSlowInDesired;
		this.isSlowOutDesired = isSlowOutDesired;
	}

	public boolean getIsSlowOutDesired() {
		return isSlowOutDesired;
	}

	public boolean getIsSlowInDesired() {
		return isSlowInDesired;
	}

	public static AnimationStyle getAnimationStyleFromTwoKeyFramStyles( KeyFrameStyles previous, KeyFrameStyles current ) {
		return getAnimationStyleForBooleanPair( ( previous == null ) || previous.isSlowOutDesired, current.isSlowInDesired );
	}

	public static KeyFrameStyles getKeyFrameStyleFromTwoAnimationStyles( AnimationStyle first, AnimationStyle second ) {
		if( first.equals( AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY ) || first.equals( AnimationStyle.BEGIN_AND_END_GENTLY ) ) {
			if( second.equals( AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY ) || second.equals( AnimationStyle.BEGIN_AND_END_ABRUPTLY ) ) {
				return ARRIVE_GENTLY_AND_EXIT_ABRUPTLY;
			} else {
				return ARRIVE_AND_EXIT_GENTLY;
			}
		} else {
			if( second.equals( AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY ) || second.equals( AnimationStyle.BEGIN_AND_END_ABRUPTLY ) ) {
				return ARRIVE_AND_EXIT_ABRUPTLY;
			} else {
				return ARRIVE_ABRUPTLY_AND_EXIT_GENTLY;
			}
		}
	}

	private static AnimationStyle getAnimationStyleForBooleanPair( boolean startGently, boolean endGently ) {
		if( startGently && endGently ) {
			return AnimationStyle.BEGIN_AND_END_GENTLY;
		} else if( startGently && !endGently ) {
			return AnimationStyle.BEGIN_GENTLY_AND_END_ABRUPTLY;
		} else if( !startGently && endGently ) {
			return AnimationStyle.BEGIN_ABRUPTLY_AND_END_GENTLY;
		} else {
			return AnimationStyle.BEGIN_AND_END_ABRUPTLY;
		}
	}
}
