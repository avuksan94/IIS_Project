//
// This file was generated by the Eclipse Implementation of JAXB, v3.0.0 
// See https://eclipse-ee4j.github.io/jaxb-ri 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2024.03.12 at 06:57:05 PM CET 
//


package com.avtest.soap_web_servicecf;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;
import lombok.Getter;
import lombok.Setter;


/**
 * <p>Java class for WorkoutResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="WorkoutResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="workout" type="{http://avtest.com/soap-web-serviceCF}XMLWorkoutResponseType" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkoutResponse", propOrder = {
        "workouts"
})
@Getter
@Setter
public class WorkoutResponseType {

    @XmlElementWrapper(name = "workouts")
    @XmlElement(name = "workout")
    private List<XMLWorkoutResponseType> workouts;
    /**
     * Gets the value of the workout property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the Jakarta XML Binding object.
     * This is why there is not a <CODE>set</CODE> method for the workout property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWorkout().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLWorkoutResponseType }
     * 
     * 
     */

}
