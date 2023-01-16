import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.gson.ToNumberPolicy.LAZILY_PARSED_NUMBER;

public class MaxCategory {
    @Expose
    private String category;
    @Expose
    private String title;
    @Expose
    private String date;
    private List<MaxCategory> categorySumList;
    private List<MaxCategory> titleDateSumList;
    private List<MaxCategory> maxCategoryList;
    private MaxCategory getTitleDateSum;
    private List<Category> categoryList;
    private List<Object> statOfBuy = new ArrayList<>();
    @Expose
    private int sum;
    private int indexCategoryList;
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

    public List<MaxCategory> getTitleDateSumList() {
        return titleDateSumList;
    }

    public void setTitleDateSumList(List<MaxCategory> titleDateSumList) {
        this.titleDateSumList = titleDateSumList;
    }

    public List<MaxCategory> getMaxCategoryList() {
        return maxCategoryList;
    }

    public void setMaxCategoryList(List<MaxCategory> maxCategoryList) {
        this.maxCategoryList = maxCategoryList;
    }

    public MaxCategory getGetTitleDateSum() {
        return getTitleDateSum;
    }

    public void setGetTitleDateSum(MaxCategory getTitleDateSum) {
        this.getTitleDateSum = getTitleDateSum;
    }

    // Запись данных в List для Json
    public List<Object> writeCategorySum(String pkg) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.setPrettyPrinting().create();
        // Полученный запрос из JSON
        getTitleDateSum = gson.fromJson(pkg, MaxCategory.class);
        // Получение списка категорий из CSV в List
        if (categoryList == null || categoryList.isEmpty()) {
            categoryList = Category.singleLine(pathCsv);
        }
        MaxCategory categoryBuild;
        // Перебор наличия категорий в CSV
        int count = 0;
        for (int i = 0; i < categoryList.size(); i++) {
            // Сопоставление i-категории из CSV с JSON
            if (categoryList.get(i).getName().equalsIgnoreCase(getTitleDateSum.getTitle())) {
                System.out.println(categoryList.get(i).getName() + " " + getTitleDateSum.getTitle());
                String setCategory = categoryList.get(i).getCategory();
                int sumCategory = getTitleDateSum.getSum();
                // Создание объекта MaxCategory с переданной категорией и суммой
                categoryBuild = new MaxCategory(setCategory, sumCategory);
                // Добавление созданного объекта MaxCategory в List
                // Добавление в List впервые
                i = categoryList.size();
                if (categorySumList.isEmpty()) {
                    categorySumList.add(categoryBuild);
                    indexCategoryList = 0;
                    count++;
                } else {
                    // Добавление в List, если там уже есть данные с суммированием категории из CSV
                    for (int j = 0; j < categorySumList.size(); j++) {
                        // Если категория в итоговом List совпала с полученной
                        if (categorySumList.get(j).getCategory().equals(categoryBuild.getCategory())) {
                            int sumCategoryList = categorySumList.get(j).getSum();
                            int sumCategoryBuild = categoryBuild.getSum();
                            categorySumList.get(j).setSum(sumCategoryList + sumCategoryBuild);
                            indexCategoryList = j;
                            j = categorySumList.size();
                            count++;
                        }
                    }
                    // Если это новая категория из CSV для итогового List
                    if (count == 0) {
                        categorySumList.add(categoryBuild);
                        indexCategoryList = categorySumList.size() - 1;
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
                indexCategoryList = 0;
            } else {
                // Поиск категории "другое" в итоговом List
                for (int m = 0; m < categorySumList.size(); m++) {
                    if (categorySumList.get(m).getCategory().equals("другое")) {
                        int sumMore = categorySumList.get(m).getSum();
                        int sumTitleDate = getTitleDateSum.getSum();
                        categorySumList.get(m).setSum(sumMore + sumTitleDate);
                        indexCategoryList = m;
                        m = categorySumList.size();
                        count++;
                    }
                }
                if (count == 0) {
                    categoryBuild = new MaxCategory("другое", getTitleDateSum.getSum());
                    categorySumList.add(categoryBuild);
                    indexCategoryList = categorySumList.size() - 1;
                }
            }
        }

        maxCategoryList = selectMaxCategory(categorySumList);

        // Запись введенных данных в data.bin
        recordLog(getTitleDateSum);

        // Расчет дополнительных полей статистики покупок:
        // максимальная категория за год, месяц, день покупки
        dateStat(titleDateSumList);
        System.out.println("statOfBuy: " + statOfBuy);

        return statOfBuy;
    }

    // Запись введенных данных в data.bin
    public void recordLog(MaxCategory getTitleDateSum) throws Exception {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter("data.bin", false)) {
            MaxCategory titleDateSumForList = new MaxCategory(
                    categorySumList.get(indexCategoryList).getCategory(),
                    getTitleDateSum.getTitle(),
                    getTitleDateSum.getDate(),
                    getTitleDateSum.getSum());
            titleDateSumList.add(titleDateSumForList);
            String s = gson.toJson(titleDateSumList);
            writer.write(s);
        }
    }

