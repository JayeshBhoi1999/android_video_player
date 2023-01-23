package com.example.videoplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView=findViewById(R.id.listview);


        Dexter.withContext(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Toast.makeText(MainActivity.this,"Permission Granted",Toast.LENGTH_SHORT);
                        ArrayList<File> mydir = fetchdir(Environment.getExternalStorageDirectory());
                        String [] itams = new String[mydir.size()];
                        for (int i=0;i<mydir.size();i++){
                            itams[i] =mydir.get(i).getName().replace(".mp3","");
                        }
                        ArrayAdapter<String> adapter= new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,itams);
                        listView.setAdapter(adapter);



                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                                String currentdir=listView.getItemAtPosition(i).toString();
                                intent.putExtra("videolist",mydir);
                               intent.putExtra("currentdir",currentdir);
                                intent.putExtra("position",i);
                               startActivity(intent);

                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                })
                .check();
    }

    public ArrayList <File> fetchdir(File file)
    {
        ArrayList arrayList =new ArrayList();
        File [] songs =file.listFiles();
        if(songs !=null) {
            for(File myFile: songs){
                if(!myFile.isHidden() && myFile.isDirectory()){

                    arrayList.addAll(fetchdir(myFile));

            }
                else{

                    if(myFile.getName().endsWith(".mkv") && !myFile.getName().startsWith(".")){

                        if(!arrayList.contains(myFile.getParentFile())){
                            arrayList.add(myFile.getParentFile());}
                    }
                }
        }
        }
        return  arrayList;
    }



}