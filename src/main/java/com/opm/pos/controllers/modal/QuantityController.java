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
import java.util.List;
import java.util.ResourceBundle;

import com.opm.pos.controllers.SaleController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import com.opm.pos.models.Product;
import com.opm.pos.models.Sale;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.ProductServiceImpl;



public class QuantityController implements Initializable {
		
	
	private SaleController controller;
	
	
	public void setSaleController(SaleController controller){
	    this.controller = controller;
	}

	@FXML
    private TextField txtCount;
	
    @FXML
    private Button btnCancel;

    @FXML
    private Button btnAdd;
    
	private  ProductService productService = new ProductServiceImpl();
    Alert alert = new Alert(AlertType.NONE);
    private Product product= new Product();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	txtCount.textProperty().addListener(new ChangeListener<String>() {
    	    @Override
    	    public void changed(ObservableValue<? extends String> observable, String oldValue, 
    	        String newValue) {
    	        if (!newValue.matches("\\d*")) {
    	        	txtCount.setText(newValue.replaceAll("[^\\d]", ""));
    	        }
    	    }
    	});
        	
    }

    public void initData(Sale sale) {
    	TableView tb= controller.tblData;    	
    	txtCount.setText(String.valueOf(sale.getCount()));
    }
    
    
    @FXML
    private void handleCancel (ActionEvent event) {    	
    	Node node = (Node) event.getSource();
	    Stage stage = (Stage) node.getScene().getWindow();
	    stage.close();
    }

    @FXML
    private void handleAdd (ActionEvent event) {   
    	
    	if("".equals(txtCount.getText())  || 
    	   "0".equals(txtCount.getText())
    	  ) {
    		 alert.setAlertType(AlertType.ERROR);
             alert.setContentText("Debe de ingresar una cantidad valida");
             alert.show();
    	}else {
    		
    		
	    	TableView tb= controller.tblData;    	
	    	ObservableList<Sale> data,items;
	    	items= tb.getSelectionModel().getSelectedItems();
	    	Sale verify= (Sale)tb.getSelectionModel().getSelectedItem();
	    	
	    	if(verifyStock(verify.getSerie(), Integer.valueOf(txtCount.getText()))){
		    	items.forEach(
		    	          (sale)  -> {
		    	        	  sale.setCount(Integer.valueOf(txtCount.getText()));
		    	        	  sale.setTotal(sale.getCount() * sale.getPriceProduct());
		    	          });
		    	controller.tblData.refresh();	
		    	
		    	
		    	
		    	List<Sale> sales= controller.tblData.getItems();
        		Double total= sales.stream().mapToDouble(o->o.getTotal()).sum();
        		controller.txtSubtotal.setText("$"+ String.valueOf(total));
        		controller.txtTotal.setText(String.valueOf(total));

        		Node node = (Node) event.getSource();
			    Stage stage = (Stage) node.getScene().getWindow();
			    stage.close();
	    	}else {
	    		 alert.setAlertType(AlertType.ERROR);
	    		 alert.setTitle("Error");
	             alert.setContentText("Cantidad ingresada excede a existencias, total en existencia: "+product.getCount());
	             alert.show();
	 		
	    	}
	    	    	
    	}
    }


    public boolean verifyStock(String serie,int count){
    	product= productService.getProduct(serie);
    	if(product.getCount()<count) {
    		return false;
    	}else {
    		return true;
    	}
    }
}
