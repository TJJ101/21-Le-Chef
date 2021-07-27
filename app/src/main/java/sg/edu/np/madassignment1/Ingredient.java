package sg.edu.np.madassignment1;

import java.io.Serializable;

public class Ingredient implements Serializable {
    public String name;
    public String getName() {
        return name;
    }
    public void setName(String n) {
        this.name = n;
    }

    public double quantity;
    public double getQuantity() {return quantity;}
    public void setQuantity(double q) {this.quantity = q;}

    public String measurement;
    public String getMeasurement(){return measurement;}
    public void setMeasurement(String m) {this.measurement = m;}

    Ingredient(){}

    public Ingredient(String n, double q, String m){
        this.name = n;
        this.quantity = q;
        this.measurement = m;
    }
}
