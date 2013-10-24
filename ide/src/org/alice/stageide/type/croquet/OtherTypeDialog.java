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
package org.alice.stageide.type.croquet;

/**
 * @author Dennis Cosgrove
 */
public class OtherTypeDialog extends org.lgna.croquet.SingleValueCreatorInputDialogCoreComposite<org.lgna.croquet.components.Panel, org.lgna.project.ast.AbstractType> {
	private static class SingletonHolder {
		private static OtherTypeDialog instance = new OtherTypeDialog();
	}

	public static OtherTypeDialog getInstance() {
		return SingletonHolder.instance;
	}

	private java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map = edu.cmu.cs.dennisc.java.util.Collections.newHashMap();
	private final AssignableTab assignableTab = new AssignableTab( this );
	private final ContainsTab containsTab = new ContainsTab();

	private final org.lgna.croquet.TabSelectionState tabState = this.createTabSelectionState( this.createKey( "tabState" ), 0, this.assignableTab, this.containsTab );
	private final TypeTreeState typeTreeState = new TypeTreeState();
	private final org.lgna.croquet.StringValue descriptionText = new org.lgna.croquet.HtmlStringValue( java.util.UUID.fromString( "5417d9ee-bbe5-457b-aa63-1e5d0958ae1f" ) ) {
	};
	private final org.alice.stageide.type.croquet.data.SceneFieldListData sceneFieldListData = new org.alice.stageide.type.croquet.data.SceneFieldListData();
	private final org.lgna.croquet.MultipleSelectionState<org.lgna.project.ast.UserField> sceneFieldsState = new SceneFieldsState( sceneFieldListData );

	private final ErrorStatus noSelectionError = this.createErrorStatus( this.createKey( "noSelectionError" ) );

	private boolean isInTheMidstOfLowestCommonAncestorSetting;
	private final org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode> typeListener = new org.lgna.croquet.event.ValueListener<org.alice.stageide.type.croquet.TypeNode>() {
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.stageide.type.croquet.TypeNode> e ) {
			org.alice.stageide.type.croquet.TypeNode nextValue = e.getNextValue();
			handleTypeChange( nextValue != null ? nextValue.getType() : null );
		}
	};

	private final org.lgna.croquet.event.ValueListener<java.util.List<org.lgna.project.ast.UserField>> sceneFieldListener = new org.lgna.croquet.event.ValueListener<java.util.List<org.lgna.project.ast.UserField>>() {
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

	@Override
	protected Integer getWiderGoldenRatioSizeFromHeight() {
		return 600;
	}

	public org.lgna.croquet.TabSelectionState getTabState() {
		return this.tabState;
	}

	public org.lgna.croquet.MultipleSelectionState<org.lgna.project.ast.UserField> getSceneFieldsState() {
		return this.sceneFieldsState;
	}

	public org.lgna.croquet.TreeSelectionState<TypeNode> getTypeTreeState() {
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
			return IS_GOOD_TO_GO_STATUS;
		} else {
			return this.noSelectionError;
		}
	}

	private static TypeNode build( org.lgna.project.ast.AbstractType<?, ?, ?> type, java.util.Map<org.lgna.project.ast.AbstractType<?, ?, ?>, TypeNode> map ) {
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
		org.lgna.project.ast.JavaType rootType = org.lgna.project.ast.JavaType.getInstance( org.lgna.story.SThing.class );
		final boolean IS_SCENE_TYPE_DESIRED = true;
		org.lgna.project.ast.JavaType filterType = IS_SCENE_TYPE_DESIRED ? rootType : org.lgna.project.ast.JavaType.getInstance( org.lgna.story.STurnable.class );
		map.clear();
		TypeNode rootNode = new TypeNode( rootType );
		map.put( rootType, rootNode );
		for( org.lgna.project.ast.NamedUserType type : types ) {
			if( filterType.isAssignableFrom( type ) ) {
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
		super.handlePreActivation();
	}

	@Override
	public void handlePostDeactivation() {
		this.sceneFieldsState.removeNewSchoolValueListener( this.sceneFieldListener );
		this.typeTreeState.removeNewSchoolValueListener( this.typeListener );
		super.handlePostDeactivation();
	}

	@Override
	protected org.lgna.croquet.components.Panel createView() {
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

		java.util.ArrayList<? extends org.lgna.project.ast.AbstractMethod> methods = type.getDeclaredMethods();

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

		if( this.isInTheMidstOfLowestCommonAncestorSetting ) {
			this.getView().repaint();
		} else {
			java.util.List<org.lgna.project.ast.UserField> fields = edu.cmu.cs.dennisc.java.util.Collections.newLinkedList();
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

	public static void main( String[] args ) throws Exception {
		javax.swing.UIManager.LookAndFeelInfo lookAndFeelInfo = edu.cmu.cs.dennisc.javax.swing.plaf.PlafUtilities.getInstalledLookAndFeelInfoNamed( "Nimbus" );
		if( lookAndFeelInfo != null ) {
			javax.swing.UIManager.setLookAndFeel( lookAndFeelInfo.getClassName() );
		}

		new org.lgna.croquet.simple.SimpleApplication();

		org.lgna.project.Project project = org.lgna.project.io.IoUtilities.readProject( args[ 0 ] );
		org.alice.ide.ProjectStack.pushProject( project );
		org.lgna.croquet.triggers.Trigger trigger = null;
		try {
			OtherTypeDialog.getInstance().getValueCreator().fire( trigger );
		} finally {
			System.exit( 0 );
		}
	}

}
