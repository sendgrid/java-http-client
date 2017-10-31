import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.sendgrid.Client;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class Example {
  public static void main(String[] args) throws IOException {
    Client client = new Client();

    Request request = new Request();
    request.setBaseUri("api.sendgrid.com");
    request.addHeader("Authorization", "Bearer " + System.getenv("SENDGRID_API_KEY"));

    Response response = new Response();

    // GET Collection
    request.setMethod(Method.GET);
    request.setEndpoint("/v3/api_keys");
    request.addQueryParam("limit", "100");
    request.addQueryParam("offset", "0");
    try {
      logResponse(client, request);
    } catch (IOException ex) {
      throw ex;
    }
    request.clearQueryParams();

    // POST
    request.setMethod(Method.POST);
    request.setEndpoint("/v3/api_keys");
    request.setBody("{\"name\": \"My api Key\",\"scopes\": [\"mail.send\",\"alerts.create\",\"alerts.read\"]}");

    try {
      response = client.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
    String apiKeyId = "";
    try {
      ObjectMapper mapper = new ObjectMapper();
      JsonNode json = mapper.readTree(response.getBody());
      apiKeyId = json.path("api_key_id").asText();
    } catch (IOException ex) {
      throw ex;
    }
    request.clearBody();

    // GET Single
    request.setMethod(Method.GET);
    request.setEndpoint("/v3/api_keys/" + apiKeyId);
    try {
      logResponse(client, request);
    } catch (IOException ex) {
      throw ex;
    }

    // PATCH
    request.setMethod(Method.PATCH);
    request.setBody("{\"name\": \"A New Ho}");
    try {
      logResponse(client, request);
    } catch (IOException ex) {
      throw ex;
    }
    request.clearBody();

    // PUT
    request.setMethod(Method.PUT);
    request.setBody("{\"name\": \"A New Hope\",\"scopes\": [\"user.profile.read\",\"user.profile.update\"]}");
    try {
      logResponse(client, request);
    } catch (IOException ex) {
      throw ex;
    }
    request.clearBody();

    // DELETE
    request.setMethod(Method.DELETE);
    try {
      response = client.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    }
  }
  
  private static void logResponse(Client client, Request request) {
    Response response = client.api(request);
    System.out.println(response.getStatusCode());
    System.out.println(response.getBody());
    System.out.println(response.getHeaders());
  }
}