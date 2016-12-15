package fragment.pages;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Administrator on 2016/12/15.
 */

public class NotesFragment extends Fragment {
    View view;
    ListView listView;
    List<Review> data;
    View btnLoadMore;
    TextView textLoadMore;
    Integer page = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_pager_notes_list, null);
            listView = (ListView) view.findViewById(R.id.list_notes);
            listView.setAdapter(notesAdapter);
            view.findViewById(R.id.review_to_me).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goreadReviewToMe();
                }
            });
            view.findViewById(R.id.review_others).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goreadMyReview();
                }
            });

        }
        return view;
    }

    BaseAdapter notesAdapter = new BaseAdapter() {
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
            View view;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                view = inflater.inflate(R.layout.simple_list_item_review, null);
            } else {
                view = convertView;
            }
            TextView textReview = (TextView) view.findViewById(R.id.user_review_details);
            TextView timeReview = (TextView) view.findViewById(R.id.time_review);
            TextView authorReview = (TextView) view.findViewById(R.id.user_name_review);
            AvatarView avatar = (AvatarView) view.findViewById(R.id.user_image);

            Review review = data.get(position);
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

    void goreadReviewToMe() {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithApi("review/")
                .method("get", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    final Page<Review> data = new ObjectMapper().readValue(responseString, new TypeReference<Page<Review>>() {
                    });

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NotesFragment.this.page = data.getNumber();
                            NotesFragment.this.data = data.getContent();
                            notesAdapter.notifyDataSetChanged();
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

    void goreadMyReview() {
        OkHttpClient client = Server.getSharedClient();
        Request request = Server.requestBuilderWithApi("Ireview/")
                .method("get", null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            //
            public void onFailure(Call call, IOException e) {


            }
            //连接成功就跳转
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String responseString = response.body().string();
                    final Page<Review> data = new ObjectMapper().readValue(responseString, new TypeReference<Page<Review>>() {
                    });
                    get连接不成功就不跳转Activity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            NotesFragment.this.page = data.getNumber();
                            NotesFragment.this.data = data.getContent();
                            notesAdapter.notifyDataSetChanged();

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
}










