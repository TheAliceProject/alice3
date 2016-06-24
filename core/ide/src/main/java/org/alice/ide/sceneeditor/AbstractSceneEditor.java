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
package org.alice.ide.sceneeditor;

import org.lgna.common.ComponentThread;
import org.lgna.project.ast.Expression;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;

/**
 * @author Dennis Cosgrove
 */
public abstract class AbstractSceneEditor extends org.lgna.croquet.views.BorderPanel {

	private org.lgna.project.virtualmachine.VirtualMachine vm;

	private java.util.Map<org.lgna.project.ast.UserField, org.lgna.project.virtualmachine.UserInstance> mapSceneFieldToInstance = new java.util.HashMap<org.lgna.project.ast.UserField, org.lgna.project.virtualmachine.UserInstance>();
	private java.util.Map<org.lgna.project.virtualmachine.UserInstance, org.lgna.project.ast.UserField> mapSceneInstanceToField = new java.util.HashMap<org.lgna.project.virtualmachine.UserInstance, org.lgna.project.ast.UserField>();

	private java.util.Map<org.lgna.project.ast.UserField, org.lgna.project.ast.Statement> mapSceneFieldToInitialCodeState = new java.util.HashMap<org.lgna.project.ast.UserField, org.lgna.project.ast.Statement>();

	private org.lgna.project.ast.NamedUserType programType;
	private org.lgna.project.virtualmachine.UserInstance programInstance;
	private org.lgna.project.ast.UserField selectedField;

	private final SceneFieldState sceneFieldListSelectionState = new SceneFieldState();

	private final org.lgna.croquet.State.ValueListener<org.alice.ide.ProjectDocument> projectListener = new org.lgna.croquet.State.ValueListener<org.alice.ide.ProjectDocument>() {
		@Override
		public void changing( org.lgna.croquet.State<org.alice.ide.ProjectDocument> state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
		}

		@Override
		public void changed( org.lgna.croquet.State<org.alice.ide.ProjectDocument> state, org.alice.ide.ProjectDocument prevValue, org.alice.ide.ProjectDocument nextValue, boolean isAdjusting ) {
			AbstractSceneEditor.this.handleProjectOpened( nextValue != null ? nextValue.getProject() : null );
		}
	};

