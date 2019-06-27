package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;


public class RoomListActivity extends AppCompatActivity {

    Button btnAddNew1, btnAddNew2, btnAddNew3;  // 목표를 추가하는 다이얼로그 창 띄우기 위한 버튼
    EditText editSearch1, editSearch2, editSearch3;    // 검색
    ListView listView1, listView2, listView3=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);

        ListViewAdapter adapter1, adapter2, adapter3;

        // Adapter 생성
        adapter1 = new ListViewAdapter() ;
        adapter2 = new ListViewAdapter() ;
        adapter3 = new ListViewAdapter() ;

        // 리스트뷰 참조 및 Adapter달기
        listView1 = (ListView) findViewById(R.id.listview1);
        listView1.setAdapter(adapter1);

        listView2 = (ListView) findViewById(R.id.listview2);
        listView2.setAdapter(adapter2);

        listView3 = (ListView) findViewById(R.id.listview3);
        listView3.setAdapter(adapter3);

        // 위젯 연결
        btnAddNew1 = (Button) findViewById(R.id.btnAddNew1);
        btnAddNew2 = (Button) findViewById(R.id.btnAddNew2);
        btnAddNew3 = (Button) findViewById(R.id.btnAddNew3);
        editSearch1 = (EditText) findViewById(R.id.autoCompleteTextView1);
        editSearch2 = (EditText) findViewById(R.id.autoCompleteTextView2);
        editSearch3 = (EditText) findViewById(R.id.autoCompleteTextView3);


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

        // 검색 기능 및 리스트뷰 구현 - 7일
        adapter1.addItem("식단관리 고고", "뱃살공듀") ;
        adapter1.addItem("일주일만에 5kg 뺄사람?", "디톡스") ;
        adapter1.addItem("벼락치기하고 A+맞자", "교수님의귀요미") ;
        adapter1.addItem("중국어 하루 30분", "레드") ;
        adapter1.addItem("마인드 컨트롤 집중력", "명상최고") ;
        adapter1.addItem("일주일 식단", "손오공") ;
        adapter1.addItem("A+맞자 공부인증해!", "과제중") ;

        editSearch1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView1.setFilterText(filterText) ;
                } else {
                    listView1.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;

        // 검색 기능 및 리스트뷰 구현 - 1개월
        adapter2.addItem("토익 900점 넘자", "유학생") ;
        adapter2.addItem("영어 하루 딱 한시간", "공부하자") ;
        adapter2.addItem("토익공부 인증!!", "명상최고") ;
        adapter2.addItem("밀가루 안먹기.. 식단인증", "라면최고") ;
        adapter2.addItem("토익 대비 단어 암기", "whffu") ;
        adapter2.addItem("한달만에 몸만들기", "으쌰으쌰") ;

        editSearch2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView2.setFilterText(filterText) ;
                } else {
                    listView2.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;


        // 검색 기능 및 리스트뷰 구현 - 3개월
        adapter3.addItem("HTML마스터하기", "컴맹") ;
        adapter3.addItem("3개월간 10kg 빼자!!", "취미는자전거타기") ;
        adapter3.addItem("봉사활동 인증(주1회)", "집돌이") ;

        editSearch3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String filterText = edit.toString() ;
                if (filterText.length() > 0) {
                    listView3.setFilterText(filterText) ;
                } else {
                    listView3.clearTextFilter() ;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        }) ;



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
