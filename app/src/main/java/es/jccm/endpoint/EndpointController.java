package es.jccm.endpoint;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EndpointController {
	@Autowired
    private Environment env;
	
    @RequestMapping("/")
    public String index() {
    	URL url;
    	String contenido="";
        int status=0;
        String endpointUrl=env.getProperty("ENDPOINT_URL");
		try {
			//"https://test.cm-pre.jccm.es/greeting"
			url = new URL(endpointUrl);
	    	HttpURLConnection con = (HttpURLConnection) url.openConnection();
	    	con.setRequestMethod("GET");
	    	con.setInstanceFollowRedirects(true);
	    	//Map<String, String> parameters = new HashMap<>();
	    	//parameters.put("param1", "val");
	    	//out.writeBytes(ParameterStringBuilder.getParamsString(parameters));	    	
	    	status = con.getResponseCode();
	    	BufferedReader in = new BufferedReader(
	    	  new InputStreamReader(con.getInputStream()));
	    	String inputLine;
	    	StringBuffer content = new StringBuffer();
	    	while ((inputLine = in.readLine()) != null) {
	    	    content.append(inputLine);
	    	}
	    	in.close();
	    	con.disconnect();
	    	contenido=content.toString();
		
		} catch (MalformedURLException e) {
			contenido="Error. URL incorrecta.";
		}
		catch (IOException ioe ) {
		    contenido="Error. E/S incorrecta";
		}
		
        if (status==200) {
        	return "Solicitando URL: "+endpointUrl+"<br/>Endpoint alcanzado<br/>Respuesta: <br/>"+contenido;
        }
        else {
        	return "Solicitando URL: <a href='"+endpointUrl+"'>"+endpointUrl+"</a> <br/>Error al pedir endpoint<br/>HTTP Status: "+Integer.toString(status)+"<br/>"+contenido;
        }
    }
    
}
