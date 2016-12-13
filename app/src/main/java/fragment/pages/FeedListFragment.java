package fragment.pages;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.myapplication.Article;
import com.example.administrator.myapplication.FeedContentActivity;
import com.example.administrator.myapplication.Page;
import com.example.administrator.myapplication.R;
import com.example.api.Server;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/8.
 */

public class FeedListFragment extends Fragment {
    View view;
    ListView listView;
    List<Article> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pager_feed_list, null);
            listView = (ListView) view.findViewById(R.id.list);
            btnLoadMore=inflater.inflate(R.layout.widget_load_more_button,null);
            textLoadMore=(TextView)btnLoadMore.findViewById(R.id.text);
            listView.addFooterView(btnLoadMore);
            listView.setAdapter(listAdapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemClicked(position);
                }
            });
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadmore();
                }
            });
        }
        return view;
    }

    BaseAdapter listAdapter = new BaseAdapter() {
        @SuppressLint("InflateParams")
        @Override
        public int getCount() {
            return data == null ? 0 : data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if ((convertView == null)) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.simple_list_item_me, null);
            } else {
                view = convertView;

            }
            TextView textTitle = (TextView) view.findViewById(R.id.title);
            TextView textDetails = (TextView) view.findViewById(R.id.user_details);
            TextView textAuthor=(TextView)view.findViewById(R.id.user_name) ;
            TextView textTime=(TextView)view.findViewById(R.id.time);
            AvatarView avater=(AvatarView)view.findViewById(R.id.user_image);
            Article article=data.get(position);
            textTitle.setText(article.getTitle());
            textDetails.setText("内容"+article.getText());
            textAuthor.setText(article.getAuthorName());
            avater.load(Server.serverAddress+article.getAuthorAvater());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", article.getCreateDate()).toString();
            textTime.setText(dateStr);


            return view;
        }
    };

    void onItemClicked(int position) {


        Intent itnt = new Intent(getActivity(), FeedContentActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("content",data.get(position));
        itnt.putExtras(bundle);
        startActivity(itnt);
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    void reload() {
        //请求服务器返回数据
        OkHttpClient client= Server.getSharedClient();
        Request request=Server.requestBuilderWithApi("feeds")
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    Page<Article> data=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Article>>(){});
                    FeedListFragment.this.page=data.getNumber();
                    FeedListFragment.this.data=data.getContent();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }catch (final Exception e )
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            new AlertDialog.Builder(getActivity()).setMessage(e.getMessage())
                                    .show();
                        }
                    });
                }

            }
        });

    }
    void loadmore()
    {
        btnLoadMore.setEnabled(false);
        textLoadMore.setText("载入中...");
        Request request=Server.requestBuilderWithApi("feeds/+(page+1)").get().build();
        Server.getSharedClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        btnLoadMore.setEnabled(true);
                        textLoadMore.setText("加载更多");
                    }
                });
                try {
                    Page<Article> feeds=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Article>>(){});
                    if (feeds.getNumber()>page)
                    {
                        data=feeds.getContent();
                    }else
                    {
                        data.addAll(feeds.getContent());
                    }
                    page=feeds.getNumber();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            listAdapter.notifyDataSetChanged();
                        }
                    });
                }catch (Exception e)
                {

                }

            }
        });

    }
}
