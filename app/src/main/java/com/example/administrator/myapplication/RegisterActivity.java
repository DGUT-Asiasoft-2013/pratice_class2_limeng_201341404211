package com.example.administrator.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.EOFException;
import java.io.IOException;

import fragment.SimpleTextInputCellFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends Activity{
    SimpleTextInputCellFragment fragInputCellAccount;
    SimpleTextInputCellFragment fragInputCellPassword;
    SimpleTextInputCellFragment fragInputCellPasswordRepeat;
    SimpleTextInputCellFragment fragInputEmail;
    SimpleTextInputCellFragment fragmentCellname;
    PictureInputCellFragment fragInputAvata;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fragInputCellAccount = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.account);
        fragmentCellname = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.name);
        fragInputCellPassword = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.password);
        fragInputCellPasswordRepeat = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.surepassord);
        fragInputEmail = (SimpleTextInputCellFragment) getFragmentManager().findFragmentById(R.id.input_email);
        //fragInputAvata=(PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.InputPicture) ;

        fragInputAvata=(PictureInputCellFragment)getFragmentManager().findFragmentById(R.id.input_picture) ;
        findViewById(R.id.btn_sure_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        fragInputCellAccount.setLabelText("账户名");
        fragInputCellAccount.setHintText("请输入用户名");
        fragInputCellPassword.setLabelText("密码");
        fragInputCellPassword.setHintText("请输入密码");
        fragInputCellPassword.setIsPassword(true);
        fragInputCellPasswordRepeat.setLabelText("重复密码");
        fragInputCellPasswordRepeat.setHintText("请重复输入密码");
        fragInputEmail.setHintText("请输入邮箱");
        fragInputEmail.setLabelText("邮箱");
        fragInputCellPasswordRepeat.setIsPassword(true);
        fragmentCellname.setHintText("请输入昵称");
        fragmentCellname.setLabelText("昵称");

    }

    void submit() {
        /*获取组件的输入数据
        在fragInputCellPassword中编写获取函数getText();
        public String getText()
       {
           return edit.getText().toString();
       }
       */
        String password = fragInputCellPassword.getText();
        String passwordRepeat=fragInputCellPasswordRepeat.getText();
        if(!password.equals(passwordRepeat))
        {
            //构建一个对话框
            new AlertDialog.Builder(RegisterActivity.this)
                .setMessage("重复密码不一致")
                ///.setIcon(android.R.drawable.ic_dialog_alertc)
                .setNegativeButton("好",null)
                .show();
            return ;
        }
        //用户密码加密
        password=MD5.getMD5(password);
        //获取视图中组件的输入内容
        String account=fragInputCellAccount.getText();
        String name=fragmentCellname.getText();
        String email=fragInputEmail.getText();


         ////创建okHttpClient对象
        OkHttpClient client=new OkHttpClient();


        //创建一个FROM数据组
        //表单MultipartBody
        //addFormDataPart("account",account)   前者的account是数据库的字段名，后者是存入的数据
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("account",account)
                .addFormDataPart("name",name)
                .addFormDataPart("email",email)
                .addFormDataPart("passwordHash",password);
        //定义一个字节型数组，保存图片文件
        byte[] pngData = fragInputAvata.getPngData();
        if (pngData!=null)
        {
            RequestBody pngRequestBody = RequestBody.create(MediaType.parse("image/png"),pngData);
            bodyBuilder.addFormDataPart("avatar","avatar.png",pngRequestBody);
        }
        //创建连接数据的url,以及数据
        MultipartBody postBody = bodyBuilder.build();

        //创建一个客户端请求：需要输入请求服务器的url,以及请求的方法
        Request request=new Request.Builder()
                .url("Http://172.27.0.36:8080/membercenter/api/register")
                .method("post",null)
                .post(postBody)
                .build();
        final ProgressDialog progressDialog=new ProgressDialog(RegisterActivity.this);
        progressDialog.setMessage("请稍后");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        //客户端往服务器发送一个请求，并把这个请求放进队列
        //请求加入调度
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Call call, final IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        onFailure(call,e);
                    }
                });

            }

            @Override
            public void onResponse(final Call call, final Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        try {
                            RegisterActivity.this.onResponse(call,response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            RegisterActivity.this.onFailure(call,e);
                        }

                    }
                });

            }
        });

    }
    void onResponse(Call call,String responseBody)
    {
        new AlertDialog.Builder(this)
                .setTitle("注册成功")
                .setMessage(responseBody)
                .setPositiveButton("好", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                }).show();
    }
    void onFailure(Call call,Exception e)
    {
        new AlertDialog.Builder(this)
                .setTitle("请求失败哦")
                .setMessage(e.getLocalizedMessage())
                .setNegativeButton("好",null)
                .show();

    }




}
