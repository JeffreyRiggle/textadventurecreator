package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.views.layout.LayoutComponent;
import ilusr.textadventurecreator.views.layout.LayoutComponentProvider;
import ilusr.textadventurecreator.views.layout.ToolKitModel;

public class ToolKitModelUnitTest {

	private LayoutComponentProvider provider;
	
	private ToolKitModel model;
	
	@Before
	public void setup() {
		provider = mock(LayoutComponentProvider.class);
		when(provider.getComponents()).thenReturn(Arrays.asList(mock(LayoutComponent.class), mock(LayoutComponent.class), mock(LayoutComponent.class)));
		
		model = new ToolKitModel(provider);
	}
	
	@Test
	public void testComponents() {
		assertEquals(3, model.components().size());
	}

}
