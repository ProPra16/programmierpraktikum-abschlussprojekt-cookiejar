package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exercise {
    public Classes classes;
    public Tests tests;
    @XmlElement
    public Classes getClasses(){
        return classes;
    }

    @XmlElement
    public Tests getTests(){
        return tests;
    }
}
