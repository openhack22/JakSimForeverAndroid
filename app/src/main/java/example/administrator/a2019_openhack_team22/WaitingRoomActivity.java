package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * // TODO: 아직 이 엑티비티로 넘어오는 코드 안 짰음!! -> 추가함
 * ListActivity -> WaitingRoomActivity
 * 목표의 상세정보를 나타내는 액티비티
 */
public class WaitingRoomActivity extends AppCompatActivity {

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

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_room);

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
                // TODO: 서버 통신 부분 넣어야 함
                Toast.makeText(WaitingRoomActivity.this, "참여하기 버튼 누름!", Toast.LENGTH_SHORT).show();
            }
        });

        // 방 시작 화면으로 전환
        btnJoin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(WaitingRoomActivity.this, RoomActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력
            }
        });
    }
}
