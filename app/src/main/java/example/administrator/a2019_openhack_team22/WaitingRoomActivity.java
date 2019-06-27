package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ListActivity -> WaitingRoomActivity
 * 목표의 상세정보를 나타내는 액티비티
 */
public class WaitingRoomActivity extends AppCompatActivity {
    public static final String TAG = "LogTest";

    TextView txtUserName;   // 유저 네임 텍스트
    TextView txtStartDate;  // 시작 날짜 텍스트
    TextView txtTitle;      // 목표 이름 텍스트
    TextView txtDuration;   // 기간 텍스트
    TextView txtAmount;     // 금액 텍스트
    TextView txtUserLimit;  // 사용자 최대 인원수 텍스트
    TextView txtDescription;    // 설명 텍스트

    ImageView imgDuration;      // 기간 아이콘
    ImageView imgAmount;        // 금액 아이콘
    ImageView imgUserLimit;     // 인원 제한 아이콘

    Button btnJoin;             // 참여하기 버튼

    String strId, strUserName, goalId;
    String SERVER_URL = "http://10.10.2.88:5000";
    JoinRoomAsyncTask joinRoomAsyncTask = new JoinRoomAsyncTask();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

        Intent intent = getIntent();
        strId = intent.getStringExtra("id");
        strUserName = intent.getStringExtra("username");
        goalId = intent.getStringExtra("goal_id");

        GetRoomAsyncTask getRoomAsyncTask = new GetRoomAsyncTask();
        getRoomAsyncTask.execute("/");  // goal 정보 가져올 때 현재 이 골에 참여한 유저 몇명인지도 같이 보내줄수 있?

        txtUserName = findViewById(R.id.txt_user_name);
        txtStartDate = findViewById(R.id.txt_start_date);
        txtTitle = findViewById(R.id.txt_title);
        txtDuration = findViewById(R.id.txt_duration);
        txtAmount = findViewById(R.id.txt_amount);
        txtUserLimit = findViewById(R.id.txt_user_limit);
        txtDescription = findViewById(R.id.txt_description);
        imgDuration = findViewById(R.id.img_duration);
        imgAmount = findViewById(R.id.img_amount);
        imgUserLimit = findViewById(R.id.img_user_limit);
        btnJoin = findViewById(R.id.btn_join);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinRoomAsyncTask.execute("/joinRoom");
                btnJoin.setEnabled(false);
            }
        });


