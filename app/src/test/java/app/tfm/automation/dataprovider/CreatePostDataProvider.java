package app.tfm.automation.dataprovider;

import org.testng.annotations.DataProvider;
import java.util.List;

public class CreatePostDataProvider {

    @DataProvider(name = "create-post-data", parallel = false)
    public static Object[][] getData() {
        List<Object[]> list = CreatePostExcelManager.getPendingUsers();
        return list.toArray(new Object[0][]);
    }
}
