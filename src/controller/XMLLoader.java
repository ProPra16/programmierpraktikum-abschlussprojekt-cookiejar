package controller;

import models.ClassStruct;
import models.Exercise;

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
public class XMLLoader {
    XMLStreamReader reader;
    List<Exercise> exercises = new ArrayList<>();

    public List<Exercise> getExercises(File file){
        try {
            InputStream is = new FileInputStream(file);
            XMLInputFactory factory = XMLInputFactory.newInstance();
            reader = factory.createXMLStreamReader(is);

            exercises = getExerciseList(file);//CHECK FOR EXERCISES
            if(exercises.isEmpty()) throw new NullPointerException();

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
                    if (reader.getLocalName().equals("identifier")) {
                        String temp = reader.getElementText();
                        exercises.get(i).setIdentifier(temp);
                    }
                    //GET CLASSES FOR THIS EXERCISE
                    if(reader.getLocalName().equals("classes")){
                        exercises.get(i).setClasses(getClassStructList(false));
                    }
                    //GET TESTS FOR THIS EXERCISE
                    if(reader.getLocalName().equals("tests")) {
                        exercises.get(i).setTests(getClassStructList(true));
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

    public List<ClassStruct> getClassStructList(boolean isTest) throws XMLStreamException{
        List<ClassStruct> classStructs = new ArrayList<>();
        ClassStruct classStruct = new ClassStruct();
        classStruct.setTest(isTest);
        while(reader.hasNext()) {
            if (reader.getEventType() == XMLStreamConstants.START_ELEMENT) {
                if (reader.getLocalName().equals("testName") || reader.getLocalName().equals("className")) {
                    String name = reader.getElementText();
                    classStruct.setName(name);
                }
                if (reader.getLocalName().equals("test") || reader.getLocalName().equals("class")) {
                    String temp = reader.getElementText();
                    classStruct.setCode(removeExcess(temp));
                    classStructs.add(classStruct);

                    classStruct = new ClassStruct();
                    classStruct.setTest(isTest);
                }
            }
            if (reader.getEventType() == XMLStreamConstants.END_ELEMENT) {//write classstruct in xml ?
                if (reader.getLocalName().equals("tests") || reader.getLocalName().equals("classes")) {
                    return classStructs;
                }
            }
            reader.next();
        }
        return classStructs;
    }

    private String removeExcess(String string){
        string = string.replace("  ", "");
        string = string.trim();
        return string;
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
