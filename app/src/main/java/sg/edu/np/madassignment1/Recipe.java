package sg.edu.np.madassignment1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Recipe implements Serializable {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String n) {
        this.name = n;
    }

    private String cuisine;
    public String getCuisine(){
        return cuisine;
    }
    public void setCuisine(String c){
        this.cuisine = c;
    }

    private String rating;
    public String getRating(){ return rating; }
    public void setRating(String r){
        this.rating = r;
    }

    private String description;
    public String getDescription(){ return description; }
    public void setDescription(String d){
        this.description = d;
    }

    public ArrayList<Ingredient> ingredientList;
    public ArrayList<Ingredient> getIngredientList() {return ingredientList;}
    public void addIngredient(Ingredient i){
        ingredientList.add(i);
    }


    public Recipe(String n, String r, String c, String d, ArrayList<Ingredient> i) {
        this.name = n;
        this.cuisine = c;
        this.rating = r;
        this.description = d;
        this.ingredientList = i;
    }
}
