#print "-->", __name__

import java
import javax
import edu

from edu.cmu.cs.dennisc import alice

import ecc

class AbstractRenameOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, member ):
		self._member = member
		self.putValue( javax.swing.Action.NAME, "rename..." )

	def _getSiblings( self ):
		raise "Override"

	def prepare( self, e, observer ):
		inputPane = ecc.dennisc.alice.ide.inputpanes.RenamePane( self._member, self._getSiblings() )
		self._nextName = inputPane.showInJDialog( title="Rename" )
		if self._nextName:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		self._prevName = self._member.name.getValue()
		self.redo()
	
	def undo( self ):
		self._member.name.setValue( self._prevName )
	def redo( self ):
		self._member.name.setValue( self._nextName )


class RenameMethodOperation( AbstractRenameOperation ):
	def _getSiblings( self ):
		return self._member.getDeclaringType().getDeclaredMethods()

class RenameFieldOperation( AbstractRenameOperation ):
	def _getSiblings( self ):
		return self._member.getDeclaringType().getDeclaredFields()

class RenameParameterOperation( AbstractRenameOperation ):
	def __init__( self, member, code ):
		self._code = code
		AbstractRenameOperation.__init__( self, member )
	def _getSiblings( self ):
		return self._code.getParameters()


class AbstractDeleteMemberOperation( alice.ide.AbstractUndoableOperation ):
	def __init__( self, member, membersProperty ):
		self._member = member
		self._membersProperty = membersProperty
		self.putValue( javax.swing.Action.NAME, "delete..." )

	def _getHasReferencesText(self, N):
		raise "Override"
	def prepare( self, e, observer ):
		references = self.getIDE().getReferences( self._member )
		N = len( references )
		if N:
			s = self._getHasReferencesText(N)
			javax.swing.JOptionPane.showMessageDialog( self.getIDE(), s )
			return alice.ide.Operation.PreparationResult.CANCEL
		else:
			return alice.ide.Operation.PreparationResult.PERFORM_AND_ADD_TO_HISTORY

	def perform( self ):
		self._index = self._membersProperty.indexOf( self._member )
		self.redo()
		self.getIDE().handleDelete( self._member )
	
	def undo( self ):
		self._membersProperty.add( self._index, [self._member] )
	def redo( self ):
		self._membersProperty.remove( self._index )

class DeleteFieldOperation( AbstractDeleteMemberOperation ):
	def __init__( self, field ):
		AbstractDeleteMemberOperation.__init__(self, field, field.getDeclaringType().fields )
	def _getHasReferencesText(self, N):
		s = "Unable to delete property named \""
		s += self._member.getName()
		s += "\" because it has "  
		if N == 1:
			s += "an access refrence"
		else:
			s += str( N )
			s += " access refrences"
		s += " to it.\nYou must remove "
		if N == 1:
			s += "this reference"
		else:
			s += "these references"
		s += " if you want to delete \""
		s += self._member.getName()
		s += "\" ."
		return s  

class DeleteMethodOperation( AbstractDeleteMemberOperation ):
	def __init__( self, method ):
		AbstractDeleteMemberOperation.__init__(self, method, method.getDeclaringType().methods )
	def _getHasReferencesText(self, N):
		s = "Unable to delete "
		if self._member.isProcedure():
			s += "procedure"
		else:
			s += "function"
		s += " named \"";
		s += self._member.getName()
		s += "\" because it has "  
		if N == 1:
			s += "an invocation reference"
		else:
			s += str( N )
			s += " invocation references"
		s += " to it.\nYou must remove "
		if N == 1:
			s += "this reference"
		else:
			s += "these references"
		s += " if you want to delete \""
		s += self._member.getName()
		s += "\" ."  
		return s  

class EditCodeOperation( alice.ide.AbstractOperation ):
	def __init__( self, code ):
		self._code = code
	def prepare( self, e, observer ):
		return alice.ide.Operation.PreparationResult.PERFORM
	def perform( self ):
		alice.ide.IDE.getSingleton().setFocusedCode( self._code )

class EditMethodOperation( EditCodeOperation ):
	def __init__( self, code ):
		EditCodeOperation.__init__( self, code )
		self.putValue( javax.swing.Action.NAME, "edit" )

class EditConstructorOperation( EditCodeOperation ):
	def __init__( self, code ):
		EditCodeOperation.__init__( self, code )
		self.putValue( javax.swing.Action.NAME, "edit constructor" )


