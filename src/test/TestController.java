package test;

import exception.CountryNotFoundException;
import exception.ExistingRecord;
import exception.InvalidFormatException;
import junit.framework.TestCase;
import model.Controller;

public class TestController extends TestCase {

    private Controller controller;

    /**
     * Escenario: Aplicación con Colombia registrada.
     */
    public void setUpStage1(){

        controller = new Controller();

        String command1 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('COL', 'Colombia', 50.2, '+57')";

        controller.executeCommand(command1);

    }

    /**
     * Escenario: Aplicación con Colombia y varias ciudadas de Colombia registradas.
     */
    public void setUpStage2(){

        controller = new Controller();

        String command1 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('COL', 'Colombia', 50.2, '+57')";

        String command2 = "INSERT INTO cities(id, name, countryID, population) VALUES ('CAL_COL', 'Cali', 'COL', 2.2)";
        String command3 = "INSERT INTO cities(id, name, countryID, population) VALUES ('MED_COL', 'Medellin', 'COL', 3.2)";
        String command4 = "INSERT INTO cities(id, name, countryID, population) VALUES ('BOG_COL', 'Bogota', 'COL', 8.4)";
        String command5 = "INSERT INTO cities(id, name, countryID, population) VALUES ('BQL_COL', 'Barranquilla', 'COL', 1.4)";
        String command6 = "INSERT INTO cities(id, name, countryID, population) VALUES ('PAS_COL', 'Pasto', 'COL', 1.2)";
        String command7 = "INSERT INTO cities(id, name, countryID, population) VALUES ('PER_COL', 'Pereira', 'COL', 0.9)";
        String command8 = "INSERT INTO cities(id, name, countryID, population) VALUES ('MAN_COL', 'Manizales', 'COL', 2.8)";

        controller.executeCommand(command1);
        controller.executeCommand(command2);
        controller.executeCommand(command3);
        controller.executeCommand(command4);
        controller.executeCommand(command5);
        controller.executeCommand(command6);
        controller.executeCommand(command7);
        controller.executeCommand(command8);

    }

    /**
     * Escenario: Aplicación con paises varios paises registrados. Algunos con mas de 100 millones de habitantes.
     */
    public void setUpStage3(){

        controller = new Controller();

        String command1 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('CHI', 'China', 1400.5, '+86')";
        String command2 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('IND', 'India', 1389.6, '+91')";
        String command3 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('BRA', 'Brasil', 217.8, '+55')";
        String command4 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('RUS', 'Rusia', 142.3, '+7')";
        String command5 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('COL', 'Colombia', 50.2, '+57')";
        String command6 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('PER', 'Peru', 33.6, '+51')";
        String command7  = "INSERT INTO countries(id, name, population, countryCode) VALUES ('ECU', 'Ecuador', 17.9, '+593')";
        String command8  = "INSERT INTO countries(id, name, population, countryCode) VALUES ('CHI', 'Chile', 19.2, '+56')";

        controller.executeCommand(command1);
        controller.executeCommand(command2);
        controller.executeCommand(command3);
        controller.executeCommand(command4);
        controller.executeCommand(command5);
        controller.executeCommand(command6);
        controller.executeCommand(command7);
        controller.executeCommand(command8);

    }

    /**
     * Verificar si un nuevo registro de ciudades efectivamente se agrega a los datos
     */

    public void testInsert1(){
        setUpStage1();

        String command1 = "INSERT INTO cities(id, name, countryID, population) VALUES ('CAL_COL', 'Cali', 'COL', 2.2)";
        String command2 = "INSERT INTO cities(id, name, countryID, population) VALUES ('MED_COL', 'Medellin', 'COL', 3.2)";
        String command3 = "INSERT INTO cities(id, name, countryID, population) VALUES ('BOG_COL', 'Bogota', 'COL', 8.4)";
        String command4 = "INSERT INTO cities(id, name, countryID, population) VALUES ('BQL_COL', 'Barranquilla', 'COL', 1.4)";


        try {
            if(controller.validateInsert(command1))
                controller.registerData(command1);

            if(controller.validateInsert(command2))
                controller.registerData(command2);

            if(controller.validateInsert(command3))
                controller.registerData(command3);

            if(controller.validateInsert(command4))
                controller.registerData(command4);

            assertEquals(4, controller.getCities().size());
            assertTrue(controller.countryRegistered("COL"));
            assertTrue(controller.cityRegistered("CAL_COL"));
            assertTrue(controller.cityRegistered("MED_COL"));
            assertTrue(controller.cityRegistered("BQL_COL"));

        } catch (InvalidFormatException | CountryNotFoundException | ExistingRecord e) {
            e.printStackTrace();
        }

    }

    /**
     * Verificar si un nuevo registro de países efectivamente se agrega a los datos
     */

