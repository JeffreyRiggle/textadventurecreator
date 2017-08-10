package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;

import org.junit.Test;

import ilusr.textadventurecreator.error.EmailAttachment;

public class EmailAttachmentUnitTest {

	@Test
	public void testCreate() {
		EmailAttachment attachment = new EmailAttachment("c:/path/something.txt");
		assertNotNull(attachment);
		assertEquals("c:/path/something.txt", attachment.getFile());
	}

	@Test
	public void testFullCreate() {
		EmailAttachment attachment = new EmailAttachment("c:/path/something.txt", "something.txt");
		assertNotNull(attachment);
		assertEquals("c:/path/something.txt", attachment.getFile());
		assertEquals("something.txt", attachment.getFileName());
	}
	
	@Test
	public void testFile() {
		EmailAttachment attachment = new EmailAttachment("c:/path/something.txt", "something.txt");
		attachment.setFile("d:/path/somethingelse.txt");
		assertEquals("d:/path/somethingelse.txt", attachment.getFile());
	}
	
	@Test
	public void testFileName() {
		EmailAttachment attachment = new EmailAttachment("c:/path/something.txt", "something.txt");
		attachment.setFileName("something");
		assertEquals("something", attachment.getFileName());
	}
}
