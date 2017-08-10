package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.ChangePlayerPropertyView;

public class ChangePlayerPropertyViewUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	@Test
	public void testGetData() {
		ChangePlayerPropertyView view = new ChangePlayerPropertyView("TestValue");
		assertEquals(view.getData().get(), "TestValue");
	}

}
