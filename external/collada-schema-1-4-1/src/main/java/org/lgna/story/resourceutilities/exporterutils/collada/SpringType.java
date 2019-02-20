
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SpringType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="SpringType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN">
 *     &lt;enumeration value="LINEAR"/>
 *     &lt;enumeration value="ANGULAR"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "SpringType", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum SpringType {

    LINEAR,
    ANGULAR;

    public String value() {
        return name();
    }

    public static SpringType fromValue(String v) {
        return valueOf(v);
    }

}
