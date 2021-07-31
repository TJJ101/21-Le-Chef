package sg.edu.np.madassignment1;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private Ratings rating;
    public double getRating(){ return rating.getOverallRating(); }
    public void setRating(String r){
        //idk what to put here
        this.rating = r;
    }

    private String recipeId;
    public String getRecipeId() {
        return recipeId;
    }
    public void setRecipeId(String recipeId) {
        this.recipeId = recipeId;
    }

    private List<Steps> stepsList = new ArrayList<  Steps>();
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

    public ArrayList<Ingredient> ingredientList = new ArrayList<>();
    public ArrayList<Ingredient> getIngredientList() {return ingredientList;}
    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    private String creatorId;

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public void addIngredient(Ingredient i){
        ingredientList.add(i);
    }
    public Recipe(){};

    public Recipe(String n, String c, String d, ArrayList<Ingredient> i, ArrayList<Steps> s) {
        this.name = n;
        this.cuisine = c;
        this.description = d;
        this.ingredientList = i;
        this.stepsList = s;
    }

    public void addSteps(String stepDescription, String time){
        stepsList.add(new Steps(stepsList.size()+1, stepDescription, time));
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("recipeId", recipeId);
        result.put("name", name);
        result.put("cuisine", cuisine);
        result.put("description", description);
        result.put("ingredientList", ingredientList);
        result.put("stepsList", stepsList);
        return result;
    }
}
