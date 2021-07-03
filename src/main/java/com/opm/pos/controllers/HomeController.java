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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.opm.pos.models.SysProperty;



public class HomeController  implements Initializable {
	
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
      
    private Pane view;
    
    @FXML
    private AnchorPane anchorPane;

    @FXML
    private FontAwesomeIconView iconProd;
    
    @FXML
    private FontAwesomeIconView iconCustomer;
    @FXML
    private FontAwesomeIconView iconCredits;
    @FXML
    private FontAwesomeIconView iconSales;
    @FXML
    private FontAwesomeIconView iconSetting;
    @FXML
    private FontAwesomeIconView iconExit;
    
    @FXML
    private BorderPane brdPane;
    
    private static Timer timer;
    @FXML
    private  Label lblTitle;
	@Override
    public void initialize(URL url, ResourceBundle rb) { 
    	brdPane.setStyle("-fx-border-color : black; -fx-border-width : 0 5 ");
		if(!SysProperty.getIsAdmin()) {
			btnSetup.setVisible(false);
			timer= new Timer();
        	view =  getPage("Sales");
        	brdPane.setCenter(view);   
        	lblTitle.setText("Ventas");
		}else {
			btnSetup.setVisible(true);
 		}
	}
    
    
    @FXML
    private void handleButtonClicks(javafx.event.ActionEvent mouseEvent) {

    	Node node = (Node) mouseEvent.getSource();
         Stage stage = (Stage) node.getScene().getWindow();
         
    	if(null!= SysProperty.getIsActiveTimer() && SysProperty.getIsActiveTimer()) {
    		timer.cancel();
    	}
    	
        if (mouseEvent.getSource() == btnProds) {
        	view =  getPage("Products");
        	lblTitle.setText("Productos");
        
        } else if (mouseEvent.getSource() == btnCustomers) {
        	view =  getPage("Customers");
        	lblTitle.setText("Clientes");
        } else if (mouseEvent.getSource() == btnCredits) {
        	view =  getPage("Credits");
        	lblTitle.setText("Cr�ditos");
        } else if (mouseEvent.getSource() == btnSales) {
        	timer= new Timer();
        	view =  getPage("Sales");
        	lblTitle.setText("Ventas");
        } else if (mouseEvent.getSource() == btnSetup) {
        	view =  getPage("Configuration");
        	lblTitle.setText("Configuraci�n");
        } else if (mouseEvent.getSource() == btnExit){
        	try {
	            stage.setMaximized(false);
	            stage.setResizable(false);
	            stage.close();
	            Parent root = FXMLLoader.load(getClass().getResource("/com/opm/pos/fxml/Login.fxml"));
	            Scene scene = new Scene(root);
	            stage.setScene(scene);
	            stage.show();
			} catch (IOException e) {
				System.out.println("Login error");
				e.printStackTrace();
			}
        }      
        brdPane.setCenter(view);        	
    }
    
    private Pane getPage(String fileName) {
    	try {
    		URL fileURL= getClass().getResource("/com/opm/pos/fxml/"+fileName+".fxml");
    		if(null==fileURL) {
    			throw new FileNotFoundException("No se encontr� archivo fxml");
    		}
    		view = new FXMLLoader().load(fileURL);
    	}catch(Exception e ) {
    		System.out.println("Exception "+e);
    	}
    	
    	return view;
    }
    
    public static Timer getTimer() {
		return timer;
	}


}
