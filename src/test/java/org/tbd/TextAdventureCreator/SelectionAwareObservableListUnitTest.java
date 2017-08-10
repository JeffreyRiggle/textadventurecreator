package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Test;

import ilusr.textadventurecreator.views.SelectionAwareObservableList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class SelectionAwareObservableListUnitTest {

	@Test
	public void testCreate() {
		SelectionAwareObservableList<String> slist = new SelectionAwareObservableList<>();
		assertNotNull(slist.list());
		assertNotNull(slist.selected());
	}

	@Test
	public void testCreateWithList() {
		ObservableList<String> baselist = FXCollections.observableArrayList();
		baselist.add("Test");
		
		SelectionAwareObservableList<String> slist = new SelectionAwareObservableList<>(baselist);
		assertNotNull(slist.list());
		assertTrue(slist.list().contains("Test"));
		assertNotNull(slist.selected());
	}
	
	@Test
	public void testCreateWithListAndSelected() {
		ObservableList<String> baselist = FXCollections.observableArrayList();
		baselist.add("Test");
		
		SelectionAwareObservableList<String> slist = new SelectionAwareObservableList<>(baselist, "Test");
		assertNotNull(slist.list());
		assertTrue(slist.list().contains("Test"));
		assertNotNull(slist.selected());
		assertEquals("Test", slist.selected().get());
	}
	
	@Test
	public void testList() {
		SelectionAwareObservableList<String> slist = new SelectionAwareObservableList<>();
		assertEquals(0, slist.list().size());
		slist.list().add("TestValue");
		assertEquals(1, slist.list().size());
		assertTrue(slist.list().contains("TestValue"));
	}
	
	@Test
	public void testSelected() {
		SelectionAwareObservableList<String> slist = new SelectionAwareObservableList<>();
		slist.list().add("TestValue1");
		slist.list().add("TestValue2");
		assertNull(slist.selected().get());
		slist.selected().set("TestValue1");
		assertEquals("TestValue1", slist.selected().get());
		slist.selected().set("TestValue2");
		assertEquals("TestValue2", slist.selected().get());
	}
}
