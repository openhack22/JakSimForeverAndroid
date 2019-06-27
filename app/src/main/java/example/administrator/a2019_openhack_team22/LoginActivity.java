package example.administrator.a2019_openhack_team22;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

/*
로그인 기능 수행하는 액티비티
- 이해원
 */
public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LogTest";
    String SERVER_URL = "http://192.168.0.65:3000";
    final int REQUEST_JOIN = 1;
    EditText eID, ePwd;
    ImageView ivLogin, ivJoin;

    LoginAsyncTask loginAsyncTask;
    Context context;
    String userID, pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();

        // 뷰 정의
        eID = (EditText) findViewById(R.id.editTextID);
        ePwd = (EditText) findViewById(R.id.editTextPwd);
        ivLogin = (ImageView) findViewById(R.id.loginBtn);
        ivJoin = (ImageView) findViewById(R.id.joinBtn);

        // 로그인버튼 클릭 시
        ivLogin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        userID = eID.getText().toString();
                        pwd = ePwd.getText().toString();

                        if(userID.equals(null))  // 공백 입력 방지
                            Log.d(TAG, "id 공백");
                        else if(pwd.equals(null))
                            Log.d(TAG, "id 공백");
                        else {
                            cancelAsyncTask(loginAsyncTask);
                            loginAsyncTask.execute("/login");
                        }
                        break;
                }
                return true;
            }
        });

        // 회원가입 버튼 클릭 시
        ivJoin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent(context, JoinActivity.class);
                        startActivityForResult(intent, REQUEST_JOIN);
                        break;
                }
                return true;
            }
        });
    }

    public class LoginAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "LoginAsyncTask doInBackground");
            String login_url = SERVER_URL + strings[0];    // URL
// POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", userID);
                jsonObject.accumulate("password", pwd);
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
            if(str.equals(null)) {
                Log.d(TAG, "로그인 실패! 수신받은 값 unll");
            }

            try {
                JSONObject jsonObject = new JSONObject(str);
                if(jsonObject.get("username") == null)
                    Log.d(TAG, "로그인 실패! username 안담김");
                else {
                    String username = jsonObject.get("username").toString();
                    Intent intent = new Intent(context, MainActivity.class);
                    Log.d(TAG, "로그인 성공!");
                    intent.putExtra("id", userID);  // 성공했으니 id 담아서 인텐트 시작
                    intent.putExtra("username", username);  // 성공했으니 id 담아서 인텐트 시작
                    startActivity(intent);
                }

            } catch (JSONException e) {
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

    private void cancelAsyncTask(LoginAsyncTask loginAsyncTask) {
        if(loginAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            loginAsyncTask.cancel(true);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        switch (requestCode) {
            case REQUEST_JOIN:
                if(resultCode == RESULT_OK) {
                    Log.d(TAG, "회원가입 성공! id, pwd 값 얻어오는 과정");
                    userID = intent.getStringExtra("id");
                    pwd = intent.getStringExtra("pwd");

                    // edittext 설정
                    eID.setText(userID);
                    ePwd.setText(pwd);
                }
                else {
                    Log.d(TAG, "팝업창에서 취소 누름!");
                }
                break;
            default:
                break;
        }
    }
}
