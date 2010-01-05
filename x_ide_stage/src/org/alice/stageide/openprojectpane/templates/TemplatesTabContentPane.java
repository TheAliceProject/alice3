package org.alice.stageide.openprojectpane.templates;

public class TemplatesTabContentPane extends org.alice.ide.openprojectpane.ListPane {
	private static java.net.URI[] uris;
	static {
		java.util.List< java.net.URI > list = new java.util.LinkedList< java.net.URI >();
		String[] resourceNames = { "DirtProject.a3p", "GrassyProject.a3p", "MoonProject.a3p", "SandyProject.a3p", "SeaProject.a3p", "SnowyProject.a3p" };
		for( String resourceName : resourceNames ) {
			java.net.URI uri = getURI( resourceName );
			if( uri != null ) {
				list.add( uri );
			}
		}
		TemplatesTabContentPane.uris = new java.net.URI[ list.size() ];
		list.toArray( TemplatesTabContentPane.uris );
	}

	private static java.net.URI getURI( String resourceName ) {
		java.io.File applicationRootDirectory = org.alice.ide.IDE.getSingleton().getApplicationRootDirectory();
		//java.io.File applicationRootDirectory = new java.io.File( "c:/Program Files/Alice3Beta" );
		java.io.File file = new java.io.File( applicationRootDirectory, "projects/templates/" + resourceName );
		if( file != null ) {
			if( file.exists() ) {
				return file.toURI();
			} else {
				return null;
			}
		} else {
			return null;
		}
//		java.net.URL url = TemplatesTabContentPane.class.getResource( resourceName );
//		if( url != null ) {
//			try {
//				return url.toURI();
//			} catch( java.net.URISyntaxException urise ) {
//				return null;
//			}
//		} else {
//			return null;
//		}
	}
	@Override
	public String getTabTitleText() {
		return "Templates";
	}
	@Override
	protected String getTextForZeroProjects() {
		return "there are no template projects.";
	}
	@Override
	protected java.net.URI[] getURIs() {
		return TemplatesTabContentPane.uris;
	}
}
