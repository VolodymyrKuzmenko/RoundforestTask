package roundforest.mock;


import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import roundforest.domain.ReviewDTO;

import java.net.URI;

@Slf4j
public class MockRestTemplate extends RestTemplate {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String MOCK_JSON_RESPONSE = "{\"text\": \"Salut Jean, comment vas tu?\"}";

    @Override
    public <T> T postForObject(URI url, Object request, Class<T> responseType) throws RestClientException {
        if (request instanceof ReviewDTO) {
            try {
                String requestBody = mapper.writeValueAsString(request);
                logger.info(requestBody);

                //mock long post operation
                Thread.sleep(200);

                JsonParser parser = mapper.getJsonFactory().createJsonParser(MOCK_JSON_RESPONSE);
                JsonNode node = mapper.readTree(parser);
                return (T) mapper.readValue(node.get("text"), String.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
