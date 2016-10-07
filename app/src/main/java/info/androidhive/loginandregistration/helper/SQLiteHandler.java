/**
 * Author: Ravi Tamada
 * URL: www.androidhive.info
 * twitter: http://twitter.com/ravitamada
 * */
package info.androidhive.loginandregistration.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;
import java.util.StringTokenizer;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID_USER = "iduser";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_ATUALIZADO_EM = "atualizado_em";
	private static final String KEY_PHONE_NUMBER = "phone_number";
	private static final String KEY_ID_TABLE = "idtable";


	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_UID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_ID_USER + " TEXT,"
				+ KEY_ATUALIZADO_EM + "TEXT," + KEY_PHONE_NUMBER
				+ "TEXT,"+ KEY_ID_TABLE + "TEXT" +")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		//db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String iduser, String name, String email,String atualizado_em,String phone_number, String idTable) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
        values.put(KEY_ID_USER, iduser); // idUser do Servidor
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email

		values.put(KEY_ATUALIZADO_EM, atualizado_em); // Data Atualização
		values.put(KEY_PHONE_NUMBER,phone_number);//Phone Number
		values.put(KEY_ID_TABLE,idTable);//id da Tabela do Banco de Dados

        // Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "Novo usuário adicionado na tabela: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if (cursor.getCount() > 0) {
			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
            user.put("iduser", cursor.getString(3));
            user.put("atualizado_em", cursor.getString(4));
			user.put("phone_number", cursor.getString(5));
			user.put("idtable", cursor.getString(6));
		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Pegando usuario do Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

}
