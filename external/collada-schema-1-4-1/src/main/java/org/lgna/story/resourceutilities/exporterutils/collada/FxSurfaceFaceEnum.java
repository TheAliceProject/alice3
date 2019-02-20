
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for fx_surface_face_enum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="fx_surface_face_enum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="POSITIVE_X"/>
 *     &lt;enumeration value="NEGATIVE_X"/>
 *     &lt;enumeration value="POSITIVE_Y"/>
 *     &lt;enumeration value="NEGATIVE_Y"/>
 *     &lt;enumeration value="POSITIVE_Z"/>
 *     &lt;enumeration value="NEGATIVE_Z"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "fx_surface_face_enum", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum FxSurfaceFaceEnum {

    POSITIVE_X,
    NEGATIVE_X,
    POSITIVE_Y,
    NEGATIVE_Y,
    POSITIVE_Z,
    NEGATIVE_Z;

    public String value() {
        return name();
    }

    public static FxSurfaceFaceEnum fromValue(String v) {
        return valueOf(v);
    }

}
