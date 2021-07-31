package sg.edu.np.madassignment1;

public class Ratings {
    private String recipeId;
    public String getRecipeId() {return recipeId;}
    public void setRecipeId(String Id) {this.recipeId = Id;}

    private int fiveStar;
    public int getFiveStar(){return fiveStar;}
    public void addFiveStar(int rating){this.fiveStar += rating;}

    private int fourStar;
    public int getFourStar(){return fourStar;}
    public void addFourStar(int rating){this.fourStar += rating;}

    private int threeStar;
    public int getThreeStar(){return threeStar;}
    public void addThreeStar(int rating){this.threeStar += rating;}

    private int twoStar;
    public int getTwoStar(){return twoStar;}
    public void addTwoStar(int rating){this.twoStar += rating;}

    private int oneStar;
    public int getOneStar(){return oneStar;}
    public void addOneStar(int rating){this.oneStar += rating;}

    public Ratings(String recipeId, int fiveStar, int fourStar, int threeStar, int twoStar, int oneStar) {
        this.recipeId = recipeId;
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStar = threeStar;
        this.twoStar = twoStar;
        this.oneStar = oneStar;
    }

    public double getRatings(){
        return ((5 * fiveStar) + (4 * fourStar) + (3 * threeStar) + (2 * twoStar) + (1 * oneStar)) / (fiveStar+fourStar+threeStar+twoStar+oneStar);
    }


}
