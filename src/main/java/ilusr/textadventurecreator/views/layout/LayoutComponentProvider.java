package ilusr.textadventurecreator.views.layout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ilusr.logrunner.LogRunner;
import ilusr.textadventurecreator.views.assets.AssetLoader;
import javafx.scene.image.Image;
import textadventurelib.persistence.LayoutNodePersistenceObject;
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;
import textadventurelib.persistence.StyleType;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutComponentProvider {

	private Map<String, LayoutComponent> components;
	
	/**
	 * Creates a LayoutComponentProvider.
	 */
	public LayoutComponentProvider() {
		components = new HashMap<String, LayoutComponent>();
		
		initialize();
	}
	
	private void initialize() {
		try {
			LogRunner.logger().info("Initializing layout components.");
			LayoutStyle contentViewStyle = new LayoutStyle("ContentView", new StyleSelectorPersistenceObject());
			contentViewStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			
			LayoutStyle textInputStyle = new LayoutStyle("TextInput", new StyleSelectorPersistenceObject());
			textInputStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			textInputStyle.addProperty("Foreground Color", new StylePropertyPersistenceObject(StyleType.Color, ""));
			textInputStyle.addProperty("Font Family", new StylePropertyPersistenceObject(StyleType.FontFamily, "Arial"));
			textInputStyle.addProperty("Font Size", new StylePropertyPersistenceObject(StyleType.FontSize, "10"));
			textInputStyle.addProperty("Font Type", new StylePropertyPersistenceObject(StyleType.FontStyle, "normal"));
			
			LayoutStyle buttonInputStyle = new LayoutStyle("Button Input", new StyleSelectorPersistenceObject());
			buttonInputStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			
			LayoutStyle buttonStyle = new LayoutStyle("Button", new StyleSelectorPersistenceObject());
			buttonStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			buttonStyle.addProperty("Foreground Color", new StylePropertyPersistenceObject(StyleType.Color, ""));
			buttonStyle.addProperty("Font Family", new StylePropertyPersistenceObject(StyleType.FontFamily, "Arial"));
			buttonStyle.addProperty("Font Size", new StylePropertyPersistenceObject(StyleType.FontSize, "10"));
			buttonStyle.addProperty("Font Type", new StylePropertyPersistenceObject(StyleType.FontStyle, "normal"));
			buttonStyle.setSelector(".button");
			
			LayoutStyle buttonHoverStyle = new LayoutStyle("Button Hover", new StyleSelectorPersistenceObject());
			buttonHoverStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			buttonHoverStyle.addProperty("Foreground Color", new StylePropertyPersistenceObject(StyleType.Color, ""));
			buttonHoverStyle.addProperty("Font Family", new StylePropertyPersistenceObject(StyleType.FontFamily, "Arial"));
			buttonHoverStyle.addProperty("Font Size", new StylePropertyPersistenceObject(StyleType.FontSize, "10"));
			buttonHoverStyle.addProperty("Font Type", new StylePropertyPersistenceObject(StyleType.FontStyle, "normal"));
			buttonHoverStyle.setSelector(".button:hover");
			
			buttonInputStyle.addChild(buttonStyle);
			buttonInputStyle.addChild(buttonHoverStyle);
			
			LayoutStyle textOnlyStyle = new LayoutStyle("TextOnly", new StyleSelectorPersistenceObject());
			textOnlyStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			textOnlyStyle.addProperty("Foreground Color", new StylePropertyPersistenceObject(StyleType.Color, ""));
			textOnlyStyle.addProperty("Font Family", new StylePropertyPersistenceObject(StyleType.FontFamily, "Arial"));
			textOnlyStyle.addProperty("Font Size", new StylePropertyPersistenceObject(StyleType.FontSize, "10"));
			textOnlyStyle.addProperty("Font Type", new StylePropertyPersistenceObject(StyleType.FontStyle, "normal"));
			
			LayoutStyle consoleStyle = new LayoutStyle("Console", new StyleSelectorPersistenceObject());
			consoleStyle.addProperty("Background Color", new StylePropertyPersistenceObject(StyleType.Background, ""));
			consoleStyle.addProperty("Foreground Color", new StylePropertyPersistenceObject(StyleType.Color, ""));
			consoleStyle.addProperty("Font Family", new StylePropertyPersistenceObject(StyleType.FontFamily, "Arial"));
			consoleStyle.addProperty("Font Size", new StylePropertyPersistenceObject(StyleType.FontSize, "10"));
			consoleStyle.addProperty("Font Type", new StylePropertyPersistenceObject(StyleType.FontStyle, "normal"));
			
			LayoutNodePersistenceObject contentView = new LayoutNodePersistenceObject();
			contentView.setLayoutValue("MultiTypeContentView");
			contentView.addProperty("contentResource", "");
			
			LayoutNodePersistenceObject textInput = new LayoutNodePersistenceObject();
			textInput.setLayoutValue("TextInputView");
			
			LayoutNodePersistenceObject buttonInput = new LayoutNodePersistenceObject();
			buttonInput.setLayoutValue("ButtonInput");
			buttonInput.addProperty("maxColumns", "10");
			
			LayoutNodePersistenceObject textOnly = new LayoutNodePersistenceObject();
			textOnly.setLayoutValue("TextOnlyView");
			
			LayoutNodePersistenceObject console = new LayoutNodePersistenceObject();
			console.setLayoutValue("ConsoleView");
			console.addProperty("prefix", ">");
			
			components.put("MultiTypeContentView", new LayoutComponent("Content View", contentView, 
					new Image(AssetLoader.getResourceURL("contentviewimage.png").toExternalForm(), 32, 32, true, true),
					contentViewStyle));
			components.put("TextInputView", new LayoutComponent("Text Input", textInput, 
					new Image(AssetLoader.getResourceURL("textinputview.png").toExternalForm(), 32, 32, true, true),
					textInputStyle));
			components.put("ButtonInput", new LayoutComponent("Button Input", buttonInput, 
					new Image(AssetLoader.getResourceURL("buttoninputview.png").toExternalForm(), 32, 32, true, true),
					buttonInputStyle));
			components.put("TextOnlyView", new LayoutComponent("Text Only", textOnly, 
					new Image(AssetLoader.getResourceURL("textonlyview.png").toExternalForm(), 32, 32, true, true),
					textOnlyStyle));
			components.put("ConsoleView", new LayoutComponent("Console", console, 
					new Image(AssetLoader.getResourceURL("consoleview.png").toExternalForm(), 32, 32, true, true),
					consoleStyle));
		} catch (Exception e) {
			LogRunner.logger().severe(e);
		}
	}
	
	/**
	 * 
	 * @param persistence The @see LayoutNodePersistenceObject to use.
	 * @return The associated @see LayoutComponent.
	 */
	public LayoutComponent getLayoutFromPersistence(LayoutNodePersistenceObject persistence) {
		return components.get(persistence.getLayoutValue());
	}
	
	/**
	 * 
	 * @return The @see LayoutComponent for content views.
	 */
	public LayoutComponent getContentView() {
		return components.get("MultiTypeContentView");
	}
	
	/**
	 * 
	 * @return The @see LayoutComponent for text input views.
	 */
	public LayoutComponent getTextInput() {
		return components.get("TextInputView");
	}
	
	/**
	 * 
	 * @return The @see LayoutComponent for button input views.
	 */
	public LayoutComponent getButtonInput() {
		return components.get("ButtonInput");
	}
	
	/**
	 * 
	 * @return The @see LayoutComponent for text only views.
	 */
	public LayoutComponent getTextOnly() {
		return components.get("TextOnlyView");
	}
	
	/**
	 * 
	 * @return The @see LayoutComponent for console views.
	 */
	public LayoutComponent getConsole() {
		return components.get("ConsoleView");
	}
	
	/**
	 * 
	 * @return All registered @see LayoutComponet 's.
	 */
	public List<LayoutComponent> getComponents() {
		return new ArrayList<LayoutComponent>(components.values());
	}
}
