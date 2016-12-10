package fragment.pages;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication.FeedContentActivity;
import com.example.administrator.myapplication.R;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedListFragment extends Fragment{
    View view;
    ListView listView;

    String[] data;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view==null)
        {
            view=inflater.inflate(R.layout.fragment_pager_feed_list,null);
            listView = (ListView) view.findViewById(R.id.list);
            listView.setAdapter(listAdapter);

            Random rand = new Random();
            data = new String[10+Math.abs(rand.nextInt()%20)];

            for(int i=0; i<data.length; i++){
                data[i] = "THIS ROW IS "+rand.nextInt();
            }
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClicked(position);
                }
            });
        }
        return view;
    }
    BaseAdapter listAdapter=new BaseAdapter() {

        @Override
        public int getCount() {
            return data==null ? 0 : data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view=null;
            if ((convertView==null))
            {
                LayoutInflater inflater=LayoutInflater.from(parent.getContext());
                view=inflater.inflate(R.layout.simple_list_item_me,null);
            }
            else
            {
                view = convertView;

            }
            TextView text1 = (TextView) view.findViewById(R.id.user_details);
            text1.setText(data[position]);


            return view;
        }
    };
    void onItemClicked(int position){
        String text = data[position];

        Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
        itnt.putExtra("text", text);


        startActivity(itnt);
    }
}
