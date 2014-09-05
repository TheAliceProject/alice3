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
package org.lgna.ik.poser.animation.composites;

import org.lgna.croquet.SimpleComposite;
import org.lgna.ik.poser.animation.KeyFrameData;
import org.lgna.ik.poser.animation.TimeLine;
import org.lgna.ik.poser.animation.views.TimeLineView;
import org.lgna.story.Pose;

/**
 * @author Matt May
 */
public class TimeLineComposite extends SimpleComposite<TimeLineView> {

	private boolean isTimeMutable = true;
	private final TimeLine timeLine = new TimeLine();

	public TimeLineComposite() {
		super( java.util.UUID.fromString( "45b24458-c06e-4480-873a-f1698bf03edb" ) );
	}

	public void selectKeyFrame( KeyFrameData keyFrameData ) {
		KeyFrameData selected = timeLine.getSelectedKeyFrame();
		if( selected != null ) {
			getView().deselect( selected );
		}
		timeLine.setSelectedKeyFrame( keyFrameData );
	}

	public double getDurationForKeyFrame( KeyFrameData pose ) {
		return timeLine.getDurationForKeyFrame( pose );
	}

	@Override
	public TimeLineView createView() {
		return new TimeLineView( this );
	}

	public void setEditEnabled( boolean b ) {
		isTimeMutable = !b;
	}

	public boolean getIsTimeMutable() {
		return isTimeMutable;
	}

	public TimeLine getTimeLine() {
		return timeLine;
	}

	public void setInitialPose( Pose<?> pose ) {
		this.timeLine.setInitialPose( pose );
	}

	public KeyFrameData getSelectedKeyFrame() {
		return timeLine.getSelectedKeyFrame();
	}
}
