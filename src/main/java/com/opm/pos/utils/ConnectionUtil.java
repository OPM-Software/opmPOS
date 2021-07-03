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


package com.opm.pos.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionUtil {

		private static final Logger logger = Logger.getLogger(ConnectionUtil.class.getName());
		private static final String DB_DRIVER ="sun.jdbc.odbc.JdbcOdbcDriver";
		private  final static String DB_CONNECTION = "jdbc:ucanaccess://"; 
		private static final String DB_USER = "";
		private static final String DB_PASSWORD = "";
		
		private ConnectionUtil() {
			
		}
		
		public static Connection getDBConnection() throws SQLException {

			Connection connection = null;
			try {
				Class.forName(DB_DRIVER);
			} catch (ClassNotFoundException exception) {
				logger.log(Level.SEVERE, exception.getMessage());
			}

			try {
				String urlDB= "C:\\opmPOS\\sysman.accdb";//System.getProperty("urlDB");
				System.out.println("urlDB="+urlDB);
				connection = DriverManager.getConnection(DB_CONNECTION+urlDB, DB_USER, DB_PASSWORD);
				return connection;
			} catch (SQLException exception) {
				logger.log(Level.SEVERE, exception.getMessage());
			}

			return connection;
		}
}
