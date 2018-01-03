package com.jxb.myrfid.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jxb.myrfid.R;
import com.jxb.myrfid.entity.Note;
import com.jxb.myrfid.entity.NoteDao;
import com.jxb.myrfid.entity.myData;

import org.greenrobot.greendao.query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import android_serialport_api.SerialPortFinder;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.test2)
    TextView test2;

    private NoteDao noteDao;
    private Query<Note> notesQuery;
    //private NotesAdapter notesAdapter;

    //数据存储
    //ui
    //多线程
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String ret = readAssetsTxt(this,"init.json");
        JsonToJavaBean(ret);

        SerialPortFinder serialPortFinder = new SerialPortFinder();
        String[] list = serialPortFinder.getAllDevices();


//        // get the note DAO
//        DaoSession daoSession = ((UHFApplication) getApplication()).getDaoSession();
//        noteDao = daoSession.getNoteDao();
//
//        Note note = new Note();
//        note.setText("测试12334");
//        note.setDate(new Date());
//        noteDao.insert(note);
//        Log.d("DaoExample", "Inserted new note, ID: " + note.getId());
//
//        // query all notes, sorted a-z by their text
//        notesQuery = noteDao.queryBuilder().orderAsc(NoteDao.Properties.Text).build();
//        updateNotes();

    }

    private void updateNotes() {
        List<Note> notes = notesQuery.list();
        int i = 0;
        //notesAdapter.setNotes(notes);
    }

    @OnClick(R.id.test2)
    public void onViewClicked() {

    }

    //将Json对象转化成JavaBean
    public void JsonToJavaBean(String text){

        myData user = JSONObject.parseObject(text, myData.class);
        int i = 0;
    }
    /**
     * 读取assets下的txt文件，返回utf-8 String
     * @param context
     * @param fileName 不包括后缀
     * @return
     */
    public static String readAssetsTxt(Context context, String fileName){
        try {

            InputStreamReader inputStreamReader=new InputStreamReader(context.getAssets().open(fileName),"UTF-8");
            BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
            String line;
            StringBuilder stringBuilder=new StringBuilder();
            while ((line=bufferedReader.readLine())!=null){
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();

            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}
