package ilusr.textadventurecreator.views.layout;

import javafx.scene.Cursor;
import javafx.scene.Node;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import ilusr.core.data.Tuple;
import ilusr.core.interfaces.Callback;
import ilusr.core.url.InternalURLProvider;
import ilusr.iroshell.dockarea.ISelectable;
import ilusr.iroshell.dockarea.SelectionManager;
import ilusr.iroshell.services.IDialogService;
import ilusr.iroshell.services.IStyleContainerService;
import ilusr.textadventurecreator.views.Dialog;
import ilusr.textadventurecreator.views.IDialogProvider;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.PathTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * 
 * @author Jeff Riggle
 *
 */
public class LayoutComponentView extends GridPane implements Initializable, ISelectable {

	@FXML
	private ImageView componentImage;
	
	@FXML
	private Label componentLabel;
	
	@FXML
	private Pane componentNode;
	
	@FXML
	private BorderPane styleArea;
	
	@FXML
	private Button closeBtn;
	
	@FXML
	private Button styleBtn;
	
	@FXML
	private StackPane pane;
	
	@FXML
	private Rectangle rect1;

	@FXML
	private Rectangle rect2;
	
	@FXML
	private Rectangle rect3;
	
	@FXML
	private Rectangle rect4;
	
	@FXML
	private Rectangle rect5;
	
	@FXML
	private Rectangle rect6;
	
	@FXML
	private Rectangle rect7;
	
	@FXML
	private Rectangle rect8;
	
	@FXML
	private Rectangle rect9;
	
	@FXML
	private HBox buttonArea;
	
	private final IDialogProvider dialogProvider;
	private final IStyleContainerService styleService;
	private final InternalURLProvider urlProvider;
	
	private LayoutComponent component;
	private Runnable closeAction;
	private Runnable styleUpdated;
	private StackPane parent;
	private SelectionManager manager;
	private IDialogService dialogService;
	
	private Callback<Tuple<Double, Double>> resize;
	private Callback<Tuple<Double, Double>> relocate;
	private List<Resizer> resizers;
	private NodeRelocator relocater;
	