    // Чтение файла data.bin для установки начальной информации о записанной ранее категории и сумме
    public void startWork(File data) throws Exception {
        MaxCategory forStartBuildCategorySum;
        if (data.canRead()) {
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder
                    .setObjectToNumberStrategy(LAZILY_PARSED_NUMBER)
                    .setPrettyPrinting()
                    .create();

            try (BufferedReader reader = new BufferedReader(new FileReader(data))) {
                // Определить свой собственный абстрактный класс базового ответа,
                // чтобы gson распознавал тип данных MaxCategory в List.
                Type typeMyType = new TypeToken<ArrayList<MaxCategory>>() {
                }.getType();
                // List c MaxCategory - все поля
                List<MaxCategory> readJson = gson.fromJson(reader, typeMyType);

                // Группировка List в Map с суммированием (значения) по Категории (ключ)
                Map<String, Integer> collect = readJson.stream()
                        .collect(Collectors.groupingBy(
                                MaxCategory::getCategory,
                                Collectors.summingInt(MaxCategory::getSum)
                        ));

                // Формирование из Map - List<MaxCategory> - запись введенных ранее данных
                // из JSON
                for (Map.Entry<String, Integer> stringIntegerEntry : collect.entrySet()) {
                    String categoryForStart = stringIntegerEntry.getKey();
                    int sumForStart = stringIntegerEntry.getValue();
                    forStartBuildCategorySum = new MaxCategory(categoryForStart, sumForStart);
                    categorySumList.add(forStartBuildCategorySum);
                }

                // Установка в общий List начальных значений из data.bin
                this.setTitleDateSumList(readJson);

                // Перезапись введенных данных в data.bin - корректный List с MaxCategory
                try (FileWriter writer = new FileWriter("data.bin", false)) {
                    String s = gson.toJson(titleDateSumList);
                    writer.write(s);
                }
            }
        }
    }

    // Выбор в итоговом List категории с максимальной суммой
    public List<MaxCategory> selectMaxCategory(List<MaxCategory> maxSumCategoryList) {
        List<MaxCategory> collect = maxSumCategoryList.stream()
                .sorted(Comparator.comparing(MaxCategory::getSum).reversed())
                .limit(1)
                .collect(Collectors.toList());
        return collect;
    }

