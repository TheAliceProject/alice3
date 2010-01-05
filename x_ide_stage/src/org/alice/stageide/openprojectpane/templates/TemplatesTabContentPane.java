package org.alice.stageide.openprojectpane.templates;

public class TemplatesTabContentPane extends org.alice.ide.openprojectpane.ListPane {
	private static java.net.URI[] uris;
	static {
		uris = new java.net.URI[ 6 ];
		uris[ 0 ] = getURI( "DirtProject.a3p" );
		uris[ 1 ] = getURI( "GrassyProject.a3p" );
		uris[ 2 ] = getURI( "MoonProject.a3p" );
		uris[ 3 ] = getURI( "SandyProject.a3p" );
		uris[ 4 ] = getURI( "SeaProject.a3p" );
		uris[ 5 ] = getURI( "SnowyProject.a3p" );
	}

	private static java.net.URI getURI( String resource ) {
		java.io.File applicationRootDirectory = org.alice.ide.IDE.getSingleton().getApplicationRootDirectory();
		//java.io.File applicationRootDirectory = new java.io.File( "c:/Program Files/Alice3Beta" );
		java.io.File file = new java.io.File( applicationRootDirectory, "projects/templates/" + resource );
		return file.toURI();
//		java.net.URL url = TemplatesTabContentPane.class.getResource( resource );
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
