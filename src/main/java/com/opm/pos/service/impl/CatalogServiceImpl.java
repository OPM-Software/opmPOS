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

import com.opm.pos.models.Catalog;
import com.opm.pos.service.CatalogService;
import com.opm.pos.utils.ConnectionUtil;

public class CatalogServiceImpl implements CatalogService {

	private static final Logger logger = Logger.getLogger(CatalogServiceImpl.class.getName());

	
	@Override
	public List<Catalog> getBrands() throws SQLException {		
		Connection con = null;
		con = ConnectionUtil.getDBConnection();
    	System.out.println("Conected...");
		List<Catalog> items = new ArrayList<Catalog>();
		ResultSet rs =null;
		
    	try {    	
			Catalog catalog = new Catalog();
			String SQL = "SELECT id,descripcion from marcas where id>0;";
			rs = con.createStatement().executeQuery(SQL);
		
			while (rs.next()) {
				catalog = new Catalog();
				catalog.setId(rs.getInt("id"));
				catalog.setDescription(rs.getString("descripcion"));
				items.add(catalog);
			}

    	} catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			rs.close();		
			if (null != con) {
				con.close();
		    	System.out.println("close connection...");
			}
		}
		return items;
	}

	@Override
	public List<Catalog> getCategories() throws SQLException {
	
		Connection con = null;
		con = ConnectionUtil.getDBConnection();
    	System.out.println("Conected...");
		List<Catalog> items = new ArrayList<Catalog>();
		ResultSet rs =null;
		
    	try {    	
			Catalog catalog = new Catalog();
			String SQL = "SELECT id,descripcion from categorias where id>0;";
			rs = con.createStatement().executeQuery(SQL);
		
			while (rs.next()) {
				catalog = new Catalog();
				catalog.setId(rs.getInt("id"));
				catalog.setDescription(rs.getString("descripcion"));
				items.add(catalog);
			}

    	} catch (SQLException exception) {
			logger.log(Level.SEVERE, exception.getMessage());
		} finally {
			rs.close();		
			if (null != con) {
				con.close();
		    	System.out.println("close connection...");
			}
		}
		return items;
	}

	@Override
	public void add(Catalog cat,String type) throws SQLException {
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con= ConnectionUtil.getDBConnection();
			String catalogName="";
			if (type.equals("BRAND")) {
				catalogName="marcas";
			}else {
				catalogName="categorias";
			}
			
			String st = "INSERT INTO " +catalogName+" (descripcion) VALUES (?);";
			preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setString(1,cat.getDescription());
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

	@Override
	public void update(Catalog cat,String type) throws SQLException {
	
		Connection con = null;
		PreparedStatement preparedStatement = null;

		try {
			con= ConnectionUtil.getDBConnection();
			String catalogName="";
			if (type.equals("BRAND")) {
				catalogName="marcas";
			}else {
				catalogName="categorias";
			}
			
			String st = "update "+catalogName
					+ " set descripcion=?"
					+ " where id=?";
			
	        preparedStatement =  con.prepareStatement(st);
	        preparedStatement.setString(1,cat.getDescription());
	        preparedStatement.setInt(2,cat.getId());		       
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
