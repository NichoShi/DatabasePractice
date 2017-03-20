package com.example.nichoshi.databasepractice;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by NichoShi on 2017/3/16.
 */

public class MyContentProvider extends ContentProvider {
    public static final String Authority = "com.example.nichoshi.databasepractice.provider";
    public static final int BOOK_DIR = 0;
    public static final int BOOK_ITEM = 1;
    private static UriMatcher matcher;
    private MyDatabaseHelper myDatabaseHelper;
    static {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(Authority,"Book",BOOK_DIR);
        matcher.addURI(Authority,"Book/#",BOOK_ITEM);

    }


    @Override
    public boolean onCreate() {
        myDatabaseHelper = new MyDatabaseHelper(getContext(),"BookStore.db",null,1);
        return true;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        Uri UriReturn = null;
        switch (matcher.match(uri)){
            case BOOK_DIR:
                try{
                    long id = db.insert("Book",null,values);
                    UriReturn = Uri.parse("content://"+ Authority + "/Book/" + id );

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }

                break;
            case BOOK_ITEM:

            default:
                break;

        }
        return UriReturn;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int updateRow = 0;
        switch (matcher.match(uri)){
            case BOOK_DIR:
                try{
                    updateRow = db.update("Book",values,selection,selectionArgs);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;
            case BOOK_ITEM:
                try{
                    String id = uri.getPathSegments().get(1);
                    updateRow = db.update("Book",values,"id = ?",new String[]{id});
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;

            default:
                break;
        }

        return updateRow;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        int deleteRow = 0;
        switch (matcher.match(uri)){
            case BOOK_DIR:
                try{
                    deleteRow = db.delete("Book",selection,selectionArgs);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;

            case BOOK_ITEM:
                try{
                    String id = uri.getPathSegments().get(1);
                    deleteRow = db.delete("Book","id = ?",new String[]{id});
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;

            default:
                break;
        }
        return deleteRow;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db = myDatabaseHelper.getWritableDatabase();
        db.beginTransaction();
        Cursor cursor = null;
        switch (matcher.match(uri)){
            case BOOK_DIR:
                try{
                    cursor = db.query("Book",projection,selection,selectionArgs,null,null,sortOrder);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;

            case BOOK_ITEM:
                try{
                    String id = uri.getPathSegments().get(1);
                    cursor = db.query("Book",projection,"id = ?",new String[]{id},null,null,sortOrder);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
                break;

            default:
                break;
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (matcher.match(uri)){
            case BOOK_DIR:
                return "vnd.android.cursor.dir/vnd."+ Authority +"."+ "Book";

            case BOOK_ITEM:
                return "vnd.android.cursor.item/vnd."+ Authority +"."+ "Book";

            default:
                break;

        }
        return null;
    }
}
