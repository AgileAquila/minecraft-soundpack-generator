package agileaquila;

import java.util.Arrays;
import java.util.List;

public class PackFormat {
    private int formatValue;
    private String versions;

    public int getFormatValue() {
        return formatValue;
    }

    public List<String> getVersions() {
        return Arrays.asList(versions.split(","));
    }
}
