package fr.gph3.classifier.utils.distances;

import fr.gph3.classifier.models.columns.IColumn;
import fr.gph3.classifier.models.columns.INormalizableColumn;
import fr.gph3.classifier.models.columns.NumberColumn;
import fr.gph3.classifier.models.points.IPoint;

import java.util.List;

public class ManhattanDistance implements IDistance {

    private final List<IColumn> columns;

    public ManhattanDistance(List<IColumn> columns) {
        this.columns = columns;
    }

    @Override
    public double distance(IPoint p1, IPoint p2) {
        if (p1 == null || p2 == null) return 0;
        double distance = 0.0;
        for (IColumn column : columns) {
            if (column instanceof INormalizableColumn) {
                double value1 = ((NumberColumn) column).getNormalizedValue(p1.getValue(column));
                double value2 = ((NumberColumn) column).getNormalizedValue(p2.getValue(column));
                distance += Math.abs(value1 - value2);
            } else {
                distance += p1.getValue(column).equals(p2.getValue(column)) ? 0 : 1;
            }
        }
        return distance;
    }
}
