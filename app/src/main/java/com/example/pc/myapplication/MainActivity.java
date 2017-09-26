package com.example.pc.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private List<Bean.DataBean.HotcourseBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);

        new Thread() {

            private ByteArrayOutputStream arr;

            @Override
            public void run() {
                super.run();

                String path = "http://www.meirixue.com/api.php?c=index&a=index";

                try {
                    URL url = new URL(path);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setConnectTimeout(2000);
                    int responseCode = urlConnection.getResponseCode();

                    if (responseCode == 200) {

                        InputStream inputStream = urlConnection.getInputStream();
                        arr = new ByteArrayOutputStream();

                        byte[] b = new byte[1024];
                        int read = 0;

                        while ((read = inputStream.read(b)) != -1) {
                            arr.write(b, 0, read);
                        }

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            String s = arr.toString();
                            //Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
                            Gson gson = new Gson();
                            Bean bean = gson.fromJson(s, Bean.class);

                            list = bean.getData().getHotcourse();

                            lv.setAdapter(new MyAdapter());

                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = View.inflate(MainActivity.this,R.layout.list_item,null);

            ImageView iv = (ImageView) convertView.findViewById(R.id.iv);
            TextView tv = (TextView) convertView.findViewById(R.id.tv);

            ImageLoader.getInstance().displayImage(list.get(position).getImg(),iv);
            tv.setText(list.get(position).getTitle());

            return convertView;
        }
    }

}
