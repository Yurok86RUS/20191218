package com.yur0k.a20191218hw6;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "Logs";

    Button btnAdd, btnRead, btnClear;
    EditText cityName;
    TextView textViewTemperature;

    DBHelper dbHelper;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnRead = (Button) findViewById(R.id.btnRead);
        btnRead.setOnClickListener(this);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        cityName = (EditText) findViewById(R.id.cityName);
        textViewTemperature = (TextView) findViewById(R.id.textViewTemperature);

        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);
    }

    @Override
    public void onClick(View v) {

        // создаем объект для данных
        ContentValues cv = new ContentValues();

        // получаем данные из полей ввода
        String cityName = this.cityName.getText().toString();
        String temperature = textViewTemperature.getText().toString();

        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.btnAdd:

                //тут должен быть Retrofit, который получит температуру и мы ее запишем
                //но он мне пока что так и не поддался. Пока что

                cv.put("cityName", cityName);
                cv.put("temperature", temperature);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("mytable", null, cv);
                Log.d(LOG_TAG, "Строка " + rowID + " добавлена ");
                break;
            case R.id.btnRead:
                Cursor c = db.query("mytable", null, null, null, null, null, null);

                if (c.moveToFirst()) {
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("cityName");
                    int emailColIndex = c.getColumnIndex("temperature");

                    do {
                        Log.d(LOG_TAG,
                                "ID = " + c.getInt(idColIndex) +
                                        ", cityName = " + c.getString(nameColIndex) +
                                        ", temperature = " + c.getString(emailColIndex));
                    } while (c.moveToNext());
                } else
                    Log.d(LOG_TAG, "В таблиценет строк");
                c.close();
                break;
            case R.id.btnClear:
                int clearCount = db.delete("mytable", null, null);
                Log.d(LOG_TAG, "Таблица очищена! Удалено строк: " + clearCount);
                break;
        }
        dbHelper.close();
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "myDBB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL("create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "cityName text,"
                    + "temperature text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
