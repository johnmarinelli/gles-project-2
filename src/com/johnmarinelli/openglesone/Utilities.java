package com.johnmarinelli.openglesone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;

public class Utilities {

	/*
	 * retrieve from a text file
	 */
	public static String readFile(Context ctx, int resID) {
		InputStream is = ctx.getResources().openRawResource(resID);
		InputStreamReader inputReader = new InputStreamReader(is);
		BufferedReader bufferedReader = new BufferedReader(inputReader);
		
		String code = "";
		StringBuilder stringBuilder = new StringBuilder();
		
		try {
			while((code = bufferedReader.readLine()) != null) {
				stringBuilder.append(code);
				stringBuilder.append('\n');
			}
		}
		catch(IOException e) {
			Log.e("IOExceptioN", e.toString());
		}
		
		return stringBuilder.toString();
	}
}
