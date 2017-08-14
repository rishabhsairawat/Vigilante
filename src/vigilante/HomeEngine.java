package vigilante;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HomeEngine {
	 static WebView browser;
	 static WebEngine webEngine;
         HomeEngine(){
	    browser = new WebView();
	    webEngine = browser.getEngine();
            webEngine.load("http://www.google.com");
	}
	 	
	
	
	public WebView getWebView(){
		return browser;
	}
	
	public WebEngine getWebEngine(){
		return webEngine;
	}	
}