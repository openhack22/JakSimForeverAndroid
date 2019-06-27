package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.util.ArrayList;

/*
회원가입 기능을 수행하는 액티비티
- 이해원
 */
public class JoinActivity extends FontActivity {
    public static final String TAG = "LogTest";
    String SERVER_URL = "http://10.10.2.88:5000";

    EditText eID, ePwd, ePwdReCheck, eUsername;
    EditText eCardName1, eCardName2, eCardName3, eCardName4, eCVC;
    EditText eCardPwd;
    Spinner sBank, sYear, sMonth, sDay;
    ImageView ivJoin, ivIdCheckBtn, ivUsernameCheckBtn;

    String userID, pwd, pwdReCheck, username;
    String bank, cardNum1, cardNum2, cardNum3, cardNum4, cardNum;
    String cvc, year, month, day, date, cardPwd;
    Boolean hasCheckID = false;
    Boolean hasCheckUsername = false;
    JoinAsyncTask joinAsyncTask;

    ArrayList<String> yearArray = new ArrayList<>();
    ArrayList<String> monthArray = new ArrayList<>();
    ArrayList<String> dayArray = new ArrayList<>();
    ArrayAdapter bankAdapter, yearAdapter, monthAdapter, dayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        // 뷰 정의
        eID = (EditText) findViewById(R.id.editTextID);
        ePwd = (EditText) findViewById(R.id.editTextPwd);
        ePwdReCheck = (EditText) findViewById(R.id.editTextPwdReCheck);
        eUsername = (EditText) findViewById(R.id.editTextUsername);
        eCardName1 = (EditText) findViewById(R.id.editTextCardName1);
        eCardName2 = (EditText) findViewById(R.id.editTextCardName2);
        eCardName3 = (EditText) findViewById(R.id.editTextCardName3);
        eCardName4 = (EditText) findViewById(R.id.editTextCardName4);
        eCVC = (EditText) findViewById(R.id.editTextCardCVC);
        eCardPwd = (EditText) findViewById(R.id.editTextCardPwd);
        sBank = (Spinner) findViewById(R.id.bankSpinner);
        sYear = (Spinner) findViewById(R.id.yearSpinner);
        sMonth = (Spinner) findViewById(R.id.monthSpinner);
//        sDay = (Spinner) findViewById(R.id.daySpinner);

        ivIdCheckBtn = (ImageView) findViewById(R.id.idCheckBtn);
        ivUsernameCheckBtn = (ImageView) findViewById(R.id.usernameCheckBtn);
        ivJoin = (ImageView) findViewById(R.id.joinBtn);
        joinAsyncTask = new JoinAsyncTask();

        // Spinner ArrayList 생성
        for(int i = 2019; i <= 2029; i++) {
            yearArray.add(String.valueOf(i));
        }
        for(int i = 1; i <= 12; i++) {
            if(i < 10) {
                monthArray.add("0" + String.valueOf(i));
            }
            else
                monthArray.add(String.valueOf(i));
        }


