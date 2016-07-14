
import controller.XMLLoader;
import models.Exercise;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

public class XMLLoaderTest {
    XMLLoader xmlLoader;
    List<Exercise> exercises;

    @Before
    public void setUp() {
        xmlLoader = new XMLLoader();
        File file = new File("src/test/java/testfiles/exercisesXMLTest.xml");
        exercises = xmlLoader.getExercises(file);
    }

    @Test
    public void testClassesSize() {
        assertEquals(2, exercises.get(0).getClasses().size());
    }

    @Test
    public void testTestsSize() {
        assertEquals(1, exercises.get(0).getTests().size());
    }

    @Test
    public void testName() {
        assertEquals("TestExercise", exercises.get(0).getName());
    }

    @Test
    public void testDescription() {
        assertEquals("description", exercises.get(0).getDescription());
    }

    @Test
    public void testIdentifier() {
        assertEquals("ident123", exercises.get(0).getIdentifier());
    }

    @Test
    public void checkForClassesAndCodeClass1() {
        assertEquals(true, exercises.get(0).getClasses().get(0).getName().equals("TestClass1"));
        assertEquals(true, exercises.get(0).getClasses().get(0).getCode().contains("TestClass1"));
    }

    @Test
    public void checkForClassesAndCodeClass2() {
        assertEquals(true, exercises.get(0).getClasses().get(1).getName().equals("TestClass2"));
        assertEquals(true, exercises.get(0).getClasses().get(1).getCode().contains("TestClass2"));
    }

    @Test
    public void checkForTestsAndCodeTest1() {
        assertEquals(true, exercises.get(0).getTests().get(0).getName().equals("Test1"));
        assertEquals(true, exercises.get(0).getTests().get(0).getCode().contains("Test1"));
    }

    @Test
    public void testForOneExercise() {
        assertEquals(1, exercises.size());
    }
}