    public void testInsert2(){
        setUpStage1();

        String command1 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('ESP', 'España', 47.5, '+34')";
        String command2 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('MEX', 'México', 128.9, '+52')";
        String command3 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('FRA', 'Francia', 67.5, '+33')";
        String command4 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('ARG', 'Argentina', 45.8, '+54')";
        String command5 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('ITA', 'Italia', 59.1, '+39')";

        try {
            if(controller.validateInsert(command1))
                controller.registerData(command1);

            if(controller.validateInsert(command2))
                controller.registerData(command2);

            if(controller.validateInsert(command3))
                controller.registerData(command3);

            if(controller.validateInsert(command4))
                controller.registerData(command4);

            if(controller.validateInsert(command5))
                controller.registerData(command5);

            assertEquals(6, controller.getCountries().size());
            assertTrue(controller.countryRegistered("COL"));
            assertTrue(controller.countryRegistered("ESP"));
            assertTrue(controller.countryRegistered("MEX"));
            assertTrue(controller.countryRegistered("FRA"));
            assertTrue(controller.countryRegistered("ARG"));
            assertTrue(controller.countryRegistered("ITA"));

        } catch (InvalidFormatException | CountryNotFoundException | ExistingRecord e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verificar qué pasa si se intenta registrar un país con ID repetido
     */

    public void testInsert3(){
        setUpStage1();

        String command1 = "INSERT INTO countries(id, name, population, countryCode) VALUES ('COL', 'Polombia', 47.5, '+57')";

        try {
            if(controller.validateInsert(command1))
                controller.registerData(command1);

            fail();

        } catch (InvalidFormatException | CountryNotFoundException | ExistingRecord e) {
            System.out.println(e.getMessage());
        }

    }

    /**
     * Verificar qué pasa si se intenta registrar una ciudad con un countryID de un país que NO está registrado en la base de datos
     */

    public void testInsert4(){
        setUpStage1();

        String command1 = "INSERT INTO cities(id, name, countryID, population) VALUES ('BAR_ESP', 'Barcelona', 'ESP', 3.6)";

        try {
            if(controller.validateInsert(command1))
                controller.registerData(command1);

            fail();

        } catch (InvalidFormatException | CountryNotFoundException | ExistingRecord e) {
            System.out.println(e.getMessage());
        }
        
    }

    /**
     * Verificar si puedo listar todas la ciudades de 'Colombia'. Para este test debe tener en sus registros a Colombia y a algunas ciudades de Colombia.
     */

    public void testSelect1(){
        setUpStage2();

        String command1 = "SELECT * FROM cities WHERE countryID = 'COL'";
        String response = "";

        try {
            if(controller.validateSelect(command1))
                response = controller.listData(command1);

            assertFalse(response.isEmpty());

            System.out.println(response);

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verificar si puede ordenar la lista de ciudades por name
     */

    public void testSelect2(){
        setUpStage2();

        String command1 = "SELECT * FROM cities WHERE countryID = 'COL' ORDER BY name";
        String response = "";

        try {
            if(controller.validateOrderBy(command1))
                response = controller.listData(command1);

            assertFalse(response.isEmpty());

            System.out.println(response);

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verificar si puedo ver todos los países con population > 100.
     * Para este test debe garantizar que hayan países registrados con valores de population mayores a 100"
     */

    public void testSelect3(){
        setUpStage3();

        String command1 = "SELECT * FROM countries WHERE population > 100";
        String response = "";

        try {
            if(controller.validateSelect(command1))
                response = controller.listData(command1);

            assertFalse(response.isEmpty());

            System.out.println(response);

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verificar si el comando de eliminar sí es capaz de eliminar un registro por su ID.
     */

    public void testDelete1(){
        setUpStage3();

        String command1 = "DELETE FROM countries WHERE id = 'PER'";
        String command2 = "DELETE FROM countries WHERE id = 'CHI'";
        String command3 = "DELETE FROM countries WHERE id = 'BRA'";

        int size = controller.getCountries().size();

        try {
            if(controller.validateDelete(command1))
                controller.deleteData(command1);

            if(controller.validateDelete(command2))
                controller.deleteData(command2);

            if(controller.validateDelete(command3))
                controller.deleteData(command3);

            System.out.println(controller.executeCommand("SELECT * FROM countries"));

            assertFalse(controller.countryRegistered("PER"));
            assertFalse(controller.countryRegistered("CHI"));
            assertFalse(controller.countryRegistered("BRA"));
            assertEquals(size - 3, controller.getCountries().size());

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            fail();
        }

    }

    /**
     * Verificar si el programa es capaz de eliminar todas las ciudades de Colombia.
     * Para este test debe garantizar que esté registrado Colombia y que hayan algunas ciudades de Colombia registradas"
     */

    public void testDelete2(){
        setUpStage2();

        String command1 = "DELETE FROM cities WHERE countryID = 'COL'";

        try {

            if(controller.validateDelete(command1))
                System.out.println(controller.deleteData(command1));

            assertTrue(controller.executeCommand("SELECT * FROM cities").isEmpty());
            assertEquals(0, controller.getCities().size());

        } catch (InvalidFormatException e) {
            e.printStackTrace();
            fail();
        }

    }

}
