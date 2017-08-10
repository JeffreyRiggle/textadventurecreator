package ilusr.textadventurecreator.library;

import ilusr.iroshell.services.TabContent;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LibraryContentTab extends TabContent {

	private final ILanguageService languageService;
	private Label titleLabel;
	
	/**
	 * 
	 * @param view The @see LibraryView to display.
	 * @param languageService A @see LanguageService to use for display strings.
	 */
	public LibraryContentTab(LibraryView view, ILanguageService languageService) {
		this.languageService = languageService;
		super.content().set(view);
		super.titleGraphic(createTitle());
		super.toolTip().set(languageService.getValue(DisplayStrings.LIBRARY));
		super.canClose().set(true);
		
		languageService.addListener(() -> {
			super.toolTip().set(languageService.getValue(DisplayStrings.LIBRARY));
			titleLabel.setText(languageService.getValue(DisplayStrings.LIBRARY));
		});
	}
	
	private Node createTitle() {
		HBox root = new HBox(5);
		
		root.getChildren().add(new ImageView(new Image(AssetLoader.getResourceURL("Library.png").toExternalForm())));
		titleLabel = new Label(languageService.getValue(DisplayStrings.LIBRARY));
		titleLabel.setPrefHeight(25);
		root.getChildren().add(titleLabel);
		return root;
	}
}
