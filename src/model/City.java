package model;

public class City {

    private String id;
    private String name;
    private String countryID;

    private double population;

    public City(String id, String name, String countryID, double population) {
        this.id = id;
        this.name = name;
        this.countryID = countryID;
        this.population = population;
    }

    @Override
    public String toString() {
        return "City{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", countryID='" + countryID + '\'' +
                ", population=" + population +
                "} \n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountryID() {
        return countryID;
    }

    public void setCountryID(String countryID) {
        this.countryID = countryID;
    }

    public double getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

}
