import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class CategoryTest {

    @Mock
    private Category category;
    private String categories;
    private String pathCsv = "categories.csv";

    @BeforeEach
    void setUp() {
        categories = "[Булка - еда, колбаса - еда, сухарики - еда, курица - еда, " +
                "тапки - одежда, шапка - одежда, мыло - быт, акции - финансы]";
    }

    @Test
    void singleLine() throws Exception {

        Assertions.assertEquals(categories, Category.singleLine(pathCsv).toString());
    }
}