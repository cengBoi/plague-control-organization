package ops;
public class Route {
    public boolean isOnRoute;
    public int monetaryGain;
    public int remainingDay;
    public double infectionRateChange;
    public City destination;
    public String vehicle;
    public String HQ_Name;

    public Route(int remainingDay, int monetaryGain, double infectionRateChange, City destination, String vehicle, String HQ_Name) {
        this.isOnRoute = true;
        this.remainingDay = remainingDay;
        this.monetaryGain = monetaryGain;
        this.infectionRateChange = infectionRateChange;
        this.destination = destination;
        this.vehicle = vehicle;
        this.HQ_Name = HQ_Name;
    }

    public String toString() {
        return "classd{" +
                "isOnRoute=" + isOnRoute +
                ", monetaryGain=" + monetaryGain +
                ", remainingDay=" + remainingDay +
                ", infectionRateChange=" + infectionRateChange +
                ", destination=" + destination +
                ", vehicle='" + vehicle + '\'' +
                ", HQ_Name='" + HQ_Name + '\'' +
                '}';
    }
}
