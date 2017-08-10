package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.language.LanguageItem;

public class LanguageItemUnitTest {

	private LanguageItem item;
	
	@Before
	public void setup() {
		item = new LanguageItem("Tst", "Test", "テスト");
	}
	
	@Test
	public void testKeyword() {
		assertEquals("Tst", item.getKeyword());
		item.setKeyword("Test");
		assertEquals("Test", item.getKeyword());
	}
	
	@Test
	public void testEnValue() {
		assertEquals("Test", item.getEnValue());
		item.setEnValue("Test Value");
		assertEquals("Test Value", item.getEnValue());
	}

	@Test
	public void testNewValue() {
		assertEquals("テスト", item.getNewValue());
		item.setNewValue("すごい");
		assertEquals("すごい", item.getNewValue());
	}
}
