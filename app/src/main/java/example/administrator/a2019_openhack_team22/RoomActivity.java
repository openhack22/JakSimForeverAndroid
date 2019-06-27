package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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

//    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);

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

        // 인증하기 버튼 클릭했을 때 화면 전환
        btnAuth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RoomActivity.this, AuthActivity.class);
                startActivity(intent);	//새로운 액티비티를 화면에 출력
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
}
