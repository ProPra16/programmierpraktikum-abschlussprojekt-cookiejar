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

        try {
            InputStream is = new FileInputStream(file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(is);

            exercises = getExerciseList(file);
            if(exercises.isEmpty()) throw new NullPointerException();

            for(int j = 0; j < exercises.size(); j++){
                List<String> classes = new ArrayList<>();
                List<String> tests = new ArrayList<>();
                exercises.get(j).setTests(tests);
                exercises.get(j).setClasses(classes);
            }

            int i = 0;
            while (reader.hasNext()) { //Then fill exercises with content
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("description")) {
                        String temp = reader.getElementText();
                        exercises.get(i).setDescription(temp);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("class")) {
                        String temp = reader.getElementText();
                        temp = temp.replace("                ", "");
                        exercises.get(i).addClasses(temp);
                    }
                }
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("test")) {
                        String temp = reader.getElementText();
                        temp = temp.replace("                ", "");
                        exercises.get(i).addTests(temp);
                    }
                }
                if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                    if(reader.getLocalName().equals("exercise")){
                        i++;
                    }
                }
                reader.next(); //delete whitespace!
            }
        } catch(FileNotFoundException fnfe){
            System.out.println("This file does not exist.");
        } catch(XMLStreamException xmlse){
            System.out.println();
        } catch(NullPointerException npe){
            System.out.println("The file is broken.");
        }
        return exercises;
    }

    private List<Exercise> getExerciseList(File file) throws FileNotFoundException, XMLStreamException{
        InputStream is = new FileInputStream(file);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);
        List<Exercise> exercises = new ArrayList<>();

        while(reader.hasNext()){ //First check for all Exercises
            if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                if(reader.getLocalName().equals("exercise")){
                    Exercise exercise = new Exercise(reader.getLocalName());
                    exercises.add(exercise);
                }
            }
            reader.next();
        }
        return exercises;
    }
}
