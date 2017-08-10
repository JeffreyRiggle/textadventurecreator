package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.statusbars.ProjectStatusService;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class ProjectStatusServiceUnitTest {

	private ProjectStatusService service;
	
	@Before
	public void setup() {
		service = new ProjectStatusService();
	}
	
	@Test
	public void testCreate() {
		assertNotNull(service);
		assertNotNull(service.currentItem());
	}

	@Test
	public void testAddItem() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action);
		service.addStatusItemToQueue(item);
		
		assertTrue(action.ran());
	}
	
	@Test
	public void testRemovePendingItem() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action);
		
		RunAction action2 = new RunAction();
		StatusItem item2 = new StatusItem("Test2", action2);
		service.addStatusItemToQueue(item);
		service.addStatusItemToQueue(item2);
		service.removeStatusItemFromQueue(item2);
		
		item.finished().set(true);
		
		assertTrue(action.ran());
		assertFalse(action2.ran());
	}
	
	@Test
	public void testMultipleAdd() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action);
		
		RunAction action2 = new RunAction();
		StatusItem item2 = new StatusItem("Test2", action2);
		service.addStatusItemToQueue(item);
		service.addStatusItemToQueue(item2);
		
		item.finished().set(true);
		
		assertTrue(action.ran());
		assertTrue(action2.ran());
	}
	
	private class RunAction implements Runnable {
		
		private boolean ran;
		
		public RunAction() {
			ran = false;
		}
		
		@Override
		public void run() {
			ran = true;
		}
		
		public boolean ran() {
			return ran;
		}
		
	}
}