//        // 3초 후에 award 액티비티로 전환
//        new Handler().postDelayed(new Runnable() {// 3 초 후에 실행
//            @Override
//            public void run() {
//                // 실행할 동작 코딩
//                Intent intent = new Intent(RoomActivity.this, AwardActivity.class);
//                startActivity(intent);	//새로운 액티비티를 화면에 출력
//
////
////                mHandler.sendEmptyMessage(0);	// 실행이 끝난후 알림
//            }
//        }, 3000);

    }

    // 방 참여하기 위해 서버에 정보 전송
    public class JoinRoomAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "JoinRoomAsyncTask doInBackground");
            String login_url = SERVER_URL + strings[0];    // URL
            // POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", strId);
                jsonObject.accumulate("goal_id", "1"/*goal id 가져오는 코드 필요*/);

                Log.d(TAG, "url : " + login_url);

                URL url = new URL(login_url);  // URL 객체 생성
                Log.d(TAG, "jsonObject String : " + jsonObject.toString());

                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
                con.setRequestMethod("POST"); // POST방식 설정
                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Accept", "text/json"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
                con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미

                // 서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                // 버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
                writer.flush();
                writer.close(); // 버퍼를 받아줌

                con.connect();

                // 응답 코드 구분
                int responseCode = con.getResponseCode();   // 응답 코드 설정
                if(responseCode == HttpURLConnection.HTTP_OK)  // 200 정상 연결
                {
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
                    while((line = reader.readLine()) != null){
                        buffer.append(line); // buffer에 데이터 저장
                    }
                    return buffer.toString();
                }
                else {  // 정상 연결 아닐 시
                    printConnectionError(con);
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            if(str == null) {
                Log.d(TAG, "방 참가 실패! 수신받은 값 unll");
            }

            try {
                JSONObject jsonObject = new JSONObject(str);
                if(jsonObject.get("result") == null)
                    Log.d(TAG, "방 참가 실패! result 안담김");
                else {
                    if(jsonObject.get("result").toString().equals("200")) {
                        Log.d(TAG, "방 참가 성공!");
                        Toast.makeText(WaitingRoomActivity.this, "참여 됐습니다!", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    // 이 방의 정보를 가져오기 위해 리퀘 전송
    public class GetRoomAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "GetRoomAsyncTask doInBackground");
            String login_url = SERVER_URL + strings[0];    // URL
            // POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("goal_id", goalId == null ? "1" : goalId /*임시*/);

                Log.d(TAG, "url : " + login_url);

                URL url = new URL(login_url);  // URL 객체 생성
                Log.d(TAG, "jsonObject String : " + jsonObject.toString());

                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
                con.setRequestMethod("POST"); // POST방식 설정
                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Accept", "text/json"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
                con.setDoOutput(true); // Outstream으로 post 데이터를 넘겨주겠다는 의미
                con.setDoInput(true); // Inputstream으로 서버로부터 응답을 받겠다는 의미

                // 서버로 보내기위해서 스트림 만듬
                OutputStream outStream = con.getOutputStream();
                // 버퍼를 생성하고 넣음
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                writer.write(jsonObject.toString());    // searchword : 검색키워드 식으로 전송
                writer.flush();
                writer.close(); // 버퍼를 받아줌

                con.connect();

                // 응답 코드 구분
                int responseCode = con.getResponseCode();   // 응답 코드 설정
                if(responseCode == HttpURLConnection.HTTP_OK)  // 200 정상 연결
                {
                    //서버로 부터 데이터를 받음
                    InputStream stream = con.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    StringBuffer buffer = new StringBuffer();
                    String line;    // 한 줄씩 읽어오기 위한 임시 String 변수
                    while((line = reader.readLine()) != null){
                        buffer.append(line); // buffer에 데이터 저장
                    }
                    return buffer.toString();
                }
                else {  // 정상 연결 아닐 시
                    printConnectionError(con);
                    return null;
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, String.valueOf(e));
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String str) {
            if(str == null) {
                Log.d(TAG, "방 참가 실패! 수신받은 값 unll");
            }

            try {
                JSONObject jsonObject = new JSONObject(str);
                if(jsonObject.get("result") == null)
                    Log.d(TAG, "방 정보 가져오기 실패! result 안담김");
                else {
                    if(jsonObject.get("result").toString().equals("200")) {
                        Log.d(TAG, "방 정보 가져오기 성공!");
                        txtTitle.setText((String) jsonObject.get("goal_name"));
                        txtDescription.setText((String) jsonObject.get("description"));
                        txtDuration.setText(jsonObject.get("duration") + "일");
                        txtAmount.setText(jsonObject.get("amount") + "원");
                        txtUserLimit.setText(jsonObject.get("user_limit") + "명");
                        txtUserName.setText(strUserName);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    /*
    HttpURLConnection 연결 잘 안되는 경우 원인 내용 Log 출력
     */
    public static void printConnectionError(HttpURLConnection con) throws IOException {
        Log.d(TAG, "printConnectionError");
        InputStream is = con.getErrorStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] byteBuffer = new byte[1024];
        byte[] byteData = null;
        int nLength = 0;
        while((nLength = is.read(byteBuffer, 0, byteBuffer.length)) != -1) {
            baos.write(byteBuffer, 0, nLength);
        }
        byteData = baos.toByteArray();
        String response = new String(byteData);
        Log.d(TAG, "응답 코드 발생! 오류 내용 = " + response);
    }
}
