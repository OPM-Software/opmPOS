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



public class CatalogController implements Initializable {
	
	
	private CategoryController categoryController;
	private BrandController brandController;
	
	
	public void setCategoryController(CategoryController categoryController){
	    this.categoryController = categoryController;
	}
	
	public void setBrandController(BrandController brandController){
	    this.brandController = brandController;
	}

	private String type="";
	private Integer identifier=0;
	@FXML
    private TextField txtDescription;
	@FXML
    private Button btnSave;
	@FXML
	private Label lblStatus;
	
	private CatalogService catalogService = new CatalogServiceImpl();
    
    public void initData(Catalog catalog,String action, String operationType){
    	if("UPDATE".equals(action)) {
	    	identifier=catalog.getId();
	    	txtDescription.setText(catalog.getDescription());
	    	btnSave.setText("Modificar");
    	}
    	type=operationType;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
    }

    
    @FXML
    private void HandleEvents(MouseEvent event) {    	
        if (txtDescription.getText().isEmpty()) {
	        lblStatus.setTextFill(Color.TOMATO);
	        lblStatus.setText("Favor de llenar el formulario");
        } else {
            String result= saveData();
            if("Success".equals(result)) {
                Node node = (Node) event.getSource();
    	        Stage stage = (Stage) node.getScene().getWindow();
    	        
    	        System.out.println("type  =  = ="+type);
    	        if(type.equals("BRAND")) {
    	        	brandController.fetRowList();
    	        }else {
        	        categoryController.fetRowList();    	        	
    	        }
    	        
    	        stage.close();
            }	        
        }
    }

    private String saveData() {
    	
        try {      	
        
        	Catalog catalog = new Catalog();
        	catalog.setDescription(txtDescription.getText());
        	System.out.println("identifier     ="+identifier);
        	
        	if(identifier>0) {
        		catalog.setId(identifier);
            	catalogService.update(catalog,type);             
        	}else {
        		catalogService.add(catalog,type);
        	}
        	
            return "Success";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Exception";
        }
    }    
}
