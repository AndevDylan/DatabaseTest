package com.example.administrator.databasetest;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button createDatabase,addData,updateData,deleteData,queryData;
    MyDatabaseHelper dbHelper;
    TextView showInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDatabase=findViewById(R.id.create_database);
        addData=findViewById(R.id.add_data);
        updateData=findViewById(R.id.update_data);
        deleteData=findViewById(R.id.delete_data);
        queryData=findViewById(R.id.query_data);
        showInfo=findViewById(R.id.show_info);

        dbHelper=new MyDatabaseHelper(MainActivity.this,"BookStore.db",null,2);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();//获取数据库对象
                ContentValues values=new ContentValues();
                values.put("name","Da Vinci Code");
                values.put("author","Dan Brown");
                values.put("pages",454);
                values.put("price",19.26);
                db.insert("Book",null,values);//向表中插入第一条数据
                values.clear();

                values.put("name","Android First Code");
                values.put("author","Guo Lin");
                values.put("price",52.00);
                values.put("pages",578);
                db.insert("Book",null,values);
                Toast.makeText(MainActivity.this, "数据添加成功！", Toast.LENGTH_SHORT).show();
            }
        });

        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                ContentValues values=new ContentValues();
                values.put("price",79);
                db.update("Book",values,"name=?",new String[]{"Android First Code"});
                Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();
            }
        });

        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Book","pages>?",new String[]{"500"});
                Toast.makeText(MainActivity.this, "删除完成！", Toast.LENGTH_SHORT).show();
            }
        });

        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor=db.query("Book",null,null,null,null,null,null);
                if (cursor.moveToFirst()){
                    do {
                        String name=cursor.getString(cursor.getColumnIndex("name"));
                        String author=cursor.getString(cursor.getColumnIndex("author"));
                        int pages=cursor.getInt(cursor.getColumnIndex("pages"));
                        double price=cursor.getDouble(cursor.getColumnIndex("price"));
                        showInfo.append("书名："+name+"\n作者："+author+"\n页数："+pages+"\n价格："+price+"\n\n");
                    }while (cursor.moveToNext());
                    showInfo.append("\n\n加载完毕！");
                }
                cursor.close();
            }
        });
    }
}
