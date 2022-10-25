package model;

import exception.CountryNotFoundException;
import exception.InvalidFormatException;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<City> cities;
    private List<Country> countries;
    public Controller(){
        cities = new ArrayList<>();
        countries = new ArrayList<>();
    }

    public void executeCommand(String command) throws InvalidFormatException, CountryNotFoundException {

        String [] commandParts = command.split(" ");

        switch (commandParts[0]){

            case "INSERT":
                if(validateInsert(command)) registerData(command);
                break;
            case "SELECT":

                break;
            case "DELETE":

                break;

            default:
                break;

        }


    }

    public void registerData(String command) throws CountryNotFoundException {

        String [] parts = command.split(" VALUES ");

        String aux = parts[1].substring(1, parts[1].length() - 1);
        aux =  aux.replace(", ", " ");
        aux = aux.replace("'", "");

        String[] parameters = aux.split(" ");

        if(parts[1].contains("countries") && !countryRegistered(parameters[0])){

            countries.add(new Country(
                    parameters[0],
                    parameters[1],
                    Double.parseDouble(parameters[2]),
                    parameters[3]));

        }

        if(parts[1].contains("cities") && !cityRegistered(parameters[0])){

            if(countryRegistered(parameters[2])){

                cities.add(new City(
                        parameters[0],
                        parameters[1],
                        parameters[2],
                        Integer.parseInt(parameters[3])
                ));

            }else {
                throw new CountryNotFoundException("Error 404: Country Not Found");
            }

        }

    }

    public String listData(String command){
        return "";
    }

    public boolean validateInsert(String command) throws InvalidFormatException {

        boolean isValid = false;

        String[] parts = command.split(" VALUES ");

        if(!parts[0].equals("INSERT INTO countries(id, name, population, countryCode)") &&
                !parts[0].equals("INSERT INTO cities(id, name, countryID, population)") &&
                parts.length != 2)
            throw new InvalidFormatException("Invalid insert command");

        if(!parts[1].startsWith("(") && !parts[1].endsWith(")"))
            throw new InvalidFormatException("Misplaced parentheses");

        String aux = parts[1].substring(1, parts[1].length() - 1);
        aux =  aux.replace(", ", " ");

        String [] parameters = aux.split(" ");

        if(parameters.length != 4)
            throw new InvalidFormatException("Incorrect parameter number");

        if(parts[0].contains("countries")){
            isValid = isValidString(parameters[0]) &&
                    isValidString(parameters[1]) &&
                    isANumber(parameters[2]) &&
                    isValidString(parameters[3]);
        }else {
            isValid = isValidString(parameters[0]) &&
                    isValidString(parameters[1]) &&
                    isValidString(parameters[2]) &&
                    isANumber(parameters[3]);
        }

        return isValid;
    }

    public boolean validateSelect(String command) throws InvalidFormatException {

        boolean isValid = true;

        String [] parts = command.split(" WHERE ");

        if(!parts[0].equals("SELECT * FROM countries")
        && !parts[0].equals("SELECT * FROM cities")
        && parts.length != 2 && parts.length != 1)
            throw new InvalidFormatException("Invalid select command");

        if(parts.length == 2){

            String[] parameters = parts[1].split(" ");

            if(parameters.length != 3){
                throw new InvalidFormatException("Incorrect parameter number");
            }

            if(!parameters[1].equals("=") && !parameters[1].equals(">") && !parameters[1].equals("<"))
                throw new InvalidFormatException("Invalid operator");

            if(parts[0].contains("countries")){

                if(!parameters[0].equals("id") &&
                        !parameters[0].equals("name") &&
                        !parameters[0].equals("population") &&
                        !parameters[0].equals("countryCode"))
                    throw new InvalidFormatException("Invalid comparator");

                validateComparison(parameters, !parameters[0].equals("population"));


            }

            if(parts[0].contains("cities")){

                if(!parameters[0].equals("id") &&
                        !parameters[0].equals("name") &&
                        !parameters[0].equals("countryID") &&
                        !parameters[0].equals("population"))
                    throw new InvalidFormatException("Invalid comparator");

                validateComparison(parameters, !parameters[0].equals("population"));

            }

        }

        return isValid;

    }

    public boolean isValidString(String str){
        return str.startsWith("'") && str.endsWith("'");
    }

    public boolean isANumber(String str){

        boolean isANumber = true;

        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            isANumber = false;
        }

        return isANumber;
    }

    public void validateComparison(String [] parameters, boolean isString) throws InvalidFormatException {

        if(isString && !parameters[1].equals("=") && !isValidString(parameters[2]))
            throw new InvalidFormatException("Invalid " + parameters[0] + " comparision");

        if(!isString && !parameters[1].equals(">") && !parameters[1].equals("<") & !isANumber(parameters[2]))
            throw new InvalidFormatException("Invalid " + parameters[0] + " comparision");

    }

    public boolean countryRegistered(String id){

        boolean flag = false;

        for(int i = 0; i < countries.size() && !flag; i++)
            if(countries.get(i).getId().equals(id)) flag = true;

        return flag;
    }

    public boolean cityRegistered(String id){

        boolean flag = false;

        for(int i = 0; i < countries.size() && !flag; i++)
            if(cities.get(i).getId().equals(id)) flag = true;

        return flag;
    }


}
