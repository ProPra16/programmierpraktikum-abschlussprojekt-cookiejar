package models;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
public class Exercises {

    public List<Exercise> getExercises(File file){
        List<Exercise> exercises = new ArrayList<>();
        System.out.println(file.getAbsoluteFile());

        String description;
        List<String> classes = new ArrayList<>();
        List<String> tests = new ArrayList<>();
        try {
            System.out.println(file.length());
            InputStream is = new FileInputStream(file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);
            Exercise exercise = new Exercise("");
            while (reader.hasNext()) {
                if(reader.getEventType() == XMLStreamConstants.ENTITY_DECLARATION){
                    if(reader.getLocalName().equals("exercise")){
                        exercise = new Exercise(reader.getElementText());
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("description")) {
                        description = reader.getElementText();
                        exercise.setDescription(description);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("class")) {
                        String temp = reader.getElementText();
                        classes.add(temp);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("test")) {
                        String temp = reader.getElementText();
                        tests.add(temp);
                    }
                }
                if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                    if(reader.getLocalName().equals("exercise")){
                        exercise.setClasses(classes);
                        exercise.setTests(tests);
                        exercises.add(exercise);
                    }
                }
                reader.next();
            }
        } catch(FileNotFoundException fnfe){
            System.out.println("This file does not exist.");
        } catch(XMLStreamException xmlse){
            System.out.println();
        } catch(NullPointerException npe){
            System.out.println("The file is broken");
        }
        return exercises;
    }
}
