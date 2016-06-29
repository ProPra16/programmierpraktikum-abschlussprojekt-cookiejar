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

            exercises = getExerciseList(file);//CHECK FOR EXERCISES
            if(exercises.isEmpty()) throw new NullPointerException();

            for(int j = 0; j < exercises.size(); j++){
                List<Class> classes = new ArrayList<>();
                List<Test> tests = new ArrayList<>();
                exercises.get(j).setTests(tests);
                exercises.get(j).setClasses(classes);
            }

            int i = 0;
            while (reader.hasNext() && i < exercises.size()) { //FILL WITH CONTENT
                if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    if (reader.getLocalName().equals("name")) {
                        String temp = reader.getElementText();
                        exercises.get(i).setName(temp);
                    }
                    if (reader.getLocalName().equals("description")) {
                        String temp = reader.getElementText();
                        exercises.get(i).setDescription(temp);
                    }
                    //GET CLASSES FOR ONE EXERCISE
                    if(reader.getLocalName().equals("classes")){
                        Class class1 = new Class();
                        while(reader.hasNext()){
                            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (reader.getLocalName().equals("className")) {
                                    String name = reader.getElementText();
                                    class1.setName(name);
                                }
                            }
                            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (reader.getLocalName().equals("class")) {
                                    String temp = reader.getElementText();
                                    temp = temp.replace("  ", "");
                                    class1.setCode(temp);
                                    exercises.get(i).addClasses(class1);
                                    class1 = new Class();
                                }
                            }
                            if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                                if(reader.getLocalName().equals("classes")){
                                    break;
                                }
                            }
                            reader.next();
                        }
                    }
                    //GET TESTS FOR ONE EXERCISE
                    if(reader.getLocalName().equals("tests")){
                        Test test = new Test();
                        while(reader.hasNext()){
                            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (reader.getLocalName().equals("testName")) {
                                    String name = reader.getElementText();
                                    test.setName(name);
                                }
                            }
                            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                                if (reader.getLocalName().equals("test")) {
                                    String temp = reader.getElementText();
                                    temp = temp.replace("  ", "");
                                    test.setTest(temp);
                                    exercises.get(i).addTests(test);
                                    test = new Test();
                                }
                            }
                            if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                                if(reader.getLocalName().equals("tests")){
                                    break;
                                }
                            }
                            reader.next();
                        }
                    }
                }
                if(reader.getEventType() == XMLStreamConstants.END_ELEMENT){
                    if(reader.getLocalName().equals("exercise")){
                        i++;
                    }
                }
                reader.next();
            }
        } catch(FileNotFoundException fnfe){
            System.out.println("This file does not exist.");
        } catch(XMLStreamException xmlse){
            System.out.println();
        } catch(NullPointerException npe){
            System.out.println("The file is empty or broken.");
        }
        return exercises;
    }

    private List<Exercise> getExerciseList(File file) throws FileNotFoundException, XMLStreamException{
        InputStream is = new FileInputStream(file);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(is);
        List<Exercise> exercises = new ArrayList<>();

        while(reader.hasNext()){
            if(reader.getEventType() == XMLStreamConstants.START_ELEMENT){
                if(reader.getLocalName().equals("exercise")){
                    Exercise exercise = new Exercise();
                    exercises.add(exercise);
                }
            }
            reader.next();
        }
        return exercises;
    }
}
