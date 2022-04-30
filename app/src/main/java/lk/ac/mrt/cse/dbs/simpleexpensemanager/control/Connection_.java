package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;


import android.content.Context;

import androidx.room.Room;

public class Connection_ {

    private  static Connection_ instance;
    private AppDatabase database;

    private  Connection_(Context context){
        database= Room
                .databaseBuilder(context,AppDatabase.class,"190056J").allowMainThreadQueries().build();

    }

    public static Connection_ getInstance(Context context){

        if (instance==null || !instance.getDatabase().isOpen()){

            instance=new Connection_(context);
        }
        return instance;
    }


    public AppDatabase getDatabase(){
        return database;
    }

}
