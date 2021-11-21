package com.example.bookstore.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.bookstore.Book;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SQLHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "BOOKSTORE";
    static final int DB_VERSION = 1;
    static final String DB_TABLE_ACCOUNT = "Accounts";
    static final String DB_TABLE_COMMENT= "Comments";
    static final String DB_TABLE_CART= "Carts";
    SQLiteDatabase sqLiteDatabase;
    ContentValues contentValues;

    public SQLHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + DB_TABLE_ACCOUNT + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, password TEXT)";

        String query2= "CREATE TABLE " + DB_TABLE_COMMENT + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "username TEXT, time TEXT, content TEXT, imageLink TEXT)";

        String query3= "CREATE TABLE " + DB_TABLE_CART + "(ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "idS NUMBER, img TEXT, tensach TEXT, sao NUMBER, giaban NUMBER)";
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_ACCOUNT);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_COMMENT);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_CART);
            onCreate(db);
        }
    }


    public boolean insertAccount(Account account) {
        sqLiteDatabase = getWritableDatabase();
        if (checkExists(account.getUsername())) {
            return false;
        }else{
            contentValues = new ContentValues();
            contentValues.put("username", account.getUsername());
            contentValues.put("password", account.getPassword());
            sqLiteDatabase.insert(DB_TABLE_ACCOUNT, null, contentValues);
            return true;
        }
    }

    public boolean addToCart(Book book) {
        sqLiteDatabase = getWritableDatabase();
        if (checkExistsOnCart(book.getId())) {
            return false;
        }else{
            contentValues = new ContentValues();
            contentValues.put("idS", book.getId());
            contentValues.put("img", book.getImageLink());
            contentValues.put("tensach", book.getTitle());
            contentValues.put("sao", book.getRateStar());
            contentValues.put("giaban", book.getPrice());
            sqLiteDatabase.insert(DB_TABLE_CART, null, contentValues);
            return true;
        }
    }

    public void deleteFromCart(int id){
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_CART, "idS=?", new String[]{id + ""});
    }

    public List<Book> getAllCart(){
        List<Book> list=new ArrayList<>();
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_TABLE_CART, new String[]{});
        while (cursor.moveToNext()){
            int id=cursor.getInt(cursor.getColumnIndex("idS"));
            String img=cursor.getString(cursor.getColumnIndex("img"));
            String tenSach=cursor.getString(cursor.getColumnIndex("tensach"));
            int sao=cursor.getInt(cursor.getColumnIndex("sao"));
            long giaBan=cursor.getLong(cursor.getColumnIndex("giaban"));

            list.add(new Book(id, img, tenSach, null, 0, null, sao, 0, giaBan, null));
        }
        return list;
    }

    public int getCountCart(){
        int c=0;
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB_TABLE_CART, new String[]{});
        while (cursor.moveToNext()){
            c++;
        }
        return c;
    }

    public void insertComment(Account account, Book book, String contentComment){
        sqLiteDatabase=getWritableDatabase();
        contentValues=new ContentValues();
        contentValues.put("username", account.getUsername());

        Calendar now = Calendar.getInstance();
        String strDateFormat = "dd/MM/yyyy HH:mm:ss";
        SimpleDateFormat sdf=new SimpleDateFormat(strDateFormat);
        String date=sdf.format(now.getTime());

        contentValues.put("time",date);
        contentValues.put("content", contentComment);
        contentValues.put("imageLink", book.getImageLink());
        sqLiteDatabase.insert(DB_TABLE_COMMENT, null, contentValues);
    }
    public List<Comment> GetCommentOfBook(String imageLink){
        List<Comment> list=new ArrayList<>();
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_TABLE_COMMENT+" WHERE imageLink=?", new String[]{imageLink +""});
        while (cursor.moveToNext()){
            String username=cursor.getString(cursor.getColumnIndex("username"));
            String time =cursor.getString(cursor.getColumnIndex("time"));
            String content=cursor.getString(cursor.getColumnIndex("content"));
            list.add(new Comment(username, time, content, null));
        }
        return list;
    }

    public void deleteAccount(int id) {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_ACCOUNT, "id=?", new String[]{id + ""});
    }

    public void deleteAllAcount() {
        sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_ACCOUNT, null, null);
    }

    public void deleteAllCart(){
        sqLiteDatabase= getWritableDatabase();
        sqLiteDatabase.delete(DB_TABLE_CART, null, null);

    }
    public boolean checkExists(String username) {
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB_TABLE_ACCOUNT + " WHERE username=?", new String[]{username + ""});
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }
    public boolean checkExistsOnCart(int id) {
        sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + DB_TABLE_CART + " WHERE idS=?", new String[]{id + ""});
        if (cursor.getCount() == 1) {
            return true;
        }
        return false;
    }
    public boolean checkLogin(Account account){
        sqLiteDatabase=getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("SELECT * FROM "+DB_TABLE_ACCOUNT +" WHERE username=? and password=?", new String[]{account.getUsername()+"", account.getPassword()+""});
        if (cursor.getCount()==1){
            return true;
        }
        return false;
    }

}
