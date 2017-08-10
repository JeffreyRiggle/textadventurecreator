package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.layout.LayoutComponentProvider;
import textadventurelib.persistence.LayoutNodePersistenceObject;

public class LayoutComponentProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	private LayoutComponentProvider provider;
	
	@Before
	public void setup() {
		provider = new LayoutComponentProvider();
	}
	
	@Test
	public void testGetFromPersistence() {
		LayoutNodePersistenceObject node = mock(LayoutNodePersistenceObject.class);
		when(node.getLayoutValue()).thenReturn("ConsoleView");
		assertNotNull(provider.getLayoutFromPersistence(node));
	}

	@Test
	public void testGetContentView() {
		assertNotNull(provider.getContentView());
	}
	
	@Test
	public void testGetTextInputView() {
		assertNotNull(provider.getTextInput());
	}
	
	@Test
	public void testGetButtonInputView() {
		assertNotNull(provider.getButtonInput());
	}
	
	@Test
	public void testTextOnlyView() {
		assertNotNull(provider.getTextOnly());
	}
	
	@Test
	public void testGetConsole() {
		assertNotNull(provider.getConsole());
	}
	
	@Test
	public void testComponents() {
		assertEquals(5, provider.getComponents().size());
	}
}
