package model;

import com.google.gson.Gson;
import exception.CountryNotFoundException;
import exception.ExistingRecord;
import exception.InvalidFormatException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class Controller {

    private List<City> cities;
    private List<Country> countries;

    public final String COUNTRIES_PATH = "db/countries.json";
    public final String CITIES_PATH = "db/cities.json";

    public Controller(){
        cities = new ArrayList<>();
        countries = new ArrayList<>();
    }

    public String executeCommand(String command){

        String [] commandParts = command.split(" ");
        String response = "";

        try {

            if(commandParts[0].equals("INSERT") && validateInsert(command))
                response = registerData(command);

            if(commandParts[0].equals("SELECT") && !command.contains("ORDER BY") && validateSelect(command))
                response = listData(command);

            if(commandParts[0].equals("SELECT") && command.contains("ORDER BY") && validateOrderBy(command))
                response = listData(command);

            if(commandParts[0].equals("DELETE") && validateDelete(command))
                response = deleteData(command);


        } catch (InvalidFormatException | CountryNotFoundException | ExistingRecord e) {
            response = e.getMessage();
        }

        return response;

    }

    public String registerData(String command) {

        String [] parts = command.split(" VALUES ");
        String response = "";

        String aux = parts[1].substring(1, parts[1].length() - 1);
        aux =  aux.replace(", ", " ");
        aux = aux.replace("'", "");

        String[] parameters = aux.split(" ");

        if(parts[0].contains("countries") && !countryRegistered(parameters[0])){

            countries.add(new Country(
                    parameters[0],
                    parameters[1],
                    Double.parseDouble(parameters[2]),
                    parameters[3]));

            response = parameters[1] + " registered succesfully";

        }

        if(parts[0].contains("cities") && !cityRegistered(parameters[0])){

            cities.add(new City(
                    parameters[0],
                    parameters[1],
                    parameters[2],
                    Double.parseDouble(parameters[3])
            ));

            response = parameters[1] + " registered succesfully";

        }

        return response;

    }

    public String listData(String command){
        command = command.replace("'", "");

        StringBuilder data = new StringBuilder();
        String[] order = command.split(" ORDER BY ");
        String[] filter = order[0].split(" WHERE ");

        if(command.contains("countries")){
            List<Country> list = null;

            if(filter.length == 2){
                list = listCountries(filter[1]);
            }else {
                list = listCountries("");
            }

            if(order.length == 2){
                list = orderCountryData(list, order[1]);
            }

            list.forEach(country -> data.append(country.toString()));

        }

        if(command.contains("cities")){

            List<City> list = null;

            if(filter.length == 2){
                list = listCities(filter[1]);
            }else {
                list = listCities("");
            }

            if(order.length == 2){
                list = orderCityData(list, order[1]);
            }

            list.forEach(city -> data.append(city.toString()));

        }

        return data.toString();

    }

    public String deleteData(String command) {
        command = command.replace("'", "");

        String[] parts = command.split(" WHERE ");
        boolean deleted = false;

        if(parts[0].contains("countries"))
            deleted = deleteCountries(parts[1]);

        if(parts[0].contains("cities"))
            deleted = deleteCities(parts[1]);

        return deleted
                ? "Successfully deleted data"
                : "No data was deleted";

    }

    public List<Country> listCountries(String criteria){

        List<Country> list = null;
        String [] parts = criteria.split(" ");

        if(criteria.isEmpty()) list = countries;

        if(parts[0].equals("population")){

            double number = Double.parseDouble(parts[2]);

            if(parts[1].equals(">")){
                list = countries.stream()
                        .filter(country -> country.getPopulation() > number)
                        .collect(Collectors.toList());
            }

            if(parts[1].equals("<")){
                list = countries.stream()
                        .filter(country -> country.getPopulation() < number)
                        .collect(Collectors.toList());
            }

            if(parts[1].equals("=")){
                list = countries.stream()
                        .filter(country -> country.getPopulation() == number)
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

            double number = Double.parseDouble(parts[2]);

            if(parts[1].equals(">")){
                list = cities.stream()
                        .filter(e -> e.getPopulation() > number)
                        .collect(Collectors.toList());
            }

            if(parts[1].equals("<")){
                list = cities.stream()
                        .filter(city -> city.getPopulation() < number)
                        .collect(Collectors.toList());
            }

            if(parts[1].equals("=")){
                list = cities.stream()
                        .filter(city -> city.getPopulation() == number)
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

        if(parts[0].equals("countryID")){
            list = cities.stream()
                    .filter(city -> city.getCountryID().equals(parts[2]))
                    .collect(Collectors.toList());
        }

        return list;

    }

    public boolean deleteCountries(String criteria){

        String[] parts = criteria.split(" ");

        boolean deleted = false;

        if(parts[0].equals("population")){

            double number = Double.parseDouble(parts[2]);

            if(parts[1].equals(">"))
                deleted = countries.removeIf(country -> country.getPopulation() > number);

            if(parts[1].equals("<"))
                deleted = countries.removeIf(country -> country.getPopulation() < number);

            if(parts[1].equals("="))
                deleted = countries.removeIf(country -> country.getPopulation() == number);

        }

        if(parts[0].equals("id"))
            deleted = countries.removeIf(country -> country.getId().equals(parts[2]));

        if(parts[0].equals("name"))
            deleted = countries.removeIf(country -> country.getName().equals(parts[2]));

        if(parts[0].equals("countryCode"))
            deleted = countries.removeIf(country -> country.getCountryCode().equals(parts[2]));

        return deleted;

    }

    public boolean deleteCities(String criteria){
        String[] parts = criteria.split(" ");

        boolean deleted = false;

        if(parts[0].equals("population")){

            double number = Double.parseDouble(parts[2]);

            if(parts[1].equals(">"))
                deleted = cities.removeIf(city -> city.getPopulation() > number);

            if(parts[1].equals("<"))
                deleted =  cities.removeIf(city -> city.getPopulation() < number);

            if(parts[1].equals("="))
                deleted = cities.removeIf(city -> city.getPopulation() == number);

        }

        if(parts[0].equals("id"))
            deleted = cities.removeIf(city -> city.getId().equals(parts[2]));

        if(parts[0].equals("name"))
            deleted = cities.removeIf(city -> city.getName().equals(parts[2]));

        if(parts[0].equals("countryID"))
            deleted = cities.removeIf(city -> city.getCountryID().equals(parts[2]));

        return deleted;

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
    public String importData(String path){

        String response = "Data imported correctly";
        List<Country> backUpCountries = new ArrayList<>(countries);
        List<City> backUpCities = new ArrayList<>(cities);

        int count = 1;
        try{
            File file = new File(path);

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String line;

            while ((line = reader.readLine()) != null){
                if(validateInsert(line)) registerData(line);
                count++;
            }

        }catch (IOException e){
            e.printStackTrace();
        } catch (CountryNotFoundException | InvalidFormatException | ExistingRecord e) {
            response = "Error in line " + count + ": " + e.getMessage();
            countries = backUpCountries;
            cities = backUpCities;
        }

        return response;

    }
    public void loadData(){
        try {
            File file = new File(COUNTRIES_PATH);

            if(!file.exists()) return;

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder countriesJSON = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                countriesJSON.append(line);
            }
            fis.close();

            if(!countriesJSON.toString().isEmpty()){
                Gson gson = new Gson();
                Country [] countriesArr = gson.fromJson(countriesJSON.toString(), Country[].class);
                countries.addAll(Arrays.asList(countriesArr));
            }

        }catch (IOException e) {
            e.printStackTrace();
        }

        try{
            File file = new File(CITIES_PATH);

            if(!file.exists()) return;

            FileInputStream fis = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder citiesJSON = new StringBuilder();

            String line;

            while ((line = reader.readLine()) != null) {
                citiesJSON.append(line);
            }
            fis.close();

            if(!citiesJSON.toString().isEmpty()){
                Gson gson = new Gson();
                City [] citiesArr = gson.fromJson(citiesJSON.toString(), City[].class);
                cities.addAll(Arrays.asList(citiesArr));
            }


        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void saveData(){

        Gson gson = new Gson();

        String countriesJSON = gson.toJson(countries);
        String citiesJSON = gson.toJson(cities);

        try {
            FileOutputStream fos = new FileOutputStream(COUNTRIES_PATH);
            fos.write(countriesJSON.getBytes(StandardCharsets.UTF_8));
            fos.close();

        }catch (IOException e) {
            e.printStackTrace();
        }

        try {
            FileOutputStream fos = new FileOutputStream(CITIES_PATH);
            fos.write(citiesJSON.getBytes(StandardCharsets.UTF_8));
            fos.close();

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean validateInsert(String command) throws InvalidFormatException, CountryNotFoundException, ExistingRecord {

        boolean isValid = false;

        String[] parts = command.split(" VALUES ");

        if(parts.length != 2 &&
                !parts[0].equals("INSERT INTO countries(id, name, population, countryCode)") &&
                !parts[0].equals("INSERT INTO cities(id, name, countryID, population)"))
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

            parameters[0] = parameters[0].replace("'", "");

            if(countryRegistered(parameters[0]))
                throw new ExistingRecord("There is already a country with this id");

        }

        if(parts[0].contains("cities")){
            isValid = isValidString(parameters[0]) &&
                    isValidString(parameters[1]) &&
                    isValidString(parameters[2]) &&
                    isANumber(parameters[3]);

            parameters[2] = parameters[2].replace("'", "");
            parameters[0] = parameters[0].replace("'", "");

            if(!countryRegistered(parameters[2]))
                throw new CountryNotFoundException("Error 404: Country not found");

            if(cityRegistered(parameters[0]))
                throw new ExistingRecord("There is already a city with this id");
        }

        return isValid;
    }

    public boolean validateSelect(String command) throws InvalidFormatException {

        boolean isValid = true;

        String [] parts = command.split(" WHERE ");

        if(parts.length != 2 && parts.length != 1 &&
                !parts[0].equals("SELECT * FROM countries") &&
                !parts[0].equals("SELECT * FROM cities"))
            throw new InvalidFormatException("Invalid select command");

        if(parts.length == 2){

            String[] parameters = parts[1].split(" ");

            if(parameters.length != 3){
                throw new InvalidFormatException("Invalid condition");
            }

            if(!parameters[1].equals("=") && !parameters[1].equals(">") && !parameters[1].equals("<"))
                throw new InvalidFormatException("Invalid operator");

            if(parameters[0].equals("population") && !isANumber(parameters[2]))
                throw new InvalidFormatException(parameters[2] + " is not a number");

            if(!parameters[0].equals("population") && !isValidString(parameters[2]))
                throw new InvalidFormatException(parameters[2] + " is not a valid string");

            if(parts[0].contains("countries") && !validateCountryParameter(parameters[0]))
                throw new InvalidFormatException(parameters[0] + ": Invalid parameter");

            if(parts[0].contains("cities") && !validateCityParameter(parameters[0]))
                throw new InvalidFormatException(parameters[0] + ": Invalid parameter");

            if(!validateComparison(parameters, !parameters[0].equals("population")))
                throw new InvalidFormatException("Invalid " + parameters[0] + " comparision");

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

    public boolean validateDelete(String command) throws InvalidFormatException {

        boolean isValid = true;

        String[] parts = command.split(" WHERE ");


        if(parts.length != 2 &&
                !parts[0].equals("DELETE FROM countries") &&
                !parts[0].equals("DELETE FROM cities"))
            throw new InvalidFormatException("Invalid delete command");


        String [] parameters = parts[1].split(" ");

        if(parameters.length != 3)
            throw new InvalidFormatException("Incorrect parameter number");

        if(parameters[0].equals("population") && !isANumber(parameters[2]))
            throw new InvalidFormatException(parameters[2] + " is not a number");

        if(!parameters[0].equals("population") && !isValidString(parameters[2]))
            throw new InvalidFormatException(parameters[2] + " is not a valid string");

        if(parts[0].contains("countries") && !validateCountryParameter(parameters[0]))
            throw new InvalidFormatException(parameters[0] + ": Invalid parameter");

        if(parts[0].contains("cities") && !validateCityParameter(parameters[0]))
            throw new InvalidFormatException(parameters[0] + ": Invalid parameter");

        if(!validateComparison(parameters, !parameters[0].equals("population")))
            throw new InvalidFormatException("Invalid " + parameters[0] + " comparision");

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

    public boolean validateComparison(String [] parameters, boolean isString) throws InvalidFormatException {

        boolean isValid = true;

        if(isString && !parameters[1].equals("=") && !isValidString(parameters[2]))
            isValid = false;

        if(!isString && !isANumber(parameters[2]))
            isValid = false;

        return isValid;
    }

    public boolean validateCountryParameter(String parameter){
        return parameter.equals("id") ||
                parameter.equals("name") ||
                parameter.equals("population") ||
                parameter.equals("countryCode");
    }

    public boolean validateCityParameter(String parameter){
        return parameter.equals("id") ||
                parameter.equals("name") ||
                parameter.equals("countryID") ||
                parameter.equals("population");
    }

    public boolean countryRegistered(String id){

        boolean flag = false;

        for(int i = 0; i < countries.size() && !flag; i++)
            if(countries.get(i).getId().equals(id)) flag = true;

        return flag;
    }

    public boolean cityRegistered(String id){

        boolean flag = false;

        for(int i = 0; i < cities.size() && !flag; i++)
            if(cities.get(i).getId().equals(id)) flag = true;

        return flag;
    }

    public List<City> getCities() {
        return cities;
    }

    public List<Country> getCountries() {
        return countries;
    }

}
