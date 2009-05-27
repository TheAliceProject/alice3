public class LaunchGenerator {
	private static String getCanonicalPathIfPossible( java.io.File file ) {
		if( file != null ) {
			try {
				return file.getCanonicalPath();
			} catch( java.io.IOException ioe ) {
				return file.getAbsolutePath();
			}
		} else {
			return null;
		}
	}

	private java.io.File installDirectory;
	private java.util.List< java.io.File > libraryPath = new java.util.LinkedList< java.io.File >();
	private java.util.List< java.io.File > classPath = new java.util.LinkedList< java.io.File >();
	private java.io.File trustStore;
	private String mainClassName;

	public static boolean isOSX() {
		return System.getProperty( "os.name" ).toLowerCase().startsWith( "mac os x" );
		//return true;
	}
	public static boolean isAMD64() {
		return System.getProperty( "os.arch" ).equals( "amd64" );
	}
	
	private static String getSeparator() {
		if( isOSX() ) {
			return ":";
		} else {
			return ";";
		}
	}

	public java.io.File getInstallDirectory() {
		return this.installDirectory;
	}
	public void setInstallDirectory( java.io.File installDirectory ) {
		this.installDirectory = installDirectory;
	}

	public java.io.File getTrustStore() {
		return this.trustStore;
	}
	public void setTrustStore( java.io.File trustStore ) {
		this.trustStore = trustStore;
	}
	public void addToLibraryPath( java.io.File f ) {
		this.libraryPath.add( f );
	}
	public void addToClassPath( java.io.File f ) {
		this.classPath.add( f );
	}

	public String getMainClassName() {
		return this.mainClassName;
	}
	public void setMainClassName( String mainClassName ) {
		this.mainClassName = mainClassName;
	}

	private void appendNonClassPathOptions( StringBuffer sb ) {
		final String SEPARATOR = getSeparator();
		sb.append( "-ea " );
		sb.append( "-Xmx1024m" );
		if( isOSX() ) {
			//pass
		} else {
			sb.append( " " );
			sb.append( "-Dswing.aatext=true " );
			sb.append( "-Dorg.alice.ide.IDE.install.dir=" );
			sb.append( "\"" );
			sb.append( getCanonicalPathIfPossible( this.installDirectory ) );
			sb.append( "\" " );
			if( this.trustStore != null ) {
				sb.append( "-Djavax.net.ssl.trustStore=" );
				sb.append( "\"" );
				sb.append( getCanonicalPathIfPossible( this.trustStore ) );
				sb.append( "\" " );
			}
			String separator = "";
			sb.append( "-Djava.library.path=" );
			sb.append( "\"" );
			for( java.io.File f : this.libraryPath ) {
				sb.append( separator );
				sb.append( getCanonicalPathIfPossible( f ) );
				separator = SEPARATOR;
			}
			sb.append( "\" " );
		}
	}
	
	public String encode() {
		final String SEPARATOR = getSeparator();
		StringBuffer sb = new StringBuffer();
		if( isOSX() ) {
			sb.append( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" );
			sb.append( "<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\n" );
			sb.append( "<plist version=\"1.0\">\n" );
			sb.append( "\t<dict>\n" );
			sb.append( "\t\t<key>CFBundleAllowMixedLocalizations</key>\n" );
			sb.append( "\t\t<string>true</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleDevelopmentRegion</key>\n" );
			sb.append( "\t\t<string>English</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleExecutable</key>\n" );
			sb.append( "\t\t<string>JavaApplicationStub</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleIconFile</key>\n" );
			sb.append( "\t\t<string>alice.icns</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleInfoDictionaryVersion</key>\n" );
			sb.append( "\t\t<string>6.0</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleName</key>\n" );
			sb.append( "\t\t<string>Alice-3.beta</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundlePackageType</key>\n" );
			sb.append( "\t\t<string>APPL</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleSignature</key>\n" );
			sb.append( "\t\t<string>????</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>CFBundleVersion</key>\n" );
			sb.append( "\t\t<string>100.0</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t<key>Java</key>\n" );
			sb.append( "\t\t<dict>\n" );
			sb.append( "\t\t\t<key>Arguments</key>\n" );
			sb.append( "\t\t\t<string></string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>ClassPath</key>\n" );
			sb.append( "\t\t\t<array>\n" );
			
			for( java.io.File f : this.classPath ) {
				sb.append( "\t\t\t\t<string>" );
				sb.append( getCanonicalPathIfPossible( f ) );
				sb.append( "</string>\n" );
			}
			
			sb.append( "\t\t\t</array>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>JVMVersion</key>\n" );
			sb.append( "\t\t\t<string>1.5*</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>MainClass</key>\n" );
			sb.append( "\t\t\t<string>" );
			sb.append( this.mainClassName );
			sb.append( "</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>Properties</key>\n" );
			sb.append( "\t\t\t<dict>\n" );
			sb.append( "\t\t\t\t<key>apple.laf.useScreenMenuBar</key>\n" );
			sb.append( "\t\t\t\t<string>true</string>\n" );
			sb.append( "\t\t\t\t<key>swing.aatext</key>\n" );
			sb.append( "\t\t\t\t<string>true</string>\n" );
			sb.append( "\t\t\t\t<key>javax.net.ssl.trustStore</key>\n" );
			sb.append( "\t\t\t\t<string>" );
			sb.append( getCanonicalPathIfPossible( this.trustStore ) );
			sb.append( "</string>\n" );
			sb.append( "\t\t\t\t<key>org.alice.ide.IDE.install.dir</key>\n" );
			sb.append( "\t\t\t\t<string>" );
			sb.append( getCanonicalPathIfPossible( this.installDirectory ) );
			sb.append( "</string>\n" );
			sb.append( "\t\t\t\t<key>java.library.path</key>\n" );
			sb.append( "\t\t\t\t<string>" );
			String separator = "";
			for( java.io.File f : this.libraryPath ) {
				sb.append( separator );
				sb.append( getCanonicalPathIfPossible( f ) );
				separator = SEPARATOR;
			}
			sb.append( "</string>\n" );
			sb.append( "\t\t\t</dict>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>VMOptions</key>\n" );
			sb.append( "\t\t\t<string>" );

			this.appendNonClassPathOptions( sb );
			
			sb.append( "</string>\n" );
			sb.append( "\n" );
			sb.append( "\t\t\t<key>WorkingDirectory</key>\n" );
			sb.append( "\t\t\t<string>" );
			sb.append( getCanonicalPathIfPossible( this.installDirectory ) );
			sb.append( "</string>\n" );
			sb.append( "\t\t</dict>\n" );
			sb.append( "\t</dict>\n" );
			sb.append( "</plist>\n" );
		} else {
			sb.append( ".\\jre6\\bin\\java " );
			this.appendNonClassPathOptions( sb );
			String separator = "";
			sb.append( "-classpath " );
			sb.append( "\"" );
			for( java.io.File f : this.classPath ) {
				sb.append( separator );
				sb.append( getCanonicalPathIfPossible( f ) );
				separator = SEPARATOR;
			}
			sb.append( "\" " );

			sb.append( this.mainClassName );
			sb.append( " %1\n" );
			sb.append( "pause" );
		}

		return sb.toString();
	}
	public java.io.File encode( java.io.File rv ) throws java.io.IOException {

		rv.getParentFile().mkdirs();

		java.io.FileOutputStream fos = new java.io.FileOutputStream( rv );
		String s = this.encode();
		System.out.println( s );
		fos.write( s.getBytes() );
		return rv;
	}
	public static void main( String[] args ) throws Exception {
		java.io.File installDirectory = new java.io.File( "c:/Program Files/Alice3BetaInstallTest" );
		LaunchGenerator launchGenerator = new LaunchGenerator();

		launchGenerator.setInstallDirectory( installDirectory );

		launchGenerator.setTrustStore( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/truststore.jks" ) );

		launchGenerator.addToLibraryPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/jogl/lib/windows-i586" ) );
		launchGenerator.addToLibraryPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/windows-i586" ) );

		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/jaf/activation.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/javamail/mail.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/javamail/lib/smtp.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/jogl/lib/jogl.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/jogl/lib/gluegen-rt.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/swingworker/swing-worker-1.2.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0000/tools/xmlrpc/xmlrpc-client-1.1.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/foundation.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/moveandturn.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/lg_walkandtouch.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/stage.jar" ) );
		launchGenerator.addToClassPath( new java.io.File( "C:/Program Files/Alice/3.beta.0053/application/ide.jar" ) );

		launchGenerator.setMainClassName( "org.alice.stageide.EntryPoint" );

		String filename;
		if( isOSX() ) {
			filename = "Info.plist";
		} else {
			filename = "alice.bat";
		}
		launchGenerator.encode( new java.io.File( installDirectory, filename ) );
	}
}
