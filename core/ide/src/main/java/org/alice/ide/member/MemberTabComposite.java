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
package org.alice.ide.member;

/**
 * @author Dennis Cosgrove
 */
public abstract class MemberTabComposite<V extends org.alice.ide.member.views.MemberTabView> extends MemberOrControlFlowTabComposite<V> {
	public static boolean ARE_TOOL_PALETTES_INERT = true;

	public static boolean getExpandedAccountingForInert( boolean isExpanded ) {
		if( ARE_TOOL_PALETTES_INERT ) {
			return true;
		} else {
			return isExpanded;
		}
	}

	protected static final String GROUP_BY_CATEGORY = "group by category";
	protected static final String SORT_ALPHABETICALLY = "sort alphabetically";

	public static org.alice.ide.member.MethodsSubComposite SEPARATOR = null;

	protected static boolean isInclusionDesired( org.lgna.project.ast.AbstractMember member ) {
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

	private class InstanceFactoryListener implements org.lgna.croquet.event.ValueListener<org.alice.ide.instancefactory.InstanceFactory> {
		private boolean isActive;

		public boolean isActive() {
			return this.isActive;
		}

		public void setActive( boolean isActive ) {
			this.isActive = isActive;
		}

		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.instancefactory.InstanceFactory> e ) {
			if( e.isAdjusting() ) {
				//pass
			} else {
				if( this.isActive ) {
					MemberTabComposite.this.refreshContentsLater();
				}
				MemberTabComposite.this.repaintTitles();
			}
		}
	}

	private final InstanceFactoryListener instanceFactoryListener = new InstanceFactoryListener();

