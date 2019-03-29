package org.lgna.story.resourceutilities.exporterutils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.cmu.cs.dennisc.math.AffineMatrix4x4;
import edu.cmu.cs.dennisc.math.AxisAlignedBox;
import edu.cmu.cs.dennisc.math.EpsilonUtilities;
import edu.cmu.cs.dennisc.math.OrthogonalMatrix3x3;
import edu.cmu.cs.dennisc.math.Point3;
import edu.cmu.cs.dennisc.math.Vector3;
import edu.cmu.cs.dennisc.xml.XMLUtilities;

public class Pose {

	public static final String DEFAULT_NAME = "DEFAULT";

	public final String name;
	public final Map< Short, AffineMatrix4x4> transforms = new HashMap<Short, AffineMatrix4x4>();

	public Pose( String name ) {
		this.name = name;
	}

	public Pose( String name, double[] poseData ) {
		this.name = name;
		this.setPoseFromRawData(poseData);
	}

	public Pose( String name, Map<Short, AffineMatrix4x4> poseMap ) {
		this.name = name;
		this.setPoseFromMapData(poseMap);
	}

	public Pose( Pose other ){
		this.name = other.name;
		this.setPoseFromMapData(other.transforms);
	}

	public static Map<String, AffineMatrix4x4> createNamedJointPoseData( Pose pose, Map<Short, String> nameMap) {
		Map<String, AffineMatrix4x4> namedJointsPoseData = new HashMap<String, AffineMatrix4x4>();
		for (Entry<Short, AffineMatrix4x4> poseEntry : pose.transforms.entrySet()) {
			namedJointsPoseData.put(nameMap.get(poseEntry.getKey()), poseEntry.getValue());
		}
		return namedJointsPoseData;
	}

	public static Element createPoseXMLElement(Document doc, Pose pose)
    {
        Element poseElement = doc.createElement("Pose");
        poseElement.setAttribute("name", pose.name);

        for (Entry<Short, AffineMatrix4x4> transformEntry : pose.transforms.entrySet()){
        	Element transformElement = doc.createElement("Transform");
        	transformElement.setAttribute("ref", transformEntry.getKey().toString());

        	Element orientationElement = doc.createElement("Orientation");

        	Element rightElement = doc.createElement("Right");
        	rightElement.setAttribute("x", Double.toString(transformEntry.getValue().orientation.right.x));
        	rightElement.setAttribute("y", Double.toString(transformEntry.getValue().orientation.right.y));
        	rightElement.setAttribute("z", Double.toString(transformEntry.getValue().orientation.right.z));
        	orientationElement.appendChild(rightElement);

        	Element upElement = doc.createElement("Up");
        	upElement.setAttribute("x", Double.toString(transformEntry.getValue().orientation.up.x));
        	upElement.setAttribute("y", Double.toString(transformEntry.getValue().orientation.up.y));
        	upElement.setAttribute("z", Double.toString(transformEntry.getValue().orientation.up.z));
        	orientationElement.appendChild(upElement);

        	Element backElement = doc.createElement("Back");
        	backElement.setAttribute("x", Double.toString(transformEntry.getValue().orientation.backward.x));
        	backElement.setAttribute("y", Double.toString(transformEntry.getValue().orientation.backward.y));
        	backElement.setAttribute("z", Double.toString(transformEntry.getValue().orientation.backward.z));
        	orientationElement.appendChild(backElement);

        	Element translationElement = doc.createElement("Translation");
        	translationElement.setAttribute("x", Double.toString(transformEntry.getValue().translation.x));
        	translationElement.setAttribute("y", Double.toString(transformEntry.getValue().translation.y));
        	translationElement.setAttribute("z", Double.toString(transformEntry.getValue().translation.z));

        	transformElement.appendChild(orientationElement);
        	transformElement.appendChild(translationElement);

        	poseElement.appendChild(transformElement);
        }
        return poseElement;
    }

	public static OrthogonalMatrix3x3 createOrthogonalMatrix3x3FromXMLElement( Element orientationElement) {
		Element rightElement = XMLUtilities.getSingleChildElementByTagName(orientationElement, "Right");
		Vector3 right = createVector3FromXMLElement(rightElement);

		Element upElement = XMLUtilities.getSingleChildElementByTagName(orientationElement, "Up");
		Vector3 up = createVector3FromXMLElement(upElement);

		Element backElement = XMLUtilities.getSingleChildElementByTagName(orientationElement, "Back");
		Vector3 backward = createVector3FromXMLElement(backElement);

		return new OrthogonalMatrix3x3(right, up, backward);
	}

