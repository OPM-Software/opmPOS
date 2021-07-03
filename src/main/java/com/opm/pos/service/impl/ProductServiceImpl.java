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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.opm.pos.models.Product;
import com.opm.pos.service.ProductService;
import com.opm.pos.utils.ConnectionUtil;

public class ProductServiceImpl implements ProductService {
	
	private static final Logger logger = Logger.getLogger(ProductServiceImpl.class.getName());
	private List<Product> products = new ArrayList<Product>();


	
	@Override
	public List<Product>  getProducts() throws SQLException {
		Connection con = null;
		con = ConnectionUtil.getDBConnection();
    	System.out.println("Conected...");
		List<Product> getAll = new ArrayList<Product>();
		ResultSet rs =null;
    	try {    	
			Product product = new Product();
			String SQL = "SELECT id,descripcion,precio,marca, categoria,existencia, serie from products where id>0;";
			rs = con.createStatement().executeQuery(SQL);
		
			while (rs.next()) {
				product = new Product();
				product.setId(rs.getInt("id"));
				product.setDescription(rs.getString("descripcion"));
				product.setPrice(rs.getDouble("precio"));
				product.setBrand(rs.getString("marca"));
				product.setCategory(rs.getString("categoria"));
				product.setCount(rs.getInt("existencia"));
				product.setSerie(rs.getString("serie"));
	
				if(product.getCount()>0) {
					product.setAvailable(true);				
				}else {
					product.setAvailable(false);
				}
				getAll.add(product);
			}
	    	System.out.println("success...");
    	} catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			rs.close();		
			if (null != con) {
				con.close();
		    	System.out.println("close connection...");

			}
		}
		
		return getAll;
	}

	@Override
	public Product getProduct(String serie){	
		Connection con = null;
		ResultSet rs =null;
		Product product = null;
    	try {    	    		
    		con = ConnectionUtil.getDBConnection();			
    		String SQL = "SELECT id,descripcion,precio,marca, categoria,existencia, serie from products where serie="+serie+";";
			rs = con.createStatement().executeQuery(SQL);
		
			while (rs.next()) {
				product = new Product();
				product.setId(rs.getInt("id"));
				product.setDescription(rs.getString("descripcion"));
		  		
    			System.out.println("descripcion " +product.getDescription());
				product.setPrice(rs.getDouble("precio"));
    			System.out.println("precio " +product.getPrice());

				product.setBrand(rs.getString("marca"));
				product.setCategory(rs.getString("categoria"));
				product.setCount(rs.getInt("existencia"));
				product.setSerie(rs.getString("serie"));
				if(product.getCount()>0) {
					product.setAvailable(true);				
				}else {
					product.setAvailable(false);
				}
			}
	    } catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			try {
				rs.close();		
				if (null != con) {
					con.close();
			    	System.out.println("close connection...");
	
				}
			}catch(Exception e) {
				logger.log(Level.SEVERE, e.getMessage());
			}
		}
		
		return product;
	}

	@Override
	public void addProduct(Product prod) throws SQLException{			
	
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con= ConnectionUtil.getDBConnection();
		
			String st = "INSERT INTO products ( serie, descripcion, precio, marca, categoria,existencia) VALUES (?,?,?,?,?,?);";
	        preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setString(1,prod.getSerie() );
	        preparedStatement.setString(2,prod.getDescription());
	        preparedStatement.setDouble(3,prod.getPrice());
	        preparedStatement.setString(4,prod.getBrand());
	        preparedStatement.setString(5,prod.getCategory());
	        preparedStatement.setInt(6,prod.getCount());	
	        preparedStatement.executeUpdate();
			System.out.println("Success Insert...");

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
	
	
	@Override
	public void updateProduct(Product prod) throws SQLException{			
    	System.out.println("idProducts     ="+prod.getId());

		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con= ConnectionUtil.getDBConnection();
			System.out.println("Conected...");
		
			String st = "update products "
					+ " set serie =?,"
					+ " descripcion=?,"
					+ " precio=?,"
					+ " marca=?,"
					+ " categoria=?,"
					+ " existencia=?"
					+ " where id=?";
			
	        preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setString(1,prod.getSerie() );
	        preparedStatement.setString(2,prod.getDescription());
	        preparedStatement.setDouble(3,prod.getPrice());
	        preparedStatement.setString(4,prod.getBrand());
	        preparedStatement.setString(5,prod.getCategory());
	        preparedStatement.setInt(6,prod.getCount());	
	        preparedStatement.setInt(7,prod.getId());	
	       
	        preparedStatement.executeUpdate();
			System.out.println("Success Update...");

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
	
	
	
	

	@Override
	public boolean sales(Product product, int count) {
		int assignedCount = product.getCount();
		if (count > assignedCount) {
			return false;
		} else {
			product.setCount(assignedCount - count);
			products.remove(product.getId() - 1);
			products.add(product.getId() - 1, product);
			return true;
		}
	}

	@Override
	public Integer getCountProducts() throws SQLException {
		
		Connection con = null;
		ResultSet resultSet = null;
		Integer total=0;
		try {
			String SQL = " select (id) as total from products";
			ResultSet rs=  con.createStatement().executeQuery(SQL);
			total= rs.getInt("total");
		}catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
		
			if (null != con) {
				con.close();
			}
		}
		
		return total;
	}
}
