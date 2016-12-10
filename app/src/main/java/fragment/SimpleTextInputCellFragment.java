package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.administrator.myapplication.R;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/12/5.
 */

public class SimpleTextInputCellFragment extends Fragment{
     //弱引用
    TextView label;
    EditText edit;


    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View view =inflater.inflate(R.layout.fragment_inputtcell_simpletext,container);
        label=(TextView)view.findViewById(R.id.label);
        edit=(EditText)view.findViewById(R.id.edit);
        return view ;
    }
    public void setLabelText(String labelText)
    {
        label.setText(labelText);
    }

    public void setHintText(String hintText)
    {
        edit.setHint(hintText);
    }
    //获取编辑框的数据
    public String getText()
    {
        return edit.getText().toString();
    }
    public void setIsPassword(boolean isPassword)
    {
        if (isPassword)
        {
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT|EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        }else
        {
            edit.setInputType(EditorInfo.TYPE_CLASS_TEXT);
        }
    }


}