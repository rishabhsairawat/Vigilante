package vigilante;

import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import static vigilante.BrowserLayout.DoubleClickResponse;


public class HistoryView{
 
    private  TableView<History> table = new TableView<>();
    private  ObservableList<History> data = FXCollections.observableArrayList();
    private  VBox vbox = new VBox();
    private  ArrayList<History> historylist; 
   
    HistoryView(ArrayList<History> h) {       
    	historylist = h;
    }

    public void createHistoryView(){
 
        historylist.forEach((h) -> {
            data.add(h);
        });
        TableColumn urlCol = new TableColumn("Url");
        urlCol.setMinWidth(900);
        urlCol.setCellValueFactory(
                new PropertyValueFactory<>("Url"));
 
        TableColumn timeCol = new TableColumn("Time");
        timeCol.setMinWidth(200);
        timeCol.setCellValueFactory(
                new PropertyValueFactory<>("Time"));
 
        table.setItems(data);
        table.getColumns().addAll(urlCol, timeCol);
        table.setMinHeight(900); 
        timeCol.setSortType(TableColumn.SortType.ASCENDING);
        timeCol.setSortable(true);
        
        
        
     table.setOnMousePressed((MouseEvent event) -> {
         if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
             System.out.println(table.getSelectionModel().getSelectedItem().getUrl());
             DoubleClickResponse(HomeEngine.browser, HomeEngine.webEngine,table.getSelectionModel().getSelectedItem().getUrl());
             
         }
        });
     
        
        
        
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(table);
        
    }
    
    public VBox getVbox(){
    	return vbox;
    }

    
}