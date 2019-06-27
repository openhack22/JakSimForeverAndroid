package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView userName, plusMoney, minusMoney, numGoal;  // 사용자 닉네임, 얻은 돈, 잃은 돈, 참여중인 공동목표
    Button btnJoin; // 공동목표 참여하기 버튼

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 위젯 연결
        plusMoney = (TextView) findViewById(R.id.plusMoney);
        minusMoney = (TextView) findViewById(R.id.minusMoney);
        userName = (TextView) findViewById(R.id.name);
        numGoal = (TextView) findViewById(R.id.numGoal);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        // userName DB에서 받아와 표시 : userName


        // 얻은 돈, 잃은 돈 DB에서 받아와 표시 : plusMoney, minusMoney


        // 참여중인 목표 개수 DB에서 받아와 표시 : numGoal


        // '공동목표 참여하기'버튼 클릭하면 리스트화면 들어가기
        btnJoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력
            }
        });
    }


}
