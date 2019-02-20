
package org.lgna.story.resourceutilities.exporterutils.collada;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for cg_pipeline_stage.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="cg_pipeline_stage">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="VERTEX"/>
 *     &lt;enumeration value="FRAGMENT"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "cg_pipeline_stage", namespace = "http://www.collada.org/2005/11/COLLADASchema")
@XmlEnum
public enum CgPipelineStage {

    VERTEX,
    FRAGMENT;

    public String value() {
        return name();
    }

    public static CgPipelineStage fromValue(String v) {
        return valueOf(v);
    }

}
