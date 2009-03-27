package org.alice.stageide;

public class StageIDE extends org.alice.ide.IDE {
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
	protected org.alice.ide.sceneeditor.AbstractSceneEditor createSceneEditor() {
		return new org.alice.stageide.sceneeditor.MoveAndTurnSceneEditor();
	}
	@Override
	protected zoot.ActionOperation createAboutOperation() {
		return new org.alice.stageide.operations.help.AboutOperation();
	}
	@Override
	protected org.alice.ide.gallerybrowser.AbstractGalleryBrowser createGalleryBrowser() {
		java.io.File thumbnailRoot = new java.io.File( org.alice.apis.moveandturn.gallery.GalleryModel.getGalleryRootDirectory(), "thumbnails" );
		return new org.alice.stageide.gallerybrowser.GalleryBrowser( thumbnailRoot );
	}
	@Override
	protected java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > addExpressionFillerInners( java.util.List< org.alice.ide.cascade.fillerinners.ExpressionFillerInner > rv ) {
		super.addExpressionFillerInners( rv );
		rv.add( new org.alice.ide.cascade.fillerinners.ConstantsOwningFillerInner( org.alice.apis.moveandturn.Color.class ) );
		rv.add( new org.alice.stageide.cascade.fillerinners.AngleFillerInner() );
		//rv.add( new org.alice.stageide.cascade.fillerinners.PortionFillerInner() );
		return rv;
	}
	public static void main( String[] args ) {
		edu.cmu.cs.dennisc.alice.reflect.ClassInfoManager.setDirectory( new java.io.File( "/program files/alice/3.beta.0027/application/classinfos" ) );
		StageIDE ide = new StageIDE();
		ide.loadProjectFrom( new java.io.File( edu.cmu.cs.dennisc.alice.io.FileUtilities.getMyProjectsDirectory(), "a.a3p" ) );
		ide.setSize( 1280, 1024 );
		ide.setVisible( true );
	}

}
