package com.goodrice.goodrice3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodrice.zxinglib.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class KeyActivity extends AppCompatActivity {
    private Button btnScan, btnOk;
    private TextView tvBarcode, tvProduct, tvCompany,tvResult;
    private ImageView imgResult;
    private EditText edtSearch;
    public String resultString;

  

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key);
        initView();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(KeyActivity.this,CaptureActivity.class);
                startActivityForResult(intent,1001);
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultString = edtSearch.getText().toString().trim();
                if (resultString.equals("")) {
                    Toast.makeText(KeyActivity.this, "請輸入條碼數字", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Toast.makeText(KeyActivity.this, "輸入的數字為 "+resultString, Toast.LENGTH_SHORT).show();
                    new TransTask().execute("https://qar3yghz7uxscjkmkzp8rg-on.drv.tw/goodrice.json");
                    edtSearch.setText("");
                    edtSearch.clearFocus();
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(KeyActivity.this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);


                }
                
            }
        });
    }
    class TransTask extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            StringBuilder sb = new StringBuilder();
            BufferedReader reader = null;
            try{
                URL url = new URL(params[0]);
                reader = new BufferedReader(new InputStreamReader(url.openStream()));
                String line = reader.readLine();
                while (line != null){
                    sb.append(line);
                    line = reader.readLine();
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            String s = sb.toString();
            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            parseJSON(s);
        }
    }
    public void parseJSON(String s){
        ArrayList<Transaction> trans = new ArrayList<>();
        try{
            JSONArray array = new JSONArray(s);
            for (int i=0;i<array.length();i++){
                JSONObject obj = array.getJSONObject(i);
                String phase = obj.getString("phase");
                String barcode = obj.getString("barcode");
                String product = obj.getString("product");
                String company = obj.getString("company");
                String result = obj.getString("result");
                Log.d("JSON:", phase + "/" + barcode + "/" + product + "/" + company + "/" + result);
                Transaction t = new Transaction(phase, barcode, product, company, result);
                trans.add(t);
                Log.e("TESTONE","TEST"+String.valueOf(i));

                if (trans.get(i).getBarcode().equals(resultString)){
                    tvBarcode.setText(trans.get(i).getBarcode());
                    tvProduct.setText(trans.get(i).getProduct());
                    tvCompany.setText(trans.get(i).getCompany());
                    tvResult.setText(trans.get(i).getResult());
                    if(tvResult.getText().equals("不合格")){
                        imgResult.setImageResource(R.mipmap.failed);
                    }else{
                        imgResult.setImageResource(R.mipmap.pass);
                    }
                    Log.e("TESTTWO",trans.get(i).getProduct());

                    break;
                }else{
                    tvBarcode.setText("查無資料");
                    tvProduct.setText("");
                    tvCompany.setText("");
                    tvResult.setText("");
                    imgResult.setImageResource(R.mipmap.logo);

                }
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }
    public void initView(){
        tvBarcode = findViewById(R.id.tvBarcode);
        tvProduct = findViewById(R.id.tvProduct);
        tvCompany = findViewById(R.id.tvCompany);
        tvResult = findViewById(R.id.tvResult);
        imgResult = findViewById(R.id.imgResult);
        btnOk = findViewById(R.id.btnOk);
        edtSearch = findViewById(R.id.edtSearch);
        btnScan = findViewById(R.id.btnScan);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001 && resultCode== Activity.RESULT_OK)
        {
            resultString=data.getStringExtra(CaptureActivity.KEY_DATA);
            Toast.makeText(this, "輸入的數字為 "+resultString, Toast.LENGTH_SHORT).show();
            new TransTask().execute("https://qar3yghz7uxscjkmkzp8rg-on.drv.tw/goodrice.json");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.privacy:
                AlertDialog.Builder builder = null;
                builder = new AlertDialog.Builder(this);
                builder.setTitle("使用條款")
                        .setMessage("『呷好米』APP 僅提供您快速查詢行政院農委會提供之市售包裝米之檢驗結果，不會蒐集您的任何個人資料，敬請安心使用。若使用上有任何意見，歡迎來信指教 goodrice.service@gmail.com")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case R.id.copyright:
                AlertDialog.Builder builder2 = null;
                builder2 = new AlertDialog.Builder(this);
                builder2.setTitle("版權宣告")
                        .setMessage("『呷好米』APP 為 陳彥伃、蔡鴻淇、黃湧崑 於 2018年健行科技大學推廣教育中心-Android程式設計班，共同創作完成之成果。本APP僅為學習成果展現，無任何商業之運用。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
            case R.id.disclaimer:
                AlertDialog.Builder builder3 = null;
                builder3 = new AlertDialog.Builder(this);
                builder3.setTitle("免責聲明")
                        .setMessage("『呷好米』APP 所提供之資料皆自『行政院農業委員會資料開放平台』取得，市售包裝米之檢驗結果與品質，本APP將不負任何相關之責任。若對資料內容有任何疑義，請以農委會資料開放平台網站為準。")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}