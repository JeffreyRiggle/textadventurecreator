package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.views.layout.LayoutComponent;
import ilusr.textadventurecreator.views.layout.LayoutStyle;
import javafx.scene.Node;
import javafx.scene.image.Image;
import textadventurelib.persistence.LayoutNodePersistenceObject;

public class LayoutComponentUnitTest {

	private String name;
	private LayoutNodePersistenceObject node;
	private Image icon;
	private Node displayNode;
	private LayoutStyle style;
	
	private LayoutComponent component;
	
	@Before
	public void setup() {
		name = "TestComponent";
		node = mock(LayoutNodePersistenceObject.class);
		icon = mock(Image.class);
		displayNode = mock(Node.class);
		style = mock(LayoutStyle.class);
		
		component = new LayoutComponent(name, node, icon, displayNode, style);
	}
	
	@Test
	public void testName() {
		assertEquals(name, component.getName());
		component.setName("TestComp2");
		assertEquals("TestComp2", component.getName());
	}
	
	@Test
	public void testIcon() {
		assertEquals(icon, component.getIcon());
		Image ico = mock(Image.class);
		component.setIcon(ico);
		assertEquals(ico, component.getIcon());
	}

	@Test
	public void testRow() {
		component.setRow(3);
		verify(node, times(1)).setRow(3);
	}
	
	@Test
	public void testCol() {
		component.setCol(4);
		verify(node, times(1)).setColumn(4);
	}
	
	@Test
	public void testRowSpan() {
		component.setRowSpan(2);
		verify(node, times(1)).setRowSpan(2);
	}
	
	@Test
	public void testColSpan() {
		component.setColSpan(7);
		verify(node, times(1)).setColumnSpan(7);
	}
	
	@Test
	public void testFXML() {
		when(node.getLayoutValue()).thenReturn("Something");
		assertEquals("Something", component.fxmlValue());
	}
	
	@Test
	public void testCompile() {
		when(node.compile()).thenReturn("<Something row=\"0\" col\"0\"/>");
		assertEquals("<Something row=\"0\" col\"0\"/>", component.compile());
	}
	
	@Test
	public void testDisplayNode() {
		assertEquals(displayNode, component.getDisplayNode());
		Node dn2 = mock(Node.class);
		component.setDisplayNode(dn2);
		assertEquals(dn2, component.getDisplayNode());
	}
	
	@Test
	public void testPersistence() {
		assertEquals(node, component.node());
	}
	
	@Test
	public void testStyle() {
		assertEquals(style, component.style().get());
	}
}
