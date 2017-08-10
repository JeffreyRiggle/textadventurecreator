package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;

import ilusr.core.test.JavaFXRule;
import ilusr.textadventurecreator.views.action.ChangePlayerPropertyViewProvider;

public class ChangePlayerPropertyViewProviderUnitTest {

	@Rule public JavaFXRule javafxRule = new JavaFXRule();
	
	@Test
	public void testCreate() {
		ChangePlayerPropertyViewProvider provider = new ChangePlayerPropertyViewProvider("Test Data");
		assertNotNull(provider.getView());
	}

}
