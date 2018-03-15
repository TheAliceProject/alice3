
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Common_profile_input.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="Common_profile_input">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="BINORMAL"/>
 *     &lt;enumeration value="COLOR"/>
 *     &lt;enumeration value="CONTINUITY"/>
 *     &lt;enumeration value="IMAGE"/>
 *     &lt;enumeration value="IN_TANGENT"/>
 *     &lt;enumeration value="INPUT"/>
 *     &lt;enumeration value="INTERPOLATION"/>
 *     &lt;enumeration value="INV_BIND_MATRIX"/>
 *     &lt;enumeration value="JOINT"/>
 *     &lt;enumeration value="LINEAR_STEPS"/>
 *     &lt;enumeration value="MORPH_TARGET"/>
 *     &lt;enumeration value="MORPH_WEIGHT"/>
 *     &lt;enumeration value="NORMAL"/>
 *     &lt;enumeration value="OUTPUT"/>
 *     &lt;enumeration value="OUT_TANGENT"/>
 *     &lt;enumeration value="POSITION"/>
 *     &lt;enumeration value="TANGENT"/>
 *     &lt;enumeration value="TEXBINORMAL"/>
 *     &lt;enumeration value="TEXCOORD"/>
 *     &lt;enumeration value="TEXTANGENT"/>
 *     &lt;enumeration value="UV"/>
 *     &lt;enumeration value="VERTEX"/>
 *     &lt;enumeration value="WEIGHT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "Common_profile_input", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum CommonProfileInput {

    BINORMAL,
    COLOR,
    CONTINUITY,
    IMAGE,
    IN_TANGENT,
    INPUT,
    INTERPOLATION,
    INV_BIND_MATRIX,
    JOINT,
    LINEAR_STEPS,
    MORPH_TARGET,
    MORPH_WEIGHT,
    NORMAL,
    OUTPUT,
    OUT_TANGENT,
    POSITION,
    TANGENT,
    TEXBINORMAL,
    TEXCOORD,
    TEXTANGENT,
    UV,
    VERTEX,
    WEIGHT;

    public String value() {
        return name();
    }

    public static CommonProfileInput fromValue(String v) {
        return valueOf(v);
    }

}
