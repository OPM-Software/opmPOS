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

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.opm.pos.models.SysProperty;
import com.opm.pos.utils.ConnectionUtil;


public class LoginController implements Initializable {

    @FXML
    private Label lblErrors;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtPassword;

    @FXML
    private Button btnSignin;
    Connection con = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    @FXML
    public void handleButtonAction(MouseEvent event) {

        if (event.getSource() == btnSignin) {
            if (logIn().equals("Success")) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.setMaximized(true);
                    stage.setResizable(false);
                    stage.close();
                    Parent root = FXMLLoader.load(getClass().getResource("/com/opm/pos/fxml/Home.fxml"));
                    Scene scene = new Scene(root);
                    stage.setScene(scene);                    
                    stage.show();
                } catch (Exception ex) {
                    System.err.println(ex.getMessage());
                }
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        if (con == null) {
            lblErrors.setTextFill(Color.TOMATO);
            lblErrors.setText("Error en Servidor de Base de Datos");
        } else {
            lblErrors.setTextFill(Color.GREEN);
            lblErrors.setText("Servidor de Base de Datos trabajando");
        }
    }

    public LoginController() {
        try {
			con = ConnectionUtil.getDBConnection();
		} catch (SQLException e) {
		   lblErrors.setTextFill(Color.TOMATO);
           lblErrors.setText("Error en Servidor.");
		}
    }

    private String logIn() {    	
        String status = "Success";
        String email = txtUsername.getText();
        String password = txtPassword.getText();
        if(email.isEmpty() || password.isEmpty()) {
            setLblError(Color.TOMATO, "Credentiales vacios");
            status = "Error";
        } else {        
        	String sql = "SELECT * FROM users Where email = ? and password = ?";
            try {
                preparedStatement = con.prepareStatement(sql);
                preparedStatement.setString(1, email);
                preparedStatement.setString(2, password);
                resultSet = preparedStatement.executeQuery();
                if (!resultSet.next()) {
                    setLblError(Color.TOMATO, "Usuario y Password incorrecto.");
                    status = "Error";
                    SysProperty.setIsSuccess(false);
                } else {
                    setLblError(Color.GREEN, "Login exitoso..redireccionando..");
                    SysProperty.setIsAdmin(resultSet.getBoolean("isAdmin"));
                    SysProperty.setIsSuccess(true);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                status = "Exception";
                SysProperty.setIsSuccess(false);
            }finally {
            	
            	try {
            		if(SysProperty.getIsSuccess()) {
    					con.close();            			
            		}
				} catch (SQLException e) {
	                System.err.println(e.getMessage());
				}
            }
        }
        
        return status;
    }
    
    private void setLblError(Color color, String text) {
        lblErrors.setTextFill(color);
        lblErrors.setText(text);
        System.out.println(text);
    }
}
