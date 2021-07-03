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

import com.opm.pos.controllers.ProductController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.opm.pos.models.Catalog;
import com.opm.pos.models.Product;
import com.opm.pos.service.CatalogService;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.CatalogServiceImpl;
import com.opm.pos.service.impl.ProductServiceImpl;



public class ProductMaintenanceController implements Initializable {
	
	
	private ProductController controller;
	public void setProductController(ProductController controller){
	    this.controller = controller;
	}
	
	private Integer identifier=0;
    @FXML
    private TextField txtSerie;
	@FXML
    private TextField txtDescription;
    @FXML
    private TextField txtPrice;
    @FXML
    private ComboBox<String> txtBrand;
    @FXML
    private ComboBox<String> txtCategory;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtCountInStock;
    @FXML
    private Label lblStatus;
    
    private ProductService productService = new ProductServiceImpl();
    private CatalogService catalogService = new CatalogServiceImpl();
    
    public void initData(Product product){
    	identifier=product.getId();
    	txtSerie.setText(product.getSerie());
    	txtCategory.getSelectionModel().select(product.getCategory());
    	txtBrand.getSelectionModel().select(product.getBrand());
    	txtCountInStock.setText(String.valueOf(product.getCount()));
    	txtDescription.setText(product.getDescription());
    	txtPrice.setText(String.valueOf(product.getPrice()));
    	btnSave.setText("Modificar");
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	initializeCombo();
    }

    
    @FXML
    private void HandleEvents(MouseEvent event) {    	
        if (txtSerie.getText().isEmpty() || txtDescription.getText().isEmpty()|| 
        	txtPrice.getText().isEmpty() || txtBrand.getValue().equals(null) || 
        	txtCategory.getValue().equals(null) || txtCountInStock.getText().isEmpty()) {
	        lblStatus.setTextFill(Color.TOMATO);
	        lblStatus.setText("Favor de llenar el formulario");
        } else {
            String result= saveData();
            if("Success".equals(result)) {
                Node node = (Node) event.getSource();
    	        Stage stage = (Stage) node.getScene().getWindow();
    	        controller.fetRowList();
    	        stage.close();
            }	        
        }
    }

    private String saveData() {
    	
        try {      	
        
        	Product product = new Product();
        	product.setSerie(txtSerie.getText());
        	product.setDescription(txtDescription.getText());
        	product.setPrice(Double.parseDouble(txtPrice.getText()));
        	product.setCount(Integer.parseInt(txtCountInStock.getText()));
        	product.setBrand(txtBrand.getValue().toString());
        	product.setCategory(txtCategory.getValue().toString());
        	product.setAvailable(product.getCount()>0?true:false);        	

        	System.out.println("identifier     ="+identifier);
        	
        	if(identifier>0) {
        		product.setId(identifier);
            	productService.updateProduct(product);             
        	}else {
            	productService.addProduct(product);
        	}
        	
            return "Success";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Exception";
        }
    }
    
    private  void initializeCombo() {
    	try {

    		List<Catalog> brands= catalogService.getBrands();
        	List<Catalog> categories= catalogService.getCategories();    		
        	
        	txtBrand.getItems().add("N/A");
        	txtCategory.getItems().add("N/A");
        	
        	for(Catalog brand: brands) {
        		txtBrand.getItems().add(brand.getDescription());
        	}
        	for(Catalog category: categories) {
        		txtCategory.getItems().add(category.getDescription());
        	}
        	txtBrand.getSelectionModel().selectFirst();
        	txtCategory.getSelectionModel().selectFirst();

    	}catch(SQLException ex) {
    		 System.out.println(ex.getMessage());
    	}
    	

    }
}
