package fr.gph3.classifier.utils.parsers;

import fr.gph3.classifier.models.points.Iris;
import fr.gph3.classifier.models.points.builders.IrisBuilder;

public class IIrisParser extends IPointParser {

    @Override
    public Iris parse(String line) {
        String[] args = line.split(" ");
        try {
            return new IrisBuilder()
                    .withPetalLength(Double.parseDouble(parseArg("petal.length", args)))
                    .withPetalWidth(Double.parseDouble(parseArg("petal.width", args)))
                    .withSepalLength(Double.parseDouble(parseArg("sepal.length", args)))
                    .withSepalWidth(Double.parseDouble(parseArg("sepal.width", args)))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
