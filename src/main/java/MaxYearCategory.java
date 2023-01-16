public class MaxYearCategory {
    private int year;
    private String category;
    private int sumYear;

    public MaxYearCategory(int year, String category, int sumYear) {
        this.year = year;
        this.category = category;
        this.sumYear = sumYear;
    }

    public MaxYearCategory(String category, int sumYear) {
        this.category = category;
        this.sumYear = sumYear;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public MaxYearCategory() {
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getSumYear() {
        return sumYear;
    }

    public void setSumYear(int sumYear) {
        this.sumYear = sumYear;
    }

    @Override
    public String toString() {
        return "maxYearCategory: {" +
                "category: '" + category + '\'' +
                ", sum: " + sumYear +
                '}';
    }
}
