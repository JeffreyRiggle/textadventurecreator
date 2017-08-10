package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.junit.Before;
import org.junit.Test;

import ilusr.iroshell.services.ILayoutService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;

public class TextAdventureProjectPersistenceUnitTest {

	private ILayoutService layoutService;
	private ISettingsManager settingsManager;
	
	private TextAdventureProjectPersistence persistence;
	
	@Before
	public void setup() {
		layoutService = mock(ILayoutService.class);
		settingsManager = mock(ISettingsManager.class);
		
		try {
			persistence = new TextAdventureProjectPersistence(layoutService, settingsManager);
		} catch (TransformerConfigurationException | ParserConfigurationException e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testTextAdventure() {
		assertNotNull(persistence.getTextAdventure());
	}

	@Test
	public void testGameInfo() {
		assertNotNull(persistence.getGameInfo());
	}
	
	@Test
	public void testIconLocation() {
		assertNotNull(persistence.getIconLocation());
		persistence.setIconLocation("c:/testfile/test.png");
		assertEquals("c:/testfile/test.png", persistence.getIconLocation());
	}
	
	@Test
	public void testStandAlone() {
		assertFalse(persistence.getIsStandAlone());
		persistence.setIsStandAlone(true);
		assertTrue(persistence.getIsStandAlone());
	}
	
	@Test
	public void testDev() {
		assertFalse(persistence.getIsDev());
		persistence.setIsDev(true);
		assertTrue(persistence.getIsDev());
	}
	
	@Test
	public void testLanguage() {
		assertNotNull(persistence.getLanguage());
		persistence.setLanguage("Java");
		assertEquals("Java", persistence.getLanguage());
	}
	
	@Test
	public void testProjectLocation() {
		assertNotNull(persistence.getProjectLocation());
		persistence.setProjectLocation("c:/testpath/testfile.xml");
		assertEquals("c:/testpath/testfile.xml", persistence.getProjectLocation());
	}
	
	@Test
	public void testBackgroundLocation() {
		assertNotNull(persistence.getBackgroundLocation());
		persistence.setBackgroundLocation("c:/testpath/testfile.jpg");
		assertEquals("c:/testpath/testfile.jpg", persistence.getBackgroundLocation());
	}
	
	@Test
	public void testLayout() {
		assertNotNull(persistence.layout());
	}
	
	@Test
	public void testPrepareXml() {
		persistence.getGameInfo().gameName("My Game");
		persistence.getGameInfo().creator("UnitTester");
		
		persistence.getTextAdventure().gameName("My Game");
		
		persistence.setIconLocation("c:/ico/test.png");
		persistence.setProjectLocation("c:/testloc/test.xml");
		persistence.setLanguage("Java");
		persistence.setBackgroundLocation("c:/back/testbak.png");
		
		try {
			persistence.prepareXml();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testConversion() {
		try {
			TextAdventureProjectPersistence original = new TextAdventureProjectPersistence(layoutService, settingsManager);
			original.getGameInfo().gameName("My Game");
			original.getGameInfo().creator("UnitTester");
			
			original.getTextAdventure().gameName("My Game");
			
			original.setIconLocation("c:/ico/test.png");
			original.setProjectLocation("c:/testloc/test.xml");
			original.setBackgroundLocation("c:/back/testbak.png");
			original.prepareXml();
			
			persistence.convertFromPersistence(original);
		} catch (Exception e) {
			fail(e.getMessage());
		}
		
		assertEquals("My Game", persistence.getGameInfo().gameName());
		assertEquals("UnitTester", persistence.getGameInfo().creator());
		assertEquals("My Game", persistence.getTextAdventure().gameName());
		assertEquals("c:/ico/test.png", persistence.getIconLocation());
		assertEquals("c:/testloc/test.xml", persistence.getProjectLocation());
		assertEquals("c:/back/testbak.png", persistence.getBackgroundLocation());
		assertFalse(persistence.getIsDev());
		assertFalse(persistence.getIsStandAlone());
		assertEquals(new String(), persistence.getLanguage());
	}
}
