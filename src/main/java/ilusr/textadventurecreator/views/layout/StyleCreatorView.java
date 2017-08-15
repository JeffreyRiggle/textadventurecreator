package ilusr.textadventurecreator.views.layout;

import java.io.File;
import java.net.URL;
import java.util.Map.Entry;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.core.StyleUpdater;
import ilusr.iroshell.core.ViewSwitcher;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.iroshell.services.IStyleWatcher;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.style.StyledComponents;

import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class StyleCreatorView extends AnchorPane implements Initializable, IStyleWatcher {

	@FXML
	private GridPane root;
	
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private StyleUpdater styleUpdater;
	private LayoutComponent component;
	private LayoutStyle newStyle;
	
	/**
	 * 
	 * @param component The @see LayoutComponent to bind to.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public StyleCreatorView(LayoutComponent component, IStyleContainerService styleService, InternalURLProvider urlProvider) {
		this.component = component;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource("StyleCreatorView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.getStyleClass().add("root");
		newStyle = new LayoutStyle(component.style().get());
		addStyleControls(0, component.style().get());
		setupStyles();
	}
	
	private void setupStyles() {
		styleUpdater = new StyleUpdater(urlProvider, "stylecreatorstylesheet.css", this);
		styleService.startWatchStyle(Arrays.asList(StyledComponents.STYLE_CREATOR), this, false);
		
		String style = styleService.get(StyledComponents.STYLE_CREATOR);
		if (style != null && !style.isEmpty()) {
			styleUpdater.update(style);
		}
	}
	
	@Override
	public void update(String style, String css) {
		styleUpdater.update(css);
	}

	private int addStyleControls(int currentRow, LayoutStyle style) {
		root.getRowConstraints().add(createRow());
		root.add(new Label(style.displayName()), 0, currentRow);
		currentRow++;
		
		for (Entry<String, StylePropertyPersistenceObject> entry : newStyle.getProperties().entrySet()) {
			if (entry.getValue().getPropertyType() == StyleType.BackgroundRepeat ||
				entry.getValue().getPropertyType() == StyleType.BackgroundSize ||
				entry.getValue().getPropertyType() == StyleType.None) {
				continue;
			}
			
			root.getRowConstraints().add(createRow());
			root.add(new Label(entry.getKey()), 0, currentRow);
			root.add(createStyleNode(entry.getValue()), 1, currentRow);
			currentRow++;
		}
		
		for (LayoutStyle childStyle : style.getChildren()) {
			currentRow = addStyleControls(currentRow, childStyle);
		}
		
		return currentRow;
	}
	
	/**
	 * 
	 * @return The updated @see LayoutStyle to use.
	 */
	public LayoutStyle getUpdatedStyles() {
		return newStyle;
	}
	
	private Node createStyleNode(StylePropertyPersistenceObject value) {
		Node retVal = null;
		
		switch (value.getPropertyType()) {
			case Background:
				retVal = createBackgroundPicker(value);
				break;
			case Color:
				retVal = createColorPicker(value);
				break;
			case FontSize:
				retVal = createSizePicker(value);
				break;
			case FontFamily:
				retVal = createFamilyPicker(value);
				break;
			case FontStyle:
				retVal = createStylePicker(value);
				break;
			default:
				retVal = new Label("Not implemented");
				break;
		}
		
		return retVal;
	}
	
	private HBox createBackgroundPicker(StylePropertyPersistenceObject value) {
		HBox root = new HBox(5);
		ComboBox<String> selector = new ComboBox<String>();
		
		selector.getItems().addAll("Color", "Image");
		
		ViewSwitcher<String> switcher = new ViewSwitcher<String>();
		ColorPicker picker = new ColorPicker();
		
		picker.valueProperty().addListener((v, o, n) -> {
			value.setPropertyValue("#" + Integer.toHexString(n.hashCode()));
		});
		
		HBox img = new HBox(5);
		
		TextField imgTxt = new TextField();
		imgTxt.textProperty().addListener((v, o, n) -> {
			value.setPropertyValue(n);
		});
		Button imgBrowse = new Button("Browse...");
		imgBrowse.setOnAction(e -> {
			FileChooser chooser = new FileChooser();
			File content = chooser.showOpenDialog(super.getScene().getWindow());
					
			if (content == null) {
				return;
			}
					
			String fileName = content.getAbsolutePath().replaceAll("\\\\", "/").replaceAll(" ", "%20");
			imgTxt.setText("file:" + fileName);
		});
		
		img.getChildren().add(imgTxt);
		img.getChildren().add(imgBrowse);
		
		switcher.addView("Color", () -> picker);
		switcher.addView("Image", () -> img);
		
		root.getChildren().add(selector);
		root.getChildren().add(switcher);
		
		selector.valueProperty().addListener((v, o, n) -> {
			if (n.equals("Color")) {
				if (hasBackgroundSize()) {
					newStyle.removeProperty("Background Size");
					newStyle.removeProperty("Background Repeat");
				}
			} else {
				if (!hasBackgroundSize()) {
					try {
						StylePropertyPersistenceObject bSize = new StylePropertyPersistenceObject();
						bSize.setPropertyType(StyleType.BackgroundSize);
						bSize.setPropertyValue("stretch");
						StylePropertyPersistenceObject bRepeat = new StylePropertyPersistenceObject();
						bRepeat.setPropertyType(StyleType.BackgroundRepeat);
						bRepeat.setPropertyValue("no-repeat");
						
						newStyle.addProperty("Background Size", bSize);
						newStyle.addProperty("Background Repeat", bRepeat);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			switcher.switchView(n);
		});
		
		if (!value.getPropertyValue().isEmpty() && !value.isBackgroundColor()) {
			imgTxt.setText(value.getPropertyValue());
			selector.setValue("Image");
		} else {
			if (value.getPropertyValue().isEmpty()) {
				picker.setValue(Color.WHITE);
				value.setPropertyValue("#" + Integer.toHexString(Color.WHITE.hashCode()));
			} else {
				picker.setValue(Color.web(value.getPropertyValue()));
			}
			
			selector.setValue("Color");
		}
		
		return root;
	}
	
	private boolean hasBackgroundSize() {
		for (Entry<String, StylePropertyPersistenceObject> prop : newStyle.getProperties().entrySet()) {
			if (prop.getValue().getPropertyType() == StyleType.BackgroundSize) {
				return true;
			}
		}
		
		return false;
	}
	
	private ColorPicker createColorPicker(StylePropertyPersistenceObject value) {
		ColorPicker picker = new ColorPicker();
		
		if (value.getPropertyValue() != null && !value.getPropertyValue().isEmpty()) {
			picker.setValue(Color.web(value.getPropertyValue()));
		}
		
		picker.valueProperty().addListener((v, o, n) -> {
			value.setPropertyValue("#" + Integer.toHexString(n.hashCode()));
		});
		
		return picker;
	}
	
	private Node createSizePicker(StylePropertyPersistenceObject value) {
		ComboBox<Integer> sizePicker = new ComboBox<Integer>();
		sizePicker.getItems().add(8);
		sizePicker.getItems().add(9);
		sizePicker.getItems().add(10);
		sizePicker.getItems().add(11);
		sizePicker.getItems().add(12);
		sizePicker.getItems().add(14);
		sizePicker.getItems().add(16);
		sizePicker.getItems().add(18);
		sizePicker.getItems().add(20);
		sizePicker.getItems().add(22);
		sizePicker.getItems().add(24);
		sizePicker.getItems().add(26);
		sizePicker.getItems().add(28);
		sizePicker.getItems().add(36);
		sizePicker.getItems().add(48);
		sizePicker.getItems().add(72);
		
		if (value.getPropertyValue() != null && !value.getPropertyValue().isEmpty()) {
			sizePicker.setValue(Integer.parseInt(value.getPropertyValue()));
		}
		
		sizePicker.valueProperty().addListener((v, o, n) -> {
			value.setPropertyValue(n.toString());
		});
		
		return sizePicker;
	}
	
	private Node createFamilyPicker(StylePropertyPersistenceObject value) {
		ComboBox<Label> picker = new ComboBox<Label>();
		String initialValue = value.getPropertyValue() == null ? null : value.getPropertyValue();
		
		for (String family : Font.getFamilies()) {
			Label label = new Label(family);
			label.setFont(new Font(family, 11));
			label.textFillProperty().set(Color.BLACK);
			picker.getItems().add(label);
		}
		
		if (initialValue != null) {
			picker.setValue(null);
			picker.setPromptText(initialValue);
		}
		
		picker.valueProperty().addListener((v, o, n) -> {
			value.setPropertyValue(n.getText());
		});
		
		return picker;
	}
	
	private Node createStylePicker(StylePropertyPersistenceObject value) {
		ComboBox<Label> picker = new ComboBox<Label>();
		
		Label label = new Label("normal");
		label.setStyle("-fx-font-style:normal;");
		label.textFillProperty().set(Color.BLACK);
		picker.getItems().add(label);
		
		Label label2 = new Label("italic");
		label2.setStyle("-fx-font-style:italic;");
		label2.textFillProperty().set(Color.BLACK);
		picker.getItems().add(label2);
		
		Label label3 = new Label("bold");
		label3.setStyle("-fx-font-weight:bold;");
		label3.textFillProperty().set(Color.BLACK);
		picker.getItems().add(label3);
		
		picker.valueProperty().addListener((v, o, n) -> {
			value.setPropertyValue(n.getText());
		});
		
		if (value.getPropertyValue() != null && !value.getPropertyValue().isEmpty()) {
			picker.setValue(null);
			picker.setPromptText(value.getPropertyValue());
		}
		
		return picker;
	}
	
	private RowConstraints createRow() {
		RowConstraints retVal = new RowConstraints();
		retVal.setFillHeight(true);
		retVal.setVgrow(Priority.ALWAYS);
		return retVal;
	}
}
