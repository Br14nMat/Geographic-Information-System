package test;

import exception.InvalidFormatException;
import junit.framework.TestCase;
import model.Controller;

public class TestProgram extends TestCase {

    Controller controller;

    public void test() throws InvalidFormatException {

        controller = new Controller();

        String par = "(a, s, a, s, s, a)";

        par = par.substring(1, par.length() - 1);

        par =  par.replace(", ", " ");

        String spl[] =par.split(" ");

        System.out.println(par);

        //assertEquals(spl.length, 6);

        //assertTrue(controller.validateInsert("INSERT INTO countries(id, name, population, countryCode) VALUES ('6ec3e8ec-3dd0-11ed-b878-0242ac120002', 'Colombia', 50.2, '+57')"));
        //assertTrue(controller.validateInsert("INSERT INTO cities(id, name, countryID, population) VALUES 'e4aa04f6-3dd0-11ed-b878-0242ac120002', 'Cali', '6ec3e8ec-3dd0-11ed-b878-0242ac120002', 2.2)"));

        String [] parts = "SELECT * FROM countries".split(" WHERE ");

        for (String part : parts) {
            System.out.println(part);
        }

    }


}
