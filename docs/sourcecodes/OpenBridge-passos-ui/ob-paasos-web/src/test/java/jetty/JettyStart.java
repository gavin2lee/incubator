package jetty;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

public class JettyStart {
	public static void main(String[] args) {
		//部署项目，启动jetty服务器
	    long startTime = System.currentTimeMillis();
        Server jettyServer = new Server(8288);
//        Connector conn = new SocketConnector();
//        conn.setPort(8188);
//        jettyServer.setConnectors(new Connector[]{conn});
        WebAppContext wah = new WebAppContext();
        wah.setDefaultsDescriptor("webdefault.xml");
        wah.setContextPath("/os");
        wah.setWar("src/main/webapp");
        wah.setMaxFormContentSize(2000000000);
        jettyServer.setHandler(wah);
        try {
        	jettyServer.start();
			System.out.println("app jetty Server started "+(System.currentTimeMillis()-startTime)+" ms");
			jettyServer.join();
		} catch (Exception e) { 
			e.printStackTrace();
			System.exit(1);//异常结束jvm
		}
	}
}
