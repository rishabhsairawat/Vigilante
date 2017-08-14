package vigilante;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.Scene;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.image.*;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.ListChangeListener.Change;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class BrowserLayout {
	
	public  static BookmarkDB bookmarkdb;
	public  static HistoryDB historydb;

	
	public  static BorderPane root;
	public   Scene scene;
	public  ToolBar toolBar;
	 static TextField searchBox;
	static String bck = "", fwd = "";
	
	public  static Button backward, forward, mic, bookmark, refresh;
	public static ToggleButton home, historylist, bookmarklist; 
	
	public  Stack<String> forwardlist;
	public  Stack<String> backwardlist;
	
	BrowserLayout(BookmarkDB b, HistoryDB h, WebListDB w){
		
		bookmarkdb = b;
		historydb = h;
		
		root = new BorderPane();
		scene = new Scene(root, 300, 250, Color.WHITE);
		forwardlist = new Stack<>();
		backwardlist = new Stack<>();
	}

	public BorderPane getRoot(){
		return root;
	}
	
	public Scene getScene(){
		return scene;
	}
	
	public ToolBar getToolBar(){
		return toolBar;
	}

	public void createLayout(WebView browser){
            HBox topContainer = new HBox();
	
	    toolBar = new ToolBar();

	    backward = new Button();
	    backward.setGraphic(new ImageView("file:icons/backward.png"));
	    backward.setDisable(true);    

	    forward = new Button();
	    forward.setGraphic(new ImageView("file:icons/forward.png"));
	    forward.setDisable(true);    

	    refresh = new Button();
	    refresh.setGraphic(new ImageView("file:icons/refresh.png"));

	    bookmark = new Button();
	    bookmark.setGraphic(new ImageView("file:icons/bookmark.png"));

	    mic = new Button();
	    mic.setGraphic(new ImageView("file:icons/micOff.png"));
	    
	    home = new ToggleButton();
	    home.setGraphic(new ImageView("file:icons/home.png"));

	    historylist = new ToggleButton();
	    historylist.setGraphic(new ImageView("file:icons/historylist.png"));


	    bookmarklist = new ToggleButton();
	    bookmarklist.setGraphic(new ImageView("file:icons/bookmarklist.png"));

	    searchBox = new TextField ();
	    searchBox.setText("Enter Website URL");
	    searchBox.setMinWidth(250);
	    
	    ToggleGroup extra = new ToggleGroup();

	    bookmarklist.setToggleGroup(extra);
	    historylist.setToggleGroup(extra);

	    home.setToggleGroup(extra);
	    home.setSelected(true);

	    HBox.setHgrow(searchBox, Priority.ALWAYS);
	
	    toolBar.getItems().addAll(backward, forward, refresh, searchBox, 
	            bookmark, mic , new Separator(),new Separator(),home, bookmarklist, historylist);

	    topContainer.getChildren().add(toolBar);
	    
	    root.setTop(topContainer);   
	    root.setCenter(browser);
	}
	
	public static void activateSearchBox(WebView browser, WebEngine webEngine){
	    searchBox.setOnMousePressed((MouseEvent me) -> {
                if("Enter Website URL".equals(searchBox.getText()))
                    searchBox.setText("");
            });

	    browser.setOnMousePressed((MouseEvent me) -> {
                if("".equals(searchBox.getText()))
                    searchBox.setText("Enter Website URL");
            });


	    searchBox.setOnKeyPressed((KeyEvent keyEvent) -> {
                if(keyEvent.getCode() == KeyCode.ENTER){
                    
                    String url = searchBox.getText();
                    
                    if (!url.contains("http://") || !url.contains("https://") )
                        url = "https://" + url;
                    
                    
                    String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
                    
                    Pattern p = Pattern.compile(URL_REGEX);
                    Matcher m = p.matcher(url);
                    if(m.find()) {
                        webEngine.load(url);
                    }
                    else{ 
                        url = searchBox.getText();
                        url = "http://www.google.com/search?q=" + url;
                        webEngine.load(url);
                    }
                    searchBox.setText(url);
                }
            });
	}
	
	public  void activateHistoryService(WebEngine webEngine, Stage primaryStage){
	    
	  final WebHistory history = webEngine.getHistory();
      history.getEntries().addListener((Change<? extends Entry> c) -> {
          c.next();
          c.getAddedSubList().stream().map((e) -> {            	
            historydb.insert(e.getUrl());
                return e;
            }).map((e) -> {
                
            
                primaryStage.titleProperty().bind(webEngine.titleProperty());
              if(!backwardlist.isEmpty()){
                  bck = backwardlist.peek();
              }
              return e;
          }).map((e) -> {
              backwardlist.push(e.getUrl());
              return e;
          }).map((e) -> {
              if(backwardlist.size() == 1){
                  backward.setDisable(true);
              }
              else{
                  backward.setDisable(false);
              }
              return e;
          }).forEachOrdered((e) -> {
                searchBox.setText(e.getUrl());
          });
          });
      
      toolBar.prefWidthProperty().bind(primaryStage.widthProperty());
	}
	
	public void activateButton(WebEngine webEngine, Stage primaryStage){
	    refresh.setOnMousePressed((MouseEvent me) -> {
                webEngine.load(searchBox.getText());
            });
	   
	    bookmark.setOnMousePressed((MouseEvent me) -> {
                bookmarkdb.insert(webEngine.getTitle(), webEngine.getLocation());
            });

            backward.setOnMousePressed((MouseEvent me) -> {
                forwardlist.push(backwardlist.pop());
                String temp = backwardlist.pop();
                webEngine.load(temp);
                searchBox.setText(temp);
                bck = backwardlist.peek();
                fwd = forwardlist.peek();
                if(backwardlist.empty())
                    backward.setDisable(true);
                if(!forwardlist.empty())
                    forward.setDisable(false);
            });

	    forward.setOnMousePressed((MouseEvent me) -> {
                String temp = forwardlist.pop();
                backwardlist.push(temp);
                webEngine.load(temp);
                searchBox.setText(temp);
                bck = backwardlist.peek();
                fwd = forwardlist.peek();
                if(forwardlist.empty())
                    forward.setDisable(true);
                if(!backwardlist.empty())
                    backward.setDisable(false);
            });    
	}
	
	public void activateToggleButton(WebView w){
	    home.setOnAction((ActionEvent e) -> {
                System.out.println("home");
                root.setCenter(w);
            });
		
	    bookmarklist.setOnAction((ActionEvent e) -> {
                BookmarkView b = new BookmarkView(bookmarkdb.view());
                b.createBookmarkView();
                
                System.out.println("BookmarksList");
                Button delButton=new Button("Delete All Bookmarks");
                delButton.setFont(Font.font("Arial Bold", 16));
                VBox vb=new VBox();
                HBox hb=new HBox();
                hb.setAlignment(Pos.TOP_RIGHT);
                hb.getChildren().add(delButton);
                vb.getChildren().addAll(hb,b.getVbox());
                root.setCenter(vb);
                delButton.setOnMousePressed((MouseEvent me) -> {
               bookmarkdb.deleteAll();
            });
                
            });

	    historylist.setOnAction((ActionEvent e) -> {
                HistoryView h = new HistoryView(historydb.view());
                h.createHistoryView();
                Button delButton=new Button("Delete All History");
                delButton.setFont(Font.font("Arial Bold", 16));
                 VBox vb=new VBox();
                 HBox hb=new HBox();
                hb.setAlignment(Pos.TOP_RIGHT);
                hb.getChildren().add(delButton);
                vb.getChildren().addAll(hb,h.getVbox());
                root.setCenter(vb);
                 delButton.setOnMousePressed((MouseEvent me) -> {
               historydb.deleteAll();
            });
            });
	}
	
	 public  void activateMic(WebView browser, WebEngine webEngine){
	    // speech recongization 
	    mic.setOnMousePressed((MouseEvent me) -> {
                mic.setGraphic(new ImageView("file:icons/mic.png"));
                SpeechRecognizer recognizer=new SpeechRecognizer();
               recognizer.start();

            });
	}
      
	 public class SpeechRecognizer extends Thread{
        @Override
        public void run() {
            try {
                TranscriberDemo.transcribe();
               
            } catch (Exception ex) {
                Logger.getLogger(BrowserLayout.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        }
         
         public static void ResponseForURL(WebView browser, WebEngine webEngine,String response){
         if(response.contains("GOOGLE")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load("http://www.google.com");
                 }
             });
         }
         else if(response.contains("FACEBOOK")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load("https://www.facebook.com");
                 }
             });
         }
         else if(response.contains("YOUTUBE")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load("https://www.youtube.com");
                 }
             });
         }
         else if(response.contains("GMAIL")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load("https://accounts.google.com/");
                 }
             });
         }
          else if(response.contains("TWITTER")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load("https://twitter.com/");
                 }
             });
         }
         
         else{
         }
         }
         
         public static void ResponseForGo(WebView browser, WebEngine webEngine,String response){
             if(response.contains("FORWARD")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load(fwd);
                 }
             });
         }
             else if(response.contains("BACKWARD")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load(bck);
                 }
             });
         }
        
             else{
             }
         }
         
         public static void Refresh(WebView browser,WebEngine webEngine,String response){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load(webEngine.getLocation());
                 }
             });
         
         }
         
         public static void AddBookmark(WebView browser,WebEngine webEngine,String response){
          if(response.contains("ADD BOOKMARK")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                    bookmarkdb.insert(webEngine.getTitle(), webEngine.getLocation());
                 }
             });
          }
         }
         
         public static void Show(WebView browser, WebEngine webEngine,String response){
         if(response.contains("HOME")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                   root.setCenter(browser);
                 }
             });
          }
         else if(response.contains("BOOKMARK")||response.contains("BOOKMARKS")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                         BookmarkView b = new BookmarkView(bookmarkdb.view());
                b.createBookmarkView();
                
                System.out.println("BookmarksList");
                Button delButton=new Button("Delete All Bookmarks");
                delButton.setFont(Font.font("Arial Bold", 16));
                VBox vb=new VBox();
                HBox hb=new HBox();
                hb.setAlignment(Pos.TOP_RIGHT);
                hb.getChildren().add(delButton);
                vb.getChildren().addAll(hb,b.getVbox());
                root.setCenter(vb);
                delButton.setOnMousePressed((MouseEvent me) -> {
               bookmarkdb.deleteAll();
            });
                
                 }
             });
          }
          else if(response.contains("HISTORY")){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                HistoryView h = new HistoryView(historydb.view());
                h.createHistoryView();
                Button delButton=new Button("Delete All History");
                delButton.setFont(Font.font("Arial Bold", 16));
                 VBox vb=new VBox();
                 HBox hb=new HBox();
                hb.setAlignment(Pos.TOP_RIGHT);
                hb.getChildren().add(delButton);
                vb.getChildren().addAll(hb,h.getVbox());
                root.setCenter(vb);
                 delButton.setOnMousePressed((MouseEvent me) -> {
               historydb.deleteAll();
            });
               }
             });
          }
         
         
         }
            
         public static void DoubleClickResponse(WebView browser, WebEngine webEngine,String url){
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     webEngine.load(url);
     
                 }
             });
         }
         
}	