	private final org.lgna.croquet.event.ValueListener<String> sortListener = new org.lgna.croquet.event.ValueListener<String>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<String> e ) {
			MemberTabComposite.this.getView().refreshLater();
		}
	};

	private final org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> declarationCompositeListener = new org.lgna.croquet.event.ValueListener<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>>() {
		@Override
		public void valueChanged( org.lgna.croquet.event.ValueEvent<org.alice.ide.declarationseditor.DeclarationComposite<?, ?>> e ) {
			MemberTabComposite.this.refreshContentsLater();
		}
	};

	private final java.util.List<javax.swing.JComponent> jTitlesInNeedOfRepaintWhenInstanceFactoryChanges = edu.cmu.cs.dennisc.java.util.Lists.newCopyOnWriteArrayList();
	private final AddMethodMenuModel addMethodMenuModel;

	public MemberTabComposite( java.util.UUID migrationId, AddMethodMenuModel addMethodMenuModel ) {
		super( migrationId );
		this.addMethodMenuModel = addMethodMenuModel;
	}

	public AddMethodMenuModel getAddMethodMenuModel() {
		return this.addMethodMenuModel;
	}

	@Override
	protected void initialize() {
		super.initialize();
		org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().addNewSchoolValueListener( this.instanceFactoryListener );
	}

	public abstract org.lgna.croquet.ImmutableDataSingleSelectListState<String> getSortState();

	@Override
	protected final org.lgna.croquet.views.ScrollPane createScrollPaneIfDesired() {
		return null;
	}

	private void repaintTitles() {
		try {
			for( javax.swing.JComponent jComponent : this.jTitlesInNeedOfRepaintWhenInstanceFactoryChanges ) {
				jComponent.repaint();
			}
		} catch( Throwable t ) {
			// deemed not worth an exception
			t.printStackTrace();
		}
	}

	private void refreshContentsLater() {
		for( MethodsSubComposite subComposite : this.getSubComposites() ) {
			if( subComposite != null ) {
				subComposite.getView().refreshLater();
			}
		}
		this.getView().refreshLater();
	}

	protected abstract boolean isAcceptable( org.lgna.project.ast.AbstractMethod method );

	protected abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getPotentialCategorySubComposites();

	protected abstract java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> getPotentialCategoryOrAlphabeticalSubComposites();

	protected abstract UserMethodsSubComposite getUserMethodsSubComposite( org.lgna.project.ast.NamedUserType type );

	protected abstract UnclaimedJavaMethodsComposite getUnclaimedJavaMethodsComposite();

	public java.util.List<org.alice.ide.member.MethodsSubComposite> getSubComposites() {
		java.util.List<org.alice.ide.member.MethodsSubComposite> rv = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		java.util.List<org.lgna.project.ast.JavaMethod> javaMethods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		org.alice.ide.instancefactory.InstanceFactory instanceFactory = org.alice.ide.instancefactory.croquet.InstanceFactoryState.getInstance().getValue();
		if( instanceFactory != null ) {
			org.lgna.project.ast.AbstractType<?, ?, ?> type = instanceFactory.getValueType();
			while( type != null ) {
				if( type instanceof org.lgna.project.ast.NamedUserType ) {
					org.lgna.project.ast.NamedUserType namedUserType = (org.lgna.project.ast.NamedUserType)type;
					UserMethodsSubComposite userMethodsSubComposite = this.getUserMethodsSubComposite( namedUserType );
					rv.add( userMethodsSubComposite );
				} else if( type instanceof org.lgna.project.ast.JavaType ) {
					org.lgna.project.ast.JavaType javaType = (org.lgna.project.ast.JavaType)type;
					for( org.lgna.project.ast.JavaMethod javaMethod : javaType.getDeclaredMethods() ) {
						if( this.isAcceptable( javaMethod ) ) {
							if( isInclusionDesired( javaMethod ) ) {
								javaMethods.add( javaMethod );
							}
						}
					}
				}
				if( type.isFollowToSuperClassDesired() ) {
					type = type.getSuperType();
				} else {
					break;
				}
			}
		}

		if( rv.size() > 0 ) {
			rv.add( SEPARATOR );
		}

		String sortValue = this.getSortState().getValue();
		if( SORT_ALPHABETICALLY.equals( sortValue ) ) {
			//todo
		} else {
			java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> potentialSubComposites = this.getPotentialCategorySubComposites();
			for( FilteredJavaMethodsSubComposite potentialSubComposite : potentialSubComposites ) {
				java.util.List<org.lgna.project.ast.JavaMethod> acceptedMethods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
				java.util.ListIterator<org.lgna.project.ast.JavaMethod> methodIterator = javaMethods.listIterator();
				while( methodIterator.hasNext() ) {
					org.lgna.project.ast.JavaMethod method = methodIterator.next();
					if( potentialSubComposite.isAcceptingOf( method ) ) {
						acceptedMethods.add( method );
						methodIterator.remove();
					}
				}

				if( acceptedMethods.size() > 0 ) {
					potentialSubComposite.sortAndSetMethods( acceptedMethods );
					rv.add( potentialSubComposite );
				}
			}
		}

		java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> postSubComposites = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();

		java.util.List<org.alice.ide.member.FilteredJavaMethodsSubComposite> potentialSubComposites = this.getPotentialCategoryOrAlphabeticalSubComposites();
		for( FilteredJavaMethodsSubComposite potentialSubComposite : potentialSubComposites ) {
			java.util.List<org.lgna.project.ast.JavaMethod> acceptedMethods = edu.cmu.cs.dennisc.java.util.Lists.newLinkedList();
			java.util.ListIterator<org.lgna.project.ast.JavaMethod> methodIterator = javaMethods.listIterator();
			while( methodIterator.hasNext() ) {
				org.lgna.project.ast.JavaMethod method = methodIterator.next();
				if( potentialSubComposite.isAcceptingOf( method ) ) {
					acceptedMethods.add( method );
					methodIterator.remove();
				}
			}

			if( acceptedMethods.size() > 0 ) {
				potentialSubComposite.sortAndSetMethods( acceptedMethods );
				postSubComposites.add( potentialSubComposite );
			}
		}

		if( javaMethods.size() > 0 ) {
			UnclaimedJavaMethodsComposite unclaimedJavaMethodsComposite = this.getUnclaimedJavaMethodsComposite();
			unclaimedJavaMethodsComposite.sortAndSetMethods( javaMethods );
			rv.add( unclaimedJavaMethodsComposite );
		}

		rv.addAll( postSubComposites );

		return rv;
	}

	@Override
	public void handlePreActivation() {
		super.handlePreActivation();
		this.instanceFactoryListener.setActive( true );
		this.getSortState().addNewSchoolValueListener( this.sortListener );
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().addNewSchoolValueListener( this.declarationCompositeListener );
		this.refreshContentsLater();
		this.repaintTitles();
	}

	@Override
	public void handlePostDeactivation() {
		org.alice.ide.IDE.getActiveInstance().getDocumentFrame().getDeclarationsEditorComposite().getTabState().removeNewSchoolValueListener( this.declarationCompositeListener );
		this.getSortState().removeNewSchoolValueListener( this.sortListener );
		this.instanceFactoryListener.setActive( false );
		super.handlePostDeactivation();
	}

	@Override
	public void customizeTitleComponentAppearance( org.lgna.croquet.views.BooleanStateButton<?> button ) {
		super.customizeTitleComponentAppearance( button );
		final boolean IS_ICON_DESIRED = org.alice.ide.croquet.models.ui.preferences.IsAlwaysShowingBlocksState.getInstance().getValue() == false;
		if( IS_ICON_DESIRED ) {
			button.getModel().setIconForBothTrueAndFalse( org.alice.ide.instancefactory.croquet.views.icons.IndirectCurrentAccessibleTypeIcon.SINGLTON );
			button.setHorizontalTextPosition( org.lgna.croquet.views.HorizontalTextPosition.TRAILING );
			this.jTitlesInNeedOfRepaintWhenInstanceFactoryChanges.add( button.getAwtComponent() );
		}
	}
}
