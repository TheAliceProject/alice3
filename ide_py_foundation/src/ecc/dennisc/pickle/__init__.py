import java
import ecc.dennisc.io

def pickle( o, path, isSerializationDesired=True ):
	ecc.dennisc.io.createParentDirectoriesIfNecessary( path )
	if isSerializationDesired:
		oos = java.io.ObjectOutputStream( java.io.FileOutputStream( path ) )
		try:
			oos.writeObject( o )
		finally:
			oos.close()
	else:
		f = open( picklePath, "w" )
		try:
			from pickle import Pickler
			pickler = Pickler( f )
			pickler.dump( assetInfos )
		finally:
			f.close()

def unpickle( path, isSerializationDesired=True ):
	if isSerializationDesired:
		ois = java.io.ObjectInputStream( java.io.FileInputStream( path ) )
		try:
			return ois.readObject()
		finally:
			ois.close()
	else:
		f = open( path, "r" )
		try:
			from pickle import Unpickler
			unpickler = Unpickler( f )
			return unpickler.load()
		finally:
			f.close()