	public static Vector3 createVector3FromXMLElement( Element v3Element) {
		double x = Double.parseDouble(v3Element.getAttribute("x"));
		double y = Double.parseDouble(v3Element.getAttribute("y"));
		double z = Double.parseDouble(v3Element.getAttribute("z"));
		return new Vector3(x, y, z);
	}

	public static Point3 createPoint3FromXMLElement( Element v3Element) {
		double x = Double.parseDouble(v3Element.getAttribute("x"));
		double y = Double.parseDouble(v3Element.getAttribute("y"));
		double z = Double.parseDouble(v3Element.getAttribute("z"));
		return new Point3(x, y, z);
	}

	public static Pose createPoseFromXMLElement( Element poseElement )
	{
		if( poseElement != null )
		{
			String name = poseElement.getAttribute("name");
			Map<Short, AffineMatrix4x4> poseData = new HashMap<Short, AffineMatrix4x4>();

			java.util.List<org.w3c.dom.Element> transformElements = XMLUtilities.getChildElementsByTagName(poseElement, "Transform");
			for (int i=0; i<transformElements.size(); i++) {
				Element transformElement = transformElements.get(i);
				short ref = Short.parseShort(transformElement.getAttribute("ref"));

				Element orientationElement = XMLUtilities.getSingleChildElementByTagName(transformElement, "Orientation");
				Element translationElement = XMLUtilities.getSingleChildElementByTagName(transformElement, "Translation");

				OrthogonalMatrix3x3 orientation = createOrthogonalMatrix3x3FromXMLElement(orientationElement);
				Point3 translation = createPoint3FromXMLElement(translationElement);

				AffineMatrix4x4 matrix = new AffineMatrix4x4(orientation, translation);

				poseData.put(ref, matrix);
			}
			Pose pose = new Pose(name, poseData);
			return pose;
		}

		return null;
	}

	private void setPoseFromMapData( Map<Short, AffineMatrix4x4> poseMap ) {
    	this.transforms.clear();
    	for (Entry<Short, AffineMatrix4x4> poseEntry : poseMap.entrySet()) {
    		this.transforms.put(poseEntry.getKey(), poseEntry.getValue());
    	}
    }

	private void setPoseFromRawData( double[] poseDataArray ) {
		this.transforms.clear();
		double[] transformData = new double[12];
		for (int i=0; i<poseDataArray.length; i+=13) {
			double refVal = poseDataArray[i];
			short ref = (short)refVal;
			System.arraycopy(poseDataArray, i+1, transformData, 0, 12);
			this.transforms.put(ref, AffineMatrix4x4.createFromColumnMajorArray12(transformData));
		}
	}

	public void removeUnusedReferencesFromPose(Map<String, Short> jointMap) {
        Iterator<Entry<Short, AffineMatrix4x4>> poseIterator = this.transforms.entrySet().iterator();
        while (poseIterator.hasNext()) {
        	Entry<Short, AffineMatrix4x4> poseEntry = poseIterator.next();
        	if (!jointMap.containsValue(poseEntry.getKey())) {
        		poseIterator.remove();
        	}
        }
    }

	public void removeMatchingOrientationsFromPose(Pose basePose) {
        Iterator<Entry<Short, AffineMatrix4x4>> poseIterator = this.transforms.entrySet().iterator();
        while (poseIterator.hasNext()) {
        	Entry<Short, AffineMatrix4x4> poseEntry = poseIterator.next();
        	AffineMatrix4x4 poseTransform = poseEntry.getValue();
        	AffineMatrix4x4 baseTransform = basePose.transforms.get(poseEntry.getKey());
        	assert baseTransform != null;
        	if (baseTransform.orientation.isWithinEpsilonOf(poseTransform.orientation, EpsilonUtilities.REASONABLE_EPSILON) &&
        			baseTransform.translation.isWithinEpsilonOf(poseTransform.translation, EpsilonUtilities.REASONABLE_EPSILON) ){
        		poseIterator.remove();

        		System.out.println("REMOVING "+poseEntry.getKey());
        	}
        	else {
        		System.out.println("SAVING "+poseEntry.getKey());
        	}
        }
    }
}
