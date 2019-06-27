package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
새로운 목표 방 추가하는 액티비티
- 이해원
 */
public class AddRoomActivity extends AppCompatActivity {
    public static final String TAG = "LogTest";
    String SERVER_URL = "http://10.10.2.88:5000";
    String strId, strUserName;

    EditText eGoalName, eGoalDescription;
    ImageView ivSevenDay, ivOneMonth, ivThreeMonth;
    ImageView ivThreePeople, ivFourPeople, ivFivePeople, ivSixPeople;
    ImageView ivAddRoomBtn;
    SeekBar seekMoneyBar;
    TextView tvMoney;

    ImageView addRoomBtn;

    AddRoomAsyncTask addRoomAsyncTask = new AddRoomAsyncTask();
    String userID;
    String goalName, goalDescription;
    int duration = 0;   // 0 : default  1 : 7day  2 : 1month  3: 3month
    int money = 0;
    int userNum = 0;    // 0 : default  1 : 3people  2 : 4people  3 : 5people  4 : 6people

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        Intent getIntent = getIntent();
        strId = getIntent.getStringExtra("id");
        strUserName = getIntent.getStringExtra("username");

        // 뷰 정의
        eGoalName = (EditText) findViewById(R.id.editTextGoalName);
        eGoalDescription = (EditText) findViewById(R.id.editTextGoalDescription);
        ivSevenDay = (ImageView) findViewById(R.id.sevenDayBtn);
        ivOneMonth = (ImageView) findViewById(R.id.oneMonthBtn);
        ivThreeMonth = (ImageView) findViewById(R.id.threeMonthBtn);
        ivThreePeople = (ImageView) findViewById(R.id.threePeopleBtn);
        ivFourPeople = (ImageView) findViewById(R.id.fourPeopleBtn);
        ivFivePeople = (ImageView) findViewById(R.id.fivePeopleBtn);
        ivSixPeople = (ImageView) findViewById(R.id.sixPeopleBtn);
        ivAddRoomBtn = (ImageView) findViewById(R.id.addRoomBtn);
        seekMoneyBar = (SeekBar) findViewById(R.id.moneySeekBar);
        tvMoney = (TextView) findViewById(R.id.textViewMoney);
        addRoomBtn = (ImageView) findViewById(R.id.addRoomBtn);

        // RoomListActivity에서 아이디와 닉네임 가지고 옴.
//        Intent userInfoIntent = getIntent();
//        if(userInfoIntent != null) {    // 인텐트 null 체크
//            // id를 가지고옴
//            if(userInfoIntent.hasExtra("id") && userInfoIntent.hasExtra("username"))
//                userID = userInfoIntent.getStringExtra("id");   // ID String에 저장
//            else {
//                Toast.makeText(getApplicationContext(), "필수 값을 불러올 수 없습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
//                finish();
//            }
//        }
//        else {
//            Toast.makeText(getApplicationContext(), "타입이 선택되지 않았습니다. 이전 화면으로 돌아갑니다.", Toast.LENGTH_LONG).show();
//            finish();
//        }

