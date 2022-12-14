package fr.gph3.classifier.models.columns;

import fr.gph3.classifier.models.IDataSet;
import fr.gph3.classifier.utils.normalizers.INormalizer;

public abstract class INormalizableColumn extends IColumn {

    private INormalizer normalizer;

    public INormalizableColumn(String name, IDataSet dataSet) {
        super(name, dataSet);
    }

    protected void initNormalizer(INormalizer normalizer) {
        this.normalizer = normalizer;
    }

    /**
     * Recupere la valeur de cette colonne dans la donnee en parametre,
     * puis normalise cette valeur (entre 0 et 1) et la retourne normalisee.
     * Si la colonne n'est pas normalisable, le comportement n'est pas
     * definit.
     */

    public double getNormalizedValue(Object value) {
        return normalizer.normalize(value);
    }

    /**
     * "Denormalise" une valeur entre 0 et 1 en une "vraie" valeur pour
     * cette colonne.
     * Si la colonne n'est pas normalisable, le comportement n'est pas
     * definit.
     */

    public Object getDenormalizedValue(double value) {
        return normalizer.denormalize(value);
    }

    public double getMin() {
        return dataSet.getPoints()
                .stream()
                .map(point -> ((Number) point.getValue(this)))
                .mapToDouble(Number::doubleValue)
                .min()
                .orElse(0);

    }

    public double getMax() {
        return dataSet.getPoints()
                .stream()
                .map(point -> ((Number) point.getValue(this)))
                .mapToDouble(Number::doubleValue)
                .max()
                .orElse(0);
    }
}