	private org.lgna.croquet.event.ValueListener<org.lgna.project.ast.UserField> selectedSceneObserver = new org.lgna.croquet.event.ValueListener<org.lgna.project.ast.UserField>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.lgna.project.ast.UserField> e ) {
			org.lgna.project.ast.UserField nextValue = e.getNextValue();
			AbstractSceneEditor.this.setActiveScene( nextValue );
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective> perspectiveListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.perspectives.ProjectPerspective>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.perspectives.ProjectPerspective> e ) {
			org.alice.ide.perspectives.ProjectPerspective prevValue = e.getPreviousValue();
			org.alice.ide.perspectives.ProjectPerspective nextValue = e.getNextValue();
			if( prevValue != nextValue ) {
				AbstractSceneEditor.this.handleExpandContractChange( nextValue == org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getSetupScenePerspective() );
			}
		}
	};

	public abstract void disableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );

	public abstract void enableRendering( org.alice.ide.ReasonToDisableSomeAmountOfRendering reasonToDisableSomeAmountOfRendering );

	public abstract void generateCodeForSetUp( org.lgna.project.ast.StatementListProperty bodyStatementsProperty );

	public abstract org.lgna.project.ast.Statement[] getDoStatementsForAddField( org.lgna.project.ast.UserField field, AffineMatrix4x4 initialTransform );

	public abstract org.lgna.project.ast.Statement[] getDoStatementsForCopyField( org.lgna.project.ast.UserField fieldToCopy, org.lgna.project.ast.UserField newField, AffineMatrix4x4 initialTransform );

	public abstract org.lgna.project.ast.Statement[] getUndoStatementsForAddField( org.lgna.project.ast.UserField field );

	public abstract org.lgna.project.ast.Statement[] getDoStatementsForRemoveField( org.lgna.project.ast.UserField field );

	public abstract org.lgna.project.ast.Statement[] getUndoStatementsForRemoveField( org.lgna.project.ast.UserField field );

	public abstract void preScreenCapture();

	public abstract void postScreenCapture();

	protected abstract void handleExpandContractChange( boolean isExpanded );

	//Initialization
	private boolean isInitialized = false;

	private void initializeIfNecessary()
	{
		if( !this.isInitialized )
		{
			initializeComponents();
			initializeObservers();
			this.isInitialized = true;
		}
	}

	protected void initializeComponents() {

	}

	protected void initializeObservers() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getPerspectiveState().addAndInvokeNewSchoolValueListener( this.perspectiveListener );
	}

	protected void setInitialCodeStateForField( org.lgna.project.ast.UserField field, org.lgna.project.ast.Statement code ) {
		this.mapSceneFieldToInitialCodeState.put( field, code );
	}

	protected org.lgna.project.ast.Statement getInitialCodeforField( org.lgna.project.ast.UserField field ) {
		return this.mapSceneFieldToInitialCodeState.get( field );
	}

	public org.lgna.project.ast.UserField getActiveSceneField() {
		return this.sceneFieldListSelectionState.getValue();
	}

	public org.lgna.project.ast.UserField getFieldForInstanceInJavaVM( Object javaInstance ) {
		return getActiveSceneInstance().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_getFieldForInstanceInJava( javaInstance );
	}

	public Object getInstanceInJavaVMForField( org.lgna.project.ast.AbstractField field ) {
		if( field == null ) {
			return null;
		}
		assert field instanceof org.lgna.project.ast.UserField;
		if( field == this.getActiveSceneField() )
		{
			return getActiveSceneInstance().getJavaInstance();
		}
		else
		{
			return getActiveSceneInstance().getFieldValueInstanceInJava( (org.lgna.project.ast.UserField)field );
		}
	}

	public Object getInstanceForExpression( org.lgna.project.ast.Expression expression ) {
		if( expression == null ) {
			return null;
		}
		try {
			Object[] values = this.getVirtualMachine().ENTRY_POINT_evaluate(
					getActiveSceneInstance(),
					new Expression[] { expression }
					);
			if( values.length > 0 ) {
				return values[ 0 ];
			}
			else {
				return null;
			}
		} catch( Throwable t ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.throwable( t );
			return null;
		}
	}

	public Object getInstanceInJavaVMForExpression( org.lgna.project.ast.Expression expression ) {
		return org.lgna.project.virtualmachine.UserInstance.getJavaInstanceIfNecessary( getInstanceForExpression( expression ) );
	}

	public <E> E getInstanceInJavaVMForField( org.lgna.project.ast.AbstractField field, Class<E> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getInstanceInJavaVMForField( field ), cls );
	}

	public <E> E getInstanceInJavaVMForExpression( org.lgna.project.ast.Expression expression, Class<E> cls ) {
		return edu.cmu.cs.dennisc.java.lang.ClassUtilities.getInstance( getInstanceInJavaVMForExpression( expression ), cls );
	}

	public <T extends org.lgna.story.implementation.EntityImp> T getImplementation( org.lgna.project.ast.AbstractField field ) {
		if( field == null ) {
			return null;
		}
		org.lgna.story.SThing entity = getInstanceInJavaVMForField( field, org.lgna.story.SThing.class );
		if( entity != null )
		{
			return org.lgna.story.EmployeesOnly.getImplementation( entity );
		}
		else
		{
			return null;
		}
	}

	public org.lgna.project.ast.UserField getSelectedField()
	{
		return this.selectedField;
	}

	public void setSelectedField( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field )
	{
		assert ( declaringType == this.getActiveSceneType() ) || ( field == this.getActiveSceneField() );
		this.selectedField = field;
	}

	public void executeStatements( org.lgna.project.ast.Statement... statements )
	{
		for( org.lgna.project.ast.Statement statement : statements )
		{
			this.getVirtualMachine().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_executeStatement( this.getActiveSceneInstance(), statement );
		}
	}

	public void addField( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field, int index, org.lgna.project.ast.Statement... statements ) {
		assert declaringType == this.getActiveSceneType() : declaringType;
		this.getVirtualMachine().ACCEPTABLE_HACK_FOR_SCENE_EDITOR_initializeField( this.getActiveSceneInstance(), field );
		org.lgna.story.SProgram program = this.getProgramInstanceInJava();
		double prevSimulationSpeedFactor = program.getSimulationSpeedFactor();
		program.setSimulationSpeedFactor( Double.POSITIVE_INFINITY );
		try {
			this.executeStatements( statements );
		} finally {
			program.setSimulationSpeedFactor( prevSimulationSpeedFactor );
		}
		this.getActiveSceneType().fields.add( index, field );
		org.alice.ide.ast.AstEventManager.fireTypeHierarchyListeners();
		this.setSelectedField( declaringType, field );

	}

	public abstract org.lgna.project.ast.Statement getCurrentStateCodeForField( org.lgna.project.ast.UserField field );

	public void setFieldToState( org.lgna.project.ast.UserField field, final org.lgna.project.ast.Statement... statements ) {
		new ComponentThread( new Runnable() {
			@Override
			public void run() {
				executeStatements( statements );
			}
		}, "SetFieldToState(" + field.getName() + ")" ).start();

	}

	public void revertFieldToInitialState( org.lgna.project.ast.UserField field ) {
		org.lgna.project.ast.Statement code = this.getInitialCodeforField( field );
		if( code == null ) {
			edu.cmu.cs.dennisc.java.util.logging.Logger.severe( "No initial state code found for field " + field );
			return;
		}
		this.setFieldToState( field, code );
	}

	public void removeField( org.lgna.project.ast.UserType<?> declaringType, org.lgna.project.ast.UserField field, org.lgna.project.ast.Statement... statements ) {
		assert declaringType == this.getActiveSceneType() : declaringType + " " + field;
		for( int i = 0; i < this.getActiveSceneType().fields.size(); i++ )
		{
			if( this.getActiveSceneType().fields.get( i ) == field )
			{
				this.getActiveSceneType().fields.remove( i );
				break;
			}
		}
		this.executeStatements( statements );
		if( this.selectedField == field )
		{
			org.lgna.project.ast.UserField uf = this.getActiveSceneField();
			this.setSelectedField( uf.getDeclaringType(), uf );
		}
	}

	public org.lgna.project.ast.NamedUserType getActiveSceneType()
	{
		org.lgna.project.ast.UserField field = this.getActiveSceneField();
		if( field != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = field.getValueType();
			if( type instanceof org.lgna.project.ast.NamedUserType ) {
				return (org.lgna.project.ast.NamedUserType)type;
			}
		}
		return null;
	}

	public org.lgna.project.virtualmachine.UserInstance getActiveSceneInstance()
	{
		return this.mapSceneFieldToInstance.get( this.getActiveSceneField() );
	}

	public <T extends org.lgna.story.implementation.EntityImp> T getActiveSceneImplementation() {
		org.lgna.story.SThing entity = getInstanceInJavaVMForField( getActiveSceneField(), org.lgna.story.SThing.class );
		if( entity != null )
		{
			return org.lgna.story.EmployeesOnly.getImplementation( entity );
		}
		else
		{
			return null;
		}
	}

	public final org.lgna.project.virtualmachine.VirtualMachine getVirtualMachine() {
		if( this.vm != null ) {
			//pass
		} else {
			this.vm = org.alice.ide.IDE.getActiveInstance().createRegisteredVirtualMachineForSceneEditor();
		}
		return this.vm;
	}

	protected void addScene( org.lgna.project.ast.UserField sceneField ) {
		org.lgna.project.ast.NamedUserType sceneType = (org.lgna.project.ast.NamedUserType)sceneField.getValueType();
		Object userInstance = this.programInstance.getFieldValue( sceneField );
		assert userInstance != null;
		assert userInstance instanceof org.lgna.project.virtualmachine.UserInstance;
		org.lgna.project.virtualmachine.UserInstance rv = (org.lgna.project.virtualmachine.UserInstance)userInstance;
		rv.ensureInverseMapExists();
		mapSceneFieldToInstance.put( sceneField, rv );
		mapSceneInstanceToField.put( rv, sceneField );
		this.sceneFieldListSelectionState.addItem( sceneField );

		//		for (org.lgna.project.ast.AbstractField field : sceneField.valueType.getValue().getDeclaredFields())
		//		{
		//			if (field instanceof org.lgna.project.ast.UserField) {
		//				org.lgna.project.ast.UserField uf = (org.lgna.project.ast.UserField)field;
		//				this.addField(sceneField.valueType.getValue(), uf, statements)
		//			}
		//		}

	}

	protected void setActiveScene( org.lgna.project.ast.UserField sceneField ) {
		//note: added by dennisc
		this.initializeIfNecessary();
		//

		this.sceneFieldListSelectionState.setValueTransactionlessly( sceneField );

		//Run the "setActiveScene" call on the program to get the active scene set in the right state
		//		edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneAliceInstance = getActiveSceneInstance();
		//		org.lookingglassandalice.storytelling.Scene sceneJavaInstance = (org.lookingglassandalice.storytelling.Scene)sceneAliceInstance.getInstanceInJava();
		//		getProgramInstanceInJava().setActiveScene(sceneJavaInstance);
	}

	protected org.lgna.project.virtualmachine.UserInstance getUserProgramInstance()
	{
		return this.programInstance;
	}

	protected org.lgna.story.SProgram getProgramInstanceInJava()
	{
		return (org.lgna.story.SProgram)this.programInstance.getJavaInstance();
	}

	protected void setProgramInstance( org.lgna.project.virtualmachine.UserInstance programInstance )
	{
		this.programInstance = programInstance;
	}

	protected org.lgna.project.virtualmachine.UserInstance createProgramInstance() {
		return getVirtualMachine().ENTRY_POINT_createInstance( this.programType );
	}

	protected void setProgramType( org.lgna.project.ast.NamedUserType programType ) {
		if( this.programType != programType ) {
			if( this.programType != null ) {
				this.sceneFieldListSelectionState.removeNewSchoolValueListener( this.selectedSceneObserver );
				this.sceneFieldListSelectionState.clear();
			}
			this.programType = programType;
			mapSceneFieldToInstance.clear();
			mapSceneInstanceToField.clear();
			if( this.programType != null ) {
				setProgramInstance( this.createProgramInstance() );
				for( org.lgna.project.ast.AbstractField programField : this.programType.getDeclaredFields() )
				{
					if( programField.getValueType().isAssignableTo( org.lgna.story.SScene.class ) )
					{
						this.addScene( (org.lgna.project.ast.UserField)programField );
					}
				}
			} else {
				setProgramInstance( null );
			}
			if( this.sceneFieldListSelectionState.getItemCount() > 0 )
			{
				this.sceneFieldListSelectionState.setSelectedIndex( 0 );
			}
			this.sceneFieldListSelectionState.addAndInvokeNewSchoolValueListener( this.selectedSceneObserver );
		}
	}

	protected void handleProjectOpened( org.lgna.project.Project nextProject ) {
		AbstractSceneEditor.this.setProgramType( nextProject.getProgramType() );
		AbstractSceneEditor.this.revalidateAndRepaint();
	}

	//	@Override
	//	protected void handleDisplayable() {
	//		super.handleDisplayable();
	//		initializeIfNecessary();
	//	}

	private boolean EPIC_HACK_isFirstAddedTo = true;

	@Override
	protected void handleAddedTo( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		if( EPIC_HACK_isFirstAddedTo ) {
			org.alice.ide.ProjectDocument projectDocument = org.alice.ide.ProjectApplication.getActiveInstance().getDocumentFrame().getDocument();
			if( projectDocument != null ) {
				this.projectListener.changed( null, null, projectDocument, false );
				edu.cmu.cs.dennisc.java.util.logging.Logger.todo( "remove firing changed", projectDocument );
			}
			EPIC_HACK_isFirstAddedTo = false;
		}
		this.initializeIfNecessary();
		org.alice.ide.project.ProjectDocumentState.getInstance().addValueListener( this.projectListener );
		super.handleAddedTo( parent );
	}

	@Override
	protected void handleRemovedFrom( org.lgna.croquet.views.AwtComponentView<?> parent ) {
		super.handleRemovedFrom( parent );
		org.alice.ide.project.ProjectDocumentState.getInstance().removeValueListener( this.projectListener );
	}

}
