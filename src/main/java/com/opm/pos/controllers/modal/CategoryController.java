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

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import com.opm.pos.controllers.modal.ProductMaintenanceController;
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
import com.opm.pos.models.Catalog;
import com.opm.pos.models.Product;
import com.opm.pos.service.CatalogService;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.CatalogServiceImpl;
import com.opm.pos.service.impl.ProductServiceImpl;



public class CategoryController implements Initializable {
	
	private CategoryController controller= this;
	private Integer identifier=0;
    @FXML
    private TextField txtFilter;
    @FXML
    Label lblFilter;
    @FXML
    TableView tblData;
   
    @FXML
    private Button btnAdd;

    @FXML
    private FontAwesomeIconView icnAdd;
    
    @FXML
    private HBox hbBox;
    @FXML
    private HBox hbBoxExit;
    
	private ObservableList<Catalog> data = FXCollections.observableArrayList();
    private CatalogService catalogService = new CatalogServiceImpl();

    @Override
    public void initialize(URL url, ResourceBundle rb) {    	
    	icnAdd = new FontAwesomeIconView(FontAwesomeIcon.PLUS);
    	fetColumnList();
        fetRowList();
    }
    
    @FXML
    private void handleButtonAddClic(javafx.event.ActionEvent mouseEvent) {    	
    	loadModal("ADD", mouseEvent,  new Catalog() ); 
    }
    
        
    private void fetColumnList() {       
    		  TableColumn idCol = new TableColumn("ID");
    		  idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	      TableColumn descCol = new TableColumn("DESCRIPCION");
    	      descCol.setCellValueFactory(new PropertyValueFactory("description"));
   
    	      TableColumn actionCol = new TableColumn("MODIFICAR");
    	      actionCol.setCellValueFactory(new PropertyValueFactory("action"));

    	      tblData.getColumns().removeAll(idCol,descCol,actionCol);  
    	      tblData.getColumns().addAll(idCol,descCol,actionCol);
    	      
    	      
    	      
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
    	                            Catalog cat = (Catalog) tblData.getSelectionModel().getSelectedItem();
    	                            loadModal("UPDATE", event,  cat ); 
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
    		List<Catalog> catalogs = catalogService.getCategories();
    		data = FXCollections.observableArrayList(catalogs);		
            tblData.setItems(data);    
            FilteredList<Catalog> filteredData = new FilteredList<>(data, p -> true);
    		txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
    			filteredData.setPredicate(catalog -> {
    				if (newValue == null || newValue.isEmpty()) {
    					return true;
    				}
    				
    				String lowerCaseFilter = newValue.toLowerCase();				
    				if(catalog.getDescription().toLowerCase().contains(lowerCaseFilter)) {
    					return true; 
    				}
    				return false; 
    			});
    		});
    		
    		SortedList<Catalog> sortedData = new SortedList<>(filteredData);
    		sortedData.comparatorProperty().bind(tblData.comparatorProperty());
    		tblData.setItems(sortedData);
            tblData.lookup(".scroll-bar:vertical");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    
    private void loadModal(String opcion, Event event, Catalog cat ) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/opm/pos/fxml/ModalCatalog.fxml"));
        	Parent root = loader.load();
	        Node node = (Node) event.getSource();
	        Stage stage = (Stage) node.getScene().getWindow();
	        Stage dialog = new Stage();
        	dialog.setScene(new Scene(root));
        	dialog.initOwner(stage);
        	dialog.getIcons().add(new Image("/com/opm/pos/images/logo.png"));
        	dialog.initModality(Modality.APPLICATION_MODAL);     
        	CatalogController modal= loader.getController();
           	modal.initData(cat,opcion,"CATEGORY");        		        	
        	modal.setCategoryController(controller);
        	dialog.setTitle("Categorias");
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
