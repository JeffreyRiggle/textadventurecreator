package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.CountDownLatch;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.FXThread;
import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.debug.BodyPartDebugModel;
import ilusr.textadventurecreator.debug.DebugNamedObjectModel;
import ilusr.textadventurecreator.language.ILanguageService;
import javafx.application.Platform;
import playerlib.characteristics.ICharacteristic;
import playerlib.equipment.BodyPart;
import playerlib.equipment.IBodyPart;

public class BodyPartDebugUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private final String HEAD = "Head";
	private final String HEAD_DESCRIPTION = "The players head";
	
	private IBodyPart bodyPart;
	private ILanguageService languageService;
	
	@Before
	public void setup() {
		bodyPart = new BodyPart(HEAD);
		bodyPart.description(HEAD_DESCRIPTION);
		
		languageService = mock(ILanguageService.class);
		when(languageService.getValue("Characteristics")).thenReturn("Characteristics");
	}
	
	@Test
	public void testCreate() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		assertNotNull(model);
	}

	@Test
	public void testDescription() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		assertEquals(HEAD, model.name().get());
	}
	
	@Test
	public void testName() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		assertEquals(HEAD_DESCRIPTION, model.description().get());
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testCharacteristicAdded() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		ICharacteristic chr = mock(ICharacteristic.class);
		when(chr.name()).thenReturn("Hair Color");
		bodyPart.addCharacteristic(chr);
		
		waitForPlatform();
		assertFalse(model.added().get());
		assertTrue(model.changed().get());
		assertFalse(model.removed().get());
		assertEquals(1, model.charactersitics().size());
		
		boolean isAdded = false;
		for (DebugNamedObjectModel chrmodel : model.charactersitics()) {
			if (chrmodel.name().get().equals(chr.name())) {
				isAdded = chrmodel.added().get();
				break;
			}
		}
		
		assertTrue(isAdded);
	}
	
	@Test
	@FXThread(runOnFXThread = false)
	public void testCharacteristicRemoved() {
		ICharacteristic chr = mock(ICharacteristic.class);
		when(chr.name()).thenReturn("Hair Color");
		bodyPart.addCharacteristic(chr);
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		assertEquals(1, model.charactersitics().size());
		
		bodyPart.removeCharacteristic(chr);
		
		waitForPlatform();
		assertFalse(model.added().get());
		assertTrue(model.changed().get());
		assertFalse(model.removed().get());
		assertEquals(1, model.charactersitics().size());
		
		boolean isRemoved = false;
		for (DebugNamedObjectModel chrmodel : model.charactersitics()) {
			if (chrmodel.name().get().equals(chr.name())) {
				isRemoved = chrmodel.removed().get();
				break;
			}
		}
		
		assertTrue(isRemoved);
	}
	
	@Test
	public void testReset() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		ICharacteristic chr = mock(ICharacteristic.class);
		when(chr.name()).thenReturn("Hair Color");
		bodyPart.addCharacteristic(chr);
		model.resetChangeNotifications();
		
		assertFalse(model.added().get());
		assertFalse(model.changed().get());
		assertFalse(model.removed().get());
	}
	
	@Test
	public void testLanguageChanged() {
		BodyPartDebugModel model = new BodyPartDebugModel(bodyPart, languageService);
		assertEquals("Characteristics", model.charTitle().get());
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
