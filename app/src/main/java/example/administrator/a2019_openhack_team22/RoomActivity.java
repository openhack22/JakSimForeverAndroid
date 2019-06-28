package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
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

public class RoomActivity extends AppCompatActivity {

    TextView txtGoalTitle;
    TextView txtDuration;
    TextView txtAmount;
    TextView txtUserLimit;
    TextView txtDescription;
    TextView txtOwnerUserName;
    TextView txtUserName1;
    TextView txtUserName2;
    TextView txtUserName3;
    TextView txtProgressOwner;
    TextView txtProgress1;
    TextView txtProgress2;
    TextView txtProgress3;

    Button btnAuth;
    Bitmap bitmap;

    String strId, strUserName, strGoalId;
    String SERVER_URL = "http://10.10.2.88:5000";
    public static final String TAG = "LogTest";
    GetRoomAsyncTask getRoomAsyncTask = new GetRoomAsyncTask();


//    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        Intent intent = getIntent();
        strId = intent.getStringExtra("id");
        strUserName = intent.getStringExtra("username");
        strGoalId = intent.getStringExtra("goal_id");

        txtGoalTitle = findViewById(R.id.txt_goal_title);
        txtDuration = findViewById(R.id.txt_duration);
        txtAmount = findViewById(R.id.txt_amount);
        txtUserLimit = findViewById(R.id.txt_user_limit);
        txtDescription = findViewById(R.id.txt_description);
        txtOwnerUserName = findViewById(R.id.txt_owner_user_name);
        txtUserName1 = findViewById(R.id.txt_joined_user_name_1);
        txtUserName2 = findViewById(R.id.txt_joined_user_name_2);
        txtUserName3 = findViewById(R.id.txt_joined_user_name_3);
        txtProgressOwner = findViewById(R.id.txt_progress);
        txtProgress1 = findViewById(R.id.txt_joined_progress_1);
        txtProgress2 = findViewById(R.id.txt_joined_progress_2);
        txtProgress3 = findViewById(R.id.txt_joined_progress_3);

        btnAuth = findViewById(R.id.btn_auth);

        getRoomAsyncTask.execute("/getGoal"); // TODO: url 확인 필요

        // 인증하기 버튼 클릭했을 때 화면 전환
        btnAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, AuthActivity.class);
                startActivityForResult(intent, 0);	//새로운 액티비티를 화면에 출력
            }
        });


        // 이스터에그 - 글씨 크게하면 커지다가 어워드액티비티로 넘어감
        txtGoalTitle.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                txtGoalTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, txtGoalTitle.getTextSize()+1);

                if (txtGoalTitle.getTextSize() >= 30) {
                    Intent intent = new Intent(RoomActivity.this, AwardActivity.class);
                    startActivity(intent);	//새로운 액티비티를 화면에 출력
                }
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
                jsonObject.accumulate("goal_id", strGoalId == null ? "1" : strGoalId /*임시*/);

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
                        txtGoalTitle.setText((String) jsonObject.get("goal_name"));
                        txtDescription.setText((String) jsonObject.get("description"));
                        txtDuration.setText(jsonObject.get("duration") + "일");
                        txtAmount.setText(jsonObject.get("amount") + "원");
                        txtUserLimit.setText(jsonObject.get("user_limit") + "인");
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

    @Override
    //세컨드 액티비티의 setResult()에서 값을 전달하면 onActivityResult()메소드를 실행한다.
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {	//setResult()에서 보낸 값이 RESULT_OK이면
            byte[] img = data.getByteArrayExtra("image"); // 인텐트(data)의  “image” 값을 가져옴.
//            bitmap = BitmapFactory.decodeByteArray( img, 0, img.length ) ;
            //ImageView.setImageBitmap(bitmap); // 이미지뷰에 비트맵 형식으로 표시
        }
    }

    // TODO: 이미지 데이터 bytearray형태로 넘기기
}
