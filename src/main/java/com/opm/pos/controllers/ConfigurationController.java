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
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ConfigurationController implements Initializable {

	@FXML
	private Button btnImport;

	@Override
	public void initialize(URL url, ResourceBundle rb) {

	}

	@FXML
	private void importData(javafx.event.ActionEvent mouseEvent) {
		Node node = (Node) mouseEvent.getSource();
		Stage stage = (Stage) node.getScene().getWindow();

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Seleccione el archivo Excel");

		// Stage selectFile = new Stage();

		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("All Excel", "*.xlsx"), new FileChooser.ExtensionFilter("XLS", "*.xls"),
				new FileChooser.ExtensionFilter("XLSX", "*.xlsx"));
		File file = fileChooser.showOpenDialog(stage);
		FileInputStream fis = null;
				
		try {
		
			if (file != null) {
				Workbook workbook = new XSSFWorkbook(fis);
				Sheet firstSheet = workbook.getSheetAt(0);
				Iterator<Row> iterator = firstSheet.iterator();

				while (iterator.hasNext()) {
					System.out.println("Dentro");
					Row nextRow = iterator.next();
					Iterator<Cell> cellIterator = nextRow.cellIterator();

					while (cellIterator.hasNext()) {
						Cell cell = cellIterator.next();
						System.out.print(cell.getStringCellValue());
						System.out.print(" - ");
					}
				}
				
				workbook.close();
			}

		} catch (IOException io) {
			System.out.println("Error al carga datos " + io);
		} finally {
				try {
					
					if(null!=fis)
						fis.close();
					
				} catch (IOException e) {
					System.out.println("Error al cerrar archivos");
					e.printStackTrace();
				}

		}

	}
}
