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
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimerTask;

import com.opm.pos.controllers.modal.QuantityController;
import com.opm.pos.controllers.modal.ListProductController;
import com.opm.pos.controllers.modal.PaymentController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.opm.pos.models.Product;
import com.opm.pos.models.Sale;
import com.opm.pos.models.SysProperty;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.impl.ProductServiceImpl;



public class SaleController   implements Initializable {
	
	private SaleController controller= this;
	public Sale item= new Sale();
	
	@FXML
    private TextField txtFilter;
    @FXML
    Label lblFilter;
    @FXML
    public TableView tblData;
    
    @FXML
    Label lblDate;
    @FXML
    Label lblHour;

	@FXML
    public  Label txtSubtotal;
	@FXML
    public  Label txtIVA;
	@FXML
    public  Label txtTotal;
	@FXML
	private Button btnSearch;
	@FXML
	private Button btnClean;
	@FXML
	private Button btnCancel;
	@FXML
	private Button btnPayment;
	@FXML
	private Button btnDelete;
	@FXML
	private Button btnCount;

	
	private  ProductService productService = new ProductServiceImpl();

    public  ObservableList<Sale> data = FXCollections.observableArrayList();
    
    Alert a = new Alert(AlertType.NONE);

    @Override
    public void initialize(URL url, ResourceBundle rb) {    	
    	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    	Date date = new Date();
    	lblDate.setText(formatter.format(date));
    	DateTimeFormatter formateador = DateTimeFormatter.ofPattern("HH:mm:ss");
    	lblHour.setText(formateador.format(LocalDateTime.now()));    	
    	HomeController.getTimer().scheduleAtFixedRate( new TimerTask(){
    		public void run() {
    			Platform.runLater(new Runnable() {
				    	@Override
				    	public void run() {
				            lblHour.setText(formateador.format(LocalDateTime.now()));
				    	}
    			});
    		}
    	},0,500);  
    	
    	txtFilter.textProperty().addListener((observable, oldValue, newValue) -> {
    	    System.out.println("textfield changed from " + oldValue + " to " + newValue);
    	});
    	SysProperty.setIsActiveTimer(true);
    	txtIVA.setText("$0.00");
    	txtSubtotal.setText("$0.00");
    	txtTotal.setText("0.00");
    	fetColumnList();
	    data.addListener((ListChangeListener<Sale>) change -> {
	        while (change.next()) {
	            if (change.wasAdded()) {
	                System.out.println(change.getAddedSubList().get(0)
	                        + " was added to the list!");
	            } else if (change.wasRemoved()) {
	                System.out.println(change.getRemoved().get(0)
	                        + " was removed from the list!");
	            }
	        }
	    });
    	
    }
    
 
    
    @FXML
    private void updateCount(ActionEvent event) {
    	
    	if(null!= tblData && tblData.getItems().size()>0 && null != tblData.getSelectionModel().getSelectedItem()) {
        	item= (Sale) tblData.getSelectionModel().getSelectedItem();
        	loadModal(event,"/com/opm/pos/fxml/ModalQuantity.fxml","COUNT");    		
    	}	
    }
         
    
    @FXML
    private void clean(ActionEvent event) {
    	
    	if(null!=tblData && tblData.getItems().size()>0) {
        	a.setAlertType(AlertType.CONFIRMATION);
            a.setContentText("Esta seguro que desea limpiar todo?");        
            Optional<ButtonType> result = a.showAndWait();
        	if(result.get() == ButtonType.OK) {
            	tblData.getItems().clear(); 	
            	txtSubtotal.setText("$0.00");
            	txtTotal.setText("0.00");    		
        	}	    		
    	}

    }
         

    @FXML
    private void deleteRow(ActionEvent event) {
    	
    	if(tblData.getItems().size()>0 && null!= tblData.getSelectionModel().getSelectedItem()) {
    		a.setAlertType(AlertType.CONFIRMATION);
            a.setContentText("Esta seguro que desea quitar el producto?");
            
            Optional<ButtonType> result = a.showAndWait();
        	if(result.get() == ButtonType.OK) {
                 	
            	Sale sale = (Sale) tblData.getSelectionModel().getSelectedItem();
            	//sales.removeIf(saleRemove-> saleRemove.getIdProduct()==sale.getIdProduct());
            	tblData.getItems().removeAll(
            			tblData.getSelectionModel().getSelectedItems()
                );   
            	tblData.refresh();  
            	
            	if(tblData.getItems().size()<=0) {
                	txtSubtotal.setText("$0.00");
                	txtTotal.setText("0.00");            		
            	}else {
            		List<Sale> sales= tblData.getItems();
            		Double total= sales.stream().mapToDouble(o->o.getTotal()).sum();
            		txtSubtotal.setText("$"+ String.valueOf(total));
            		txtTotal.setText(String.valueOf(total));
            	}
        	} else {
        		System.out.println("okoko");
        	}
        }
    }

    
    @FXML
    private void handleOnKeyPressed(KeyEvent event)
    {
        if (event.getCode().equals(KeyCode.F5)) {
        	btnSearch.fire();
        }else if(event.getCode().equals(KeyCode.F6)) {
        	btnCount.fire();
        }else if(event.getCode().equals(KeyCode.F7)) {
        	btnDelete.fire();
        }else if(event.getCode().equals(KeyCode.F8)) {
        	btnCancel.fire();
        }else if(event.getCode().equals(KeyCode.F9)) {
        	btnClean.fire();
        }else if(event.getCode().equals(KeyCode.F10)) {
        	btnPayment.fire();
        }else if(event.getCode().equals(KeyCode.PLUS)) {
        	System.out.println("+");
        }else if(event.getCode().equals(KeyCode.MINUS)) {
        	System.out.println("-");
        }
    }
    
    
    @FXML
    private void handleButtonAddProduct(javafx.event.ActionEvent mouseEvent) {     	
    	Product product= null;	    	
		if(!"".equals( txtFilter.getText())) {
			product= productService.getProduct(txtFilter.getText());   
			if (null!=product) {
    			fetRowList(product);
    		}else {
                a.setAlertType(AlertType.INFORMATION);
                a.setContentText("Producto no encontrado");
                a.show();
    		}
    	}   		
    }
    
    
    
