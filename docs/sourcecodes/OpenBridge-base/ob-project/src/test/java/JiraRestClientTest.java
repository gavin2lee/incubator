import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.atlassian.httpclient.api.Request;
import com.atlassian.httpclient.api.factory.HttpClientOptions;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.RestClientException;
import com.atlassian.jira.rest.client.api.domain.Comment;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.TransitionInput;
import com.atlassian.jira.rest.client.auth.BasicHttpAuthenticationHandler;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Effect;


public class JiraRestClientTest {
	private static final Log log = LogFactory.getLog(JiraRestClientTest.class);
	JiraRestClient restClient = null;
	@Before
	public void init() throws URISyntaxException{
		JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
//        final JiraRestClientFactory factory = new JerseyJiraRestClientFactory();
        final URI jiraServerUri = new URI("http://192.168.31.210:8680/");
        restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, "admin", "admin");
	}
	@After
	public void destory(){
		if(restClient!=null){
			try {
				restClient.close();
			} catch (Exception e) {
				log.warn("Exceptioned when close jira rest client.",e);
			}
		}
	}
	@SuppressWarnings("resource")
//	@Test
	public void controller() throws InterruptedException, ExecutionException{
		while(true){
			Scanner scanner = new Scanner(System.in);
			String cmd = scanner.nextLine();
			if("exit".equals(cmd)){
				break;
			}else if("issue".equals(cmd)){
				try {
					Issue issue = restClient.getIssueClient().getIssue("APPFACTORY-1").claim();
					System.out.println(issue.getIssueType());
					System.out.println(issue.getFieldByName("Sprint"));
					
					SearchResult result = restClient.getSearchClient().searchJql("Sprint=33").claim();
					Iterator<Issue> iterator = result.getIssues().iterator();
					while(iterator.hasNext()){
						System.out.println(iterator.next());
					}
//					System.out.println(issue);
				} catch (RestClientException e) {
//					Iterator<ErrorCollection> ec = e.getErrorCollections().iterator();
//					while(ec.hasNext()){
//						ec.next().getStatus()
//					}
					if(e.getStatusCode().get()==404){
						System.out.println("issue 不存在");
					}
					e.printStackTrace();
				}
			}else if("project".equals(cmd)){
				System.out.println(restClient.getProjectClient().getProject("APPFACTORY").claim());
			}
		}
	}
	public void main() throws URISyntaxException, InterruptedException, ExecutionException {
//	    final NullProgressMonitor pm = new NullProgressMonitor();
        final Issue issue = restClient.getIssueClient().getIssue("APPFACTORY-1").get();
 
        System.out.println(issue);
 
        // now let's vote for it
        restClient.getIssueClient().vote(issue.getVotesUri());
 
        // now let's watch it
        restClient.getIssueClient().watch(issue.getWatchers().getSelf());
 
        // now let's start progress on this issue
        final Iterable<Transition> transitions = restClient.getIssueClient().getTransitions(issue.getTransitionsUri()).get();
        final Transition startProgressTransition = getTransitionByName(transitions, "Start Progress");
        restClient.getIssueClient().transition(issue.getTransitionsUri(), new TransitionInput(startProgressTransition.getId()));
 
        // and now let's resolve it as Incomplete
        final Transition resolveIssueTransition = getTransitionByName(transitions, "Resolve Issue");
        Collection<FieldInput> fieldInputs = Arrays.asList(new FieldInput("resolution", "Incomplete"));
        final TransitionInput transitionInput = new TransitionInput(resolveIssueTransition.getId(), fieldInputs, Comment.valueOf("My comment"));
        restClient.getIssueClient().transition(issue.getTransitionsUri(), transitionInput);
    }
 
    private static Transition getTransitionByName(Iterable<Transition> transitions, String transitionName) {
        for (Transition transition : transitions) {
            if (transition.getName().equals(transitionName)) {
                return transition;
            }
        }
        return null;
    }
    
    @SuppressWarnings("rawtypes")
	public void createClientWithTimeout(){
    	final JiraRestClientFactory jiraRestClientFactory = new AsynchronousJiraRestClientFactory();
    	JiraRestClient jiraRestClient = null;
    	int jiraConnectTimeout = 10000;
    	int jiraSocketTimeout = 10000;
    	final String jiraUsername="";
    	final String jiraPassword="";
    	String jiraUri = "";
    	try {
    	  final HttpClientOptions options = new HttpClientOptions();
    	  options.setConnectionTimeout(jiraConnectTimeout, TimeUnit.MILLISECONDS);
    	  options.setSocketTimeout(jiraSocketTimeout, TimeUnit.MILLISECONDS);
    	  options.setRequestPreparer(new Effect<Request>() {
    	    @Override
    	    public void apply(final Request request) {
    	      new BasicHttpAuthenticationHandler(jiraUsername, jiraPassword).configure(request);
    	    }
    	  });
    	  URI serverUri = new URI(jiraUri);
    	  //NoOpEventPublisher RestClientApplicationProperties是AsynchronousJiraRestClientFactory内部私有静态类
//    	  final DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new NoOpEventPublisher(),
//    	      new RestClientApplicationProperties(serverUri), ThreadLocalContextManagers.noop(), options);
//    	  jiraRestClient = jiraRestClientFactory.create(serverUri, defaultHttpClient);
    	}
    	catch (URISyntaxException e) {
    	  log.error("createJiraRestClient error creating client: " + e.getMessage(), e);
    	}
    }
    
    public void sprint(){
    	
    }
}
