package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Exercises {
    List<Exercise> exercises;

    @XmlElementWrapper
    @XmlElement
    public List<Exercise> getExercise(){
        return exercises;
    }
}
