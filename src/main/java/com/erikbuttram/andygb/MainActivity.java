package com.erikbuttram.andygb;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.erikbuttram.andygb.models.AwesomePojo;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mRefreshLayout;
    private RecyclerView mRecycler;

    //normally I would use a subscriber using RXJava, but handlers are a quick
    //and dirty multithread pattern
    private Handler mHandler;

    DataAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHandler = new Handler(Looper.getMainLooper());

        LinearLayoutManager manager = new LinearLayoutManager(this);

        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        mRecycler = (RecyclerView)findViewById(R.id.list_view);
        mAdapter = new DataAdapter(this);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setLayoutManager(manager);

        //refresh layout setup
        mRefreshLayout.setColorSchemeResources(R.color.blue, R.color.light_blue, R.color.green);
        mRefreshLayout.setOnRefreshListener(this);

        getData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        getData();
    }

    //uber hackey, not a good idea to run on a different thread, but I'm giving myself an hour
    private void loadDataToTable(final ArrayList<AwesomePojo> newData) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mRefreshLayout.setRefreshing(false);
                mAdapter.setPojos(newData);
            }
        });

    }

    private void getData() {
        mRefreshLayout.setRefreshing(true);
        Thread thread = new Thread(new DataGetter());
        thread.start();
    }


    private class DataGetter implements Runnable {

        private static final String url = "http://guidebook.com/service/v2/upcomingGuides/";

        public DataGetter() {

        }

        @Override
        public void run() {
            OkHttpClient client = new OkHttpClient();
            ObjectMapper mapper = new ObjectMapper();
            //typically use this two properties for unpredictable json values
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
            Request.Builder builder = new Request.Builder();
            builder.url(url).get();
            Request request = builder.build();
            //WARNING:  Big ugly nested runnables ahead
            try {
                Response response = client.newCall(request).execute();
                JsonNode node = mapper.readTree(response.body().bytes());
                //typically I wouldn't toString the entire response, but ya know
                Log.d("MainActivity", String.format("Here it is: %s", node.toString()));
                //data parsing to pojo
                ArrayList<AwesomePojo> pojos = new ArrayList<>();
                JsonNode data = node.get("data");

                if (data.isArray()) {
                    for (JsonNode subNode : data) {
                        pojos.add(mapper.convertValue(subNode, AwesomePojo.class));
                    }
                }
                loadDataToTable(pojos);
            } catch (final IOException ioEx) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,
                                String.format("Bad things happened: %s", ioEx.getMessage()), Toast.LENGTH_LONG)
                                .show();
                        mRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }
    }

}