        bankAdapter = ArrayAdapter.createFromResource(this, R.array.BankArray, android.R.layout.simple_spinner_dropdown_item);
        //  성별 선택 값 가져오기
        sBank.setAdapter(bankAdapter);
        yearAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, yearArray);
        monthAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, monthArray);
        sYear.setAdapter(yearAdapter);
        sMonth.setAdapter(monthAdapter);

        sBank.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bank = bankAdapter.getItem(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                bank = "KB은행";
            }
        });

        //  Spinner 선택 값 가져오기
        sYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() { // 중성화 여부 선택 리스너
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year = yearAdapter.getItem(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month = monthAdapter.getItem(position).toString();
                setDayArray(month);
                dayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.support_simple_spinner_dropdown_item, dayArray);
//                sDay.setAdapter(dayAdapter);
//                sDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        day = "01";//dayAdapter.getItem(position).toString();
//                        if(Integer.valueOf(day) < 10)
//                            day = "0" + day;
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // 회원가입 버튼 클릭 시
        ivJoin.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        if(checkUserInfo()) {
                            if(checkCardInfo()) {
                                cancelAsyncTask(joinAsyncTask);
                                joinAsyncTask.execute("/regist", "joinAndCard");
                            }
                            else {
                                cancelAsyncTask(joinAsyncTask);
                                joinAsyncTask.execute("/regist", "join");
                            }
                        }
                        break;
                }
                return true;
            }
        });

        // id 중복검사 버튼 클릭 시
        ivIdCheckBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        hasCheckID = false;
                        cancelAsyncTask(joinAsyncTask);
                        userID = eID.getText().toString();
                        joinAsyncTask.execute("/checkID", "checkID");
                        break;
                }
                return true;
            }
        });

        // 닉네임 중복검사 버튼 클릭 시
        ivUsernameCheckBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        hasCheckUsername = false;
                        cancelAsyncTask(joinAsyncTask);
                        username = eUsername.getText().toString();
                        joinAsyncTask.execute("/checkUsername", "checkUsername");
                        break;
                }
                return true;
            }
        });
    }

    /*
    유저 정보 제대로 입력했는지 확인
    우선 null check만
     */
    private boolean checkUserInfo() {
        if(checkEditText(eID) && checkEditText(ePwd)
                && checkEditText(ePwdReCheck) && checkEditText(eUsername)) {
            userID = eID.getText().toString();
            pwd = ePwd.getText().toString();
            pwdReCheck = ePwdReCheck.getText().toString();
            username = eUsername.getText().toString();

            if(pwd.equals(pwdReCheck))
                return true;
        }
        return false;
    }

    /*
    카드 정보 제대로 입력했는지 확인
    우선 null check만
     */
    private boolean checkCardInfo() {
        if(checkEditText(eCardName1) && checkEditText(eCardName2) && checkEditText(eCardName3) && checkEditText(eCardName4)) {
            if(checkEditText(eCVC) && checkEditText(eCardPwd)) {
                cardNum1 = eCardName1.getText().toString();
                cardNum2 = eCardName2.getText().toString();
                cardNum3 = eCardName3.getText().toString();
                cardNum4 = eCardName4.getText().toString();
                cardNum = cardNum1 + cardNum2 + cardNum3 + cardNum4;
                cvc = eCVC.getText().toString();
                cardPwd = eCardPwd.getText().toString();
                date = year + "-" + month + "-" + "01"/*day*/;
                Log.d(TAG, cardNum + ", " + cvc + ", " + date + ", " + cardPwd + ", " + bank);
                return true;
            }
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

    public class JoinAsyncTask extends AsyncTask<String, String, String> {
        String type;

        // String... [0] : url  [1] : type  [2 ...] : type마다 필요한 값들
        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "JoinAsyncTask doInBackground");
            String join_url = SERVER_URL + strings[0];    // URL
            type = strings[1];
            // POST 전송방식을 위한 설정
            HttpURLConnection con = null;
            BufferedReader reader = null;

            // 서버에 특정 키워드 디비에서 검색 요청
            try {
                Log.d(TAG, "url : " + join_url);
                Log.d(TAG, "type : " + type);
                JSONObject jsonObject = new JSONObject();
                jsonObject = getJSONObjWithType(type);

                URL url = new URL(join_url);  // URL 객체 생성
                Log.d(TAG, "jsonObject String : " + jsonObject.toString());

                con = (HttpURLConnection) url.openConnection();
                con.setConnectTimeout(10 * 1000);   // 서버 접속 시 연결 시간
                con.setReadTimeout(10 * 1000);   // Read시 연결 시간
                con.setRequestMethod("POST"); // POST방식 설정
                con.setRequestProperty("Content-Type", "application/json"); // application JSON 형식으로 전송
                con.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
                con.setRequestProperty("Accept", "text/html"); // 서버에 response 데이터를 html로 받음 -> JSON 또는 xml
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
                Log.d(TAG, "실패! 수신받은 값 unll");
            }
            try {
                JSONObject jsonObject = new JSONObject(str);
                if(type.equals("join") || type.equals("joinAndCard")) {   // 회원가입을 위한 서버 요청인 경우
                    if(jsonObject.get("result") == null)
                        Log.d(TAG, "회원가입 실패! 이유 : result is null");
                    else {  // 회원가입 성공
                       if(jsonObject.getString("result").equals("OK")) {
                           Intent intent = new Intent();
                           intent.putExtra("id", userID);
                           intent.putExtra("pwd", pwd);
                           setResult(RESULT_OK, intent);
                       }
                    }
                }
                else if(type.equals("checkID")) {   // ID 중복검사
                    if(jsonObject.get("result") == null)
                        Log.d(TAG, "ID가 중복되었습니다.");
                    else {  // 중복검사 통과
                        if(jsonObject.getString("result").equals("OK")) {
                            Log.d(TAG, "중복된 ID가 없습니다.");
                            hasCheckID = true;
                        }
                    }
                }
                else if(type.equals("checkUsername")) { // username 중복검사
                    if(jsonObject.get("result") == null)
                        Log.d(TAG, "닉네임이 중복되었습니다.");
                    else {  // 중복검사 통과
                        if(jsonObject.getString("result").equals("OK")) {
                            Log.d(TAG, "중복된 닉네임이 없습니다.");
                            hasCheckUsername = true;
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
                Toast.makeText(JoinActivity.this, "값이 비어있습니다 ㅜㅜ", Toast.LENGTH_SHORT).show();
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

    /*
    유저정보, 카드정보 한 JSONObject에 담기
     */
    private JSONObject getJSONObjWithType(String type) {
        JSONObject jsonObject = new JSONObject();
        try {
            if(type.equals("join")) {
                jsonObject.accumulate("id", userID);
                jsonObject.accumulate("password", pwd);
                jsonObject.accumulate("username", username);
            }
            else if(type.equals("joinAndCard")) {
                Log.d(TAG, "bank : " + bank + ", year : " + year + ", month : " + month + ", day : " + day);
                jsonObject.accumulate("id", userID);
                jsonObject.accumulate("password", pwd);
                jsonObject.accumulate("username", username);
                jsonObject.accumulate("bank", bank);
                jsonObject.accumulate("cardnum", cardNum);
                jsonObject.accumulate("cvc", cvc);
                jsonObject.accumulate("carddue", date);
                jsonObject.accumulate("cardpassword", cardPwd);
            }
            else if(type.equals("checkID")) {
                jsonObject.accumulate("id", userID);
            }
            else if(type.equals("checkUsername")) {
                jsonObject.accumulate("username", username);
            }
            return jsonObject;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*
    날짜 선택. month 값을 받아서.
     */
    private void setDayArray(String month) {
        switch (month) {
            case "1":
            case "3":
            case "5":
            case "7":
            case "8":
            case "10":
            case "12":
                dayArray.clear();
                for(int i = 1; i <= 31; i++) {
                    dayArray.add(String.valueOf(i));
                }
                break;
            case "4":
            case "6":
            case "9":
            case "11":
                dayArray.clear();
                for(int i = 1; i <= 30; i++) {
                    dayArray.add(String.valueOf(i));
                }
                break;
            case "2":
                dayArray.clear();
                for(int i = 1; i <= 28; i++) {
                    dayArray.add(String.valueOf(i));
                }
                break;
        }
    }

    /*
    AsyncTask 실행 여부 체크해서 종료시키기
     */
    private void cancelAsyncTask(JoinAsyncTask joinAsyncTask) {
        if(joinAsyncTask.getStatus() == AsyncTask.Status.RUNNING) {
            joinAsyncTask.cancel(true);
        }
    }
}
