package sg.edu.np.madassignment1;

public class Recipe {
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
    public String getRating(){
        return rating;
    }
    public void setRating(String r){
        this.rating = r;
    }

    public Recipe(String n, String r, String c) {
        this.name = n;
        this.cuisine = c;
        this.rating = r;
    }
}
