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

    private String recipeId;
    public String getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    private List<Steps> stepsList = new ArrayList<Steps>();
    public List<Steps> getStepsList() {
        return stepsList;
    }
    public void setStepsList(List<Steps> stepsList) {
        this.stepsList = stepsList;
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

    public Recipe(String n, String r, String c, String d) {
        this.name = n;
        this.cuisine = c;
        this.rating = r;
        this.description = d;
        this.ingredientList = i;
    }

    public void AddSteps(int stepNum, String stepDescription){
        stepsList.add(new Steps(stepNum, stepDescription));
    }
}
