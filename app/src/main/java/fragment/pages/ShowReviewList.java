package fragment.pages;

import android.app.AlertDialog;
import android.app.Fragment;
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
import com.example.administrator.myapplication.Page;
import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.Review;
import com.example.api.Server;
import com.example.helloworld.fragments.widgets.AvatarView;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/14.
 */

public class ShowReviewList extends Fragment {
    View view;
    ListView listView;
    List<Review> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;

    Article article;

    public void setArticle(Article article) {
        this.article = article;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pager_review_list, null);
            listView = (ListView) view.findViewById(R.id.review_list);
            btnLoadMore = inflater.inflate(R.layout.widget_load_more_button, null);
            textLoadMore = (TextView) btnLoadMore.findViewById(R.id.text);
            listView.addFooterView(btnLoadMore);
            listView.setAdapter(adapter);
            /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    onItemClicked(position);
                }
            });*/
            btnLoadMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadmore();
                }
            });
        }
        return view;
    }

    BaseAdapter adapter = new BaseAdapter() {
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
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.simple_list_item_review, null);
            } else {
                view = convertView;
            }
            TextView textReview=(TextView)view.findViewById(R.id.user_review_details);
            TextView timeReview=(TextView)view.findViewById(R.id.time_review);
            TextView authorReview=(TextView)view.findViewById(R.id.user_name_review);
            AvatarView avatar = (AvatarView)view.findViewById(R.id.user_image);

            Review review =data.get(position);
            textReview.setText(review.getTextReview());
            String dateStr = DateFormat.format("yyyy-MM-dd hh:mm", review.getCreataDate()).toString();
            timeReview.setText(dateStr);
            //显示评论人

            authorReview.setText(review.getAuthorName());
            //显示图片
            avatar.load(review.getAuthor());


            return view;
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    void reload() {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithApi("article/" + article.getId() + "/comments")
                .method("GET", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    Page<Review> dataReview = new ObjectMapper().readValue(response.body().string(), new TypeReference<Page<Review>>() {
                    });
                    ShowReviewList.this.page = dataReview.getNumber();
                    ShowReviewList.this.data = dataReview.getContent();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });

                } catch (final Exception e) {
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
                    Page<Review> feeds=new ObjectMapper().readValue(response.body().string(),new TypeReference<Page<Article>>(){});
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
                            adapter.notifyDataSetChanged();
                        }
                    });
                }catch (Exception e)
                {

                }

            }
        });

    }
}

