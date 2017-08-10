package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Test;

import ilusr.textadventurecreator.statusbars.StatusIndicator;
import ilusr.textadventurecreator.statusbars.StatusItem;

public class StatusItemUnitTest {

	@Test
	public void testCreate() {
		StatusItem item = new StatusItem();
		assertNotNull(item);
	}

	@Test
	public void testCreateWithDisplay() {
		StatusItem item = new StatusItem("Test");
		assertNotNull(item);
		assertEquals("Test", item.displayText().get());
	}
	
	@Test
	public void testCreateWithDisplayAndAction() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action);
		assertNotNull(item);
		assertEquals("Test", item.displayText().get());
	}
	
	@Test
	public void testCreateWithDisplayActionAndTTL() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action, 12);
		assertNotNull(item);
		assertEquals("Test", item.displayText().get());
		assertEquals(new Integer(12), item.postFinishLiveTime().get());
	}
	
	@Test
	public void testDisplayText() {
		StatusItem item = new StatusItem("Test");
		assertEquals("Test", item.displayText().get());
		item.displayText().set("Test2");
		assertEquals("Test2", item.displayText().get());
	}
	
	@Test
	public void testProgress() {
		StatusItem item = new StatusItem("Test");
		item.progressAmount().set(.5);
		assertEquals(.5, item.progressAmount().get(), .001);
	}
	
	@Test
	public void testTTL() {
		StatusItem item = new StatusItem("Test");
		item.postFinishLiveTime().set(new Integer(12));
		assertEquals(new Integer(12), item.postFinishLiveTime().get());
	}
	
	@Test
	public void testFinished() {
		StatusItem item = new StatusItem("Test");
		assertFalse(item.finished().get());
		item.finished().set(true);
		assertTrue(item.finished().get());
	}
	
	@Test
	public void testIndicator() {
		StatusItem item = new StatusItem("Test");
		item.indicator().set(StatusIndicator.Good);
		assertEquals(StatusIndicator.Good, item.indicator().get());
	}
	
	@Test
	public void testSetRun() {
		StatusItem item = new StatusItem("Test");
		RunAction action = new RunAction();
		item.setOnStart(action);
		item.run();
		
		assertTrue(action.ran());
	}
	
	@Test
	public void testRun() {
		RunAction action = new RunAction();
		StatusItem item = new StatusItem("Test", action);
		
		item.run();
		assertTrue(action.ran());
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
