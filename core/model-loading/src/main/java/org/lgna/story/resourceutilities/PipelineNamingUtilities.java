package org.lgna.story.resourceutilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.lgna.story.SBiped;
import org.lgna.story.SFlyer;
import org.lgna.story.SQuadruped;
import org.lgna.story.SSwimmer;
import org.lgna.story.implementation.alice.AliceResourceClassUtilities;


public class PipelineNamingUtilities {

	public static String getResourceClassName(String resourceName) {
		return resourceName+"Resource";
	}


	public static Map<Class<?>, String[]> sJointsToSuppressByClass = new HashMap<Class<?>, String[]>();
	static {
		sJointsToSuppressByClass.put(SBiped.class,
			new String[] {
			"LEFT_THUMB_TIP",
			"LEFT_INDEX_FINGER_TIP",
			"LEFT_MIDDLE_FINGER_TIP",
			"LEFT_PINKY_FINGER_TIP",
			"LEFT_THUMB_END",
			"LEFT_INDEX_FINGER_END",
			"LEFT_MIDDLE_FINGER_END",
			"LEFT_PINKY_FINGER_END",
			"RIGHT_THUMB_TIP",
			"RIGHT_INDEX_FINGER_TIP",
			"RIGHT_MIDDLE_FINGER_TIP",
			"RIGHT_PINKY_FINGER_TIP",
			"RIGHT_THUMB_END",
			"RIGHT_INDEX_FINGER_END",
			"RIGHT_MIDDLE_FINGER_END",
			"RIGHT_PINKY_FINGER_END",
			"RIGHT_RING_FINGER",
			"RIGHT_RING_FINGER_KNUCKLE",
			"RIGHT_RING_FINGER_TIP",
			"RIGHT_RING_FINGER_END",
			"LEFT_RING_FINGER",
			"LEFT_RING_FINGER_KNUCKLE",
			"LEFT_RING_FINGER_TIP",
			"LEFT_RING_FINGER_END",
			"LOWER_LIP",
			"JAW_1",
			"JAW1",
			"JAW_TIP",
			"MOUTH_TIP",
//			"RIGHT_EYELID",
//			"LEFT_EYELID",
			"LEFT_TOES",
			"RIGHT_TOES",
			"LEFT_EAR_TIP",
			"RIGHT_EAR_TIP",
		});
		sJointsToSuppressByClass.put(SFlyer.class,
				new String[] {
				"LEFT_TOE",
				"RIGHT_TOE",
				"LOWER_LIP",
//				"RIGHT_EYELID",
//				"LEFT_EYELID",
			});
		sJointsToSuppressByClass.put(SQuadruped.class,
				new String[] {
				"LEFT_TOE",
				"RIGHT_TOE",
				"LOWER_LIP",
//				"RIGHT_EYELID",
//				"LEFT_EYELID",
				"RIGHT_EAR_TIP",
				"LEFT_EAR_TIP",
				"JAW_1",
				"JAW_TIP",
				"MOUTH_TIP",
			});
		sJointsToSuppressByClass.put(SSwimmer.class,
				new String[] {
				"FRONT_LEFT_FIN_TIP",
				"FRONT_RIGHT_FIN_TIP",
				"LOWER_LIP",
//				"RIGHT_EYELID",
//				"LEFT_EYELID",
				"RIGHT_EAR_TIP",
				"LEFT_EAR_TIP",
				"JAW_1",
				"JAW_TIP",
				"MOUTH_TIP",
				"BACK_BOTTOM_FIN_TIP",
				"BACK_TOP_FIN_TIP",
				"BACK_RIGHT_FIN_TIP",
				"BACK_LEFT_FIN_TIP"
			});
	}

	public static Map<String, String> sRemapJointsMap = new HashMap<String, String>();
	static {
		// From bipeds
		sRemapJointsMap.put("LEFT_THUMB_TIP", "LEFT_THUMB_KNUCKLE");
		sRemapJointsMap.put("LEFT_INDEX_FINGER_TIP", "LEFT_INDEX_FINGER_KNUCKLE");
		sRemapJointsMap.put("LEFT_RING_FINGER_TIP", "LEFT_RING_FINGER_KNUCKLE");
		sRemapJointsMap.put("LEFT_PINKY_FINGER_TIP", "LEFT_PINKY_FINGER_KNUCKLE");
		sRemapJointsMap.put("RIGHT_THUMB_TIP", "RIGHT_THUMB_KNUCKLE");
		sRemapJointsMap.put("RIGHT_INDEX_FINGER_TIP", "RIGHT_INDEX_FINGER_KNUCKLE");
		sRemapJointsMap.put("RIGHT_RING_FINGER_TIP", "RIGHT_RING_FINGER_KNUCKLE");
		sRemapJointsMap.put("RIGHT_PINKY_FINGER_TIP", "RIGHT_PINKY_KNUCKLE");
		sRemapJointsMap.put("LEFT_RING_FINGER", "LEFT_PINKY_FINGER");
		sRemapJointsMap.put("LEFT_RING_FINGER_KNUCKLE", "LEFT_PINKY_FINGER_KNUCKLE");
		sRemapJointsMap.put("LEFT_RING_FINGER_TIP", "LEFT_PINKY_FINGER_TIP");
		sRemapJointsMap.put("RIGHT_RING_FINGER", "RIGHT_PINKY_FINGER");
		sRemapJointsMap.put("RIGHT_RING_FINGER_KNUCKLE", "RIGHT_PINKY_FINGER_KNUCKLE");
		sRemapJointsMap.put("RIGHT_RING_FINGER_TIP", "RIGHT_PINKY_FINGER_TIP");
	}

