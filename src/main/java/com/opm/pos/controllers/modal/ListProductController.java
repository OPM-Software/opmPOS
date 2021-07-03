/*  
Copyright (C) 2021  Open Source Mexico
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.*/


package com.opm.pos.controllers.modal;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.opm.pos.controllers.SaleController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import com.opm.pos.models.Product;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.ProductServiceImpl;



public class ListProductController implements Initializable {
	
	private SaleController controller;
	
	public void setSaleController(SaleController controller){
	    this.controller = controller;
	}

	
	@FXML
    private TextField txtFilter;
    @FXML
    Label lblFilter;
    @FXML
    TableView tblData;
   
	private ObservableList<Product> data = FXCollections.observableArrayList();
    private ProductService productService = new ProductServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {    	
    	fetColumnList();
        fetRowList();
        
        tblData.setRowFactory( tv -> {
            TableRow<Product> row = new TableRow<>();            
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                	Product rowData = row.getItem();
                	controller.fetRowList(rowData);
                	Node node = (Node) event.getSource();
           	        Stage stage = (Stage) node.getScene().getWindow();
           	        stage.close();
                }
            });
            return row ;
        });
        
    }
          
    private void fetColumnList() {       
    		  TableColumn idCol = new TableColumn("ID");
    		  idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	      TableColumn serieCol = new TableColumn("SERIE");
    	      serieCol.setCellValueFactory(new PropertyValueFactory("serie"));
    	      TableColumn descCol = new TableColumn("DESCRIPCION");
    	      descCol.setCellValueFactory(new PropertyValueFactory("description"));
    	      TableColumn brandCol = new TableColumn("MARCA");
    	      brandCol.setCellValueFactory(new PropertyValueFactory("brand"));

    	      TableColumn catCol = new TableColumn("CATEGORIA");
    	      catCol.setCellValueFactory(new PropertyValueFactory("category"));

    	      TableColumn priceCol = new TableColumn("PRECIO");
    	      priceCol.setCellValueFactory(new PropertyValueFactory("price"));

    	      TableColumn countCol = new TableColumn("EXISTENCIA");
    	      countCol.setCellValueFactory(new PropertyValueFactory("count"));

    	      tblData.getColumns().removeAll(idCol,serieCol,descCol,brandCol,catCol,countCol,priceCol);  
    	      tblData.getColumns().addAll(idCol,serieCol,descCol,brandCol,catCol,countCol,priceCol);
    	          	                 
    }
            
    public void fetRowList() {
    	try {

    		List<Product> prods = productService.getProducts();
    		data = FXCollections.observableArrayList(prods);		
            tblData.setItems(data);    
            
            FilteredList<Product> filteredData = new FilteredList<>(data, p -> true);
    		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
    			filteredData.setPredicate(producto -> {
    				if (newValue == null || newValue.isEmpty()) {
    					return true;
    				}
    				
    				String lowerCaseFilter = newValue.toLowerCase();				
    				if (producto.getSerie().contains(lowerCaseFilter)) {
    					return true; 
    				} else if (producto.getDescription().toLowerCase().contains(lowerCaseFilter)) {
    					return true; 
    				}
    				return false; 
    			});
    		});
    		
    		SortedList<Product> sortedData = new SortedList<>(filteredData);
    		sortedData.comparatorProperty().bind(tblData.comparatorProperty());
    		tblData.setItems(sortedData);
            tblData.lookup(".scroll-bar:vertical");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }    

}