        // seekMoneyBar.incrementProgressBy(1000);
        // seekBar의 상태를 변경할 때
        seekMoneyBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // Seek의 상태가 변경되었을 때 실행될 사항
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // textview가 따라다니면서 화면에 표시
                int padding= seekBar.getPaddingLeft() + seekBar.getPaddingRight();
                int sPos = seekBar.getLeft() + seekBar.getPaddingLeft();
                int xPos = (seekBar.getWidth()-padding) * seekBar.getProgress() / seekBar.getMax() + sPos - (tvMoney.getWidth()/2);
                money = seekBar.getProgress() * 1000;  // 값 설정
                tvMoney.setX(xPos);
                if(money == 0)
                    tvMoney.setText("free");
                else
                    tvMoney.setText(String.valueOf(money) + "원");
            }
            // SeekBar의 움직임이 시작되었을 때 실행
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            // SeekBar의 움직임이 멈춘다면 실행될 사항
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if(money == 0)
                    tvMoney.setText("free");
            }
        });


        // WaitingRoomActivity로 화면 전환
        addRoomBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AddRoomActivity.this, WaitingRoomActivity.class);
                intent.putExtra("id", strId);
                intent.putExtra("username", strUserName);
                startActivity(intent);	//새로운 액티비티를 화면에 출력
            }
        });
    }

    // 각 ImageView 클릭에 따른 리스너 묶음 변수 선언
    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            ImageView image = (ImageView) v;
            switch (v.getId()) {
                case R.id.sevenDayBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        duration = 1;
                        image.setImageResource(R.drawable.addroom_sevendaybtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        // image.setImageResource(R.drawable.addroom_sevendaybtn);
                    }
                    break;
                case R.id.oneMonthBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        duration = 2;
                        image.setImageResource(R.drawable.addroom_onemonthbtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        image.setImageResource(R.drawable.addroom_onemonthbtn);
                    }
                    break;
                case R.id.threeMonthBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        duration = 3;
                        image.setImageResource(R.drawable.addroom_threemonthbtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        image.setImageResource(R.drawable.addroom_threemonthbtn);
                    }
                    break;
                case R.id.threePeopleBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        userNum = 1;
                        image.setImageResource(R.drawable.addroom_threebtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        image.setImageResource(R.drawable.addroom_threebtn);
                    }
                    break;
                case R.id.fourPeopleBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        userNum = 2;
                        image.setImageResource(R.drawable.addroom_fourbtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        image.setImageResource(R.drawable.addroom_fourbtn);
                    }
                    break;
                case R.id.fivePeopleBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        userNum = 3;
                        image.setImageResource(R.drawable.addroom_fivebtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                        image.setImageResource(R.drawable.addroom_fivebtn);
                    }
                    break;
                case R.id.sixPeopleBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        userNum = 4;
                        image.setImageResource(R.drawable.addroom_sixbtnclick);
                    } else if (event.getAction() == MotionEvent.ACTION_UP) {    // 클릭 X
                         image.setImageResource(R.drawable.addroom_sixbtn);
                    }
                    break;
                case R.id.addRoomBtn:
                    if (event.getAction() == MotionEvent.ACTION_DOWN) { // 클릭 O
                        if(checkRoomInfo()) {
                            addRoomAsyncTask.execute("/addRoom");
                        }
                    }
            }
            return false;
        }
    };

    /*
    방 생성을 위한 정보 제대로 입력했는지 확인
    우선 null check만
     */
    private boolean checkRoomInfo() {
        if(checkEditText(eGoalName) && checkEditText(eGoalDescription)) {
            goalName = eGoalName.getText().toString();
            goalDescription = eGoalDescription.getText().toString();
            if(duration != 0 && userNum != 0)
                return true;
        }
        return false;
    }

    /*
    EditText의 값 중 공백 확인
    @return : boolean(true : 앞 뒤 공백 또는 전체 공백이 아님. false : 둘 중 하나라도 해당하는 경우)
     */
    private boolean checkEditText(EditText editText) {
        String editStr = editText.getText().toString();    // 검색어 임시 변수에 저장.
        if(TextUtils.isEmpty(editStr.trim())) { // 공백처리
            return false;
        }
        if(!editStr.equals(editStr.trim())) { // 앞뒤 공백이 존재하는 단어 입력
            return false;
        }
        return true;
    }

    // 방 생성하기 위해 서버에 정보 전송
    public class AddRoomAsyncTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "AddRoomAsyncTask doInBackground");
            String login_url = SERVER_URL + strings[0];    // URL
            // POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("id", userID);
                jsonObject.accumulate("goalName", goalName);
                jsonObject.accumulate("goalDescription", goalDescription);
                jsonObject.accumulate("duration", duration);
                jsonObject.accumulate("money", money);
                jsonObject.accumulate("userNum", userNum);

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
                Log.d(TAG, "방 생성 실패! 수신받은 값 unll");
            }

            try {
                JSONObject jsonObject = new JSONObject(str);
                if(jsonObject.get("result") == null)
                    Log.d(TAG, "방 생성 실패! result 안담김");
                else {
                    if(jsonObject.get("result").toString().equals("OK")) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.putExtra("id", strId);
                        intent.putExtra("username", strUserName);
                        Log.d(TAG, "방 생성 성공!");
                        startActivity(intent);
                    }
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
}
