package example.hp.com.myclient.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

import example.hp.com.myclient.QRcode.MipcaActivityCapture;
import example.hp.com.myclient.R;
import example.hp.com.myclient.Tools.MyApplication;

/**
 * Created by hp on 2015/10/21.
 */
public class ErweimaFragment extends Fragment {
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_erweima, container, false);
//        return view;
//    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onActivityCreated(savedInstanceState);
//    }
    private final static int SCANNIN_GREQUEST_CODE = 1;
    private TextView mTextView ;
    private ImageView mImageView;
    private EditText editText;
    private Button button;
    private int QR_WIDTH = 200, QR_HEIGHT = 200;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_erweima,container,false);
        mTextView = (TextView) view.findViewById(R.id.result);
        mImageView = (ImageView) view.findViewById(R.id.qrcode_bitmap);
        editText = (EditText) view.findViewById(R.id.edit_text);
        button = (Button) view.findViewById(R.id.button2);
        Button mButton = (Button) view.findViewById(R.id.button1);
        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MyApplication.getContext(), MipcaActivityCapture.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString();
                try
                {
                    if (url == null || "".equals(url) || url.length() < 1)
                    {
                        return;
                    }
                    Hashtable<EncodeHintType, String> hints = new Hashtable<>();
                    hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
                    //图像数据转换，使用了矩阵转换
                    BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
                    int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
                    //下面这里按照二维码的算法，逐个生成二维码的图片，
                    //两个for循环是图片横列扫描的结果
                    for (int y = 0; y < QR_HEIGHT; y++)
                    {
                        for (int x = 0; x < QR_WIDTH; x++)
                        {
                            if (bitMatrix.get(x, y))
                            {
                                pixels[y * QR_WIDTH + x] = 0xff000000;
                            }
                            else
                            {
                                pixels[y * QR_WIDTH + x] = 0xffffffff;
                            }
                        }
                    }
                    //生成二维码图片的格式，使用ARGB_8888
                    Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
                    bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
                    //显示到一个ImageView上面
                    mImageView.setImageBitmap(bitmap);
                }
                catch (WriterException e)
                {
                    e.printStackTrace();
                }
            }
        });
        return view;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SCANNIN_GREQUEST_CODE:
                if(resultCode == -1) {
                    Bundle bundle = data.getExtras();
                    mTextView.setText(bundle.getString("result"));
                }
                break;
        }
    }
}