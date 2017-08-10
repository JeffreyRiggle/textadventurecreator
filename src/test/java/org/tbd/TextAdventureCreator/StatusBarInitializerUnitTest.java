package org.tbd.TextAdventureCreator;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.iroshell.core.LocationParameters;
import ilusr.iroshell.statusbar.IStatusBarService;
import ilusr.textadventurecreator.shell.StatusBarInitializer;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusItem;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;

public class StatusBarInitializerUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private IStatusBarService statusBarService;
	private ProjectStatusService statusService;
	
	private StatusBarInitializer statusBar;
	
	@Before
	public void setup() {
		statusBarService = mock(IStatusBarService.class);
		statusService = mock(ProjectStatusService.class);
		when(statusService.currentItem()).thenReturn(new SimpleObjectProperty<StatusItem>(mock(StatusItem.class)));
		
		statusBar = new StatusBarInitializer(statusBarService, statusService);
	}
	
	@Test
	public void testInitialize() {
		statusBar.initialize();
		verify(statusBarService, times(1)).addStatusBar(any(Node.class), any(LocationParameters.class));
	}

}
