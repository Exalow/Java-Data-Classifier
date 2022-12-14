package fr.gph3.classifier.controllers;

import fr.gph3.classifier.AbstractView;
import fr.gph3.classifier.models.columns.BooleanColumn;
import fr.gph3.classifier.models.columns.IColumn;
import fr.gph3.classifier.models.columns.INormalizableColumn;
import fr.gph3.classifier.models.columns.NumberColumn;
import fr.gph3.classifier.models.points.IPoint;
import fr.gph3.classifier.utils.AbstractMVCModel;
import fr.gph3.classifier.utils.KnnMethod;
import fr.gph3.classifier.utils.distances.EuclidDistance;
import fr.gph3.classifier.utils.distances.IDistance;
import fr.gph3.classifier.utils.distances.ManhattanDistance;
import fr.gph3.classifier.utils.parsers.IPointParser;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddPointView extends AbstractView {

	@FXML
	private ComboBox<String> distancesComboBox;

	@FXML
	private Spinner<Integer> knnSpinner;

	@FXML
	private TextField robustness;

	@FXML
	private VBox inputsBox;

	private PanelView panelView;

	private AbstractMVCModel model;

	private KnnMethod knnMethod;

	private IDistance distance;

	public AddPointView(Stage stage, AbstractMVCModel model) {
		super(stage);
		this.model = model;
		this.knnMethod = new KnnMethod();
	}

	/**
	 * Pour toutes les colonnes passés en parametre, ajouter sois un spinner sois un text field ou une checkbox en définissant
	 * un id avec setId correspondant au nom de la colonne à une
	 * VBox qui est à ajouter sur la vue et ajouté avec @FXML dans le controlleur
	 */

	@Override
    public void initialize(URL location, ResourceBundle resources) {
		distancesComboBox.setItems(FXCollections.observableList(Arrays.asList("Euclid", "Manhattan")));
		distancesComboBox.getSelectionModel().select("Euclid");
		knnSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(3, 10, 3, 1));
		onKnn();
		for (IColumn column : model.getNormalizableColumns()) {
			List<Node> children = inputsBox.getChildren();
			if (column instanceof NumberColumn) {
				inputsBox.getChildren().add(new Label(column.getName()));
				Spinner<Double> spinner = new Spinner<>(0.0, 1.0, 0.0, 0.1);
				spinner.setId(column.getName());
				children.add(spinner);
			} else if (column instanceof BooleanColumn) {
				CheckBox checkBox = new CheckBox(column.getName());
				checkBox.setId(column.getName());
				children.add(checkBox);
			}
		}
    }

	@FXML
	public void onKnn() {
		String selected = distancesComboBox.getSelectionModel()
				.getSelectedItem();
		this.distance = selected.equals("Euclid") ? new EuclidDistance(model.getColumns()) : new ManhattanDistance(model.getColumns());
		robustness.setText(String.valueOf(knnMethod.getRobustesse(distance, model.getPoints(), knnSpinner.getValue())));
	}

	@FXML
	public void onDistanceSelected() {
		onKnn();
	}

	@FXML
	public void onCancel() {
		stage.close();
	}

	@FXML
	public void onCreatePoint() {
		IPointParser parser = model.getParser();
		parser.initColumns(model.getNormalizableColumns());
		String toParse = parser.format();
		for (INormalizableColumn column : model.getNormalizableColumns()) {
			String columnName = column.getName();
			if (column instanceof NumberColumn) {
				Spinner<Double> spinner = (Spinner<Double>) findInputById(columnName);
				toParse = toParse.replace("${" + columnName + "}", String.valueOf(column.getDenormalizedValue(spinner.getValue())));
			} else if (column instanceof BooleanColumn){
				CheckBox checkBox = (CheckBox) findInputById(columnName);
				toParse = toParse.replace("${" + columnName + "}", String.valueOf(checkBox.isSelected()));
			}
		}
		IPoint newPoint = parser.parse(toParse);
		newPoint.setCategory("");
		newPoint.setCategory(knnMethod.classifier(knnMethod.getNeighbours(newPoint, knnSpinner.getValue(), distance, model.getPoints())));
		model.addPoint(newPoint);
	}

	private Node findInputById(String id) {
		return inputsBox.getChildren()
				.stream()
				.filter(input -> id.equals(input.getId()))
				.findFirst()
				.orElse(null);
	}

	@Override
	public Parent loadView() {
		return loadView("src/main/resources/views/AddPoint.fxml");
	}
}

