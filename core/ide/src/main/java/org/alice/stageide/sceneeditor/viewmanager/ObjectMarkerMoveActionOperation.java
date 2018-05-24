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
package org.alice.stageide.sceneeditor.viewmanager;

import java.awt.Dimension;
import java.text.MessageFormat;
import java.util.UUID;

import javax.swing.Icon;
import javax.swing.JComponent;

import org.alice.ide.instancefactory.ThisFieldAccessFactory;
import org.alice.stageide.oneshot.edits.LocalTransformationEdit;
import org.lgna.croquet.ActionOperation;
import org.lgna.croquet.Application;
import org.lgna.croquet.Element;
import org.lgna.croquet.history.CompletionStep;
import org.lgna.project.ast.AbstractField;
import org.lgna.project.ast.AbstractMethod;
import org.lgna.project.ast.AstUtilities;
import org.lgna.project.ast.Expression;
import org.lgna.project.ast.FieldAccess;
import org.lgna.project.ast.ThisExpression;
import org.lgna.project.ast.UserField;
import org.lgna.story.MoveAndOrientTo;
import org.lgna.story.SMovableTurnable;
import org.lgna.story.SThing;
import org.lgna.story.SThingMarker;

/**
 * @author dculyba
 * 
 */
public abstract class ObjectMarkerMoveActionOperation extends ActionOperation {

	protected static final Dimension ICON_DIMENSION = new Dimension( 25, 20 );

	private UserField markerField;
	private UserField selectedField;

	private UserField toMoveToField;
	private UserField toMoveField;

	private MoveToImageIcon imageIcon;

	protected ObjectMarkerMoveActionOperation( UUID id ) {
		super( Application.PROJECT_GROUP, id );
		this.markerField = null;
		this.selectedField = null;
		this.imageIcon = new MoveToImageIcon();
		this.setButtonIcon( imageIcon );
		this.updateBasedOnSettings();
	}

	protected abstract void updateMoveFields( UserField markerField, UserField selectedField );

	protected void setToMoveToField( UserField toMoveTo, Icon icon ) {
		this.toMoveToField = toMoveTo;
		if( this.toMoveToField != null ) {
			this.imageIcon.setRightImage( icon );
		}
		else {
			this.imageIcon.setRightImage( null );
		}
	}

	protected void setToMoveField( UserField toMove, Icon icon ) {
		this.toMoveField = toMove;
		if( this.toMoveField != null ) {
			this.imageIcon.setLeftImage( icon );
		}
		else {
			this.imageIcon.setLeftImage( null );
		}
	}

	@Override
	protected Class<? extends Element> getClassUsedForLocalization() {
		return ObjectMarkerMoveActionOperation.class;
	}

	private void updateBasedOnSettings()
	{
		if( ( this.toMoveToField != null ) && ( this.toMoveField != null ) )
		{
			String unformattedTooltipText = this.findLocalizedText( "tooltip" );
			MessageFormat formatter = new MessageFormat( "" );
			formatter.setLocale( JComponent.getDefaultLocale() );
			formatter.applyPattern( unformattedTooltipText );
			String tooltipText = formatter.format( new Object[] { this.toMoveField.getName(), this.toMoveToField.getName() } );
			this.setToolTipText( tooltipText );
			this.setEnabled( true );
		}
		else
		{
			this.setToolTipText( this.findLocalizedText( "disabledTooltip" ) );
			this.setEnabled( false );
		}
		this.setButtonIcon( this.imageIcon );
	}

	public void setMarkerField( UserField markerField )
	{
		if( ( markerField == null ) || markerField.getValueType().isAssignableTo( SThingMarker.class ) )
		{
			this.markerField = markerField;
		}
		else
		{
			this.markerField = null;
		}
		if( this.selectedField == this.markerField ) {
			this.selectedField = null;
		}
		updateMoveFields( this.markerField, this.selectedField );
		this.updateBasedOnSettings();
	}

	public void setSelectedField( AbstractField field )
	{
		if( ( field instanceof UserField ) && field.getValueType().isAssignableTo( SMovableTurnable.class ) )
		{
			this.selectedField = (UserField)field;
		}
		else
		{
			this.selectedField = null;
		}
		if( this.selectedField == this.markerField ) {
			this.selectedField = null;
		}
		updateMoveFields( this.markerField, this.selectedField );
		this.updateBasedOnSettings();
	}

	@Override
	protected void perform( CompletionStep<?> step ) {
		if( ( this.toMoveField != null ) && ( this.toMoveToField != null ) ) {
			Expression toMoveToExpression = new FieldAccess( new ThisExpression(), this.toMoveToField );
			AbstractMethod method = AstUtilities.lookupMethod( SMovableTurnable.class, "moveAndOrientTo", new Class<?>[] { SThing.class, MoveAndOrientTo.Detail[].class } );
			LocalTransformationEdit edit = new LocalTransformationEdit( step, ThisFieldAccessFactory.getInstance( this.toMoveField ), method, new Expression[] { toMoveToExpression } );
			step.commitAndInvokeDo( edit );
		} else {
			step.cancel();
		}
	}

}
