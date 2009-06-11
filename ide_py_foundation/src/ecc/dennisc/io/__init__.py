def createParentDirectoriesIfNecessary( path ):
	import java
	f = java.io.File( path )
	d = f.getParentFile()
	d.mkdirs()
