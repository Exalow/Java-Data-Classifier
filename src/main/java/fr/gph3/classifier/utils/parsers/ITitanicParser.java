package fr.gph3.classifier.utils.parsers;

import fr.gph3.classifier.models.points.Titanic;
import fr.gph3.classifier.models.points.builders.TitanicBuilder;

public class ITitanicParser extends IPointParser {

    @Override
    public Titanic parse(String line) {
        String[] args = line.split(" ");
        try {
            return new TitanicBuilder()
                    .withAge(Double.parseDouble(parseArg("Age", args)))
                    .withFare(Double.parseDouble(parseArg("Fare", args)))
                    .withParch(Double.parseDouble(parseArg("Parch", args)))
                    .withPassengerId(Double.parseDouble(parseArg("passengerId", args)))
                    .withSurvived(Double.parseDouble(parseArg("survived", args)))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
