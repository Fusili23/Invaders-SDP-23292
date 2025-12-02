package engine.level;

import java.io.IOException;
import java.util.List;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonLoaderTest {

    @Test
    public void parse_validJson_returnsCorrectLevelList() throws IOException {
        String jsonContent = "{\n" +
                "  \"levels\": [\n" +
                "    {\n" +
                "      \"level\": 1,\n" +
                "      \"levelName\": \"Test Level 1\",\n" +
                "      \"formationWidth\": 5,\n" +
                "      \"formationHeight\": 4,\n" +
                "      \"baseSpeed\": 60,\n" +
                "      \"shootingFrecuency\": 2000\n" +
                "    },\n" +
                "    {\n" +
                "      \"level\": 2,\n" +
                "      \"levelName\": \"Test Level 2\",\n" +
                "      \"formationWidth\": 6,\n" +
                "      \"formationHeight\": 5,\n" +
                "      \"baseSpeed\": 50,\n" +
                "      \"shootingFrecuency\": 2500\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        List<Level> levels = JsonLoader.parse(jsonContent);

        assertNotNull(levels);
        assertEquals(2, levels.size());

        Level level1 = levels.get(0);
        assertEquals(1, level1.getLevel());
        assertEquals("Test Level 1", level1.getLevelName());
        assertEquals(5, level1.getFormationWidth());
        assertEquals(4, level1.getFormationHeight());
        assertEquals(60, level1.getBaseSpeed());
        assertEquals(2000, level1.getShootingFrecuency());

        Level level2 = levels.get(1);
        assertEquals(2, level2.getLevel());
        assertEquals("Test Level 2", level2.getLevelName());
    }

    @Test
    public void parse_emptyLevelsArray_returnsEmptyList() throws IOException {
        String jsonContent = "{\"levels\": []}";
        List<Level> levels = JsonLoader.parse(jsonContent);
        assertNotNull(levels);
        assertTrue(levels.isEmpty());
    }

    @Test(expected = IOException.class)
    public void parse_missingLevelsKey_throwsIOException() throws IOException {
        String jsonContent = "{\"no_levels_here\": []}";
        JsonLoader.parse(jsonContent);
    }

    @Test(expected = IOException.class)
    public void parse_malformedJson_throwsIOException() throws IOException {
        // Malformed JSON with a trailing comma in an object
        String jsonContent = "{\"levels\": [{\"level\": 1, }]}";
        JsonLoader.parse(jsonContent);
    }

    @Test(expected = IOException.class)
    public void parse_wrongDataType_throwsIOException() throws IOException {
        // baseSpeed is a string instead of a number
        String jsonContent = "{\"levels\": [{\"level\": 1, \"baseSpeed\": \"fast\"}]}";
        JsonLoader.parse(jsonContent);
    }
}