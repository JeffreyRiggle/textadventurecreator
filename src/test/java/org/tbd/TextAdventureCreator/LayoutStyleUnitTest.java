package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.views.layout.LayoutStyle;
import textadventurelib.persistence.StylePropertyPersistenceObject;
import textadventurelib.persistence.StyleSelectorPersistenceObject;

public class LayoutStyleUnitTest {

	private String displayName;
	private StyleSelectorPersistenceObject pstyle;
	
	private LayoutStyle style;
	
	@Before
	public void setup() {
		displayName = "TestStyle";
		pstyle = mock(StyleSelectorPersistenceObject.class);
		when(pstyle.getSelector()).thenReturn("#test");
		
		style = new LayoutStyle(displayName, pstyle);
	}
	
	@Test
	public void testDisplayName() {
		assertEquals(displayName, style.displayName());
	}

	@Test
	public void testSelector() {
		assertEquals("#test", style.getSelector());
		style.setSelector("#test2");
		verify(pstyle, times(1)).setSelector("#test2");
	}
	
	@Test
	public void testAddProperty() {
		StylePropertyPersistenceObject prop = mock(StylePropertyPersistenceObject.class);
		style.addProperty("TestProp", prop);
		verify(pstyle, times(1)).addStyleProperty(prop);
		assertEquals(prop, style.getPropertyValue("TestProp"));
	}
	
	@Test
	public void testRemoveProperty() {
		StylePropertyPersistenceObject prop = mock(StylePropertyPersistenceObject.class);
		style.addProperty("TestProp", prop);
		style.removeProperty("TestProp");
		verify(pstyle, times(1)).removeStyleProperty(prop);
		assertNull(style.getPropertyValue("TestProp"));
	}
	
	@Test
	public void testUpdateProperty() {
		StylePropertyPersistenceObject prop = mock(StylePropertyPersistenceObject.class);
		style.addProperty("TestProp", prop);
		StylePropertyPersistenceObject prop2 = mock(StylePropertyPersistenceObject.class);
		style.updateProperty("TestProp", prop2);
		verify(pstyle, times(1)).addStyleProperty(prop2);
		assertEquals(prop2, style.getPropertyValue("TestProp"));
	}
	
	@Test
	public void testAddChild() {
		LayoutStyle child = mock(LayoutStyle.class);
		style.addChild(child);
		assertTrue(style.getChildren().contains(child));
	}
	
	@Test
	public void testRemoveChild() {
		LayoutStyle child = mock(LayoutStyle.class);
		style.addChild(child);
		style.removeChild(child);
		assertFalse(style.getChildren().contains(child));
	}
	
	@Test
	public void testCompile() {
		when(pstyle.compile()).thenReturn("#test { -fx-background-color: black; }");
		assertEquals("#test { -fx-background-color: black; }", style.compile());
	}
	
	@Test
	public void testPersistence() {
		assertEquals(pstyle, style.getPersistenceObject());
	}
}
