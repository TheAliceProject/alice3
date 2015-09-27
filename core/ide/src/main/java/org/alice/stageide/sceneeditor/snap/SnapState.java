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

package org.alice.stageide.sceneeditor.snap;

import org.lgna.croquet.BooleanState;
import org.lgna.croquet.BoundedDoubleState;

import edu.cmu.cs.dennisc.math.Angle;
import edu.cmu.cs.dennisc.math.AngleInDegrees;

public class SnapState
{

	private static class SingletonHolder {
		private static SnapState instance = new SnapState();
	}

	public static SnapState getInstance() {
		return SingletonHolder.instance;
	}

	private SnapState()
	{
	}

	private static org.alice.stageide.sceneeditor.side.SideComposite getSideComposite() {
		return org.alice.stageide.sceneeditor.side.SideComposite.getInstance();
	}

	private org.alice.stageide.sceneeditor.side.SnapDetailsToolPaletteCoreComposite getSnapDetailsToolPaletteCoreComposite() {
		return getSideComposite().getSnapDetailsToolPaletteCoreComposite();
	}

	public BooleanState getIsSnapEnabledState()
	{
		return getSideComposite().getIsSnapEnabledState();
	}

	public BooleanState getIsSnapToGroundEnabledState()
	{
		return getSnapDetailsToolPaletteCoreComposite().getIsSnapToGroundEnabledState();
	}

	public BooleanState getIsSnapToGridEnabledState()
	{
		return getIsSnapEnabledState();
	}

	public BooleanState getIsRotationSnapEnabledState()
	{
		return getSnapDetailsToolPaletteCoreComposite().getIsRotationState();
	}

	public BooleanState getShowSnapGridState()
	{
		return getSnapDetailsToolPaletteCoreComposite().getIsGridShowingState();
	}

	public BoundedDoubleState getSnapGridSpacingState()
	{
		return getSnapDetailsToolPaletteCoreComposite().getGridSpacingState();
	}

	public BoundedDoubleState getSnapAngleInDegreesState()
	{
		return getSnapDetailsToolPaletteCoreComposite().getAngleState();
	}

	//	public void setShouldSnapToGroundEnabled(boolean shouldSnapToGround)
	//	{
	//		this.getIsSnapToGroundEnabledState().setValue(shouldSnapToGround);
	//	}

	public boolean shouldSnapToGround()
	{
		return this.getIsSnapEnabledState().getValue() && this.getIsSnapToGroundEnabledState().getValue();
	}

	public boolean isSnapToGroundEnabled()
	{
		return this.getIsSnapToGroundEnabledState().getValue();
	}

	//	public void setShouldSnapToGridEnabled(boolean shouldSnapToGround)
	//	{
	//		this.getIsSnapToGridEnabledState().setValue(shouldSnapToGround);
	//	}

	public boolean shouldSnapToGrid()
	{
		return this.getIsSnapEnabledState().getValue() && this.getIsSnapToGridEnabledState().getValue();
	}

	public boolean isSnapToGridEnabled()
	{
		return this.getIsSnapToGridEnabledState().getValue();
	}

	//	public void setGridSpacing(double gridSpacing)
	//	{
	//		this.getSnapGridSpacingState().setValue(gridSpacing);
	//	}

	public double getGridSpacing()
	{
		return this.getSnapGridSpacingState().getValue();
	}

	//	public void setSnapEnabled(boolean snapEnabled)
	//	{
	//		this.getIsSnapEnabledState().setValue(snapEnabled);
	//	}

	public boolean isSnapEnabled()
	{
		return this.getIsSnapEnabledState().getValue();
	}

	//	public void setRotationSnapEnabled(boolean rotationSnapEnabled)
	//	{
	//		this.getIsRotationSnapEnabledState().setValue(rotationSnapEnabled);
	//	}

	public boolean shouldSnapToRotation()
	{
		return this.getIsSnapEnabledState().getValue() && this.getIsRotationSnapEnabledState().getValue();
	}

	public boolean isRotationSnapEnabled()
	{
		return this.getIsRotationSnapEnabledState().getValue();
	}

	//	public void setRotationSnapAngleInDegrees(double degrees)
	//	{
	//		this.getSnapAngleInDegreesState().setValue(degrees);
	//	}

	//	public void setRotationSnapAngle(Angle snapAngle)
	//	{
	//		setRotationSnapAngleInDegrees(new AngleInDegrees(snapAngle).getAsDegrees());
	//	}

	public Angle getRotationSnapAngle()
	{
		return new AngleInDegrees( this.getSnapAngleInDegreesState().getValue() );
	}

	public boolean shouldShowSnapGrid()
	{
		return this.getIsSnapEnabledState().getValue() && this.getShowSnapGridState().getValue();
	}

	public boolean isShowSnapGridEnabled()
	{
		return this.getShowSnapGridState().getValue();
	}

	//	public void setShowSnapGrid( boolean showSnapGrid )
	//	{
	//		this.getShowSnapGridState().setValue(showSnapGrid);
	//	}

}
