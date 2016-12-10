package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2016/12/6.
 */

public class PasswordRecoverStep1Fragment extends Fragment {
    View view;
    SimpleTextInputCellFragment fragEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_passord_step1, null);
           fragEmail=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_find_email);
            view.findViewById(R.id.btn_next).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View V) {
                    goNext();
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fragEmail.setLabelText("邮箱账号");
        fragEmail.setHintText("请输入邮箱账号");
    }

    public static interface OnGoNextListener {
        void onGoNext();
    }

    OnGoNextListener onGoNextListener;

    public void setOnGoNextListener(OnGoNextListener onGoNextListener) {
        this.onGoNextListener = onGoNextListener;
    }

    void goNext() {
        if (onGoNextListener != null) {
            onGoNextListener.onGoNext();
        }
    }
}

