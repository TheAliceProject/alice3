
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_light_model_color_control_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_light_model_color_control_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="SINGLE_COLOR"/>
 *     &lt;enumeration value="SEPARATE_SPECULAR_COLOR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_light_model_color_control_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlLightModelColorControlType {

    SINGLE_COLOR,
    SEPARATE_SPECULAR_COLOR;

    public String value() {
        return name();
    }

    public static GlLightModelColorControlType fromValue(String v) {
        return valueOf(v);
    }

}
