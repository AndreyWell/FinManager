import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.gson.ToNumberPolicy.LAZILY_PARSED_NUMBER;

public class MaxCategory implements Serializable {
    @Expose
    private String category;
    @Expose
    private String title;
    @Expose
    private String date;
    @Expose
    private int sum;

    public MaxCategory(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public MaxCategory(String category, int sum) {
        this.category = category;
        this.sum = sum;
    }

    public MaxCategory(String category, String title, String date, int sum) {
        this.category = category;
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public MaxCategory() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getSum() {
        return sum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    @Override
    public String toString() {
        return "MaxCategory{" +
                "category='" + category + '\'' +
                "sum=" + sum +
                '}';
    }
}
