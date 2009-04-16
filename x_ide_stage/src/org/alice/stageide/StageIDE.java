package org.alice.stageide;

class MoveAndTurnRuntimeProgram extends org.alice.apis.moveandturn.Program {
	private static java.awt.Dimension preferredSize = new java.awt.Dimension( 400, 300 );
	private edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType;
	private edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice sceneInstance;
	private edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm;
	public MoveAndTurnRuntimeProgram( edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType, edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm ) {
		this.sceneType = sceneType;
		this.vm = vm;
		this.addComponentListener( new java.awt.event.ComponentListener() {
			public void componentShown( java.awt.event.ComponentEvent e ) {
			}
			public void componentHidden( java.awt.event.ComponentEvent e ) {
			}
			public void componentMoved( java.awt.event.ComponentEvent e ) {
			}
			public void componentResized( java.awt.event.ComponentEvent e ) {
				e.getComponent().getSize( MoveAndTurnRuntimeProgram.preferredSize );
			}
		} );
	}
	@Override
	protected void initialize() {
		this.sceneInstance = (edu.cmu.cs.dennisc.alice.virtualmachine.InstanceInAlice)this.vm.createInstanceEntryPoint( this.sceneType );
		org.alice.apis.moveandturn.Scene scene = (org.alice.apis.moveandturn.Scene)this.sceneInstance.getInstanceInJava();
		this.setScene( scene );
	}
	@Override
	protected void run() {
		this.vm.invokeEntryPoint( this.sceneType.getDeclaredMethod( "run" ), this.sceneInstance );
	}
	
	@Override
	public java.awt.Dimension getPreferredSize() {
		return MoveAndTurnRuntimeProgram.preferredSize;
	}
	
}

public class StageIDE extends org.alice.ide.IDE {
	public StageIDE() {
		org.alice.ide.common.BeveledShapeForType.addRoundType( org.alice.apis.moveandturn.Transformable.class );		
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

	@Override
	public java.awt.Component getOverrideComponent( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( expression instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
			edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)expression;
			if( fieldAccess.expression.getValue() instanceof edu.cmu.cs.dennisc.alice.ast.ThisExpression ) {
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
				if( field.getDeclaringType().isAssignableTo( org.alice.apis.moveandturn.Scene.class ) ) {
					if( field.getValueType().isAssignableTo( org.alice.apis.moveandturn.Transformable.class ) ) {
						return new org.alice.ide.common.ExpressionPane( expression, new zoot.ZLabel( "this." + field.getName() ) );
					}
				}
			}
		}
		return super.getOverrideComponent( expression );
	}
	@Override
	public boolean isDropDownDesiredFor( edu.cmu.cs.dennisc.alice.ast.Expression expression ) {
		if( super.isDropDownDesiredFor( expression ) ) {
			edu.cmu.cs.dennisc.alice.ast.Node parent = expression.getParent();
			if( parent instanceof edu.cmu.cs.dennisc.alice.ast.FieldAccess ) {
				edu.cmu.cs.dennisc.alice.ast.FieldAccess fieldAccess = (edu.cmu.cs.dennisc.alice.ast.FieldAccess)parent;
				edu.cmu.cs.dennisc.alice.ast.AbstractField field = fieldAccess.field.getValue();
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
	@Override
	public void handleRun( zoot.ActionContext context, edu.cmu.cs.dennisc.alice.ast.AbstractType sceneType ) {
		edu.cmu.cs.dennisc.alice.virtualmachine.VirtualMachine vm = this.createVirtualMachineForRuntimeProgram();
		vm.setEntryPointType( this.getProgramType() );
		MoveAndTurnRuntimeProgram rtProgram = new MoveAndTurnRuntimeProgram( sceneType, vm );
		rtProgram.showInJDialog( this, true, new String[]{} );
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
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser() {
		java.io.File thumbnailRoot = new java.io.File( org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails" );
		java.util.Map< String, String > map = this.createGalleryThumbnailsMap();
		return new org.alice.stageide.gallerybrowser.GalleryBrowser( thumbnailRoot, map );
	}
	@Override
	protected edu.cmu.cs.dennisc.alice.ast.AbstractType getEnumTypeForInterfaceType( edu.cmu.cs.dennisc.alice.ast.AbstractType interfaceType ) {
		edu.cmu.cs.dennisc.print.PrintUtilities.println( interfaceType );
		if( interfaceType == edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.Style.class ) ) {
			return edu.cmu.cs.dennisc.alice.ast.TypeDeclaredInJava.get( org.alice.apis.moveandturn.TraditionalStyle.class );
		} else {
			return super.getEnumTypeForInterfaceType( interfaceType );
		}
	}

	@Override
	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		super.addExpressionFillerInners( rv );
		rv.add( new org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( org.alice.apis.moveandturn.Color.class ) );
		rv.add( new org.alice.stageide.cascade.fillerinners.AngleFillerInner() );
		rv.add( new org.alice.stageide.cascade.fillerinners.PortionFillerInner() );
		return rv;
	}
	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.setDirectory( new java.io.File( "/program files/alice/3.beta.0026/application/classinfos" ) );
		StageIDE ide = new StageIDE();
		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
		ide.setSize( 1280, 800 );
		ide.setVisible( true );
	}

}
