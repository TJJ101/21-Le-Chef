package sg.edu.np.madassignment1;

public class Steps {
    private int stepNum;
    public int getStepNum() {
        return stepNum;
    }
    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    private String stepDescription;
    public String getStepDescription() {
        return stepDescription;
    }
    public void setStepDescription(String stepDescription) {
        this.stepDescription = stepDescription;
    }

    private String time;
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public Steps(){}

    public Steps(int stepNum, String stepDescription) {
        this.stepNum = stepNum;
        this.stepDescription = stepDescription;
    }
}
