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
  
  private static String apiKeyId = "";
  
  private static void getCollection(Client client, Request request) throws IOException {
    request.setMethod(Method.GET);
    request.setEndpoint("/v3/api_keys");
    request.addQueryParam("limit", "100");
    request.addQueryParam("offset", "0");
    try {
      processResponse();
    } catch (IOException ex) {
      throw ex;
    }
    request.clearQueryParams();
  }
  
  private static void post(Client client, Request request) throws IOException {
    request.setMethod(Method.POST);
    request.setEndpoint("/v3/api_keys");
    request.setBody("{\"name\": \"My api Key\",\"scopes\": [\"mail.send\",\"alerts.create\",\"alerts.read\"]}");

    try {
      processResponse();
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
  }
  
  private static void getSingle(Client client, Request request) throws IOException {
    request.setMethod(Method.GET);
    request.setEndpoint("/v3/api_keys/" + apiKeyId);
    try {
      processResponse();
    } catch (IOException ex) {
      throw ex;
    } 
  }
  
  private static void patch(Client client, Request request) throws IOException {
    request.setMethod(Method.PATCH);
    request.setBody("{\"name\": \"A New Ho}");
    try {
      processResponse();
    } catch (IOException ex) {
      throw ex;
    }
    request.clearBody(); 
  }
  
  private static void put(Client client, Request request) throws IOException {
    request.setMethod(Method.PUT);
    request.setBody("{\"name\": \"A New Hope\",\"scopes\": [\"user.profile.read\",\"user.profile.update\"]}");
    try {
      processResponse();
    } catch (IOException ex) {
      throw ex;
    }
    request.clearBody(); 
  }
  
  private static void delete(Client client, Request request) throws IOException {
    request.setMethod(Method.DELETE);
    try {
      Response response = client.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getHeaders());
    } catch (IOException ex) {
      throw ex;
    } 
  }
  
  public static void main(String[] args) throws IOException {
    Client client = new Client();

    Request request = new Request();
    request.setBaseUri("api.sendgrid.com");
    request.addHeader("Authorization", "Bearer " + System.getenv("SENDGRID_API_KEY"));
    
    // GET Collection
    getCollection(client, request);

    // POST
    post(client, request);

    // GET Single
    getSingle(client, request);

    // PATCH
    patch(client, request);

    // PUT
    put(client, request);    

    // DELETE
    delete(client, request);
  }
  
  //Refactor method
  private void processResponse(){
      response = client.api(request);
      System.out.println(response.getStatusCode());
      System.out.println(response.getBody());
      System.out.println(response.getHeaders());
  }
}