class SaveTypeOperation( alice.ide.AbstractOperation ):
	def __init__( self, type ):
		self._type = type
		self.putValue( javax.swing.Action.NAME, "save type..." )

	def prepare( self, e, observer ):
		self._file = ecc.dennisc.swing.showFileSaveAsDialog( self.getIDE(), alice.io.FileUtilities.getMyTypesDirectory(), alice.io.FileUtilities.TYPE_EXTENSION )
		if self._file:
			return alice.ide.Operation.PreparationResult.PERFORM
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		alice.io.FileUtilities.writeType( self._type, self._file )

class CreateNewInstanceOperation( alice.ide.AbstractOperation ):
	def __init__( self, type ):
		self._type = type
		self.putValue( javax.swing.Action.NAME, "create new instance" )

	def prepare( self, e, observer ):
		t = self._type.getFirstTypeEncounteredDeclaredInJava()
		cls = t.getCls()
		clsName = cls.__name__
		thumbnailsRoot = self.getIDE()._getGalleryThumbnailsRoot()
		
		prefixList = []
		prefixList.append( "org.alice.apis.moveandturn.gallery" )
		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.scenes" )
		prefixList.append( "edu.wustl.cse.lookingglass.apis.walkandtouch.gallery.characters" )
		for prefix in prefixList:
			if clsName.startswith( prefix ):
				file = java.io.File( thumbnailsRoot, prefix + clsName[ len( prefix ): ].replace( ".", "/" ) + ".png" )

		inputPane = ecc.dennisc.alice.ide.moveandturn.editors.gallery.CreateInstancePane( file, thumbnailsRoot, self._type )
		owner = self.getIDE()
		self._instance = inputPane.showInJDialog( owner, "Create Instance", True )
		if self._instance:
			return alice.ide.Operation.PreparationResult.PERFORM
		else:
			return alice.ide.Operation.PreparationResult.CANCEL

	def perform( self ):
		self.getIDE()._scenePane.addInstance( self._instance )

class FieldAltTriggerMouseAdapter( edu.cmu.cs.dennisc.awt.event.AltTriggerMouseAdapter ):
	def __init__( self, field ):
		self._field = field
	def altTriggered(self, e ):
		if self._field.isDeclaredInAlice():
			operations = []
			operations.append( RenameFieldOperation( self._field ) )
			if self._field.isDeletionAllowed.getValue():
				operations.append( DeleteFieldOperation( self._field ) )
			operations.append( SaveTypeOperation( self._field.getValueType() ) )
			operations.append( CreateNewInstanceOperation( self._field.getValueType() ) )
			popup = alice.ide.MenuUtilities.createJPopupMenu( operations )
			popup.show( e.getSource(), e.getX(), e.getY() )


class MethodAltTriggerMouseAdapter( edu.cmu.cs.dennisc.awt.event.AltTriggerMouseAdapter ):
	def __init__( self, method ):
		self._method = method
	def altTriggered(self, e ):
		if self._method.isDeclaredInAlice():
			operations = []
			if self._method.isSignatureLocked.getValue():
				pass
			else:
				operations.append( ecc.dennisc.alice.ide.operations.ast.RenameMethodOperation( self._method ) )
			if self._method.isDeletionAllowed.getValue():
				operations.append( ecc.dennisc.alice.ide.operations.ast.DeleteMethodOperation( self._method ) )
			operations.append( EditMethodOperation( self._method ) )
				
			popup = alice.ide.MenuUtilities.createJPopupMenu( operations )
			popup.show( e.getSource(), e.getX(), e.getY() )

class ConstructorAltTriggerMouseAdapter( edu.cmu.cs.dennisc.awt.event.AltTriggerMouseAdapter ):
	def __init__( self, constructor ):
		self._constructor = constructor
	def altTriggered(self, e ):
		if self._constructor.isDeclaredInAlice():
			operations = []
			#if self._constructor.isDeletionAllowed.getValue():
			#	operations.append( ecc.dennisc.alice.ide.operations.ast.DeleteMethodOperation( self._constructor ) )
			operations.append( EditConstructorOperation( self._constructor ) )
				
			popup = alice.ide.MenuUtilities.createJPopupMenu( operations )
			popup.show( e.getSource(), e.getX(), e.getY() )

#print "<--", __name__
