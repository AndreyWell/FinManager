import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MaxCategoryMakerTest {
    private List<MaxCategory> category;
    private List<MaxCategory> maxCategoryList;
    @Spy
    private MaxCategoryMaker MaxCategoryMaker;
    String expected;

    @BeforeEach
    void setUp() {
        category = new ArrayList<>();
        maxCategoryList = new ArrayList<>();

        MaxCategory maxCategory = new MaxCategory("одежда", 100);
        MaxCategory maxCategory1 = new MaxCategory("финансы", 10);

        category.add(maxCategory);
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        expected = gson.toJson(category);

        System.out.println("category: " + category);
        maxCategoryList.add(maxCategory);
        maxCategoryList.add(maxCategory1);

    }

    @ParameterizedTest
    @ValueSource(strings = { "{\"title\":\"шапка\",\"date\":\"2020.10.10\",\"sum\":100}" })
    void writeCategorySum(String input) throws Exception {

        doNothing().when(MaxCategoryMaker).recordLog(ArgumentMatchers.any());

        String actual = MaxCategoryMaker.writeCategorySum(input).toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void selectMaxCategory() {
        Assertions.assertEquals(category, MaxCategoryMaker.selectMaxCategory(maxCategoryList));
    }
}