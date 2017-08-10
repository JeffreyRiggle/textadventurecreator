package ilusr.textadventurecreator.error;

import java.util.List;

public interface IReportIssueService {
	void reportIssue(String type, List<String> attachments, String reporter, String description);
}