    @FXML
    private void handleButtonSearch(javafx.event.ActionEvent mouseEvent) {     	
    	loadModal( mouseEvent,"/com/opm/pos/fxml/ModalListProducts.fxml","LIST");         			         		
    }
   
    @FXML
    private void handleButtonPay(javafx.event.ActionEvent mouseEvent) {     	
    	loadModal( mouseEvent,"/com/opm/pos/fxml/ModalPayment.fxml","PAY");         			         		
    }
    
    
    
    private void fetColumnList() {       
	      TableColumn descCol = new TableColumn("DESCRIPCION");
	      descCol.setCellValueFactory(new PropertyValueFactory("descriptionProduct"));
	      
	      TableColumn priceCol = new TableColumn("PRECIO");
	      priceCol.setCellValueFactory(new PropertyValueFactory("priceProduct"));

	      TableColumn countCol = new TableColumn("CANTIDAD");
	      countCol.setCellValueFactory(new PropertyValueFactory<>("count"));

	      TableColumn importe = new TableColumn("IMPORTE");
	      importe.setCellValueFactory(new PropertyValueFactory("total"));

	      countCol.setEditable(true);	        
	      
	      tblData.getColumns().removeAll(descCol,priceCol,countCol,importe);  
	      tblData.getColumns().addAll(descCol,priceCol,countCol,importe);
	      
	      
    }
    
    public void fetRowList(Product product) { 
    	
    	List<Sale> sales = new ArrayList<Sale>();
    	Sale sale= new Sale();
    	sale.setDescriptionProduct(product.getDescription());
    	sale.setIdProduct(product.getId());
    	sale.setPriceProduct(product.getPrice());
    	sale.setIdProduct(product.getId());
    	sale.setSerie(product.getSerie());
    	
    	
		List<Sale> resultado = data.stream().filter((prod) -> prod.getIdProduct() == product.getId()).collect(java.util.stream.Collectors.toList());
		Sale result= resultado.isEmpty() ? null : resultado.get(0);
    	
		if(null!=result) {
			
			data.forEach(item -> {
                if (item.getIdProduct() == product.getId()) {
                	item.setCount(item.getCount()+1);
                	item.setTotal(item.getCount()* item.getPriceProduct());
                } 
        	});
    	}else {
        	sale.setCount(1);
        	sale.setTotal(product.getPrice());
        	sales.add(sale);     	
        	data.add(sale);		
       }

		tblData.setItems(data);
		tblData.refresh();
    	Double sumatoria= data.stream().mapToDouble(Sale::getTotal).sum();
    	txtSubtotal.setText("$" + String.valueOf(sumatoria));
    	Double total= Double.valueOf(txtSubtotal.getText().replace("$", "")) + Double.valueOf(txtIVA.getText().replace("$", ""));
    	txtTotal.setText(String.valueOf(total));    	
        tblData.lookup(".scroll-bar:vertical");
    }
    
    
    private void loadModal(Event event, String fxml,String type ) {
        try {
        	FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml));
        	Parent root = loader.load();
	        Node node = (Node) event.getSource();
	        Stage stage = (Stage) node.getScene().getWindow();
	        Stage dialog = new Stage();
        	dialog.setScene(new Scene(root));
        	dialog.initOwner(stage);
        	dialog.getIcons().add(new Image("/com/opm/pos/images/logo.png"));
        	dialog.initModality(Modality.APPLICATION_MODAL); 
        	
        	if(type.equals("LIST")) {
            	ListProductController modal= loader.getController();
            	modal.setSaleController(controller);        		
        	}else if(type.equals("COUNT")) {
        		QuantityController count= loader.getController();
        		count.setSaleController(controller);
        		count.initData(item);
        	}else if(type.equals("PAY")) {
        		PaymentController pay= loader.getController();
        		pay.setSaleController(controller);
        		pay.initData(txtTotal.getText());        		
        	}
        	
        	dialog.setResizable(false);
        	dialog.showAndWait();
        } catch (IOException e) {
			e.printStackTrace();
			System.out.println("Error  " +e);
		}
        
    }
     
}
