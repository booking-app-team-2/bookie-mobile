package ftn.booking_app_team_2.bookie.model;

public class Period {
    private long startDate;

    private long endDate;

    public Period(long startDate, long endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Period() {
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
}
