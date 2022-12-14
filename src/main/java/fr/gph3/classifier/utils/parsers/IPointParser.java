package fr.gph3.classifier.utils.parsers;

import fr.gph3.classifier.models.columns.INormalizableColumn;
import fr.gph3.classifier.models.points.IPoint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class IPointParser {

    private List<INormalizableColumn> columns;

    public IPointParser() {
        this.columns = new ArrayList<>();
    }

    public void initColumns(List<INormalizableColumn> columns) {
        this.columns = columns;
    }

    public abstract IPoint parse(String line);

    protected String parseArg(String token, String[] args) {
        int pos = tokenPosition(token);
        return pos == -1 ? "0.0" : args[pos];
    }

    protected int tokenPosition(String token) {
        String[] split = format().split(" ");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("${" + token + "}")) {
                return i;
            }
        }
        return -1;
    }

    public String format() {
        return columns.stream()
                .map(c -> "${" + c.getName() + "} ")
                .collect(Collectors.joining());
    }

    public List<INormalizableColumn> getColumns() {
        return columns;
    }
}
