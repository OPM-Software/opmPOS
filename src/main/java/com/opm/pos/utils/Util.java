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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

	public static String generateFolio() {
		String folio = "";
		try {
			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat format = new SimpleDateFormat("yyMMddHHmmss");
			SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
			int clave = number.nextInt();

			while (clave < 1) {
				clave = number.nextInt();
			}

			folio = format.format(today) + "000" + String.valueOf(clave);
		} catch (NoSuchAlgorithmException nsae) {
			System.out.println("Error al generar folio");
		}

		return folio;
	}
	
	
	
}
