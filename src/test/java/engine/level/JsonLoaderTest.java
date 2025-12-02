package engine.level;

import org.junit.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class JsonLoaderTest {

    @Test
    public void parse_validJson_returnsCorrectLevelList() throws IOException {
        String jsonContent = "{\"levels\": [
" +
                "    {\"level\": 1,
" +
                "      \"levelName\": \"Test Level 1\",
" +
                "      \"formationWidth\": 5,
" +
                "      \"formationHeight\": 4,
" +
                "      \"baseSpeed\": 60,
" +
                "      \"shootingFrecuency\": 2000
" +
                "    },
" +
                "    {\"level\": 2,
" +
                "      \"levelName\": \"Test Level 2\",
" +
                "      \"formationWidth\": 6,
" +
                "      \"formationHeight\": 5,
" +
                "      \"baseSpeed\": 50,
" +
                "      \"shootingFrecuency\": 2500
" +
                "    }
" +
                "  ]
" +
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
