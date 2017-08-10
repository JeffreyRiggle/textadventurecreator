package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import ilusr.core.datamanager.xml.XmlGenerator;
import ilusr.core.datamanager.xml.XmlManager;
import ilusr.core.interfaces.Callback;
import ilusr.iroshell.services.ILayoutService;
import ilusr.textadventurecreator.settings.ISettingsManager;
import ilusr.textadventurecreator.shell.ProjectPersistenceManager;
import ilusr.textadventurecreator.shell.TextAdventureProjectPersistence;
import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class ProjectPersistenceManagerUnitTest {

	private XmlManager manager; 
	private ProjectStatusService statusService;
	private ILayoutService layoutService;
	private ISettingsManager settingsManager;
	
	private ProjectPersistenceManager projectManager;
	
	@Before
	public void setup() {
		manager = mock(XmlManager.class);
		when(manager.generator()).thenReturn(mock(XmlGenerator.class));
		
		statusService = mock(ProjectStatusService.class);
		layoutService = mock(ILayoutService.class);
		settingsManager = mock(ISettingsManager.class);
		
		projectManager = new ProjectPersistenceManager(manager, statusService, layoutService, settingsManager);
	}
	
	@Test
	public void testLoad() {
		try {
			Document doc = mock(Document.class);
			NodeList list = mock(NodeList.class);
			Element root = mock(Element.class);
			when(root.getNodeName()).thenReturn("TextAdventureProject");
			when(root.getAttributes()).thenReturn(mock(NamedNodeMap.class));
			when(root.getChildNodes()).thenReturn(mock(NodeList.class));
			when(list.item(0)).thenReturn(root);
			when(list.getLength()).thenReturn(1);
			when(doc.getChildNodes()).thenReturn(list);
			when(manager.document()).thenReturn(doc);
			
			assertNotNull(projectManager.load("c:/path/file.xml"));
			verify(manager, times(1)).saveLocation("c:/path/file.xml");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	@Test
	public void testLoadAsync() {
		try {
			ArgumentCaptor<StatusItem> item = ArgumentCaptor.forClass(StatusItem.class);
			
			Document doc = mock(Document.class);
			NodeList list = mock(NodeList.class);
			Element root = mock(Element.class);
			when(root.getNodeName()).thenReturn("TextAdventureProject");
			when(root.getAttributes()).thenReturn(mock(NamedNodeMap.class));
			when(root.getChildNodes()).thenReturn(mock(NodeList.class));
			when(list.item(0)).thenReturn(root);
			when(list.getLength()).thenReturn(1);
			when(doc.getChildNodes()).thenReturn(list);
			when(manager.document()).thenReturn(doc);
			
			LoadCallback cb = new LoadCallback();
			projectManager.loadAsync("c:/path/file.xml", cb);
			
			verify(statusService, times(1)).addStatusItemToQueue(item.capture());
			
			item.getValue().run();
			int waitTime = 0;
			while (waitTime < 1000) {
				if (cb.ran()) {
					break;
				}
				
				waitTime += 10;
				Thread.sleep(10);
			}
			
			assertTrue(cb.ran());
			assertNotNull(cb.project());
			verify(manager, times(1)).saveLocation("c:/path/file.xml");
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSaveAsync() {
		ArgumentCaptor<StatusItem> item = ArgumentCaptor.forClass(StatusItem.class);
		TextAdventureProjectPersistence project = mock(TextAdventureProjectPersistence.class);
		projectManager.saveAsync(project, "c:/path/file.xml");
		
		verify(statusService, times(1)).addStatusItemToQueue(item.capture());
		item.getValue().run();
		
		try {
			//TODO This is a hack.
			Thread.sleep(1000);
			verify(manager, times(1)).saveLocation("c:/path/file.xml");
			verify(project, times(1)).prepareXml();
			verify(manager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSaveAsyncWithoutPath() {
		ArgumentCaptor<StatusItem> item = ArgumentCaptor.forClass(StatusItem.class);
		TextAdventureProjectPersistence project = mock(TextAdventureProjectPersistence.class);
		projectManager.saveAsync(project);
		
		verify(statusService, times(1)).addStatusItemToQueue(item.capture());
		item.getValue().run();
		
		try {
			//TODO This is a hack.
			Thread.sleep(1000);
			verify(manager, times(0)).saveLocation(any());
			verify(project, times(1)).prepareXml();
			verify(manager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSave() {
		ArgumentCaptor<StatusItem> item = ArgumentCaptor.forClass(StatusItem.class);
		TextAdventureProjectPersistence project = mock(TextAdventureProjectPersistence.class);
		projectManager.save(project, "c:/path/file.xml");
		
		verify(statusService, times(1)).addStatusItemToQueue(item.capture());
		item.getValue().run();
		
		verify(manager, times(1)).saveLocation("c:/path/file.xml");
		try {
			verify(project, times(1)).prepareXml();
			verify(manager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	@Test
	public void testSaveWithoutPath() {
		ArgumentCaptor<StatusItem> item = ArgumentCaptor.forClass(StatusItem.class);
		TextAdventureProjectPersistence project = mock(TextAdventureProjectPersistence.class);
		projectManager.save(project);
		
		verify(statusService, times(1)).addStatusItemToQueue(item.capture());
		item.getValue().run();
		
		verify(manager, times(0)).saveLocation(any());
		try {
			verify(project, times(1)).prepareXml();
			verify(manager, times(1)).save();
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
	
	private class LoadCallback implements Callback<TextAdventureProjectPersistence> {

		private boolean ran;
		private TextAdventureProjectPersistence project;
		
		public LoadCallback() {
			ran = false;
		}
		
		@Override
		public void execute(TextAdventureProjectPersistence value) {
			ran = true;
			project = value;
		}
		
		public boolean ran() {
			return ran;
		}
		
		public TextAdventureProjectPersistence project() {
			return project;
		}
	}
}