	/**
	 * 
	 * @param component A @see LayoutComponent to bind to.
	 * @param closeAction A @see Runnable to run when this view is closed.
	 * @param parent A @see StackPane that hosts the view.
	 * @param manager A @see SelectionManager that manages the selection of all LayoutComponents.
	 * @param dialogService A @see IDialogService to display dialogs.
	 * @param dialogProvider a @see IDialogProvider to create dialogs.
	 * @param styleService service to manage styles.
	 * @param urlProvider A @see InternalURLProvider to create internal resources.
	 */
	public LayoutComponentView(LayoutComponent component, 
							   Runnable closeAction,
							   StackPane parent, 
							   SelectionManager manager,
							   IDialogService dialogService,
							   IDialogProvider dialogProvider,
							   IStyleContainerService styleService,
							   InternalURLProvider urlProvider) {
		this.component = component;
		this.closeAction = closeAction;
		this.parent = parent;
		this.manager = manager;
		this.dialogService = dialogService;
		this.dialogProvider = dialogProvider;
		this.styleService = styleService;
		this.urlProvider = urlProvider;
		
		resizers = new ArrayList<Resizer>();
		
		manager.addSelectionRequester(this);
		FXMLLoader loader = new FXMLLoader(getClass().getResource("LayoutComponentView.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (component.getDisplayNode() != null) {
			setupComponentView();
		} else if (component.getIcon() != null) {
			setupImageView();
		} else {
			componentLabel.textProperty().set(component.getName());
			componentImage.setVisible(false);
			componentNode.setVisible(false);
		}
		
		pane.setOnMouseEntered(e -> {
			enterAnimation();
		});
		
		pane.setOnMouseExited(e -> {
			exitAnimation();
		});
		
		pane.setOnMouseClicked((e) -> {
			manager.RequestSelection(this);
		});
		
		closeBtn.setOnAction(e -> {
			close();
		});
		
		styleBtn.setOnAction(e -> {
			StyleCreatorView styleView = new StyleCreatorView(component, styleService, urlProvider);
			Dialog dialog = dialogProvider.create(styleView);
			
			dialog.setOnComplete(() -> {
				component.style().set(styleView.getUpdatedStyles());
				update();
			});
			
			dialogService.displayModal(dialog, "Style Manager");
		});
		
		super.getStylesheets().add(getClass().getResource("layoutcomponent.css").toExternalForm());
		manager.RequestSelection(this);
		
		Platform.runLater(() -> {
			createResizers();
		});
	}
	
	/**
	 * 
	 * @return The associated @see LayoutComponent.
	 */
	public LayoutComponent getLayoutComponent() {
		return component;
	}
	
	private void createResizers() {
		AnchorPane aparent = (AnchorPane)this.getParent();
		resizers.add(new Resizer(rect1, Cursor.NW_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect2, Cursor.N_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect3, Cursor.NE_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect4, Cursor.W_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect5, Cursor.E_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect6, Cursor.SW_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect7, Cursor.S_RESIZE, aparent, parent, c -> { resize(c); }));
		resizers.add(new Resizer(rect8, Cursor.SE_RESIZE, aparent, parent, c -> { resize(c); }));
		relocater = new NodeRelocator(rect9, aparent, parent, c -> { relocate(c); });
	}
	
	private void resize(Tuple<Double, Double> c) {
		if (resize != null) {
			resize.execute(c);
		}
		
		updateRegions();
	}
	
	private void relocate(Tuple<Double, Double> c) {
		if (relocate != null) {
			relocate.execute(c);
		}
		
		updateRegions();
	}
	
	private void updateRegions() {
		Platform.runLater(() -> {
			resizers.forEach(r -> { r.updateRegion((AnchorPane)this.getParent());});
			relocater.updateRegion((AnchorPane)this.getParent());
		});
	}
	
	/**
	 * 
	 * @param closeAction The new action to run on close.
	 */
	public void updateCloseAction(Runnable closeAction) {
		this.closeAction = closeAction;
	}
	
	/**
	 * 
	 * @param resize A Callback to run when this node is resized.
	 */
	public void setResizeAction(Callback<Tuple<Double, Double>> resize) {
		this.resize = resize;
	}
	
	/**
	 * 
	 * @param relocate A Callback to run when this node is moved.
	 */
	public void setRelocateAction(Callback<Tuple<Double, Double>> relocate) {
		this.relocate = relocate;
	}
	
	/**
	 * 
	 * @param update A action to run when the style for this component changes.
	 */
	public void setUpdateAction(Runnable update) {
		this.styleUpdated = update;
	}
	
	private void update() {
		if (styleUpdated != null) {
			styleUpdated.run();
		}
	}
	
	private void setupImageView() {
		componentImage.setImage(component.getIcon());
		componentImage.setPreserveRatio(false);
		componentImage.fitWidthProperty().set(32);
		componentImage.fitHeightProperty().set(32);
		
		super.widthProperty().addListener((v, o, n) -> {
			componentImage.setFitWidth(n.doubleValue() - 15);
		});
		super.heightProperty().addListener((v, o, n) -> {
			componentImage.setFitHeight(n.doubleValue() - 15);
		});
		
		componentLabel.setVisible(false);
		componentNode.setVisible(false);
	}
	
	private void setupComponentView() {
		componentNode.setMaxWidth(super.getWidth() -15);
		componentNode.setMaxHeight(super.getHeight() -15);
		
		super.widthProperty().addListener((v, o, n) -> {
			componentNode.setMaxWidth(n.doubleValue() - 15);
		});
		super.heightProperty().addListener((v, o, n) -> {
			componentNode.setMaxHeight(n.doubleValue() - 15);
		});
		
		componentImage.setVisible(false);
		componentLabel.setVisible(false);
		
		Platform.runLater(() -> {
			Node node = component.getDisplayNode();
			AnchorPane root = new AnchorPane();
			componentNode.getChildren().add(root);
			root.getChildren().add(node);
			
			AnchorPane.setBottomAnchor(node, 0.0);
			AnchorPane.setTopAnchor(node, 0.0);
			AnchorPane.setLeftAnchor(node, 0.0);
			AnchorPane.setRightAnchor(node, 0.0);
		});
	}
	
	private void enterAnimation() {
		FadeTransition ftrans = new FadeTransition(Duration.millis(500), styleArea);
		ftrans.setFromValue(0.0);
		ftrans.setToValue(1.0);
		ftrans.setAutoReverse(false);

		Path path = new Path();
		double initalX = -buttonArea.getLayoutX() + buttonArea.getWidth() / 2;
		double initalY = -buttonArea.getLayoutY() + buttonArea.getHeight() / 2;
		path.getElements().add(new MoveTo(initalX, initalY + 20));
		path.getElements().add(new LineTo(initalX, initalY));
		PathTransition ptrans = new PathTransition();
		ptrans.setDuration(Duration.millis(500));
		ptrans.setNode(buttonArea);
		ptrans.setPath(path);
		
		ParallelTransition pltrans = new ParallelTransition();
		pltrans.getChildren().add(ftrans);
		pltrans.getChildren().add(ptrans);
		pltrans.play();
	}
	
	private void exitAnimation() {
		FadeTransition ftrans = new FadeTransition(Duration.millis(500), styleArea);
		ftrans.setFromValue(1.0);
		ftrans.setToValue(0.0);
		ftrans.setAutoReverse(false);
	
		Path path = new Path();
		double initalX = -buttonArea.getLayoutX() + buttonArea.getWidth() / 2;
		double initalY = -buttonArea.getLayoutY() + buttonArea.getHeight() / 2;
		path.getElements().add(new MoveTo(initalX, initalY));
		path.getElements().add(new LineTo(initalX, initalY + 20));
		PathTransition ptrans = new PathTransition();
		ptrans.setDuration(Duration.millis(500));
		ptrans.setNode(buttonArea);
		ptrans.setPath(path);
		
		ParallelTransition pltrans = new ParallelTransition();
		pltrans.getChildren().add(ftrans);
		pltrans.getChildren().add(ptrans);
		pltrans.play();
	}
	
	private void close() {
		if (closeAction != null) {
			closeAction.run();
		}
	}

	@Override
	public void select() {
		showResizers();
	}

	@Override
	public void deSelect() {
		hideResizers();
	}
	
	private void showResizers() {
		rect1.setVisible(true);
		rect2.setVisible(true);
		rect3.setVisible(true);
		rect4.setVisible(true);
		rect5.setVisible(true);
		rect6.setVisible(true);
		rect7.setVisible(true);
		rect8.setVisible(true);
		rect9.setVisible(true);
	}
	
	private void hideResizers() {
		rect1.setVisible(false);
		rect2.setVisible(false);
		rect3.setVisible(false);
		rect4.setVisible(false);
		rect5.setVisible(false);
		rect6.setVisible(false);
		rect7.setVisible(false);
		rect8.setVisible(false);
		rect9.setVisible(false);
	}
}
