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


package com.opm.pos.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opm.pos.models.Product;
import com.opm.pos.models.Sale;
import com.opm.pos.models.Transaction;
import com.opm.pos.service.ProductService;
import com.opm.pos.service.StockService;
import com.opm.pos.utils.ConnectionUtil;
import com.opm.pos.utils.Util;

public class StockServiceImpl implements StockService {

	
	private static final Logger logger = Logger.getLogger(StockServiceImpl.class.getName());
	ProductService productService= new ProductServiceImpl();
	@Override
	public String createTransaction(Transaction transaction) throws SQLException {
		Connection con = null;
		PreparedStatement preparedStatement = null;
		String folio="";
		try {
			folio=Util.generateFolio();
			con= ConnectionUtil.getDBConnection();
		
			String st = "INSERT INTO transaccion ( folio, cantidadProducto, totalVenta) "
						+ " VALUES (?,?,?);";
	        preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setString(1,folio);
	        preparedStatement.setInt(2,transaction.getProductCount());
	        preparedStatement.setDouble(3,transaction.getTotal());
	        preparedStatement.executeUpdate();
	        System.out.println("Sucess");
		} catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			if (null != preparedStatement) {
				preparedStatement.close();
			}
	
			if (null != con) {
				con.close();
			}
		}
	
		return folio;
	}

	@Override
	public void createDetails(List<Sale> sales, String folio) throws SQLException {
		
		Connection con = null;
		PreparedStatement preparedStatement = null;
		for(Sale sale: sales) {
			
			try {
				con= ConnectionUtil.getDBConnection();
			
				String st = "INSERT INTO detalleTransaccion ( folioTransaccion, idProducto, cantidad,total) "
							+ " VALUES (?,?,?,?);";
		        preparedStatement =  con.prepareStatement(st);
		        preparedStatement.setString(1,folio);
		        preparedStatement.setInt(2,sale.getIdProduct());
		        preparedStatement.setDouble(3,sale.getCount());
		        preparedStatement.setDouble(4,sale.getTotal());
		        preparedStatement.executeUpdate();
		        System.out.println("Sucess createDetails");
		        updateStock(sale.getSerie(),sale.getCount());
			} catch (SQLException exception) {
				logger.log(Level.SEVERE, exception.getMessage());
			} finally {
				if (null != preparedStatement) {
					preparedStatement.close();
				}
		
				if (null != con) {
					con.close();
				}
			}
		
		}
	}
	
	
	
	@Override
	public void updateStock(String serie, int count) throws SQLException{			
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con= ConnectionUtil.getDBConnection();
			Product product = productService.getProduct(serie);
			
			
			String st = "update products "
					+ " set existencia =?"
					+ " where id=?";
			
	        preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setInt(1,product.getCount() - count );
	        preparedStatement.setInt(2,product.getId());
	        preparedStatement.executeUpdate();
		} catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			if (null != preparedStatement) {
				preparedStatement.close();
			}
	
			if (null != con) {
				con.close();
			}
		}
	
	}
	

}
