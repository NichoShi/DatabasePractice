package com.example.nichoshi.databasepractice;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button createBtn;
    private Button insertBtn;
    private Button updateBtn;
    private Button deleteBtn;
    private Button queryBtn;
    private MyDatabaseHelper myDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myDatabaseHelper = new MyDatabaseHelper(this,"BookStore.db",null,1);
        createBtn = (Button) findViewById(R.id.createDatabaseBtn);
        insertBtn = (Button) findViewById(R.id.insertDataBtn);
        updateBtn = (Button) findViewById(R.id.updateDataBtn);
        deleteBtn = (Button) findViewById(R.id.deleteDataBtn);
        queryBtn = (Button) findViewById(R.id.queryDataBtn);

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDatabaseHelper.getReadableDatabase();

            }
        });

        insertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                db.beginTransaction();
                try{
                    ContentValues values = new ContentValues();
                    values.put("name","达芬奇密码");
                    values.put("author","你猜");
                    values.put("price",19.9);
                    values.put("pages",542);
                    db.insert("Book",null,values);

                    values.clear();
                    values.put("name","十万个冷笑话");
                    values.put("author","呵呵哒");
                    values.put("price",32.5);
                    values.put("pages",200);
                    db.insert("Book",null,values);
                    Toast.makeText(MainActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                    db.setTransactionSuccessful();

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                db.beginTransaction();
                try{
                    ContentValues values = new ContentValues();
                    values.put("price",15);
                    db.update("Book",values,"name = ?",new String[]{"达芬奇密码"});
                    Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                    db.setTransactionSuccessful();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                db.beginTransaction();
                try{
                    db.delete("Book","pages > ?",new String[]{"500"});
                    Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                    db.setTransactionSuccessful();

                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    db.endTransaction();
                }
            }
        });

        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = myDatabaseHelper.getReadableDatabase();
                db.beginTransaction();
                Cursor cursor = null;
                try{
                    cursor = db.query("Book",null,null,null,null,null,null);
                    while(cursor.moveToNext()){
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.d("check data",name + price);
                    }
                    Toast.makeText(MainActivity.this,"查询成功",Toast.LENGTH_SHORT).show();


                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if(cursor != null){
                        cursor.close();
                    }
                    db.endTransaction();
                }
            }
        });

    }
}
