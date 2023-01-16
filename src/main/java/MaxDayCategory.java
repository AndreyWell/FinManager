public class MaxDayCategory {
    private int day;
    private String category;
    private int sumDay;

    public MaxDayCategory(int day, String category, int sumDay) {
        this.day = day;
        this.category = category;
        this.sumDay = sumDay;
    }

    public MaxDayCategory(String category, int sumDay) {
        this.category = category;
        this.sumDay = sumDay;
    }

    public MaxDayCategory() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSumDay() {
        return sumDay;
    }

    public void setSumDay(int sumDay) {
        this.sumDay = sumDay;
    }

    @Override
    public String toString() {
        return "maxDayCategory: {" +
                "category: '" + category + '\'' +
                ", sum: " + sumDay +
                '}';
    }
}
