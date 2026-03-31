package framework.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * JsonReader - Đọc dữ liệu test từ file JSON (Jackson).
 *
 * Cách dùng - đọc thành List<Map>:
 *   List<Map<String,Object>> data = JsonReader.readAsListOfMaps("testdata/users.json");
 *
 * Cách dùng - đọc thành Object[][]:
 *   Object[][] data = JsonReader.readAsDataProvider("testdata/users.json", "username", "password");
 */
public class JsonReader {

    private static final ObjectMapper mapper = new ObjectMapper();

    private JsonReader() {}

    /**
     * Đọc JSON array thành List<Map<String, Object>>.
     * File được tìm trong classpath (src/test/resources/).
     */
    public static List<Map<String, Object>> readAsListOfMaps(String classpathResource) throws IOException {
        try (InputStream is = JsonReader.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new IllegalArgumentException("Không tìm thấy file: " + classpathResource);
            }
            return mapper.readValue(is, new TypeReference<List<Map<String, Object>>>() {});
        }
    }

    /**
     * Đọc JSON array và chuyển thành Object[][] theo thứ tự các keys chỉ định.
     * Dùng làm @DataProvider trực tiếp.
     *
     * @param classpathResource Đường dẫn file trong classpath
     * @param keys              Các key cần lấy từ mỗi object JSON (theo thứ tự cột)
     * @return Object[][] phù hợp với @DataProvider
     */
    public static Object[][] readAsDataProvider(String classpathResource, String... keys) throws IOException {
        List<Map<String, Object>> data = readAsListOfMaps(classpathResource);
        Object[][] result = new Object[data.size()][keys.length];

        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> row = data.get(i);
            for (int j = 0; j < keys.length; j++) {
                result[i][j] = row.getOrDefault(keys[j], "");
            }
        }
        return result;
    }

    /**
     * Đọc JSON file thành đối tượng Java POJO.
     */
    public static <T> T readAs(String classpathResource, Class<T> clazz) throws IOException {
        try (InputStream is = JsonReader.class.getClassLoader().getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new IllegalArgumentException("Không tìm thấy file: " + classpathResource);
            }
            return mapper.readValue(is, clazz);
        }
    }
}
