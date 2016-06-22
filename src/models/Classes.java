package models;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
public class Classes {
    List<Class> classes;
    @XmlElementWrapper
    @XmlElement
    public List<Class> getClasses(){
        return classes;
    }
}
