package sg.edu.np.madassignment1;

import java.io.Serializable;

public class Ratings implements Serializable {
    private double fiveStar;
    private double fourStar;
    private double threeStar;
    private double twoStar;
    private double oneStar;

    public double getFiveStar() {
        return fiveStar;
    }

    public void setFiveStar(double fiveStar) {
        this.fiveStar = fiveStar;
    }

    public double getFourStar() {
        return fourStar;
    }

    public void setFourStar(double fourStar) {
        this.fourStar = fourStar;
    }

    public double getThreeStar() {
        return threeStar;
    }

    public void setThreeStar(double threeStar) {
        this.threeStar = threeStar;
    }

    public double getTwoStar() {
        return twoStar;
    }

    public void setTwoStar(double twoStar) {
        this.twoStar = twoStar;
    }

    public double getOneStar() {
        return oneStar;
    }

    public void setOneStar(double oneStar) {
        this.oneStar = oneStar;
    }

    public Ratings(){
        this.oneStar = 0;
        this.twoStar = 0;
        this.threeStar = 0;
        this.fourStar = 0;
        this.fiveStar = 0;
    };

//    public Ratings(double fiveStar, double fourStar, double threeStar, double twoStar, double oneStar) {
//        this.fiveStar = fiveStar;
//        this.fourStar = fourStar;
//        this.threeStar = threeStar;
//        this.twoStar = twoStar;
//        this.oneStar = oneStar;
//    }

    public double calculateOverallRatings(){
        return ((5 * fiveStar) + (4 * fourStar) + (3 * threeStar) + (2 * twoStar) + (1 * oneStar)) / (fiveStar+fourStar+threeStar+twoStar+oneStar);
    }


}
