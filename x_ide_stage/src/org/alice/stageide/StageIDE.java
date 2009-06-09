package org.alice.stageide;

public class StageIDE extends org.alice.ide.IDE {
	public StageIDE() {
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resize", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeWidth", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeHeight", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "resizeDepth", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setWidth", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setHeight", org.alice.apis.moveandturn.Transformable.class );
		edu.cmu.cs.dennisc.alice.ast.Decoder.addMethodFilter( org.alice.apis.moveandturn.Model.class, "setDepth", org.alice.apis.moveandturn.Transformable.class );
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.alice.apis.moveandturn.Transformable.class );
		this.addWindowStateListener( new java.awt.event.WindowStateListener() {
			public void windowStateChanged( java.awt.event.WindowEvent e ) {
				int oldState = e.getOldState();
				int newState = e.getNewState();
				//edu.cmu.cs.dennisc.print.PrintUtilities.println( "windowStateChanged", oldState, newState, java.awt.Frame.ICONIFIED );
				if( ( oldState & java.awt.Frame.ICONIFIED ) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().incrementAutomaticDisplayCount();
				}
				if( ( newState & java.awt.Frame.ICONIFIED ) == java.awt.Frame.ICONIFIED ) {
					edu.cmu.cs.dennisc.lookingglass.opengl.LookingGlassFactory.getSingleton().decrementAutomaticDisplayCount();
				}
			}
		} );
	}
	@Override
	protected void promptForLicenseAgreements() {
		final String IS_LICENSE_ACCEPTED_PREFERENCE_KEY = "isLicenseAccepted";
		try {
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.alice.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 1 of 3): Alice 3", edu.cmu.cs.dennisc.alice.License.TEXT, "Alice" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.wustl.cse.lookingglass.apis.walkandtouch.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 2 of 3): Looking Glass Walk & Touch API",
					edu.wustl.cse.lookingglass.apis.walkandtouch.License.TEXT_FOR_USE_IN_ALICE, "the Looking Glass Walk & Touch API" );
			edu.cmu.cs.dennisc.eula.EULAUtilities.promptUserToAcceptEULAIfNecessary( edu.cmu.cs.dennisc.nebulous.License.class, IS_LICENSE_ACCEPTED_PREFERENCE_KEY, "License Agreement (Part 3 of 3): The Sims (TM) 2 Art Assets", edu.cmu.cs.dennisc.nebulous.License.TEXT, "The Sims (TM) 2 Art Assets" );
		} catch( edu.cmu.cs.dennisc.eula.LicenseRejectedException lre ) {
			javax.swing.JOptionPane.showMessageDialog( this, "You must accept the license agreements in order to use Alice 3, the Looking Glass Walk & Touch API, and The Sims (TM) 2 Art Assets.  Exiting." );
			System.exit( -1 );
		}
	}
	
	private static final edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava COLOR_TYPE = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Color.class );

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractField, ColorIcon > mapFieldToIcon = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractField, ColorIcon >();
	class ColorIcon implements javax.swing.Icon {
		private static final int SIZE = 15;
		private java.awt.Color fillColor;
		private java.awt.Color outlineColor;
		public ColorIcon( java.awt.Color color ) {
			this.fillColor = color;
			float[] hsb = new float[ 3 ];
			//todo
			java.awt.Color.RGBtoHSB( this.fillColor.getRed(), this.fillColor.getGreen(), this.fillColor.getBlue(), hsb );
			if( hsb[ 2 ] > 0.9f ) {
				this.outlineColor = java.awt.Color.GRAY;
			} else {
				this.outlineColor = null;
			}
		}
		public int getIconWidth() {
			return SIZE+3 + 2;
		}
		public int getIconHeight() {
			return SIZE+3;
		}
		public void paintIcon( java.awt.Component arg0, java.awt.Graphics g, int x, int y ) {
			g.setColor( this.fillColor );
			g.fillRect( x+1 + 2, y+1, SIZE, SIZE );
			if( this.outlineColor != null ) {
				g.setColor( this.outlineColor );
				g.drawRect( x+1 + 2, y+1, SIZE, SIZE );
			}
		}
	}
	private javax.swing.Icon getIconFor( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		if( field.getDeclaringType() == COLOR_TYPE && field.getValueType() == COLOR_TYPE ) {
			ColorIcon rv = this.mapFieldToIcon.get( field );
			if( rv != null ) {
				//pass
			} else {
				try {
					edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField fieldInJava = (edu.cmu.cs.dennisc.alice.ast.FieldDeclaredInJavaWithField)field;
					org.alice.apis.moveandturn.Color color = (org.alice.apis.moveandturn.Color)edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.get( fieldInJava.getFld(), null );
					rv = new ColorIcon( color.getInternal().getAsAWTColor() );
					this.mapFieldToIcon.put( field, rv );
				} catch( RuntimeException re ) {
					//pass
				}
			}
			return rv;
		}
		return null;
	}
	
	@Override
	protected void addCustomFillIns( cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		super.addCustomFillIns( blank, type );
		edu.cmu.cs.dennisc.alice.ast.Expression previousExpression = this.getPreviousExpression();
		if( previousExpression != null ) {
			if( type.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
				edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice enclosingMethod = previousExpression.getFirstAncestorAssignableTo( edu.cmu.cs.dennisc.alice.ast.MethodDeclaredInAlice.class );
				if( enclosingMethod != null ) {
					for( edu.cmu.cs.dennisc.alice.ast.ParameterDeclaredInAlice parameter : enclosingMethod.parameters ) {
						edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeMouseButtonEvent = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.event.MouseButtonEvent.class );
						if( parameter.getValueType() == typeMouseButtonEvent ) {
							String[] methodNames = new String[] { "getModelAtMouseLocation", "getPartAtMouseLocation" };
							for( String methodName : methodNames ) {
								edu.cmu.cs.dennisc.alice.ast.AbstractMethod method = typeMouseButtonEvent.getDeclaredMethod( methodName );
								edu.cmu.cs.dennisc.alice.ast.Expression expression = new edu.cmu.cs.dennisc.alice.ast.ParameterAccess( parameter );
								edu.cmu.cs.dennisc.alice.ast.MethodInvocation methodInvocation = new edu.cmu.cs.dennisc.alice.ast.MethodInvocation( expression, method );
								blank.addFillIn( new org.alice.ide.cascade.SimpleExpressionFillIn( methodInvocation ) );
							}
						}
					}
				}
			}
		}
	}
	@Override
	public java.awt.Component getPrefixPaneForFieldAccessIfAppropriate( edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess ) {
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
		javax.swing.Icon icon = getIconFor( field );
		if( icon != null ) {
			return zoot.ZLabel.acquire( icon );
		}
		return super.getPrefixPaneForFieldAccessIfAppropriate( fieldAccess );
	}
	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava REVOLUTIONS_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( org.alice.apis.moveandturn.AngleInRevolutions.class, Number.class ) );
	private static final edu.cmu.cs.dennisc.alice.ast.ConstructorDeclaredInJava PORTION_CONSTRUCTOR = edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.getConstructor( edu.cmu.cs.dennisc.lang.reflect.ReflectionUtilities.getConstructor( org.alice.apis.moveandturn.Portion.class, Number.class ) );
	protected org.alice.ide.common.DeclarationNameLabel createDeclarationNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
		//todo: better name
		class ThisFieldAccessNameLabel extends org.alice.ide.common.DeclarationNameLabel {
			public ThisFieldAccessNameLabel( edu.cmu.cs.dennisc.alice.ast.AbstractField field ) {
				super( field );
			}
			@Override
			protected String getNameText() {
				if( StageIDE.this.isOmittingThisFieldAccesses() ) {
					return super.getNameText();
				} else {
					return "this." + super.getNameText();
				}
			}
		}
		return new ThisFieldAccessNameLabel( field );
	}
	@Override
	public java.awt.Component getOverrideComponent( org.alice.ide.common.Factory factory, edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			edu.cmu.cs.dennisc.alice.ast.Expression fieldExpression =  fieldAccess.expression.getValue();
			if( fieldExpression instanceof edu.cmu.cs.dennisc.alice.ast.ThisExpression ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				if( field.getDeclaringType().isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
						return new org.alice.ide.common.ExpressionPane( expression, this.createDeclarationNameLabel( field ) ) {
							@Override
							protected boolean isExpressionTypeFeedbackDesired() {
								return true;
							}
						};
					}
				}
			}
		} else {
			if( this.isJava() ) {
				//pass
			} else {
				if( expression instanceof edu.cmu.cs.dennisc.alice.ast.InstanceCreation ) {
					edu.cmu.cs.dennisc.alice.ast.InstanceCreation instanceCreation = (edu.cmu.cs.dennisc.alice.ast.InstanceCreation)expression;
					edu.cmu.cs.dennisc.alice.ast.AbstractConstructor constructor = instanceCreation.constructor.getValue();
					if( constructor == REVOLUTIONS_CONSTRUCTOR ) {
						return new swing.LineAxisPane( 
								factory.createExpressionPane( instanceCreation.arguments.get( 0 ).expression.getValue() ),
								zoot.ZLabel.acquire( " revolutions" )
						);
					} else if( constructor == PORTION_CONSTRUCTOR ) {
						return factory.createExpressionPane( instanceCreation.arguments.get( 0 ).expression.getValue() );
					}
				}
			}
		}
		return super.getOverrideComponent( factory, expression );
	}
	@Override
	public boolean isDropDownDesiredFor( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
			if( parent instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)parent;
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				assert field != null;
				if( field.getDeclaringType().isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
						return false;
					}
				}
			}
			return true;
		} else {
			return false;
		}
	}
	protected MoveAndTurnRuntimeProgram createRuntimeProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType, edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm ) {
		return new MoveAndTurnRuntimeProgram( sceneType, vm );
	}
	private int xLocation = 100;
	private int yLocation = 100;
	
	protected void showInJDialog( MoveAndTurnRuntimeProgram rtProgram ) {
		this.getSceneEditor().setRenderingEnabled( false );
		//this.setRenderingEnabled( false );
		try {
			rtProgram.showInJDialog( this, true, new String[]{ "X_LOCATION="+xLocation, "Y_LOCATION="+yLocation } );
		} finally {
			try {
				this.xLocation = Integer.parseInt( rtProgram.getParameter( "X_LOCATION" ) );
			} catch( Throwable t ) {
				this.xLocation = 0;
			}
			try {
				this.yLocation = Integer.parseInt( rtProgram.getParameter( "Y_LOCATION" ) );
			} catch( Throwable t ) {
				this.yLocation = 0;
			}
			this.getSceneEditor().setRenderingEnabled( true );
			//this.setRenderingEnabled( true );
		}
	}
	
	@Override
	public void handleRun( zoot.ActionContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = this.createVirtualMachineForRuntimeProgram();
		vm.registerAnonymousAdapter( org.alice.apis.moveandturn.event.MouseButtonListener.class, org.alice.stageide.apis.moveandturn.event.MouseButtonAdapter.class );
		vm.setEntryPointType( this.getProgramType() );
		MoveAndTurnRuntimeProgram rtProgram = this.createRuntimeProgram( sceneType, vm );
		showInJDialog( rtProgram );
	}

	@Override
	public void handlePreviewMethod( zoot.ActionContext context, edu.cmu.cs.dennisc.alice.ast.MethodInvocation emptyExpressionMethodInvocation ) {
		this.generateCodeForSceneSetUp();
		edu.cmu.cs.dennisc.alice.ast.AbstractField field = this.getFieldSelection();
		if( field == this.getSceneField() ) {
			field = null;
		}
		TestMethodProgram testProgram = new TestMethodProgram( this.getSceneType(), field, emptyExpressionMethodInvocation );
		this.getSceneEditor().setRenderingEnabled( false );
		//this.setRenderingEnabled( false );
		try {
			testProgram.showInJDialog( this, true, new String[]{ "X_LOCATION="+xLocation, "Y_LOCATION="+yLocation } );
		} finally {
			try {
				this.xLocation = Integer.parseInt( testProgram.getParameter( "X_LOCATION" ) );
			} catch( Throwable t ) {
				this.xLocation = 0;
			}
			try {
				this.yLocation = Integer.parseInt( testProgram.getParameter( "Y_LOCATION" ) );
			} catch( Throwable t ) {
				this.yLocation = 0;
			}
			this.getSceneEditor().setRenderingEnabled( true );
			//this.setRenderingEnabled( true );
		}
	}
	@Override
	protected org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor() {
		return new org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor();
	}
	@Override
	protected zoot.ActionOperation createAboutOperation() {
		return new org.alice.stageide.operations.help.AboutOperation();
	}

	protected java.util.Map< String, String > createGalleryThumbnailsMap() {
		java.util.Map< String, String > rv = new java.util.HashMap< String, String >();
		rv.put( "thumbnails", "gallery" );
		rv.put( "org.alice.apis.moveandturn.gallery", "Generic Alice Models" );
		rv.put( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters", "Looking Glass Characters" );
		rv.put( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes", "Looking Glass Scenery" );
		return rv;
	}

	private java.util.Map< edu.cmu.cs.dennisc.alice.ast.AbstractType, String > mapTypeToText;

	@Override
	public String getTextFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
		if( mapTypeToText != null ) {
			//pass
		} else {
			mapTypeToText = new java.util.HashMap< edu.cmu.cs.dennisc.alice.ast.AbstractType, String >();
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class ), "(a.k.a. Generic Alice Model)" );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class ), "(a.k.a. Looking Glass Scenery)" );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Character.class ), "(a.k.a. Looking Glass Character)" );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Person.class ), "(a.k.a. Looking Glass Person)" );
			mapTypeToText.put( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ), "(a.k.a. Stage Adult)" );
		}
		return mapTypeToText.get( type );
	}

	@Override
	protected java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > addJavaTypes( java.util.Vector< edu.cmu.cs.dennisc.alice.ast.AbstractType > rv ) {
		rv = super.addJavaTypes( rv );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Model.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.PolygonalModel.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Character.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( edu.wustl.cse.lookingglass.apis.walkandtouch.Person.class ) );
		rv.add( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ) );
		return rv;
	}

	@Override
	protected void addFillInAndPossiblyPartFills( cascade.Blank blank, edu.cmu.cs.dennisc.alice.ast.Expression expression, edu.cmu.cs.dennisc.alice.ast.AbstractType type, edu.cmu.cs.dennisc.alice.ast.AbstractType type2 ) {
		super.addFillInAndPossiblyPartFills( blank, expression, type, type2 );
		if( type.isAssignableTo( org.alice.apis.moveandturn.PolygonalModel.class ) ) {
			edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = null;
			Class<?> paramCls = null;
			if( type2.isAssignableFrom( org.alice.apis.moveandturn.Model.class ) ) {
				typeInJava = type.getFirstTypeEncounteredDeclaredInJava();
				Class<?> cls = typeInJava.getCls();
				for( Class innerCls : cls.getDeclaredClasses() ) {
					if( innerCls.getSimpleName().equals( "Part" ) ) {
						paramCls = innerCls;
					}
				}
			}
			if( paramCls !=null ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractMethod getPartMethod = typeInJava.getDeclaredMethod( "getPart", paramCls );
				if( getPartMethod != null ) {
					blank.addFillIn( new org.alice.ide.cascade.MethodInvocationFillIn( expression, getPartMethod ) );
				}
			}
		}
	}
	@Override
	public java.io.File getGalleryRootDirectory() {
		return org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory();
	}
	@Override
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser( java.io.File galleryRoot ) {
		java.io.File thumbnailRoot = new java.io.File( galleryRoot, "thumbnails" );
		java.util.Map< String, String > map = this.createGalleryThumbnailsMap();
		return new org.alice.stageide.gallerybrowser.GalleryBrowser( thumbnailRoot, map );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType interfaceType ) {
		if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Style.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.TraditionalStyle.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
		} else if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
		} else {
			return super.getEnumTypeForInterfaceType( interfaceType );
		}
	}
	
//	@Override
//	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getTypeFor( edu.cmu.cs.dennisc.alice.ast.AbstractType type ) {
//		if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.EyeColor.class ) ) {
//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseEyeColor.class );
//		} else if( type == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.SkinTone.class ) ) {
//			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.BaseSkinTone.class );
//		} else {
//			return super.getTypeFor( type );
//		}
//	}

	@Override
	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		super.addExpressionFillerInners( rv );
		rv.add( new org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( org.alice.apis.moveandturn.Color.class ) );
		rv.add( new org.alice.stageide.cascade.fillerinners.AngleFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.PortionFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.MouseButtonListenerFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.OutfitFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.HairFillerInner() );
		return rv;
	}
	
	@Override
	public void declareFieldOfPredeterminedType( edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice ownerType, edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInAlice valueType, zoot.ActionContext actionContext ) {
		edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava typeInJava = valueType.getFirstTypeEncounteredDeclaredInJava();
		if( typeInJava == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.stage.Adult.class ) ) {
			javax.swing.JOptionPane.showMessageDialog( this, "todo" );
		} else {
			super.declareFieldOfPredeterminedType( ownerType, valueType, actionContext );
		}
	}
}
