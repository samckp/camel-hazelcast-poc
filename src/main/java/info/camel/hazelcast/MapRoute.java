package info.camel.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.SSLConfig;
import com.hazelcast.core.HazelcastInstance;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.hazelcast.HazelcastConstants;
import org.apache.camel.component.hazelcast.HazelcastOperation;
import org.apache.camel.component.hazelcast.topic.HazelcastTopicComponent;
import org.springframework.stereotype.Component;

@Component
public class MapRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        // setup hazelcast
        ClientConfig config = new ClientConfig();
//        config.getNetworkConfig().addAddress("hazelcast");
        config.getNetworkConfig().addAddress("192.168.43.93:5701");
        config.getNetworkConfig().setSSLConfig(new SSLConfig().setEnabled(false));
        HazelcastInstance instance = HazelcastClient.newHazelcastClient(config);

        // setup camel hazelcast
        HazelcastTopicComponent hazelcast = new HazelcastTopicComponent();
        hazelcast.setHazelcastInstance(instance);
        getContext().addComponent("hazelcast-topic", hazelcast);

        from("direct:put")
                .setHeader(HazelcastConstants.OPERATION, constant(HazelcastOperation.PUT))
                .toF("hazelcast-%sfoo", HazelcastConstants.MAP_PREFIX);

        /*from("")
                .routeId("hazelcastRoute")
                .log(LoggingLevel.INFO, "${body}")
                .to("")
                ;*/
    }
}
