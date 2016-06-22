package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;


public class Classes {
    List<Class> classes;
    @XmlElementWrapper
    @XmlElement
    public List<Class> getClasses(){
        return classes;
    }
}
