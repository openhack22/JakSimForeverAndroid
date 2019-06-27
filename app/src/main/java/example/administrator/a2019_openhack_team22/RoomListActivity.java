package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;


public class RoomListActivity extends AppCompatActivity {

    Button btnAddNew1, btnAddNew2, btnAddNew3;  // 목표를 추가하는 다이얼로그 창 띄우기 위한 버튼
    Button btnSearch, btn1, btn2, btn3, btn4, btn5, btn6, btn7; // 검색할 때 쓰일 버튼들
    EditText editSearch;    // 검색

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);

        // 위젯 연결
        btnAddNew1 = (Button) findViewById(R.id.btnAddNew1);
        btnAddNew2 = (Button) findViewById(R.id.btnAddNew2);
        btnAddNew3 = (Button) findViewById(R.id.btnAddNew3);
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn5 = (Button) findViewById(R.id.btn5);
        btn6 = (Button) findViewById(R.id.btn6);
        btn7 = (Button) findViewById(R.id.btn7);
        editSearch = (EditText) findViewById(R.id.autoCompleteTextView1);


        // 탭호스트 설정--------------------------------------------------
        TabHost tabHost1 = (TabHost) findViewById(R.id.tabHost1) ;
        tabHost1.setup() ;

        // 첫 번째 Tab. (탭 표시 텍스트:"7일"), (페이지 뷰:"content1")
        TabHost.TabSpec ts1 = tabHost1.newTabSpec("Tab Spec 1") ;
        ts1.setContent(R.id.content1) ;
        ts1.setIndicator("7일") ;
        tabHost1.addTab(ts1)  ;

        // 두 번째 Tab. (탭 표시 텍스트:"1개월"), (페이지 뷰:"content2")
        TabHost.TabSpec ts2 = tabHost1.newTabSpec("Tab Spec 2") ;
        ts2.setContent(R.id.content2) ;
        ts2.setIndicator("1개월") ;
        tabHost1.addTab(ts2) ;

        // 세 번째 Tab. (탭 표시 텍스트:"3개월"), (페이지 뷰:"content3")
        TabHost.TabSpec ts3 = tabHost1.newTabSpec("Tab Spec 3") ;
        ts3.setContent(R.id.content3) ;
        ts3.setIndicator("3개월") ;
        tabHost1.addTab(ts3) ;

        //----------------------------------------------------------------

        /* 검색할 때 자동완성 기능을 포함하며 일주일(한달) 탭에서만 구현 */

        // 검색 기능 - 일주일
        String[] items1 = { "식단관리 고고", "일주일만에 5kg 뺄사람?", "벼락치기하고 A+맞자", "중국어 하루 30분",
                "마인드 컨트롤 집중력", "일주일 식단", "A+맞자 공부인증해!" };

        AutoCompleteTextView auto1 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, items1);
        auto1.setAdapter(adapter1);

//        // 검색 기능 - 한달
//        String[] items2 = { "토익 900점 넘자", "영어 하루 딱 한시간", "매일 꾸준히 운동할사람", "중국어 하루 30분",
//                "토익공부 인증!!", "밀가루 안먹기.. 식단인증", "토익 대비 단어 암기", "한달만에 몸만들기" };
//
//        AutoCompleteTextView auto2 = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView2);
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
//                android.R.layout.simple_dropdown_item_1line, items2);
//        auto2.setAdapter(adapter2);

        // 검색 버튼 눌렀을 때 일치하는 버튼만 보이도록 함
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (editSearch.getText().toString() == btn1.getText().toString()) {
                    btn1.setVisibility(View.VISIBLE);
                }
                else {
                    btn1.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn2.getText().toString()) {
                    btn2.setVisibility(View.VISIBLE);
                }
                else {
                    btn2.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn3.getText().toString()) {
                    btn3.setVisibility(View.VISIBLE);
                }
                else {
                    btn3.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn4.getText().toString()) {
                    btn4.setVisibility(View.VISIBLE);
                }
                else {
                    btn4.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn5.getText().toString()) {
                    btn5.setVisibility(View.VISIBLE);
                }
                else {
                    btn5.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn6.getText().toString()) {
                    btn6.setVisibility(View.VISIBLE);
                }
                else {
                    btn6.setVisibility(View.GONE);
                }

                if (editSearch.getText().toString() == btn7.getText().toString()) {
                    btn7.setVisibility(View.VISIBLE);
                }
                else {
                    btn7.setVisibility(View.GONE);
                }
            }
        });

        // 목표 추가로 화면 전환
        btnAddNew1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this, AddRoomActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력
            }
        });

        btnAddNew2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this, AddRoomActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력

            }
        });

        btnAddNew3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this, AddRoomActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력

            }
        });

    }
}
