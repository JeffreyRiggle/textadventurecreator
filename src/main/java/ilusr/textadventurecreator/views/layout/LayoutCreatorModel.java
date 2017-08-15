package ilusr.textadventurecreator.views.layout;

import java.awt.GridLayout;
import java.util.UUID;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;
import ilusr.textadventurecreator.views.LanguageAwareString;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.StackPane;
import textadventurelib.layout.JFXTextAdventureLayout;
import textadventurelib.layout.TextAdventureLayoutModel;
import textadventurelib.persistence.LayoutGridPersistenceObject;
import textadventurelib.persistence.LayoutPersistenceObject;
import textadventurelib.persistence.LayoutType;
import textadventurelib.persistence.StylePersistenceObject;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutCreatorModel {

	private final IDialogService dialogService;
	private final InternalURLProvider provider;
	private final SelectionManager selectionManager;
	
	private LayoutPersistenceObject layout;
	private SimpleStringProperty id;
	private LanguageAwareString idText;
	private LanguageAwareString rowText;
	private LanguageAwareString colText;
	private LanguageAwareString toolkitText;
	private LanguageAwareString propertiesText;
	private LanguageAwareString compileText;
	private SimpleIntegerProperty columns;
	private SimpleIntegerProperty rows;
	private SimpleObjectProperty<LayoutComponent> selected;
	
	/**
	 * 
	 * @param layout A @see LayoutPersistenceObject to bind to.
	 * @param languageService A @see LanguageService to provide display strings.
	 * @param dialogService A @see IDialogService to provide dialogs.
	 * @param provider A @see InternalURLProvider to provide resources.
	 * @param selectionManager A @see SelectionManager to manage selection.
	 */
	public LayoutCreatorModel(LayoutPersistenceObject layout, 
							  ILanguageService languageService,
							  IDialogService dialogService,
							  InternalURLProvider provider,
							  SelectionManager selectionManager) {
		this.layout = layout;
		this.dialogService = dialogService;
		this.provider = provider;
		this.selectionManager = selectionManager;
		
		id = new SimpleStringProperty(layout.id());
		rows = new SimpleIntegerProperty(layout.getLayout().getRows());
		columns = new SimpleIntegerProperty(layout.getLayout().getColumns());
		idText = new LanguageAwareString(languageService, DisplayStrings.LAYOUT_ID);
		rowText = new LanguageAwareString(languageService, DisplayStrings.ROWS);
		colText = new LanguageAwareString(languageService, DisplayStrings.COLUMNS);
		toolkitText = new LanguageAwareString(languageService, DisplayStrings.TOOLKIT);
		propertiesText = new LanguageAwareString(languageService, DisplayStrings.PROPERTIES);
		compileText = new LanguageAwareString(languageService, DisplayStrings.COMPILE);
		selected = new SimpleObjectProperty<LayoutComponent>();
		
		initialize();
	}
	
	private void initialize() {
		if (layout.layoutType() != LayoutType.Custom) {
			layout.layoutType(LayoutType.Custom);
		}
		
		id.addListener((v, o, n) -> {
			layout.id(n);
		});
		
		rows.addListener((v, o, n) -> {
			layout.getLayout().setRows(n.intValue());
		});
		
		columns.addListener((v, o, n) -> {
			layout.getLayout().setColumns(n.intValue());
		});
		
		selectionManager.addSelectionListener(s -> {
			if (!(s instanceof LayoutComponentView)) {
				return;
			}
			
			selected.set(((LayoutComponentView)s).getLayoutComponent());
		});
	}
	
	/**
	 * 
	 * @return The associated grid object.
	 */
	public LayoutGridPersistenceObject layoutGrid() {
		return layout.getLayout();
	}
	
	/**
	 * 
	 * @return The associated style object.
	 */
	public StylePersistenceObject style() {
		return layout.getStyle();
	}
	
	/**
	 * 
	 * @return The id of the layout.
	 */
	public SimpleStringProperty id() {
		return id;
	}
	
	/**
	 * 
	 * @return Display string for id.
	 */
	public SimpleStringProperty idText() {
		return idText;
	}
	
	/**
	 * 
	 * @return Display string for rows.
	 */
	public SimpleStringProperty rowText() {
		return rowText;
	}
	
	/**
	 * 
	 * @return Display string for columns.
	 */
	public SimpleStringProperty colText() {
		return colText;
	}
	
	/**
	 * 
	 * @return Display string for toolkit.
	 */
	public SimpleStringProperty toolkitText() {
		return toolkitText;
	}
	
	/**
	 * 
	 * @return Display string for properties.
	 */
	public SimpleStringProperty propertiesText() {
		return propertiesText;
	}
	
	/**
	 * 
	 * @return Display string for compile.
	 */
	public SimpleStringProperty compileText() {
		return compileText;
	}
	
	/**
	 * 
	 * @return The number of columns for the layout.
	 */
	public SimpleIntegerProperty columns() {
		return columns;
	}
	
	/**
	 * 
	 * @return The number of columns for the layout.
	 */
	public SimpleIntegerProperty rows() {
		return rows;
	}
	
	/**
	 * 
	 * @return The associated persistence object.
	 */
	public LayoutPersistenceObject persistableLayout() {
		return layout;
	}
	
	/**
	 * 
	 * @return The selected LayoutComponent.
	 */
	public SimpleObjectProperty<LayoutComponent> selectedComponent() {
		return selected;
	}
	
	/**
	 * 
	 * @return The associated selection manager.
	 */
	public SelectionManager getSelectionManager() {
		return selectionManager;
	}
	
	/**
	 * Compiles and displays the layout.
	 */
	public void compile() {
		LogRunner.logger().info(String.format("Compiling layout %s", id.get()));
		
		TextAdventureLayoutModel model = new TextAdventureLayoutModel("Text Log Area!");
		model.resource().set("https://www.google.com/");
		model.inputs().addAll("Button1", "Button2", "Button3");
		String style = layout.getStyle().compile();
		System.out.printf("Using style\n%s\n", style);
		String resource = null;
		if (style != null && !style.isEmpty()) {
			resource = provider.prepareURL(style, UUID.randomUUID().toString().replaceAll("-", "") + ".css");
		}
		
		JFXTextAdventureLayout layout = new JFXTextAdventureLayout(this.layout.getLayout().compile(), model, resource);
		layout.animate();
		SwingNode node = new SwingNode();
		
		SwingUtilities.invokeLater(() -> {
			JPanel panel = new JPanel();
			panel.setLayout(new GridLayout(1,1));
			panel.add(layout.container(), 0);
			node.setContent(panel);
		});
		
		StackPane pane = new StackPane();
		pane.getChildren().add(node);
		
		LogRunner.logger().info(String.format("Displaying layout %s", id.get()));
		dialogService.displayModal(pane);
	}
}
