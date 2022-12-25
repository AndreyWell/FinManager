import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class MaxCategory {
    @Expose
    private String title;
    @Expose
    private String date;
    private List<MaxCategory> categorySumList;
    private List<MaxCategory> titleDateSumList;
    @Expose
    private String category;
    @Expose
    private int sum;
    private String pathCsv = "categories.csv";

    public MaxCategory(String title, String date, int sum) {
        this.title = title;
        this.date = date;
        this.sum = sum;
    }

    public MaxCategory() {
        this.categorySumList = new ArrayList<>();
        this.titleDateSumList = new ArrayList<>();
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

    public List<MaxCategory> getCategorySumList() {
        return categorySumList;
    }

    public void setCategorySumList(List<MaxCategory> categorySumList) {
        this.categorySumList = categorySumList;
    }

    // Запись данных в Json
    public List<MaxCategory> writeCategorySum(String pkg) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.setPrettyPrinting().create();
        // Полученный запрос из JSON
        MaxCategory getTitleDateSum = gson.fromJson(pkg, MaxCategory.class);
        // Получение списка категорий из CSV в List
        List<Category> categoryList = Category.singleLine(pathCsv);
        MaxCategory categoryBuild;
        // Перебор наличия категорий в CSV
        int count = 0;
        for (int i = 0; i < categoryList.size(); i++) {
            // Сопоставление i-категории из CSV с JSON
            if (categoryList.get(i).getName().equalsIgnoreCase(getTitleDateSum.getTitle())) {
                String setCategory = categoryList.get(i).getCategory();
                int sumCategory = getTitleDateSum.getSum();
                // Создание объекта MaxCategory с переданной категорией и суммой
                categoryBuild = new MaxCategory(setCategory, sumCategory);
                // Добавление созданного объекта MaxCategory в List
                // Добавление в List впервые
                if (categorySumList.isEmpty()) {
                    categorySumList.add(categoryBuild);
                    count++;
                } else {
                    // Добавление в List, если там уже есть данные с суммированием категории из CSV
                    for (int j = 0; j < categorySumList.size(); j++) {
                        // Если категория в итоговом List совпала с полученной
                        if (categorySumList.get(j).getCategory().equals(categoryBuild.getCategory())) {
                            int sumCategoryList = categorySumList.get(j).getSum();
                            int sumCategoryBuild = categoryBuild.getSum();
                            categorySumList.get(j).setSum(sumCategoryList + sumCategoryBuild);
                            count++;
                        }
                    }
                    // Если это новая категория из CSV для итогового List
                    if (count == 0) {
                        categorySumList.add(categoryBuild);
                        count++;
                    }
                }
            }
        }

        // Категория: "другое"
        if (count == 0) {
            // Добавление в пустой List впервые
            if (categorySumList.isEmpty()) {
                categoryBuild = new MaxCategory("другое", getTitleDateSum.getSum());
                categorySumList.add(categoryBuild);
            } else {
                // Поиск категории "другое" в итоговом List
                for (int m = 0; m < categorySumList.size(); m++) {
                    if (categorySumList.get(m).getCategory().equals("другое")) {
                        int sumMore = categorySumList.get(m).getSum();
                        int sumTitleDate = getTitleDateSum.getSum();
                        categorySumList.get(m).setSum(sumMore + sumTitleDate);
                        count++;
                    }
                }
                if (count == 0) {
                    categoryBuild = new MaxCategory("другое", getTitleDateSum.getSum());
                    categorySumList.add(categoryBuild);
                }
            }
        }

        List<MaxCategory> maxCategoryList = selectMaxCategory(categorySumList);

        return maxCategoryList;
    }

    // Выбор в итоговом List категории с максимальной суммой
    public List<MaxCategory> selectMaxCategory(List<MaxCategory> maxSumCategoryList) {
        List<MaxCategory> collect = maxSumCategoryList.stream()
                .sorted(Comparator.comparing(MaxCategory::getSum).reversed())
                .limit(1)
                .collect(Collectors.toList());
        return collect;
    }

    @Override
    public String toString() {
        return "MaxCategory{" +
                "category='" + category + '\'' +
                "sum=" + sum +
                '}';
    }
}
