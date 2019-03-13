package smart.league.project.server;

import io.undertow.Undertow;
import io.undertow.server.handlers.proxy.LoadBalancingProxyClient;
import io.undertow.server.handlers.proxy.ProxyHandler;

import java.net.URI;
import java.net.URISyntaxException;

public class Server {

	public static void main(String[] args) {
		try {
			LoadBalancingProxyClient loadBalancer = null;

			loadBalancer = new LoadBalancingProxyClient()
					.addHost(new URI("http://localhost:8081/api"))
					.addHost(new URI("http://localhost:8082"))
					.setConnectionsPerThread(20);

			Undertow reverseProxy = Undertow.builder()
					.addHttpListener(8080, "localhost")
					.setIoThreads(4)
					.setHandler(ProxyHandler.builder()
							.setProxyClient(loadBalancer)
							.setMaxRequestTime(30000)
							.build())
					.build();
			reverseProxy.start();

		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

}
