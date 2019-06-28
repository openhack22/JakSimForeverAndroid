package example.administrator.a2019_openhack_team22;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AuthActivity extends AppCompatActivity {

    ImageView imgShow, btnAddImg, btnWrite;
    EditText txtShow;
    byte[] byteArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        imgShow = (ImageView) findViewById(R.id.showImg);
        btnAddImg = (ImageView) findViewById(R.id.btnAddImg);
        btnWrite = (ImageView) findViewById(R.id.btnWrite);
        txtShow = (EditText) findViewById(R.id.txtShow);

        // 버튼 클릭 시 카메라 앨범으로 이동
        btnAddImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra("outputX", 100);
                        intent.putExtra("outputY", 100);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, 1);
                        break;
                }
                return true;
            }
        });


        btnWrite.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:   // 클릭 시
                        // TODO: 입력 정보 (인증 사진 및 코멘트) 저장하여 표시
                        Intent outIntent = new Intent(getApplicationContext(), RoomActivity.class);
                        outIntent.putExtra("image", byteArray);
                        setResult(RESULT_OK, outIntent);  //setResult()로 메인 액티비티의 onActivityResult() 메소드 실행
                        finish();    //액티비티 종료.
                        break;
                }
                return true;
            }
        });



//        // 원 모양으로 이미지 버튼에 그림자 넣기
//        Outline circularOutline = new Outline();
//        circularOutline.setOval(0, 0, 4, 4);
//        btnAddImg.setOutline(circularOutline);
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

                    // 비트맵 형식의 이미지를 Byte Array로 변경하여 인텐트 담아 전송
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    img.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byteArray = stream.toByteArray();



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
