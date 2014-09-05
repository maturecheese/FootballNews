package com.example.mark_i5.xmlparseasync.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by mark-i5 on 27/08/2014.
 */
public class ArticleDatabase {
    private static final String LOGTAG = "ArticleDatabase";
    private Context context;
    public ArticleDatabaseHelper dbHelper;
    public SQLiteDatabase sqLiteDatabase;

    public ArticleDatabase(Context context){

        this.context = context;

    }

    public ArticleDatabase open()throws SQLException{
        this.dbHelper = new ArticleDatabaseHelper(context);
        this.sqLiteDatabase = dbHelper.getWritableDatabase();
        return this;
    }

    public void close(){
        if (dbHelper != null){
            dbHelper.close();
        }
    }


    public long insertArticle (Article article) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ArticleDatabaseHelper.TITLE, article.getTitle());
        contentValues.put(ArticleDatabaseHelper.DESCRIPTION, article.getDescription());
        contentValues.put(ArticleDatabaseHelper.PUBLISHED_AT, article.getPublished());
        contentValues.put(ArticleDatabaseHelper.IMAGE_URL, article.getImageUrl());
        contentValues.put(ArticleDatabaseHelper.CREATED_AT_LONG, article.getCreatedAt());

        //long id = db.insert(ArticleDatabaseHelper.TABLE_NAME, null,contentValues);
        long id = sqLiteDatabase.insertWithOnConflict(ArticleDatabaseHelper.TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        Log.d(LOGTAG, "track_id: " + id);
        return id;

    }
    public boolean updateRowById(int id, String[] columns, String[] values){
        ContentValues contentValues = new ContentValues();
        if (columns.length == values.length){
            for(int i = 0 ; i < columns.length; i ++){
                contentValues.put(columns[i], values[i]);
            }
        }
        int num = sqLiteDatabase.update(ArticleDatabaseHelper.TABLE_NAME,contentValues, ArticleDatabaseHelper.TID +"="+id, null);
        return num > 0;
    }

    public Cursor fetchAllArticles(){
        Cursor cursor = sqLiteDatabase.query(ArticleDatabaseHelper.TABLE_NAME,
                ArticleDatabaseHelper.ALL_COLUMNS, null, null, null, null, ArticleDatabaseHelper.CREATED_AT_LONG + " DESC" );
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Cursor fetchFilteredArticlesAllColumns(String text){
        String desc = ArticleDatabaseHelper.DESCRIPTION;
        String title = ArticleDatabaseHelper.TITLE;
        Cursor cursor = sqLiteDatabase.query(ArticleDatabaseHelper.TABLE_NAME,
                ArticleDatabaseHelper.COLUMNS_FOR_LISTVIEW , title + " LIKE ? AND " + desc + " LIKE ?", new String[] {"%" +text + "%", "%" +text + "%" }, null,null,
                ArticleDatabaseHelper.CREATED_AT_LONG + " DESC");

        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }

    public static class ArticleDatabaseHelper extends SQLiteOpenHelper{
        private static final String DATABASE_NAME = "articles.db";
        private static final String TABLE_NAME = "article_table";
        private static final int DATABASE_VERSION = 3;

        public static final String TITLE = "title";
        public static final String DESCRIPTION = "description";
        public static final String IMAGE_URL = "image_path";
        public static final String PUBLISHED_AT = "created_at";
        public static final String IMAGE_LOCAL_PATH = "image_local_path";
        public static final String TID = "_id";
        public static final String CREATED_AT_LONG = "created_at_long";

        private static final String[] ALL_COLUMNS = {TID, TITLE, DESCRIPTION, IMAGE_URL, IMAGE_LOCAL_PATH, PUBLISHED_AT};
        private static final String[] COLUMNS_FOR_LISTVIEW ={ TID, TITLE, DESCRIPTION, IMAGE_URL, PUBLISHED_AT};



        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME +" ("+TID +
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+ TITLE +" TEXT, " + DESCRIPTION + " TEXT, "+
                IMAGE_URL +" TEXT, " + PUBLISHED_AT + " TEXT, "+ IMAGE_LOCAL_PATH +" TEXT, "+
                CREATED_AT_LONG +" INTEGER, UNIQUE (" + TITLE + "))";




        private static final String DROP_TABLE = "DROP TABLE " +TABLE_NAME;



        public ArticleDatabaseHelper(Context context){
            super(context,DATABASE_NAME, null, DATABASE_VERSION);

        }
        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            Log.d(LOGTAG, CREATE_TABLE);
            try {
                sqLiteDatabase.execSQL(CREATE_TABLE);
            }catch(Exception e){
                Log.e(LOGTAG, "onCreate Database", e);
                e.printStackTrace();
            }


        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
            Log.d(LOGTAG, "onUpgrade Database");
            sqLiteDatabase.execSQL(DROP_TABLE);
            this.onCreate(sqLiteDatabase);
        }
    }
}
