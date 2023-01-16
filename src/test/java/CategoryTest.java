import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class CategoryTest {
    private Category category;
    private String categories;
    private String pathCsv = "categories.csv";
    List<Category> listCategory;

    @BeforeEach
    void setUp() throws Exception {
        category = new Category();
        listCategory = List.of(
                new Category("Булка", "еда"),
                new Category("колбаса", "еда"),
                new Category("сухарики", "еда"),
                new Category("курица", "еда"),
                new Category("тапки", "одежда"),
                new Category("шапка", "одежда"),
                new Category("мыло", "быт"),
                new Category("акции", "финансы")
        );
    }

    @Test
    void singleLine() throws Exception {
        String expected = listCategory.toString();
        String actual = Category.singleLine(pathCsv).toString();

        Assertions.assertEquals(expected, actual);
    }
}