package model;

import exception.CountryNotFoundException;
import exception.InvalidFormatException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

        String data = "";
        String[] parts = command.split(" VALUES ");



        return data;

    }

    public List<Country> listCountries(String criteria){

        List<Country> list = null;
        String [] parts = criteria.split(" ");

        if(criteria.isEmpty()) list = countries;

        if(parts[0].equals("population")){

            int number = Integer.parseInt(parts[2]);

            if(parts[1].equals(">")){
                list = countries.stream()
                        .filter(country -> country.getPopulation() > number)
                        .collect(Collectors.toList());
            }else {
                list = countries.stream()
                        .filter(country -> country.getPopulation() > number)
                        .collect(Collectors.toList());
            }

        }

        if(parts[0].equals("id")){
            list = countries.stream()
                    .filter(country -> country.getId().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        if(parts[0].equals("name")){
            list = countries.stream()
                    .filter(country -> country.getName().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        if(parts[0].equals("countryCode")){
            list = countries.stream()
                    .filter(country -> country.getCountryCode().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        return list;

    }

    public List<City> listCities(String criteria){

        List<City> list = null;
        String [] parts = criteria.split(" ");

        if(criteria.isEmpty()) list = cities;

        if(parts[0].equals("population")){

            int number = Integer.parseInt(parts[2]);

            if(parts[1].equals(">")){
                list = cities.stream().filter(e -> e.getPopulation() > number).collect(Collectors.toList());
            }else {
                list = cities.stream()
                        .filter(city -> city.getPopulation() < number)
                        .collect(Collectors.toList());
            }

            if(parts[0].equals("id")){
                list = cities.stream()
                        .filter(city -> city.getId().equals(parts[2]))
                        .collect(Collectors.toList());
            }


        }

        if(parts[0].equals("id")){
            list = cities.stream()
                    .filter(city -> city.getId().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        if(parts[0].equals("name")){
            list = cities.stream()
                    .filter(city -> city.getName().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        if(parts[0].equals("countryCode")){
            list = cities.stream()
                    .filter(city -> city.getCountryID().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        return list;

    }

    public List<Country> orderCountryData(List<Country> list, String orderBy){

        if(orderBy.equals("id")) list.sort(Comparator.comparing(Country::getId));

        if(orderBy.equals("name")) list.sort(Comparator.comparing(Country::getName));

        if(orderBy.equals("population")) list.sort(Comparator.comparing(Country::getPopulation));

        if(orderBy.equals("countryCode")) list.sort(Comparator.comparing(Country::getCountryCode));

        return list;

    }

    public List<City> orderCityData(List<City> list, String orderBy){

        if(orderBy.equals("id")) list.sort(Comparator.comparing(City::getId));

        if(orderBy.equals("name")) list.sort(Comparator.comparing(City::getName));

        if(orderBy.equals("countryID")) list.sort(Comparator.comparing(City::getCountryID));

        if(orderBy.equals("population")) list.sort(Comparator.comparing(City::getPopulation));

        return list;
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

        if(!parts[0].equals("SELECT * FROM countries") &&
                !parts[0].equals("SELECT * FROM cities") &&
                parts.length != 2 && parts.length != 1)
            throw new InvalidFormatException("Invalid select command");

        if(parts.length == 2){

            String[] parameters = parts[1].split(" ");

            if(parameters.length != 3){
                throw new InvalidFormatException("Invalid condition");
            }

            if(!parameters[1].equals("=") && !parameters[1].equals(">") && !parameters[1].equals("<"))
                throw new InvalidFormatException("Invalid operator");

            if(parts[0].contains("countries")){

                if(!parameters[0].equals("id") &&
                        !parameters[0].equals("name") &&
                        !parameters[0].equals("population") &&
                        !parameters[0].equals("countryCode"))
                    throw new InvalidFormatException("Invalid comparator");

                if(parameters[0].equals("population") && !isANumber(parameters[2]))
                    throw new InvalidFormatException(parameters[2] + " is not a number");

                if(!parameters[0].equals("population") && !isValidString(parameters[2]))
                    throw new InvalidFormatException(parameters[2] + " is not a valid string");

                validateComparison(parameters, !parameters[0].equals("population"));

            }

            if(parts[0].contains("cities")){

                if(!parameters[0].equals("id") &&
                        !parameters[0].equals("name") &&
                        !parameters[0].equals("countryID") &&
                        !parameters[0].equals("population"))
                    throw new InvalidFormatException("Invalid comparator");

                if(parameters[0].equals("population") && !isANumber(parameters[2]))
                    throw new InvalidFormatException(parameters[2] + " is not a number");

                if(!parameters[0].equals("population") && !isValidString(parameters[2]))
                    throw new InvalidFormatException(parameters[2] + " is not a valid string");

                validateComparison(parameters, !parameters[0].equals("population"));

            }

        }


        return isValid;

    }
    public boolean validateOrderBy(String command) throws InvalidFormatException {

        boolean isValid = true;

        String [] parts = command.split(" ORDER BY ");

        if(parts.length != 2)
            throw new InvalidFormatException("Invalid order by expression");

        isValid = validateSelect(parts[0]);

        if(parts[0].contains("countries") &&
                !parts[1].equals("id") &&
                !parts[1].equals("name") &&
                !parts[1].equals("population") &&
                !parts[1].equals("countryCode"))
            throw new InvalidFormatException("Invalid order parameter");

        if(parts[0].contains("cities") &&
                !parts[1].equals("id") &&
                !parts[1].equals("name") &&
                !parts[1].equals("countryID") &&
                !parts[1].equals("population"))
            throw new InvalidFormatException("Invalid order parameter");

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
