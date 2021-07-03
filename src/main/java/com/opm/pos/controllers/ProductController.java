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


package com.opm.pos.controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import com.opm.pos.controllers.modal.ProductMaintenanceController;
import com.opm.pos.models.Product;
import com.opm.pos.models.SysProperty;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.ProductServiceImpl;



public class ProductController implements Initializable {
	
	private ProductController controller= this;
	private Integer identifier=0;
    @FXML
    private TextField txtFilter;
    @FXML
    Label lblFilter;
    @FXML
    TableView tblData;
    @FXML
    private Button btnProds;
    @FXML
    private Button btnCustomers;
    @FXML
    private Button btnCredits;
    @FXML
    private Button btnSales;
    @FXML
    private Button btnSetup;
    
    @FXML
    private Button btnExit;
        

    @FXML
    private Button btnAdd;
    

    @FXML
    private Button btnCategory;

    @FXML
    private Button btnBrand;

    @FXML
    private FontAwesomeIconView icnAdd;
        
    
    @FXML
    private HBox hbBox;
    @FXML
    private HBox hbBoxExit;
    
	private ObservableList<Product> data = FXCollections.observableArrayList();
    private ProductService productService = new ProductServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {    	
    	icnAdd = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
    	fetColumnList();
        fetRowList();
        if(!SysProperty.getIsAdmin()) {
        	btnCategory.setDisable(true);
        	btnBrand.setDisable(true);
        }
    }
    
    @FXML
    private void handleButtonAddClic(javafx.event.ActionEvent mouseEvent) {    	
    	loadModal("ADD", mouseEvent,  new Product(),"PRODUCT", "ModalProduct" ); 
    }
    
    @FXML
    private void categories(javafx.event.ActionEvent mouseEvent) {    	
    	loadModal("ADD", mouseEvent,  new Product(),"CATEGORY", "ModalCategories"  ); 
    }
    
    @FXML
    private void brands(javafx.event.ActionEvent mouseEvent) {    	
    	loadModal("ADD", mouseEvent,  new Product(),"BRAND", "ModalBrands"  ); 
    }
    
    
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {
        if (mouseEvent.getSource() == btnProds) {
            loadStage("/com/opm/pos/fxml/Products.fxml",mouseEvent);
        } else if (mouseEvent.getSource() == btnCustomers) {
            loadStage("/com/opm/pos/fxml/OnBoard.fxml",mouseEvent);
        } else if (mouseEvent.getSource() == btnCredits) {
            loadStage("/com/opm/pos/fxml/OnBoard.fxml",mouseEvent);
        } else if (mouseEvent.getSource() == btnSales) {
            loadStage("/com/opm/pos/fxml/OnBoard.fxml",mouseEvent);
        } else if (mouseEvent.getSource() == btnSetup) {
            loadStage("/com/opm/pos/fxml/OnBoard.fxml",mouseEvent);
        } else if (mouseEvent.getSource() == btnExit){
        	System.out.println("SALIR");
        	loadStage("/com/opm/pos/fxml/Login.fxml",mouseEvent);
        }
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

    	      TableColumn actionCol = new TableColumn("MODIFICAR");
    	      actionCol.setCellValueFactory(new PropertyValueFactory("action"));

    	      tblData.getColumns().removeAll(idCol,serieCol,descCol,brandCol,catCol,countCol,priceCol,actionCol);  
    	      tblData.getColumns().addAll(idCol,serieCol,descCol,brandCol,catCol,countCol,priceCol,actionCol);
    	      
    	      
    	      
    	      Callback<TableColumn<Product, String>, TableCell<Product, String>> cellFoctory = (TableColumn<Product, String> param) -> {
    	            final TableCell<Product, String> cell = new TableCell<Product, String>() {
    	                @Override
    	                public void updateItem(String item, boolean empty) {
    	                    super.updateItem(item, empty);
    	                    if (empty) {
    	                        setGraphic(null);
    	                        setText(null);

    	                    } else {
    	                    	
    	                        FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL_SQUARE);

    	                        editIcon.setStyle(
    	                                " -fx-cursor: hand ;"
    	                                + "-glyph-size:28px;"
    	                                + "-fx-fill:#00E676;"
    	                        );

    	                        editIcon.setOnMouseClicked((MouseEvent event) -> {    	                            
    	                            Product pro = (Product) tblData.getSelectionModel().getSelectedItem();
    	                            loadModal("UPDATE", event,  pro,"PRODUCT", "ModalProduct" ); 
    	                        });

    	                        HBox managebtn = new HBox(editIcon);
    	                        managebtn.setStyle("-fx-alignment:center");
    	                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
    	                        setGraphic(managebtn);
    	                        setText(null);
    	                    }
    	                }
    	            };
    	            return cell;
    	        };
    	        actionCol.setCellFactory(cellFoctory);
            
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

    
    private void loadModal(String opcion, Event event, Product pro, String type, String nameFxml ) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/opm/pos/fxml/"+nameFxml+".fxml"));
        	Parent root = loader.load();
	        Node node = (Node) event.getSource();
	        Stage stage = (Stage) node.getScene().getWindow();
	        Stage dialog = new Stage();
        	dialog.setScene(new Scene(root));
        	dialog.initOwner(stage);
        	dialog.getIcons().add(new Image("/com/opm/pos/images/logo.png"));
        	dialog.initModality(Modality.APPLICATION_MODAL); 
        	
        	if(type.equals("PRODUCT")) {
        		dialog.setTitle("Productos");
            	ProductMaintenanceController modal= loader.getController();
            	if("UPDATE".equals(opcion)) {
                	modal.initData(pro);        		
            	}
            	modal.setProductController(controller);        		
        	}else if(type.equals("CATEGORY")) {
        		dialog.setTitle("Categorias");        		
        	}else if(type.equals("BRAND")) {
        		dialog.setTitle("Marcas");
        	}
        	
        	
        	dialog.setResizable(false);
        	dialog.showAndWait();
        } catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error  " +e);
		}
        
    }

    
    private void loadStage(String fxml, javafx.event.ActionEvent event) {
        try {
        	Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Node node = (Node) event.getSource();
            Stage stage = (Stage) node.getScene().getWindow();
            stage.getScene().setRoot(root);            	
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
