package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Exercises {
    Exercise exercise;
    @XmlElement
    public Exercise getExercise(){
        return exercise;
    }
}