    public void dateStat(List<MaxCategory> titleDateSumListAll) {
        System.out.println("titleDateSumListAll.get(0).getDate(): " + titleDateSumListAll.get(0).getDate());

        // List с данными за Год, Месяц, День - все введенные данные без группировки
        List<MaxYearCategory> yearSumList = new ArrayList<>();
        List<MaxMonthCategory> monthSumList = new ArrayList<>();
        List<MaxDayCategory> daySumList = new ArrayList<>();

        MaxCategory maxCategory;

        MaxYearCategory yearCategory;
        MaxMonthCategory monthCategory;
        MaxDayCategory dayCategory;

        // Формирование List - данные за Год, Месяц, День
        for (MaxCategory maxCategoryFor : titleDateSumListAll) {
            LocalDate datePattern = LocalDate.parse(maxCategoryFor.getDate(),
                    DateTimeFormatter.ofPattern("yyyy.MM.dd"));
            int year = datePattern.getYear();
            int month = datePattern.getMonthValue();
            int day = datePattern.getDayOfMonth();
            String category = maxCategoryFor.getCategory();

            int sum = maxCategoryFor.getSum();

            yearCategory = new MaxYearCategory(year, category, sum);
            monthCategory = new MaxMonthCategory(month, category, sum);
            dayCategory = new MaxDayCategory(day, category, sum);

            yearSumList.add(yearCategory);
            monthSumList.add(monthCategory);
            daySumList.add(dayCategory);
        }

        // !!!!!
        // ГОД
        // Группировка List в Map по Году и группировкой внутри года по Категориям
        Map<Integer, Map<String, Integer>> collectYear = yearSumList.stream()
                .collect(Collectors.groupingBy(
                        MaxYearCategory::getYear,
                        Collectors.groupingBy(MaxYearCategory::getCategory,
                                Collectors.summingInt(MaxYearCategory::getSumYear))
                ));
        System.out.println("collectYear: " + collectYear);

        System.out.println("yearSumList: " + yearSumList);

        // Очистка List
        yearSumList.clear();
        for (Map.Entry<Integer, Map<String, Integer>> integerMapEntry : collectYear.entrySet()) {
            Map<String, Integer> value = integerMapEntry.getValue();
            for (Map.Entry<String, Integer> stringIntegerEntry : value.entrySet()) {
                String categoryYear = stringIntegerEntry.getKey();
                Integer sumYear = stringIntegerEntry.getValue();
                yearCategory = new MaxYearCategory(categoryYear, sumYear);
                yearSumList.add(yearCategory);
            }
        }
        System.out.println("yearSumList: " + yearSumList);
        // Результат - год
        List<MaxYearCategory> MaxYearCategoryResult = yearSumList.stream()
                .sorted(Comparator.comparing(MaxYearCategory::getSumYear).reversed())
                .limit(1)
                .collect(Collectors.toList());
        System.out.println("MaxYearCategoryResult: " + MaxYearCategoryResult);

        // !!!!!
        // МЕСЯЦ
        // Группировка List в Map по Месяцу и группировкой внутри месяца по Категориям
        Map<Integer, Map<String, Integer>> collectMonth = monthSumList.stream()
                .collect(Collectors.groupingBy(
                        MaxMonthCategory::getMonth,
                        Collectors.groupingBy(MaxMonthCategory::getCategory,
                                Collectors.summingInt(MaxMonthCategory::getSumMonth))
                ));
        System.out.println("collectMonth: " + collectMonth);

        System.out.println("monthSumList: " + monthSumList);

        // Очистка List
        monthSumList.clear();
        for (Map.Entry<Integer, Map<String, Integer>> integerMapEntry : collectMonth.entrySet()) {
            Map<String, Integer> value = integerMapEntry.getValue();
            for (Map.Entry<String, Integer> stringIntegerEntry : value.entrySet()) {
                String categoryMonth = stringIntegerEntry.getKey();
                Integer sumMonth = stringIntegerEntry.getValue();
                monthCategory = new MaxMonthCategory(categoryMonth, sumMonth);
                monthSumList.add(monthCategory);
            }
        }
        System.out.println("monthSumList: " + monthSumList);
        // Результат - месяц
        List<MaxMonthCategory> MaxMonthCategoryResult = monthSumList.stream()
                .sorted(Comparator.comparing(MaxMonthCategory::getSumMonth).reversed())
                .limit(1)
                .collect(Collectors.toList());
        System.out.println("MaxMonthCategoryResult: " + MaxMonthCategoryResult);

        // !!!!!
        // ДЕНЬ
        // Группировка List в Map по Дню и группировкой внутри дня по Категориям
        Map<Integer, Map<String, Integer>> collectDay = daySumList.stream()
                .collect(Collectors.groupingBy(
                        MaxDayCategory::getDay,
                        Collectors.groupingBy(MaxDayCategory::getCategory,
                                Collectors.summingInt(MaxDayCategory::getSumDay))
                ));
        System.out.println("collectDay: " + collectDay);

        System.out.println("daySumList: " + daySumList);

        // Очистка List
        daySumList.clear();
        for (Map.Entry<Integer, Map<String, Integer>> integerMapEntry : collectDay.entrySet()) {
            Map<String, Integer> value = integerMapEntry.getValue();
            for (Map.Entry<String, Integer> stringIntegerEntry : value.entrySet()) {
                String categoryDay = stringIntegerEntry.getKey();
                Integer sumDay = stringIntegerEntry.getValue();
                dayCategory = new MaxDayCategory(categoryDay, sumDay);
                daySumList.add(dayCategory);
            }
        }
        System.out.println("daySumList: " + daySumList);
        // Результат - день
        List<MaxDayCategory> MaxDayCategoryResult = daySumList.stream()
                .sorted(Comparator.comparing(MaxDayCategory::getSumDay).reversed())
                .limit(1)
                .collect(Collectors.toList());
        System.out.println("MaxDayCategoryResult: " + MaxDayCategoryResult);

        // Добавление в List итогового результата
        statOfBuy.add(maxCategoryList.get(0));
        statOfBuy.add(MaxYearCategoryResult.get(0));
        statOfBuy.add(MaxMonthCategoryResult.get(0));
        statOfBuy.add(MaxDayCategoryResult.get(0));
    }

    @Override
    public String toString() {
        return "maxCategory: {" +
                "category: '" + category + '\'' +
                ", sum: " + sum +
                '}';
    }
}
