public class MaxMonthCategory {
    private int month;
    private String category;
    private int sumMonth;

    public MaxMonthCategory(int month, String category, int sumMonth) {
        this.month = month;
        this.category = category;
        this.sumMonth = sumMonth;
    }

    public MaxMonthCategory(String category, int sumMonth) {
        this.category = category;
        this.sumMonth = sumMonth;
    }

    public MaxMonthCategory() {
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSumMonth() {
        return sumMonth;
    }

    public void setSumMonth(int sumMonth) {
        this.sumMonth = sumMonth;
    }

    @Override
    public String toString() {
        return "maxMonthCategory: {" +
                "category: '" + category + '\'' +
                ", sum: " + sumMonth +
                '}';
    }
}
