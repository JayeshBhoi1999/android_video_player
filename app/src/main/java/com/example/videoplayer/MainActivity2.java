package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {
private ListView listView;
private String textcontent;
private  int position;
private String [] itams;
private ArrayList<File> dirlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        listView=findViewById(R.id.listview1);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        dirlist=(ArrayList) bundle.getParcelableArrayList("videolist");
        textcontent=intent.getStringExtra("currentSong");
        position=intent.getIntExtra("position",0);
        File cfile=  dirlist.get(position);
      //  Toast.makeText(getApplicationContext(),cfile.toString(),Toast.LENGTH_SHORT).show();
        ArrayList<File > vlist=fetchvideo(cfile);
        //Toast.makeText(getApplicationContext(),vlist.get(0).toString(),Toast.LENGTH_SHORT).show();

        String [] itams = new String[vlist.size()];
        for (int i=0;i<vlist.size();i++){
            itams[i] =vlist.get(i).getName().replace(".mp3","");
        }
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(MainActivity2.this, android.R.layout.simple_list_item_1,itams);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(MainActivity2.this,playVideo.class);
                String currentvideo=listView.getItemAtPosition(i).toString();
                intent.putExtra("videolist",vlist);
                intent.putExtra("currentvideo",currentvideo);
                intent.putExtra("position",i);
                startActivity(intent);

            }
        });



    }

    public ArrayList <File> fetchvideo(File file) {
        ArrayList arrayList = new ArrayList();
        File[] songs = file.listFiles();
        if (songs != null) {
            for (File myFile : songs) {
                if (!myFile.isHidden() && myFile.isDirectory()) {

                  //  arrayList.addAll(fetchdir(myFile));

                } else {

                    if (myFile.getName().endsWith(".mkv") && !myFile.getName().startsWith(".")) {


                            arrayList.add(myFile.getAbsoluteFile());

                    }
                }
            }
        }
        return arrayList;
    }



}