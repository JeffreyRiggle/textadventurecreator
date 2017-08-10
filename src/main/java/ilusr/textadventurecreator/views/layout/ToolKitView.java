package ilusr.textadventurecreator.views.layout;

import java.net.URL;
import java.util.ResourceBundle;

import ilusr.core.ioc.ServiceManager;
import ilusr.core.javafx.LocalDragboard;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class ToolKitView extends AnchorPane implements Initializable {

	@FXML
	private ListView<LayoutComponent> tools;
	
	private ToolKitModel model;
	
	/**
	 * Creates a toolkit view.
	 */
	public ToolKitView() {
		model = new ToolKitModel(ServiceManager.getInstance().<LayoutComponentProvider>get("LayoutComponentProvider"));
		FXMLLoader loader = new FXMLLoader(getClass().getResource("ToolKitView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		tools.getItems().addAll(model.components());
		tools.setCellFactory(new LayoutComponentLVFactory());
	}
	
	private class LayoutComponentLVFactory implements Callback<ListView<LayoutComponent>, ListCell<LayoutComponent>> {

		@Override
		public ListCell<LayoutComponent> call(ListView<LayoutComponent> arg0) {
			return new LayoutComponentListCell();
		}
		
		private class LayoutComponentListCell extends ListCell<LayoutComponent> {
			
			private LayoutComponent item;
			
			public LayoutComponentListCell() {
				super();
				super.setOnDragDetected((e) -> { startDrag(e); });
				super.setOnDragDone((e) -> { endDrag(e); });
			}
			
			@Override
			public void updateItem(LayoutComponent item, boolean empty) {
				super.updateItem(item, empty);
				this.item = item;
				
				if (empty || item == null) {
					setText("");
					setGraphic(null);
					return;
				}
				
				setDisplay(item);
			}
			
			private void setDisplay(LayoutComponent item) {
				if (item.getIcon() == null) {
					setText(item.getName());
					return;
				}
				
				HBox root = new HBox(5);
				root.getChildren().add(new ImageView(item.getIcon()));
				root.getChildren().add(new Label(item.getName()));
				setGraphic(root);
			}
			
			private void startDrag(MouseEvent e) {
				if (item == null) {
					return;
				}
				
				Dragboard db = super.startDragAndDrop(TransferMode.ANY);
		        
				if (item.getIcon() != null) {
					ClipboardContent clipboardContent = new ClipboardContent();
			        clipboardContent.putImage(item.getIcon());
			        db.setContent(clipboardContent);
				} else {
					ClipboardContent clipboardContent = new ClipboardContent();
			        clipboardContent.putString(item.getName());
			        db.setContent(clipboardContent);
				}
		        
		        LocalDragboard.getInstance().putValue(LayoutComponent.class, item);
		        
		        e.consume();
			}
			
			private void endDrag(DragEvent e) {
				LocalDragboard.getInstance().clear(LayoutComponent.class);
			}
		}
	}
}
