package vigilante;

import javafx.application.Application;
import javafx.geometry.Rectangle2D; 
import javafx.scene.image.*;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
	
	BookmarkDB bookmarkdb = new BookmarkDB();
	HistoryDB historydb = new HistoryDB();
	WebListDB weblistdb = new WebListDB();
	
  @Override
  public void start(Stage primaryStage) {
	  
	bookmarkdb.connect();
	historydb.connect();
	weblistdb.connect();

    HomeEngine browser = new HomeEngine();
  
    BrowserLayout layout = new BrowserLayout(bookmarkdb, historydb, weblistdb);
    
    layout.createLayout(browser.getWebView());
    BrowserLayout.activateSearchBox(browser.getWebView(), browser.getWebEngine());
    layout.activateHistoryService(browser.getWebEngine(), primaryStage);
    layout.activateButton(browser.getWebEngine(), primaryStage);
    layout.activateToggleButton(browser.getWebView());
    layout.activateMic(browser.getWebView(), browser.getWebEngine());
    
    primaryStage.setScene(layout.getScene());

   
    BookmarkDB bookmarkDB = new BookmarkDB();
    bookmarkDB.connect();
    

    
    primaryStage.setMinWidth(400);
    
 

    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();


    primaryStage.setX(primaryScreenBounds.getMinX());
    primaryStage.setY(primaryScreenBounds.getMinY());
    primaryStage.setWidth(primaryScreenBounds.getWidth());
    primaryStage.setHeight(primaryScreenBounds.getHeight());

    
    primaryStage.getIcons().add(new Image("file:icons/Browser-icon.png"));
    primaryStage.setTitle("Vigilante");
    
    primaryStage.show();
    
  }
  
        @Override
  public void stop(){
	  bookmarkdb.close();
	  historydb.close();
	  weblistdb.close();
  }
  
}