package ops;
public class Cure {
    public float cureRate;
    public int curePercentage;

    public Cure() {
        cureRate = 2;
        curePercentage = 0;
    }

// Not necessarily
    public void increaseCurePercentage() {

    }

    public boolean isCureDone() {
        return curePercentage == 100;
    }
}
