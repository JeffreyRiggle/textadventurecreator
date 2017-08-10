package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.FXThread;
import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.statusbars.ProjectStatusModel;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;

public class ProjectStatusModelUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private ProjectStatusService statusService;
	
	private ProjectStatusModel model;
	
	@Before
	public void setup() {
		statusService = mock(ProjectStatusService.class);
		when(statusService.currentItem()).thenReturn(new SimpleObjectProperty<StatusItem>());
		
		model = new ProjectStatusModel(statusService);
	}
	
	@Test
	public void testStatusText() {
		assertNotNull(model.statusText());
	}
	
	@Test
	public void testProgressAmount() {
		assertNotNull(model.progressAmount());
	}
	
	@Test
	public void testIndicator() {
		assertNotNull(model.indicator());
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testChangeStatusItem() {
		StatusItem item = new StatusItem("Test");
		item.indicator().set(StatusIndicator.Good);
		item.progressAmount().set(.9);
		statusService.currentItem().set(item);
		waitForPlatform();
		
		assertEquals("Test", model.statusText().get());
		assertEquals(StatusIndicator.Good, model.indicator().get());
		assertEquals(.9, model.progressAmount().get(), 0.001);
	}
	
	private void waitForPlatform() {
        final CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
        	latch.countDown();
        });
        
        try {
        	latch.await();
        } catch (Exception e) { }
	}
}
