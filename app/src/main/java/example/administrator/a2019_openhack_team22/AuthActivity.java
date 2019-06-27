package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.InputStream;

public class AuthActivity extends AppCompatActivity {

    ImageView imgShow;
    Button btnAddImg, btnWrite;
    EditText txtShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        imgShow = (ImageView) findViewById(R.id.imgShow);
        btnAddImg = (Button) findViewById(R.id.btnAddImg);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        txtShow = (EditText) findViewById(R.id.txtShow);

        // 버튼 클릭 시 카메라 앨범으로 이동
        btnAddImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);

            }
        });

        // 액티비티 종료하고 이전으로 돌아가기
        Button btnWrite = (Button) findViewById(R.id.btnWrite);
        btnWrite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: 입력 정보 (인증 사진 및 코멘트) 저장하여 표시
                finish();    //액티비티 종료.
            }
        });
    }

    // 이미지를 비트맵 형식으로 받아와 이미지뷰에 표시
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imgShow.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
