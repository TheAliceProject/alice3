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
package org.alice.stageide.type.croquet;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialog extends org.lgna.croquet.ValueCreatorInputDialogCoreComposite<org.lgna.croquet.views.Panel, org.lgna.project.ast.AbstractType> {
	private static class SingletonHolder {
		private static OtherTypeDialog instance = new OtherTypeDialog();
	}

	public static OtherTypeDialog getInstance() {
		return SingletonHolder.instance;
	}

	private class ValueCreatorForRootFilterType extends org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>> {
		public ValueCreatorForRootFilterType( org.lgna.project.ast.JavaType rootFilterType ) {
			super( java.util.UUID.fromString( "84922129-0658-47af-8e32-f2476f030e41" ) );
			this.rootFilterType = rootFilterType;
		}

		@Override
		protected Class<? extends org.lgna.croquet.Element> getClassUsedForLocalization() {
			return OtherTypeDialog.class;
		}

		@Override
		protected org.lgna.project.ast.AbstractType<?, ?, ?> createValue( org.lgna.croquet.history.Transaction transaction, org.lgna.croquet.triggers.Trigger trigger ) {
			org.lgna.croquet.history.CompletionStep<?> completionStep = org.lgna.croquet.history.CompletionStep.createAndAddToTransaction( transaction, this, trigger, new org.lgna.croquet.history.TransactionHistory() );

			OtherTypeDialog.this.initializeRootFilterType( this.rootFilterType );

			org.lgna.project.ast.AbstractType<?, ?, ?> value = OtherTypeDialog.this.createValue( completionStep );
			if( completionStep.isCanceled() ) {
				throw new org.lgna.croquet.CancelException();
			} else {
				return value;
			}
		}

		private final org.lgna.project.ast.JavaType rootFilterType;
	}

	private final edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap<org.lgna.project.ast.JavaType, org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>>> mapTypeToValueCreator = edu.cmu.cs.dennisc.java.util.Maps.newInitializingIfAbsentHashMap();

	private final java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map = edu.cmu.cs.dennisc.java.util.Maps.newHashMap();

	private final TypeTreeState typeTreeState = new TypeTreeState();
	private final org.lgna.croquet.StringValue descriptionText = new org.lgna.croquet.HtmlStringValue( java.util.UUID.fromString( "5417d9ee-bbe5-457b-aa63-1e5d0958ae1f" ) ) {
	};
	private final org.alice.stageide.type.croquet.data.SceneFieldListData sceneFieldListData = new org.alice.stageide.type.croquet.data.SceneFieldListData();
	private final org.lgna.croquet.MultipleSelectionListState<org.lgna.project.ast.UserField> sceneFieldsState = new SceneFieldsState( sceneFieldListData );

	private final AssignableTab assignableTab = new AssignableTab( this );
	private final ContainsTab containsTab = new ContainsTab( this );
	private final org.lgna.croquet.ImmutableDataTabState<?> tabState = this.createImmutableTabState( "tabState", 0, this.assignableTab, this.containsTab );

	private final ErrorStatus noSelectionError = this.createErrorStatus( "noSelectionError" );
	private final Status notAssignableError = new Status() {
		@Override
		public boolean isGoodToGo() {
			return false;
		}

		@Override
		public String getText() {
			StringBuilder sb = new StringBuilder();
			sb.append( "Select class assignable to " );
			if( rootFilterType != null ) {
				sb.append( rootFilterType.getName() );
			}
			return sb.toString();
		}
	};

	private org.lgna.project.ast.JavaType rootFilterType;

	private boolean isInTheMidstOfLowestCommonAncestorSetting;
	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode> typeListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.type.croquet.TypeNode> e ) {
			org.alice.stageide.type.croquet.TypeNode nextValue = e.getNextValue();
			handleTypeChange( nextValue != null ? nextValue.getType() : null );
		}
	};

	private final org.lgna.croquet.event.ValueListener<java.util.List<org.lgna.project.ast.UserField>> sceneFieldListener = new org.lgna.croquet.event.ValueListener<java.util.List<org.lgna.project.ast.UserField>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<java.util.List<org.lgna.project.ast.UserField>> e ) {
			java.util.List<org.lgna.project.ast.UserField> fields = e.getNextValue();
			TypeNode sharedNode = null;
			if( fields.size() > 0 ) {
				for( org.lgna.project.ast.UserField field : fields ) {
					TypeNode typeNode = map.get( field.getValueType() );
					if( sharedNode != null ) {
						sharedNode = (TypeNode)sharedNode.getSharedAncestor( typeNode );
					} else {
						sharedNode = typeNode;
					}
				}
			}
			isInTheMidstOfLowestCommonAncestorSetting = true;
			try {
				typeTreeState.setValueTransactionlessly( sharedNode );
			} finally {
				isInTheMidstOfLowestCommonAncestorSetting = false;
			}
		}
	};

	private OtherTypeDialog() {
		super( java.util.UUID.fromString( "58d24fb6-a6f5-4ad9-87b0-dfb5e9e4de41" ) );
	}

	public org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>> getValueCreator( org.lgna.project.ast.JavaType rootType ) {
		return this.mapTypeToValueCreator.getInitializingIfAbsent( rootType, new edu.cmu.cs.dennisc.java.util.InitializingIfAbsentMap.Initializer<org.lgna.project.ast.JavaType, org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>>>() {
			@Override
			public org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>> initialize( org.lgna.project.ast.JavaType key ) {
				return new ValueCreatorForRootFilterType( key );
			}
		} );
	}

	public org.lgna.croquet.ValueCreator<org.lgna.project.ast.AbstractType<?, ?, ?>> getValueCreator( Class<? extends org.lgna.story.SThing> rootCls ) {
		return this.getValueCreator( org.lgna.project.ast.JavaType.getInstance( rootCls ) );
	}

	private void initializeRootFilterType( org.lgna.project.ast.JavaType rootFilterType ) {
		this.rootFilterType = rootFilterType;
	}

	@Override
	protected Integer getWiderGoldenRatioSizeFromHeight() {
		return 600;
	}

	public org.lgna.croquet.TabState getTabState() {
		return this.tabState;
	}

	public org.lgna.croquet.MultipleSelectionListState<org.lgna.project.ast.UserField> getSceneFieldsState() {
		return this.sceneFieldsState;
	}

	public org.lgna.croquet.SingleSelectTreeState<TypeNode> getTypeTreeState() {
		return this.typeTreeState;
	}

	public org.lgna.croquet.StringValue getDescriptionText() {
		return this.descriptionText;
	}

	@Override
	protected org.lgna.project.ast.AbstractType createValue() {
		TypeNode typeNode = this.typeTreeState.getValue();
		if( typeNode != null ) {
			return typeNode.getType();
		} else {
			return null;
		}
	}

	@Override
	protected Status getStatusPreRejectorCheck( org.lgna.croquet.history.CompletionStep<?> step ) {
		TypeNode typeNode = this.typeTreeState.getValue();
		if( typeNode != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = typeNode.getType();
			//todo assert this.rootFilterType != null;
			if( ( this.rootFilterType == null ) || this.rootFilterType.isAssignableFrom( type ) ) {
				return IS_GOOD_TO_GO_STATUS;
			} else {
				return this.notAssignableError;
			}
		} else {
			return this.noSelectionError;
		}
	}

	private static TypeNode build( org.lgna.project.ast.AbstractType<?, ?, ?> type, java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map ) {
		assert type != null;
		TypeNode typeNode = map.get( type );
		if( typeNode != null ) {
			//pass
		} else {
			typeNode = new TypeNode( type );
			map.put( type, typeNode );
			org.lgna.project.ast.AbstractType<?, ?, ?> superType = type.getSuperType();
			TypeNode superTypeNode = map.get( superType );
			if( superTypeNode != null ) {
				//pass
			} else {
				superTypeNode = build( superType, map );
			}
			superTypeNode.add( typeNode );
		}
		return typeNode;
	}

	@Override
	public void handlePreActivation() {
		org.lgna.project.Project project = org.alice.ide.ProjectStack.peekProject();
		Iterable<org.lgna.project.ast.NamedUserType> types = project.getNamedUserTypes();
		map.clear();

		org.lgna.project.ast.JavaType rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class );
		TypeNode rootNode = new TypeNode( rootType );
		map.put( rootType, rootNode );
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( this.rootFilterType.isAssignableFrom( type ) ) {
				build( type, map );
			}
		}
		this.sceneFieldListData.refresh();

		// handle JavaType scene fields
		synchronized( this.sceneFieldListData ) {
			final int N = this.sceneFieldListData.getItemCount();
			for( int i = 0; i < N; i++ ) {
				org.lgna.project.ast.UserField field = this.sceneFieldListData.getItemAt( i );
				org.lgna.project.ast.AbstractType<?, ?, ?> valueType = field.getValueType();
				if( valueType instanceof org.lgna.project.ast.JavaType ) {
					org.lgna.project.ast.JavaType javaValueType = (org.lgna.project.ast.JavaType)valueType;
					build( javaValueType, map );
				}
			}
		}

		this.typeTreeState.setRoot( rootNode );
		this.typeTreeState.addAndInvokeNewSchoolValueListener( this.typeListener );
		this.sceneFieldsState.addNewSchoolValueListener( this.sceneFieldListener );

		this.containsTab.getMemberListData().connect( rootNode );
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		this.containsTab.getMemberListData().disconnect();
		this.sceneFieldsState.removeNewSchoolValueListener( this.sceneFieldListener );
		this.typeTreeState.removeNewSchoolValueListener( this.typeListener );
		super.handlePostDeactivation();
	}

	@Override
	protected org.lgna.croquet.views.Panel createView() {
		return new org.alice.stageide.type.croquet.views.OtherTypeDialogPane( this );
	}

	private static boolean isInclusionDesired( org.lgna.project.ast.AbstractMember member ) {
		if( member instanceof org.lgna.project.ast.AbstractMethod ) {
			org.lgna.project.ast.AbstractMethod method = (org.lgna.project.ast.AbstractMethod)member;
			if( method.isStatic() ) {
				return false;
			}
		} else if( member instanceof org.lgna.project.ast.AbstractField ) {
			org.lgna.project.ast.AbstractField field = (org.lgna.project.ast.AbstractField)member;
			if( field.isStatic() ) {
				return false;
			}
		}
		if( member.isPublicAccess() || member.isUserAuthored() ) {
			org.lgna.project.annotations.Visibility visibility = member.getVisibility();
			return ( visibility == null ) || visibility.equals( org.lgna.project.annotations.Visibility.PRIME_TIME );
		} else {
			return false;
		}
	}

	private static void appendMembers( StringBuilder sb, org.lgna.project.ast.AbstractType<?, ?, ?> type, boolean isSelected ) {
		if( isSelected ) {
			sb.append( "<h2>" );
		} else {
			sb.append( "<h2>" );
		}
		sb.append( "class " );
		sb.append( type.getName() );
		if( isSelected ) {
			sb.append( "</h2>" );
		} else {
			sb.append( " <em>(inherit)</em></h2>" );
		}

		java.util.List<? extends org.lgna.project.ast.AbstractMethod> methods = type.getDeclaredMethods();

		boolean isFirst = true;
		for( org.lgna.project.ast.AbstractMethod method : methods ) {
			if( isInclusionDesired( method ) ) {
				if( method.isProcedure() ) {
					if( isFirst ) {
						sb.append( "<em>procedures</em>" );
						sb.append( "<ul>" );
						isFirst = false;
					}
					sb.append( "<li>" );
					sb.append( method.getName() );
					sb.append( "</li>" );
				}
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}
		isFirst = true;
		for( org.lgna.project.ast.AbstractMethod method : methods ) {
			if( isInclusionDesired( method ) ) {
				if( method.isFunction() ) {
					if( isFirst ) {
						sb.append( "<em>functions</em>" );
						sb.append( "<ul>" );
						isFirst = false;
					}
					sb.append( "<li>" );
					sb.append( method.getName() );
					sb.append( "</li>" );
				}
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}

		isFirst = true;
		for( org.lgna.project.ast.AbstractField field : type.getDeclaredFields() ) {
			if( isInclusionDesired( field ) ) {
				if( isFirst ) {
					sb.append( "<em>properties</em>" );
					sb.append( "<ul>" );
					isFirst = false;
				}
				sb.append( "<li>" );
				sb.append( field.getName() );
				sb.append( "</li>" );
			}
		}
		if( isFirst ) {
			//pass
		} else {
			sb.append( "</ul>" );
		}

		if( type.isFollowToSuperClassDesired() ) {
			appendMembers( sb, type.getSuperType(), false );
		}
	}

	private void handleTypeChange( org.lgna.project.ast.AbstractType<?, ?, ?> type ) {
		StringBuilder sb = new StringBuilder();
		sb.append( "<html>" );
		sb.append( "<body bgcolor=\"#FFFFFF\">" );
		if( type != null ) {
			appendMembers( sb, type, true );
		} else {
			sb.append( "<em>no class selected</em>" );
		}
		sb.append( "</body>" );
		sb.append( "</html>" );
		descriptionText.setText( sb.toString() );

		org.lgna.croquet.data.ListData<org.lgna.project.ast.UserField> data = sceneFieldsState.getData();

		this.getView().repaint();
		if( this.isInTheMidstOfLowestCommonAncestorSetting ) {
			//
		} else {
			java.util.List<org.lgna.project.ast.UserField> fields = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			if( type != null ) {
				synchronized( data ) {
					final int N = data.getItemCount();
					for( int i = 0; i < N; i++ ) {
						org.lgna.project.ast.UserField item = data.getItemAt( i );
						if( type.isAssignableFrom( item.getValueType() ) ) {
							fields.add( item );
						}
					}
				}
			}
			this.sceneFieldsState.setValue( fields );
		}
	}

	public TypeNode getTypeNodeFor( org.lgna.project.ast.AbstractType<?, ?, ?> nextValue ) {
		return nextValue != null ? map.get( nextValue ) : null;
	}

	public static void main( String[] args ) throws Exception {
		edu.cmu.cs.dennisc.javax.swing.UIManagerUtilities.setLookAndFeel( "Nimbus" );

		new org.lgna.croquet.simple.SimpleApplication();

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( args[ 0 ] );
		org.alice.ide.ProjectStack.pushProject( project );
		org.lgna.croquet.triggers.Trigger trigger = null;
		OtherTypeDialog.getInstance().getValueCreator( org.lgna.story.SModel.class ).fire( trigger );
		System.exit( 0 );
	}
}
