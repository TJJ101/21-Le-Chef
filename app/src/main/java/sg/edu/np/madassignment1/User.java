package sg.edu.np.madassignment1;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String id;
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    private String username;
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    private String email;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    private String password;
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    private ArrayList<Ingredient> groceryList = new ArrayList<>();
    public ArrayList<Ingredient> getGroceryList() {return groceryList;}
    public void setGroceryList(ArrayList<Ingredient> groceryList) {
        this.groceryList = groceryList;
    }

    public void addGroceryList(ArrayList<Ingredient> iList){
        for(Ingredient i : iList){
            groceryList.add(i);
        }
    }

    private ArrayList<String> savedRecipes = new ArrayList<>();
    public ArrayList<String> getSavedRecipes() {
        return savedRecipes;
    }
    public void setSavedRecipes(ArrayList<String> savedRecipes) {
        this.savedRecipes = savedRecipes;
    }

    public User() {}

    public User(String i, String u, String e, String p){
        this.id = i;
        this.username = u;
        this.email = e;
        this.password = p;
    }

}
