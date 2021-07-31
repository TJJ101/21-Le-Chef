package sg.edu.np.madassignment1;

public class Ratings {
    private int fiveStar;
    private int fourStar;
    private int threeStar;
    private int twoStar;
    private int oneStar;

    public int getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(int fiveStar) {
        this.fiveStar = fiveStar;
    }

    public int getFourStar() {
        return fourStar;
    }

    public void setFourStar(int fourStar) {
        this.fourStar = fourStar;
    }

    public int getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(int threeStar) {
        this.threeStar = threeStar;
    }

    public int getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(int twoStar) {
        this.twoStar = twoStar;
    }

    public int getOneStar() {
        return oneStar;
    }

    public void setOneStar(int oneStar) {
        this.oneStar = oneStar;
    }

    public Ratings(int fiveStar, int fourStar, int threeStar, int twoStar, int oneStar) {
        this.fiveStar = fiveStar;
        this.fourStar = fourStar;
        this.threeStar = threeStar;
        this.twoStar = twoStar;
        this.oneStar = oneStar;
    }

    public double getOverallRatings(){
        return ((5 * fiveStar) + (4 * fourStar) + (3 * threeStar) + (2 * twoStar) + (1 * oneStar)) / (fiveStar+fourStar+threeStar+twoStar+oneStar);
    }


}
