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

    public int quantity;
    public int getQuantity() {return quantity;}
    public void setQuantity(int q) {this.quantity = q;}

    public String measurement;
    public String getMeasurement(){return measurement;}
    public void setMeasurement(String m) {this.measurement = m;}

    public Ingredient(String n, int q, String m){
        this.name = n;
        this.quantity = q;
        this.measurement = m;
    }
}
