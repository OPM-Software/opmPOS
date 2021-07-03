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


package com.opm.pos.models;

public class SysProperty {
	
	private static Boolean isAdmin;
	private static Boolean isActiveTimer;
	private static Boolean isSuccess;
	
	
	
	public static Boolean getIsAdmin() {
		return isAdmin;
	}

	public static void setIsAdmin(Boolean isadmin) {
		isAdmin = isadmin;
	}
	
	
	
	public static Boolean getIsActiveTimer() {
		return isActiveTimer;
	}

	public static void setIsActiveTimer(Boolean isactiveTimer) {
		isActiveTimer = isactiveTimer;
	}
	
	
	public static Boolean getIsSuccess() {
		return isSuccess;
	}

	public static void setIsSuccess(Boolean issuccess) {
		isSuccess = issuccess;
	}
}
