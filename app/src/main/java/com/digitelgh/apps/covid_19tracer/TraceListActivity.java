package com.digitelgh.apps.covid_19tracer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * An activity representing a list of Traces. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link TraceDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class TraceListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trace_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if (findViewById(R.id.trace_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        getJSON(getResources().getString(R.string.fetch_traces_url));
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
                    View recyclerView = findViewById(R.id.trace_list);
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
//        String tag = "JSON";
//        Log.i(tag,json);
        recyclerView.addItemDecoration(new DividerItemDecoration(TraceListActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, records, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {
        private final TraceListActivity mParentActivity;
//        private final List<DummyContent.DummyItem> mValues;
        private final String[] mValues;
        private final boolean mTwoPane;
        private OnItemClickListener mListener;

        public interface OnItemClickListener {
            void onItemClick(int position);
            void onFollowUpClickListener(int position);
        }

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Integer intObj = (Integer) view.getTag();
                String item = (String) view.getTag();
//                String item = intObj.toString();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ContinueTraceFragment.ARG_ITEM_ID, item);
                    ContinueTraceFragment fragment = new ContinueTraceFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.trace_contacts_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ContinueTraceActivity.class);
                    intent.putExtra(ContinueTraceFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        private final View.OnClickListener mFollowUpButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String item = (String) view.getTag();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(FollowUpListFragment.ARG_ITEM_ID, item);
                    FollowUpListFragment fragment = new FollowUpListFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.follow_up_form_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, FollowUpListActivity.class);
                    intent.putExtra(FollowUpListFragment.ARG_ITEM_ID, item);
                    context.startActivity(intent);
                }
            }
        };

        private final View.OnClickListener mManageContactsButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer intObj = (Integer) view.getTag();
                String item = intObj.toString();

                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(TraceDetailFragment.ARG_ITEM_ID, item);
                    TraceDetailFragment fragment = new TraceDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.trace_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ContactsListActivity.class);
                    intent.putExtra(TraceDetailFragment.ARG_ITEM_ID, item);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(TraceListActivity parent, String[] items, boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.trace_list_content, parent, false);
            return new ViewHolder(view, mListener);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
//            holder.mIdView.setText(mValues.get(position).id);

            try {
                JSONObject rec = new JSONObject(mValues[position]);
                holder.mContentView.setText(rec.getString("f_name"));
                holder.mIdView.setText(rec.getString("res_address"));

                // here comes a hack
                // TODO

                holder.itemView.setTag(rec.toString());
                holder.itemView.setOnClickListener(mOnClickListener);
                // Follow up button handlers
                String id = Integer.toString(rec.getInt("id"));
                holder.mFollowUpButton.setTag(id);
                holder.mFollowUpButton.setOnClickListener(mFollowUpButtonOnClickListener);
                // Continue trace button handlers
                holder.mManageContactsButton.setTag(position+1);
                holder.mManageContactsButton.setOnClickListener(mManageContactsButtonOnClickListener);
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
            final Button mManageContactsButton;
            final Button mFollowUpButton;

            ViewHolder(View view, final OnItemClickListener listener) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
                mManageContactsButton = (Button) view.findViewById(R.id.continue_trace_button);
                mFollowUpButton = (Button) view.findViewById(R.id.follow_up_button);
            }
        }
    }
}
