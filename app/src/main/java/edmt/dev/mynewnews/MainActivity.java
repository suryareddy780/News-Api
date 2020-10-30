package edmt.dev.mynewnews;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<ListItem> listItems;
    private static final String REQUEST_URL = "https://newsapi.org/v2/top-headlines?country=us&apiKey=f3628ff9b4b7441da8cb6cc9c822c7ae";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems = new ArrayList<>();

        loadRecyclerViewData();
    }
    public void loadRecyclerViewData() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                REQUEST_URL,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject baseObject = new JSONObject(response);
                            JSONArray articles = baseObject.getJSONArray("articles");

                            for (int i = 0; i < articles.length(); i++) {
                                JSONObject jsonObject = articles.getJSONObject(i);
                                ListItem listItem = new ListItem(jsonObject.getString("title"),
                                        jsonObject.getString("description"),
                                        jsonObject.getString("urlToImage"),
                                        jsonObject.getString("url"));

                                progressDialog.dismiss();
                                listItems.add(listItem);
                            }

                            MyAdapter myAdapter = new MyAdapter(listItems, getApplicationContext());
                            recyclerView.setAdapter(myAdapter);

                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, "Error Fetching Data", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Error ", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}