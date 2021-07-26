package sg.edu.np.madassignment1;

import java.util.ArrayList;

public class User {
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
    public User() {
    }

    private ArrayList<Ingredient> groceryList;
    public ArrayList<Ingredient> getGroceryList() {return groceryList;}
    public void setGroceryList(ArrayList<Ingredient> iList){
        for(Ingredient i : iList){
            groceryList.add(i);
        }
    }

    public User(String i, String u, String e, String p){
        this.id = i;
        this.username = u;
        this.email = e;
        this.password = p;
    }

}
