package fragment.pages;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.User;
import com.example.api.Server;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/12/10.
 */

public class MyProfileFragment extends Fragment {
    TextView textView;
    View progress;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_page_my_profile, null);
            //(TextView) view.findViewById(R.id.text1)中的（TextView）指的是强制转换
            textView=(TextView) view.findViewById(R.id.text1);
            progress = view.findViewById(R.id.progress);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);
        textView.setVisibility(View.GONE);

        OkHttpClient client= Server.getSharedClient();
        Request request=Server.requestBuilderWithApi("me")
                .method("GET",null)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MyProfileFragment.this.onFailuer(call,e);
                    }
                });

            }

            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                try
                {

                    final User user=new ObjectMapper().readValue(response.body().bytes(),User.class);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProfileFragment.this.onResponse(call,user);
                        }
                    });
                }catch(final Exception e)
                {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MyProfileFragment.this.onFailuer(call,e);
                        }
                    });
                }

            }
        });
    }

    void onResponse(Call call, User user)
    {
        progress.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.BLACK);
        textView.setText(user.getName());
    }
    void onFailuer(Call call,Exception E)
    {
        progress.setVisibility(View.GONE);
        textView.setVisibility(View.VISIBLE);
        textView.setTextColor(Color.RED);
        textView.setText(E.getMessage());
    }
}




