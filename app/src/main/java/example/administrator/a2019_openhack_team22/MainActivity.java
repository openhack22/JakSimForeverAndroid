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
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends FontActivity {
    public static final String TAG = "LogTest";

    TextView userName, plusMoney, minusMoney, numGoal;  // 사용자 닉네임, 얻은 돈, 잃은 돈, 참여중인 공동목표
    Button btnJoin; // 공동목표 참여하기 버튼
    String strUserName, strId;
    String SERVER_URL = "http://10.10.2.88:5000";

//    LoginAsyncTask loginAsyncTask;
    GetRoomAsyncTask getRoomAsyncTask = new GetRoomAsyncTask();


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

        // userName login 액티비티에서 받아와 표시 : userName
        Intent intent = getIntent();
        strUserName = intent.getStringExtra("username");
        strId = intent.getStringExtra("id");
        userName.setText(strUserName);

        getRoomAsyncTask.execute("/getMyList");

        // TODO:얻은 돈, 잃은 돈 DB에서 받아와 표시 : plusMoney, minusMoney


        // TODO:참여중인 목표 개수 DB에서 받아와 표시 : numGoal


        // RoomList액티비티로 넘어가기
        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomListActivity.class);
                intent.putExtra("id", strId);
                intent.putExtra("username", strUserName);
                startActivity(intent);
            }
        });
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
                jsonObject.accumulate("id", strId /*임시*/);

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
//                if(jsonObject.get("result") == null)
                    Log.d(TAG, "방 정보 가져오기 실패! result 안담김");
//                else {
                    Log.e("goal_id", jsonObject.get("goal_id").toString());
                    Log.e("goal_name", jsonObject.get("goal_name").toString());
                    Log.e("user_id", jsonObject.get("user_id").toString());
                    Log.e("date", jsonObject.get("date").toString());
                    Log.e("image", jsonObject.get("image").toString());
                    Log.e("status", jsonObject.get("status").toString());
//                    if(jsonObject.get("result").toString().equals("200")) {
//                        Log.d(TAG, "방 정보 가져오기 성공!");
//                    }
//                }

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

    public static String[] jsonParser(String string){
        string = string.substring(1,string.length()-2); // 괄호 지움
        string = string.replace('"', ' ');

        return string.split(",");
    }
}
