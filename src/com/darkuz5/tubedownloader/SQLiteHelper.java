package com.darkuz5.tubedownloader;
 

import android.content.Context; 
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;  

public class SQLiteHelper extends SQLiteOpenHelper {
	
	String activo      = "CREATE TABLE if not exists medidas (id varchar(100), nombre TEXT)";    
	String insertaD    = "insert into medidas (id,nombre) values ('1','0')";
	String insertaE    = "insert into medidas (id,nombre) values ('3','0')";
	String insertaN    = "insert into medidas (id,nombre) values ('5','1')";
	String insertaI    = "insert into medidas (id,nombre) values ('7','0')";	
	

	String video       = "CREATE TABLE if not exists video (id VARCHAR(100), fecha VARCHAR(100), titulo TEXT, link VARCHAR(500), lfoto VARCHAR(500), imagen BLOB, "
			+ "descripcion VARCHAR(900), duracion VARCHAR(100))";
	String videoL       = "CREATE TABLE if not exists videolocal (id VARCHAR(100), fecha TEXT, titulo TEXT,  link VARCHAR(500), imagen BLOB)";
	
	
	
 
    public SQLiteHelper(Context contexto, String nombre,
                               CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) { 
        db.execSQL(activo);  
        db.execSQL(video);  
        db.execSQL(videoL);    
        Cursor checa=db.rawQuery("select * from medidas where id='1'", null);
        if (checa.getCount() < 1 ) {
        	db.execSQL(insertaD);
        	db.execSQL(insertaE);
        	db.execSQL(insertaN);
        	db.execSQL(insertaI);
        }
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) { 
        db.execSQL(activo);  
        db.execSQL(video);     
        db.execSQL(videoL);  
        Cursor checa=db.rawQuery("select * from medidas where id='1'", null);
        if (checa.getCount() < 1 ) {
        	db.execSQL(insertaD);
        	db.execSQL(insertaE);
        	db.execSQL(insertaN);
        	db.execSQL(insertaI);
        }
        
    }
 
   
}
