package vigilante;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import static vigilante.BrowserLayout.DoubleClickResponse;


public class BookmarkView{

    private final VBox vbox = new VBox();
  
    private final TableView<Bookmark> table = new TableView<>();
    private final ObservableList<Bookmark> data =
            FXCollections.observableArrayList();
    
    BookmarkDB bookmarkdb = new BookmarkDB();
    
    
    
    private final  ArrayList<Bookmark> bookmarklist; 
    
    BookmarkView(ArrayList<Bookmark> b) {       
    	bookmarklist = b;
    }

    public void createBookmarkView(){
 
        bookmarklist.forEach((b) -> {
            data.add(b);
        });
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(250);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("Name"));
 
        TableColumn urlCol = new TableColumn("Url");
        urlCol.setMinWidth(800);
        urlCol.setCellValueFactory(
                new PropertyValueFactory<>("Url"));
 
        table.setItems(data);
        table.getColumns().addAll(nameCol, urlCol);
 
        nameCol.setSortType(TableColumn.SortType.DESCENDING);
        urlCol.setSortable(false);
        
        
        table.setOnMousePressed((MouseEvent event) -> {
         if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
             System.out.println(table.getSelectionModel().getSelectedItem().getUrl());
             DoubleClickResponse(HomeEngine.browser, HomeEngine.webEngine,table.getSelectionModel().getSelectedItem().getUrl());
             
         }
        });
       
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
 
        vbox.getChildren().addAll(table);
        table.setMinHeight(900);  
    }
    
    public VBox getVbox(){
    	return vbox;
    }
}