package ilusr.textadventurecreator.views;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.logrunner.LogRunner;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LandingPageView extends AnchorPane implements Initializable {

	@FXML
	private Hyperlink createProject;
	
	@FXML
	private Hyperlink openProject;
	
	@FXML
	private CheckBox hideLanding;
	
	@FXML
	private Hyperlink tutorial;
	
	@FXML
	private Hyperlink findMore;
	
	@FXML
	private Label title;
	
	@FXML
	private Label tagLine;
	
	private LandingPageModel model;
	
	/**
	 * 
	 * @param model The @see LandingPageModel to bind to.
	 */
	public LandingPageView(LandingPageModel model) {
		this.model = model;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LandingPageView.fxml"));
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
		super.getStylesheets().add(getClass().getResource("LandingPage.css").toExternalForm());
		
		createProject.setOnAction((e) -> {
			model.createNewProject();
		});
		createProject.textProperty().bind(model.newProjectText());
		
		openProject.setOnAction((e) -> {
			model.openProject(super.getScene().getWindow());
		});
		openProject.textProperty().bind(model.loadProjectText());
		
		model.hideLanding().bindBidirectional(hideLanding.selectedProperty());
		hideLanding.textProperty().bind(model.dontShowText());
		
		tutorial.textProperty().bind(model.tutorialText());
		tutorial.setOnAction((e) -> {
			model.showTutorial();
		});
		
		findMore.textProperty().bind(model.findMoreText());
		findMore.setOnAction((e) -> {
			model.showMore();
		});
		
		title.textProperty().bind(model.titleText());
		tagLine.textProperty().bind(model.tagLineText());
	}
}
