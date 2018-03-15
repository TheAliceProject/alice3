
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for gl_shade_model_type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="gl_shade_model_type">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FLAT"/>
 *     &lt;enumeration value="SMOOTH"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "gl_shade_model_type", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum GlShadeModelType {

    FLAT,
    SMOOTH;

    public String value() {
        return name();
    }

    public static GlShadeModelType fromValue(String v) {
        return valueOf(v);
    }

}
