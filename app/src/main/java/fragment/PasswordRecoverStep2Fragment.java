package fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.PasswordRecoverActivity;
import com.example.administrator.myapplication.R;

/**
 * Created by Administrator on 2016/12/6.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class PasswordRecoverStep2Fragment extends Fragment {
    SimpleTextInputCellFragment input_new_password;
    SimpleTextInputCellFragment input_new_surepassword;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_password_step2, null);
            input_new_password=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_new_password);
            input_new_surepassword=(SimpleTextInputCellFragment)getFragmentManager().findFragmentById(R.id.input_new_surepassword);
            view.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    submit();
                }
            });
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        input_new_password.setLabelText("输入新密码");
        input_new_password.setHintText("输入新密码");
        input_new_surepassword.setLabelText("重复输入想密码");
        input_new_surepassword.setHintText("重复输入想密码");
    }
    public String getText1()
    {
        return input_new_password.getText();

    }
    public String getText2()
    {
        return input_new_surepassword.getText();
    }
    public  static  interface OngosubmitListener
    {
        void submit();
    }
    OngosubmitListener onsubmitListener;
    public void setOngosubmitListener(OngosubmitListener submitListener)
    {
        this.onsubmitListener=submitListener;
    }
    void submit()
    {
        if(input_new_surepassword.getText().equals(input_new_password.getText()))

        {
            if(onsubmitListener!=null)
            {
                onsubmitListener.submit();
            }
        }else {

            new AlertDialog.Builder(getActivity()).setMessage("两次密码不一致").show();






        }
    }


}