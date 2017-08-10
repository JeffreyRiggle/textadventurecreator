package org.tbd.TextAdventureCreator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import ilusr.textadventurecreator.error.IReportIssueService;
import ilusr.textadventurecreator.error.ReportIssueModel;
import ilusr.textadventurecreator.language.DisplayStrings;
import ilusr.textadventurecreator.language.ILanguageService;

public class ReportIssueModelUnitTest {

	private ILanguageService languageService;
	private IReportIssueService reportIssueService;
	
	private ReportIssueModel model;
	
	@Before
	public void setup() {
		languageService = mock(ILanguageService.class);
		when(languageService.getValue(DisplayStrings.PROBLEM_TYPE)).thenReturn("Problem Type");
		when(languageService.getValue(DisplayStrings.PROBLEM_DESCRIPTION)).thenReturn("Problem Description");
		when(languageService.getValue(DisplayStrings.PROBLEM_ATTACHMENT)).thenReturn("Attachment");
		when(languageService.getValue(DisplayStrings.PROBLEM_SHOULD_REPLY)).thenReturn("Should Reply");
		when(languageService.getValue(DisplayStrings.EMAIL_ADDRESS)).thenReturn("Email Address");
		
		reportIssueService = mock(IReportIssueService.class);
		
		model = new ReportIssueModel(languageService, reportIssueService);
	}
	
	@Test
	public void testCreate() {
		assertNotNull(model);
	}
	
	@Test
	public void testValid() {
		assertFalse(model.valid().get());
		model.problemDescription().set("Something");
		assertTrue(model.valid().get());
	}

	@Test
	public void testProblemType() {
		assertEquals("Application is not working", model.problemType().selected().get());
		assertEquals(3, model.problemType().list().size());
	}
	
	@Test
	public void testProblemDescription() {
		assertEquals(null, model.problemDescription().get());
		model.problemDescription().set("Something");
		assertEquals("Something", model.problemDescription().get());
	}
	
	@Test
	public void testTypeText() {
		assertEquals("Problem Type", model.typeText().get());
	}
	
	@Test
	public void testDescriptionText() {
		assertEquals("Problem Description", model.descriptionText().get());
	}
	
	@Test
	public void testAttachmentText() {
		assertEquals("Attachment", model.attachmentText().get());
	}
	
	@Test
	public void testReplyText() {
		assertEquals("Should Reply", model.replyText().get());
	}
	
	@Test
	public void testReplyAddressText() {
		assertEquals("Email Address", model.replyAddressText().get());
	}
	
	@Test
	public void testReplyToAddressTest() {
		assertEquals(null, model.replyToAddress().get());
		model.replyToAddress().set("a@b.c");
		assertEquals("a@b.c", model.replyToAddress().get());
	}
	
	@Test
	public void testAttachments() {
		assertEquals(0, model.attachments().size());
		model.attachments().add("c:/test/testfile.txt");
		assertEquals(1, model.attachments().size());
	}
	
	@Test
	public void testIncludeReplyTo() {
		assertFalse(model.includeReplyTo().get());
		model.includeReplyTo().set(true);
		assertTrue(model.includeReplyTo().get());
	}
	
	@Test
	public void testSendRequest() {
		model.sendRequest();
		try {
			//TODO find a better way.
			Thread.sleep(1000);
		} catch (Exception e) { }
		verify(reportIssueService, times(1)).reportIssue(any(), any(), any(), any());
	}
}
