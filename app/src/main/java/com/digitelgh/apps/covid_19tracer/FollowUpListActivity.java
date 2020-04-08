package com.digitelgh.apps.covid_19tracer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

/**
 * An activity representing a list of Traces. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TraceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class FollowUpListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private boolean dataLoaded = false;
    private Integer dataCount = 1;
    private String trace_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followup_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataLoaded) {
                    Intent i = new Intent(FollowUpListActivity.this, FollowUpActivity.class);
                    i.putExtra(FollowUpFormFragment.ARG_ITEM_ID, dataCount.toString());
                    i.putExtra(FollowUpFormFragment.TRACE_ID, trace_id);
                    startActivity(i);
                    finish();
                }
            }
        });
        if (findViewById(R.id.followups_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        trace_id = getIntent().getStringExtra(FollowUpListFragment.ARG_ITEM_ID);
        Log.i("EBO:::: ",trace_id);

        getJSON(getResources().getString(R.string.fetch_followups_url)+"/"+trace_id);
    }

    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
//                    Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    View recyclerView = findViewById(R.id.followups_list_id);
                    assert recyclerView != null;
                    setupRecyclerView((RecyclerView) recyclerView, s);
//                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView, String json) throws JSONException{
        Log.d("EBO:::: ",json);
        //creating a json array from the json string
        JSONArray jsonArray = new JSONArray(json);
        //creating a string array for listview
        String[] records = new String[jsonArray.length()];
        //looping through all the elements in json array
        for (int i = 0; i < jsonArray.length(); i++) {
            //getting json object from the json array
            JSONObject obj = jsonArray.getJSONObject(i);
            records[i] = obj.toString();
        }
        // enable the fab onclick only when we have fully loaded data
        dataCount = jsonArray.length() + 1;
        dataLoaded = true;
//        String tag = "JSON";
//        Log.i(tag,json);
        recyclerView.addItemDecoration(new DividerItemDecoration(FollowUpListActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, records, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final FollowUpListActivity mParentActivity;
//        private final List<DummyContent.DummyItem> mValues;
        private final String[] mValues;
        private final boolean mTwoPane;
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(int position);
            void onFollowUpClickListener(int position);
        }

//        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Integer intObj = (Integer) view.getTag();
//                String item = intObj.toString();
//
//                if (mTwoPane) {
//                    Bundle arguments = new Bundle();
//                    arguments.putString(ContactFormFragment.ARG_ITEM_ID, item);
//                    ContactFormFragment fragment = new ContactFormFragment();
//                    fragment.setArguments(arguments);
//                    mParentActivity.getSupportFragmentManager().beginTransaction()
//                            .replace(R.id.trace_contacts_container, fragment)
//                            .commit();
//                } else {
//                    Context context = view.getContext();
//                    Intent intent = new Intent(context, ContactFormActivity.class);
//                    intent.putExtra(TraceDetailFragment.ARG_ITEM_ID, item);
//
//                    context.startActivity(intent);
//                }
//            }
//        };

        SimpleItemRecyclerViewAdapter(FollowUpListActivity parent, String[] items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.followup_list_content, parent, false);
            return new ViewHolder(view, mListener);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).id);

            try {
                JSONObject rec = new JSONObject(mValues[position]);
                holder.mContentView.setText("Follow Up on "+rec.getString("date_taken"));
                holder.mIdView.setText("Day "+rec.getString("day"));
                holder.mContactStateView.setText(rec.getString("state"));
//                TODO: Come back to this later
//                holder.itemView.setTag(position+1);
//                holder.itemView.setOnClickListener(mOnClickListener);
            } catch (Throwable t) {
                Log.e("COVID-19", "Could not parse malformed JSON: \"" + mValues[position] + "\"");
            }
        }

        @Override
        public int getItemCount() {
            return mValues.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;
            final TextView mContactStateView;
            ViewHolder(View view, final OnItemClickListener listener) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mContactStateView = (TextView) view.findViewById(R.id.contact_state_view);
            }
        }
    }
}
