import java
import edu

import re

homeDir = java.lang.System.getProperty( "user.home" )
rootDir = homeDir + "/Documents/gits/alice"

projects = [
	"core/i18n",
	"core/util",
	"core/scenegraph",
	"core/story-api",
	"core/story-api-migration",
	"core/ast",
	"core/croquet",
	"core/image-editor",
	"core/issue-reporting",
	"core/ide",
	"alice/alice-ide"
]

locales = ["ar", "de", "el", "es", "fa", "fr", "id", "nl", "pl", "pt", "pt_BR", "ro", "ru", "sl", "uk", "zh_CN", "zh_TW"]

allLocales = set()

def getLocalizedPath( path, locale ):
	a, b = path.split( ".", 1 )
	return a + "_" + locale + "." + b

def getKeys( s ):
	return re.findall( "</\\w+/>", s ) + re.findall( "</\\w+\\(\\)/>", s )

class LocalizationFile:
	def __init__( self, project, javaOrResources, path ):
		assert project
		assert path
		self._project = project
		self._javaOrResources = javaOrResources
		self._path = path
		self._localizations = []

	def sortLocalizations(self):
		self._localizations.sort(key=lambda other: other._key)

	def writeLocalizations(self):
		map = {}
		for localization in self._localizations:
			for locale, value in localization._mapLocaleToValue.items():
				try:
					keyValuePairs = map[locale]
				except KeyError:
					keyValuePairs = []
					map[ locale ] = keyValuePairs
				keyValuePairs.append( (localization._key, value) )

		dir = java.io.File( rootDir + "/" + self._project + "/src/i18n/" + self._javaOrResources )
		for locale, keyValuePairs in map.items():
			file =  java.io.File( dir, getLocalizedPath( self._path, locale ) )
			edu.cmu.cs.dennisc.java.io.FileUtilities.createParentDirectoriesIfNecessary( file )
			import re
			f = open( file.getAbsolutePath(), "w" )
			for key, value in keyValuePairs:
				#value = re.sub( "\\\\u([0-9a-f]{4})", lambda pattern: "\\u" + pattern.group( 1 ).upper(), value )
				f.write( key + "=" + value + "\n" )
			f.close()

	def __repr__( self ):
		return self._project + " " + self._path + " " + `self._localizations`

class Localization:
	def __init__( self, key, originalValue ):
		self._key = key
		self._originalValue = originalValue
		self._mapLocaleToValue = {}
	def __repr__( self ):
		return self._key + " " + self._originalValue + " " + `self._mapLocaleToValue`

localizationFiles = []

def fixSlashes( s ):
	return s.replace( "\\", "/" )

def parseFile( file ):
	f = open( file.getAbsolutePath(), "r" )
	lines = f.readlines()
	f.close()
	rv = []
	for line in lines:
		if line.strip():
			if line.endswith( "\n" ):
				line = line[:-1]
			try:
				subKey, unusedSeparator, value = line.partition( "=" )
			except:
				value = None
			if value:
				subKey = subKey.strip()
				value = value.strip()
				if len( subKey ):
					rv.append( ( subKey, value ) )
			else:
				print line
	return rv

def getSubPath( dir, file ):
	assert dir.isDirectory()
	assert file.isDirectory() == False
	rootPath = dir.getAbsolutePath()
	return fixSlashes( file.getAbsolutePath()[ len(rootPath) : ] )


#import migrate_2014_12_10 as current_migration
import null_migration as current_migration

def find_localizationFile( analogousPath ):
	for lf in localizationFiles:
		if lf._path == analogousPath:
			return lf
	return None

def find_localization( lf, key ):
	for localization in lf._localizations:
		if localization._key == key:
			return localization
	return None

def splitLocalizedPath( path ):
	path = fixSlashes(path)
	prefix, localPlusExtension = path.split("_", 1)
	locale, extension = localPlusExtension.split( "." )
	return (prefix, locale, extension)

def isKeyToBeDeleted( analogousPath, key ):
	if analogousPath in current_migration.keysToDelete:
		return key in current_migration.keysToDelete[ analogousPath ]

def mapKeyIfNecessary( analogousPath, key ):
	if analogousPath in current_migration.keysMap:
		if key in current_migration.keysMap[ analogousPath ]:
			return current_migration.keysMap[ analogousPath ][ key ]

	return key

def mapLocalizationFileForKeyfNecessary(lf, analogousPath, key ):
	for originalPath, mappedPath, keys in current_migration.keysToMove:
		if originalPath == analogousPath:
			if key in keys:
				return find_localizationFile(mappedPath)
	return lf



def buildEnglish():
	for project in projects:
		for javaOrResources in ["java", "resources"]:
			dir = java.io.File( rootDir + "/" + project + "/src/main/" + javaOrResources )
			if dir.exists():
				files = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( dir, "properties" )
				for file in files:
					subPath = getSubPath(dir, file)
					lf = LocalizationFile( project, javaOrResources, subPath )
					for key, value in parseFile(file):
						lf._localizations.append( Localization( key, value ) )
					localizationFiles.append( lf )

allLocalizedFiles = []

def buildTranslations():
	global allLocalizedFiles
	missingKeys = set()
	for project in projects:
		for javaOrResources in ["java", "resources"]:
			dir = java.io.File( rootDir + "/" + project + "/src/i18n/" + javaOrResources )
			if dir.exists():
				files = edu.cmu.cs.dennisc.java.io.FileUtilities.listDescendants( dir, "properties" )
				allLocalizedFiles += files
				for file in files:
					subPath = getSubPath(dir, file)
					prefix, locale, extension = splitLocalizedPath(subPath)
					analogousPath = prefix + "." + extension
					if current_migration.filesToMoveMap.has_key( analogousPath ):
						analogousPath = current_migration.filesToMoveMap[ analogousPath ]

					if analogousPath in current_migration.filesToDelete:
						pass
					else:
						lf = find_localizationFile(analogousPath)
						if lf:
							for key, value in parseFile(file):
								if isKeyToBeDeleted( analogousPath, key ):
									pass
									#print "deleting", analogousPath, key
								else:
									key = mapKeyIfNecessary( analogousPath, key )
									possiblyMappedLocalizatonFile = mapLocalizationFileForKeyfNecessary( lf, analogousPath, key )
									localization = find_localization(possiblyMappedLocalizatonFile, key)
									if localization:
										if value == localization._originalValue:
											pass
										elif value.strip() == localization._originalValue.strip():
											raise value
										else:
											originalKeys = getKeys( localization._originalValue )
											translatedKeys = getKeys( value )
											if originalKeys == translatedKeys:
												localization._mapLocaleToValue[ locale ] = value
												allLocales.add( locale )
											else:
												print lf._path
												print localization._key, localization._originalValue, value
									else:
										if key in missingKeys:
											pass
										else:
											print "\t", analogousPath, key, value
										missingKeys.add( key )


							#print subPath, lf
						else:
							print subPath


buildEnglish()
buildTranslations()

def updateFiles():
	for file in allLocalizedFiles:
		file.delete()
		assert file.exists() == False
	for lf in localizationFiles:
		#lf.sortLocalizations()
		lf.writeLocalizations()

updateFiles()

l = list( allLocales )
l.sort()
for locale in l:
	print locale

print "done"