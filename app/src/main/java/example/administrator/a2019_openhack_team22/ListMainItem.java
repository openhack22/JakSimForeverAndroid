package example.administrator.a2019_openhack_team22;

public class ListMainItem {
    private int goalId;
    private String goalName;
    private int percent;

    public ListMainItem(int goalId, String goalName, int percent){
        this.goalId = goalId;
        this.goalName = goalName;
        this.percent = percent;
    }


    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
        this.goalId = goalId;
    }
}
