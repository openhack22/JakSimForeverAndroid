package example.administrator.a2019_openhack_team22;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

        btnAuth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: AuthActivity로 인텐트 넘기는거 필요!
                Toast.makeText(RoomActivity.this, "인증 버튼 눌림!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
