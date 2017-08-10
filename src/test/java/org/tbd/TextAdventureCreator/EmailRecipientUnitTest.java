package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import javax.mail.Message.RecipientType;

import org.junit.Test;

import ilusr.textadventurecreator.error.EmailRecipient;

public class EmailRecipientUnitTest {

	@Test
	public void testCreate() {
		EmailRecipient recip = new EmailRecipient("a@b.c");
		assertNotNull(recip);
		assertEquals("a@b.c", recip.getAddress());
	}

	@Test
	public void testCreateFull() {
		EmailRecipient recip = new EmailRecipient("a@b.c", RecipientType.TO);
		assertNotNull(recip);
		assertEquals("a@b.c", recip.getAddress());
		assertEquals(RecipientType.TO, recip.getRecipientType());
	}
	
	@Test
	public void testAddress() {
		EmailRecipient recip = new EmailRecipient("a@b.c", RecipientType.TO);
		recip.setAddress("b@a.c");
		assertEquals("b@a.c", recip.getAddress());
	}
	
	@Test
	public void testType() {
		EmailRecipient recip = new EmailRecipient("a@b.c", RecipientType.TO);
		recip.setRecipientType(RecipientType.CC);
		assertEquals(RecipientType.CC, recip.getRecipientType());
	}
}
