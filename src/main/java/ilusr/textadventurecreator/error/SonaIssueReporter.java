package ilusr.textadventurecreator.error;

import java.io.File;
import java.util.List;
/*
import ilusr.sona.client.ApiException;
import ilusr.sona.client.api.DefaultApi;
import ilusr.sona.client.model.Incident;
import ilusr.sona.client.model.Incidentrequest;
*/

public class SonaIssueReporter implements IReportIssueService {
	
	//private DefaultApi api;
	
	public SonaIssueReporter() {
		//api = new DefaultApi();
		//api.getApiClient().setBasePath("http://localhost:8080/sona/v1");
	}
	
	@Override
	public void reportIssue(String type, List<String> attachments, String reporter, String description) {
		/*
		Incidentrequest req = new Incidentrequest();
		req.description(description);
		req.reporter(reporter);
		req.putAttributesItem("IssueType", type);
		
		try {
			Incident incident = api.create(req);
			
			for (String att : attachments) {
				File f = new File(att);
				api.uploadAttachment(incident.getId(), f);
			}
		} catch (ApiException e) {
			e.printStackTrace();
		}
		*/
	}
}