	private static Map<String, String> sMayaNameToAliceNameMap = new HashMap<String, String>();
	static {
		// From bipeds
		sMayaNameToAliceNameMap.put("pelvis", "PELVIS_LOWER_BODY");
		sMayaNameToAliceNameMap.put("hip_L", "LEFT_HIP");
		sMayaNameToAliceNameMap.put("knee_L", "LEFT_KNEE");
		sMayaNameToAliceNameMap.put("ankle_L", "LEFT_ANKLE");
		sMayaNameToAliceNameMap.put("hip_R", "RIGHT_HIP");
		sMayaNameToAliceNameMap.put("knee_R", "RIGHT_KNEE");
		sMayaNameToAliceNameMap.put("ankle_R", "RIGHT_ANKLE");
		sMayaNameToAliceNameMap.put("spine1", "SPINE_BASE");
		sMayaNameToAliceNameMap.put("spine2", "SPINE_MIDDLE");
		sMayaNameToAliceNameMap.put("spine3", "SPINE_UPPER");
		sMayaNameToAliceNameMap.put("neck", "NECK");
		sMayaNameToAliceNameMap.put("head", "HEAD");
		sMayaNameToAliceNameMap.put("clav_R", "RIGHT_CLAVICLE");
		sMayaNameToAliceNameMap.put("shoulder_R", "RIGHT_SHOULDER");
		sMayaNameToAliceNameMap.put("elbow_R", "RIGHT_ELBOW");
		sMayaNameToAliceNameMap.put("wrist_R", "RIGHT_WRIST");
		sMayaNameToAliceNameMap.put("clav_L", "LEFT_CLAVICLE");
		sMayaNameToAliceNameMap.put("shoulder_L", "LEFT_SHOULDER");
		sMayaNameToAliceNameMap.put("elbow_L", "LEFT_ELBOW");
		sMayaNameToAliceNameMap.put("wrist_L", "LEFT_WRIST");
		sMayaNameToAliceNameMap.put("eye_L", "LEFT_EYE");
		sMayaNameToAliceNameMap.put("eye_R", "RIGHT_EYE");
		sMayaNameToAliceNameMap.put("eyeLid_L", "LEFT_EYELID");
		sMayaNameToAliceNameMap.put("eyeLid_R", "RIGHT_EYELID");
		sMayaNameToAliceNameMap.put("eyeRid_R", "RIGHT_EYELID");
		sMayaNameToAliceNameMap.put("jaw", "MOUTH");
		sMayaNameToAliceNameMap.put("jaw1", "LOWER_LIP");

		sMayaNameToAliceNameMap.put("ear1_R", "RIGHT_EAR");
		sMayaNameToAliceNameMap.put("ear1_L", "LEFT_EAR");
		sMayaNameToAliceNameMap.put("ear2_R", "RIGHT_EAR_MIDDLE");
		sMayaNameToAliceNameMap.put("ear2_L", "LEFT_EAR_MIDDLE");
		sMayaNameToAliceNameMap.put("ear3_R", "RIGHT_EAR_TIP");
		sMayaNameToAliceNameMap.put("ear3_L", "LEFT_EAR_TIP");

		sMayaNameToAliceNameMap.put("thumb1_L", "LEFT_THUMB");
		sMayaNameToAliceNameMap.put("thumb2_L", "LEFT_THUMB_KNUCKLE");
		sMayaNameToAliceNameMap.put("thumb3_L", "LEFT_THUMB_TIP");
		sMayaNameToAliceNameMap.put("thumb4_L", "LEFT_THUMB_END");
		sMayaNameToAliceNameMap.put("index1_L", "LEFT_INDEX_FINGER");
		sMayaNameToAliceNameMap.put("index2_L", "LEFT_INDEX_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("index3_L", "LEFT_INDEX_FINGER_TIP");
		sMayaNameToAliceNameMap.put("index4_L", "LEFT_INDEX_FINGER_END");
		sMayaNameToAliceNameMap.put("middle1_L", "LEFT_MIDDLE_FINGER");
		sMayaNameToAliceNameMap.put("middle2_L", "LEFT_MIDDLE_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("middle3_L", "LEFT_MIDDLE_FINGER_TIP");
		sMayaNameToAliceNameMap.put("middle4_L", "LEFT_MIDDLE_FINGER_END");
		sMayaNameToAliceNameMap.put("ring1_L", "LEFT_RING_FINGER");
		sMayaNameToAliceNameMap.put("ring2_L", "LEFT_RING_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("ring3_L", "LEFT_RING_FINGER_TIP");
		sMayaNameToAliceNameMap.put("ring4_L", "LEFT_RING_FINGER_END");
		sMayaNameToAliceNameMap.put("pinky1_L", "LEFT_PINKY_FINGER");
		sMayaNameToAliceNameMap.put("pinky2_L", "LEFT_PINKY_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("pinky3_L", "LEFT_PINKY_FINGER_TIP");
		sMayaNameToAliceNameMap.put("pinky4_L", "LEFT_PINKY_FINGER_END");

		sMayaNameToAliceNameMap.put("thumb1_R", "RIGHT_THUMB");
		sMayaNameToAliceNameMap.put("thumb2_R", "RIGHT_THUMB_KNUCKLE");
		sMayaNameToAliceNameMap.put("thumb3_R", "RIGHT_THUMB_TIP");
		sMayaNameToAliceNameMap.put("thumb4_R", "RIGHT_THUMB_END");
		sMayaNameToAliceNameMap.put("index1_R", "RIGHT_INDEX_FINGER");
		sMayaNameToAliceNameMap.put("index2_R", "RIGHT_INDEX_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("index3_R", "RIGHT_INDEX_FINGER_TIP");
		sMayaNameToAliceNameMap.put("index4_R", "RIGHT_INDEX_FINGER_END");
		sMayaNameToAliceNameMap.put("middle1_R", "RIGHT_MIDDLE_FINGER");
		sMayaNameToAliceNameMap.put("middle2_R", "RIGHT_MIDDLE_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("middle3_R", "RIGHT_MIDDLE_FINGER_TIP");
		sMayaNameToAliceNameMap.put("middle4_R", "RIGHT_MIDDLE_FINGER_END");
		sMayaNameToAliceNameMap.put("ring1_R", "RIGHT_RING_FINGER");
		sMayaNameToAliceNameMap.put("ring2_R", "RIGHT_RING_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("ring3_R", "RIGHT_RING_FINGER_TIP");
		sMayaNameToAliceNameMap.put("ring4_R", "RIGHT_RING_FINGER_END");
		sMayaNameToAliceNameMap.put("pinky1_R", "RIGHT_PINKY_FINGER");
		sMayaNameToAliceNameMap.put("pinky2_R", "RIGHT_PINKY_FINGER_KNUCKLE");
		sMayaNameToAliceNameMap.put("pinky3_R", "RIGHT_PINKY_FINGER_TIP");
		sMayaNameToAliceNameMap.put("pinky4_R", "RIGHT_PINKY_FINGER_END");

//		sMayaNameToAliceNameMap.put("tail1", "TAIL");

		// From swimmers
		sMayaNameToAliceNameMap.put("finTail_left", "LEFT_TAIL_FIN");
		sMayaNameToAliceNameMap.put("finTail_right", "RIGHT_TAIL_FIN");
		sMayaNameToAliceNameMap.put("finTail_top", "TOP_TAIL_FIN");
		sMayaNameToAliceNameMap.put("finTail_bottom", "BOTTOM_TAIL_FIN");
		sMayaNameToAliceNameMap.put("finFrontTip_L", "LEFT_FRONT_FIN_TIP");
		sMayaNameToAliceNameMap.put("finFrontTip_R", "RIGHT_FRONT_FIN_TIP");
		sMayaNameToAliceNameMap.put("finFront_R", "RIGHT_FRONT_FIN");
		sMayaNameToAliceNameMap.put("finFront_L", "LEFT_FRONT_FIN");
		sMayaNameToAliceNameMap.put("jawTip", "LOWER_LIP");

	}

	private static Map<String, String> sAutomaticArrayNameToCustomArrayNameMap = new HashMap<String, String>();
	static {
		sAutomaticArrayNameToCustomArrayNameMap.put("WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("ENGINE_WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("ENGINE_WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("COAL_BOX_WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("COAL_BOX_WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("FRONT_WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("FRONT_WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("MIDDLE_WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("MIDDLE_WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("BACK_WHEEL", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("BACK_WHEELS", "WHEELS");
		sAutomaticArrayNameToCustomArrayNameMap.put("TAIL", "TAIL");
	}

	private static String[] sArrayNamesToSkip = {
		"RIGHT_RING",
		"RIGHT_THUMB",
		"RIGHT_INDEX",
		"RIGHT_PINKY",
		"RIGHT_MIDDLE",
		"LEFT_RING",
		"LEFT_THUMB",
		"LEFT_INDEX",
		"LEFT_PINKY",
		"LEFT_MIDDLE",
		"DOOR",
		"JAW"
	};

	private static List<String> sDefaultArraysToExposeFirstElementOf = new ArrayList<String>();
	static {
		sDefaultArraysToExposeFirstElementOf.add("TAIL");
		sDefaultArraysToExposeFirstElementOf.add("NECK");
	}

	public static String getAliceEnumNameForMayaJoint( String modelJointName )
	{
		List<String> nameParts = new LinkedList<String>();
		String[] parts = AliceResourceClassUtilities.fullStringSplit(modelJointName);
		boolean hasRight = false;
		boolean hasLeft = false;
		boolean hasBack = false;
		boolean hasFront = false;
		boolean hasTop = false;
		boolean hasBottom = false;
		for (String part : parts)
		{
			if (part.equalsIgnoreCase("l"))
			{
				hasLeft = true;
			}
			else if (part.equalsIgnoreCase("r"))
			{
				hasRight = true;
			}
			else if (part.equalsIgnoreCase("f"))
			{
				hasFront = true;
			}
			else if (part.equalsIgnoreCase("b"))
			{
				if (hasBack || hasFront) { //Check to see if this is after a front/back prefix
					hasBottom = true;
				}
				else {
					hasBack = true;
				}
			}
			else if (part.equalsIgnoreCase("t"))
			{
				hasTop = true;
			}
			else if (part.length() > 0)
			{
				nameParts.add(part);
			}
		}
		if (hasRight)
		{
			nameParts.add(0, "RIGHT");
		}
		else if (hasLeft)
		{
			nameParts.add(0, "LEFT");
		}
		if (hasTop) {
			nameParts.add(0, "TOP");
		}
		else if (hasBottom) {
			nameParts.add(0, "BOTTOM");
		}
		if (hasFront)
		{
			nameParts.add(0, "FRONT");
		}
		else if (hasBack)
		{
			nameParts.add(0, "BACK");
		}
		return getEnumNameForNameParts(nameParts);
	}

	public static String getEnumNameForNameParts(List<String> nameParts) {
		return getEnumNameForNameParts(nameParts.toArray(new String[nameParts.size()]));
	}

	public static String getEnumNameForNameParts(String[] nameParts) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<nameParts.length; i++)
		{
			if (i != 0)
			{
				sb.append("_");
			}
			sb.append(nameParts[i].toUpperCase());
		}
		return sb.toString();
	}

	public static boolean shouldPreserveName(String name, List<String> namesToPreserve) {
		for (String s : namesToPreserve) {
			if (name.toLowerCase().contains(s.toLowerCase())){
				return true;
			}
		}
		return false;
	}

	public static String getAliceJointNameForMayaJointName(String mayaName, boolean preserveName) {
		String aliceName = mayaName;
		if (!preserveName) {
			if (sMayaNameToAliceNameMap.containsKey(mayaName)) {
				aliceName = sMayaNameToAliceNameMap.get(mayaName);
			} else {
				System.out.println("No alice name found for " + mayaName);
				aliceName = getAliceEnumNameForMayaJoint(mayaName);
				System.out.println("No alice name found for " + mayaName
						+ ", autogen name: " + aliceName);
			}
		}
		else {
			String[] parts = AliceResourceClassUtilities.fullStringSplit(mayaName);
			aliceName = getEnumNameForNameParts(parts);
			System.out.println("Preserving name for " + mayaName
					+ ", autogen name: " + aliceName);
		}
		return aliceName;
	}

	public static String getAliceArrayJointName(String jointName, List<String> jointArray, String arrayName) {
		int index = jointArray.lastIndexOf(jointName);
		int digitCount = Integer.toString(jointArray.size() - 1).length();
		String suffix = String.format("%0"+Integer.toString(digitCount)+"d", index);
		if (arrayName != null) {
			return arrayName + "_" + suffix;
		}
		else {
			String baseName = ModelResourceExporter.getArrayNameForJoint(jointName, null, null);
			return baseName + "_" + suffix;
		}
	}

	public static Map<String, String> getDefaultCustomArrayNameMap() {
		return sAutomaticArrayNameToCustomArrayNameMap;
	}

	public static List<String> getDefaultArraysToExposeFirstElementOf() {
		return sDefaultArraysToExposeFirstElementOf;
	}

	public static String[] getDefaultArrayNamesToSkip() {
		return sArrayNamesToSkip;
	}
}
