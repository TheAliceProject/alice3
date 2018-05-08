
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_material_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_material_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="EMISSION"/>
 *     &lt;enumeration value="AMBIENT"/>
 *     &lt;enumeration value="DIFFUSE"/>
 *     &lt;enumeration value="SPECULAR"/>
 *     &lt;enumeration value="AMBIENT_AND_DIFFUSE"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_material_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlMaterialType {

    EMISSION,
    AMBIENT,
    DIFFUSE,
    SPECULAR,
    AMBIENT_AND_DIFFUSE;

    public String value() {
        return name();
    }

    public static GlMaterialType fromValue(String v) {
        return valueOf(v);
    }

}
