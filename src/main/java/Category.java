import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private String name;
    private String category;
    private String pathCsv = "categories.csv";

    public Category(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public Category() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Чтение категорий из CSV файла
    public static List<Category> singleLine(String path) throws Exception {
        List<Category> categories = new ArrayList<>();
        CSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .build();
        try (CSVReader csvReader = new CSVReaderBuilder(new FileReader(path))
                .withCSVParser(parser)
                .build();
        ) {
            String[] line;
            while ((line = csvReader.readNext()) != null) {
                categories.add(Category.buildCategory(line));
            }
        }
        return categories;
    }

    // Создание объекта Category
    public static Category buildCategory(String[] categoryAsArrayStrings) {
        return new Category(
                categoryAsArrayStrings[0],
                categoryAsArrayStrings[1]
        );
    }

    @Override
    public String toString() {
        return name + " - " + category;
    }
}
