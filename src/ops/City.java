package ops;
public class City {
    public String name;
    public int stateID;
    public boolean isNearCoast;
    public double infectedPeople;
    public double population;
    public int infectedPeoplePercent;

    public City(String name, int stateID, boolean isNearCoast, int population) {
        this.name = name;
        this.stateID = stateID;
        this.isNearCoast = isNearCoast;
        this.infectedPeople = 0;
        this.population = population;
        this.infectedPeoplePercent = 0;
    }

    public void increaseInfected() {
        infectedPeople += (int) (population / 150);
        infectedPeoplePercent = (int) (100 * (infectedPeople / population));
    }

    @Override
    public String toString() {
        return "City{" +
                "name='" + name + '\'' +
                ", stateID=" + stateID +
                ", isNearCoast=" + isNearCoast +
                ", infectedPeople=" + infectedPeople +
                ", population=" + population +
                ", getInfectedPeoplePercent=" + infectedPeoplePercent +
                "}\n";
    }
}
