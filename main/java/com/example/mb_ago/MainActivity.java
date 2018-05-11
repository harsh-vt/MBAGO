package com.example.mb_ago;


        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.OutputStream;
        import java.util.ArrayList;
        import java.util.List;
        import android.content.Intent;
        import org.opencv.android.BaseLoaderCallback;
        import org.opencv.android.CameraBridgeViewBase;
        import org.opencv.android.LoaderCallbackInterface;
        import org.opencv.android.OpenCVLoader;
        import org.opencv.android.Utils;
        import org.opencv.core.Mat;
        import org.opencv.core.Point;
        import org.opencv.core.Scalar;
        import org.opencv.core.Size;
        import org.opencv.imgcodecs.Imgcodecs;
        import org.opencv.imgproc.Imgproc;
        import android.app.Activity;
        import android.content.Context;
        import android.graphics.Bitmap;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Environment;
        import android.provider.MediaStore;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.Button;
        import android.widget.Toast;

        import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_COLOR;
        import static org.opencv.imgcodecs.Imgcodecs.IMREAD_COLOR;
        import static org.opencv.imgcodecs.Imgcodecs.imread;

public class MainActivity extends Activity {
    @SuppressWarnings("unused")
    private CameraBridgeViewBase mOpenCvCameraView;

    private BaseLoaderCallback mload=new BaseLoaderCallback(this){
        @SuppressWarnings("unused")
        public void onManagerConnectd(int status)
        {
            switch(status)
            {
                case LoaderCallbackInterface.SUCCESS:
                {
                    Log.i("Load", "OpenCV loaded successfully");
//mOpenCvCameraView.enableView();
                }break;
                default:
                {
                    super.onManagerConnected(status);
                }break;
            }
        }
    };

    @Override
    public void onResume()
    {
        super.onResume();
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_3_0, this, mload);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("dumbhash" , "74");

        final Context c=this;
        Log.i("dumbhash" , "STARTED APP");
        Button sButton = (Button) findViewById(R.id.button1);
        sButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i("dumbhash" , "inside button 1");
                Intent in=new Intent("android.media.action.IMAGE_CAPTURE");
                Toast t=Toast.makeText(c , "Capture Reference sheet", Toast.LENGTH_SHORT);
                t.show();
                File imf=new File(Environment.getExternalStorageDirectory(),"/MBAGO");
                imf.mkdirs();
                File img=new File(imf,"ref.jpg");
                boolean suc=deleteDir(img);
                System.out.println(suc);
                Uri si=Uri.fromFile(img);
                in.putExtra(MediaStore.EXTRA_OUTPUT, si);
                startActivityForResult(in,RESULT_OK);
                Log.i("dumbhash" , "92");
                Toast t1=Toast.makeText(c , "Saved at"+imf.getParent(), Toast.LENGTH_SHORT);
                t1.show();

                Log.i("dumbhash" , "1st photo taken");
            }
        });
        Button lButton = (Button) findViewById(R.id.button2);
        lButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Log.i("dumbhash" , "inside button 2");
                Intent in=new Intent("android.media.action.IMAGE_CAPTURE");
                Toast t=Toast.makeText(c , "Capture Student sheet", Toast.LENGTH_SHORT);
                t.show();
                File imf=new File(Environment.getExternalStorageDirectory(),"/MBAGO");
                imf.mkdirs();
                File img=new File(imf,"stu.jpg");
                boolean suc=deleteDir(img);
                System.out.println(suc);
                Uri si=Uri.fromFile(img);
                in.putExtra(MediaStore.EXTRA_OUTPUT, si);
                startActivityForResult(in,RESULT_OK);

                Log.i("dumbhash" , "2nd picture taken");

            }
        });
        Button rButton = (Button) findViewById(R.id.button3);
        rButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Log.i("dumbhash" , "inside button 3");

                int count=0;
                List<Long> x1=new ArrayList<Long>();
                List<Long> y1=new ArrayList<Long>();
                List<Long> x2=new ArrayList<Long>();
                List<Long> y2=new ArrayList<Long>();
                File root = Environment.getExternalStorageDirectory();
                Log.i("dumbhash" , "loading photo");

                Mat gry=new Mat();


                Mat ref;
                Log.i("dumbhash" , "141");
                ref= imread("/storage/emulated/0/MBAGO/ref.jpg",CV_LOAD_IMAGE_COLOR);

                Log.i("dumbhash" , "144");
                Imgproc.cvtColor(ref, gry, Imgproc.COLOR_BGR2GRAY);
                Log.i("dumbhash" , "146");
                Mat th=new Mat();
                Mat cir=new Mat();
                Log.i("dumbhash" , "149");
                try{
                        Imgproc.threshold(gry, th, 67, 200, Imgproc.THRESH_BINARY_INV);
                    Imgproc.GaussianBlur(th, th, new Size(51,51), 0);
                    Imgproc.HoughCircles(th, cir, Imgproc.CV_HOUGH_GRADIENT, 1, ref.rows()/17&ref.cols()/17,70,12,20,27);
                        int radius;
                        Point pt=new Point();
                        for(int x=0;x<cir.cols();x++)
                        {
                            double vCir[]=cir.get(0, x);
                            if(vCir==null)
                                break;
                            pt=new Point(Math.round(vCir[0]),Math.round(vCir[1]));
                            radius=(int)Math.round(vCir[2]);
                            Imgproc.circle(ref, pt, radius, new Scalar(0,0,255),1);
                            count=count+1;
                            Imgproc.circle(ref, pt, 4, new Scalar(0,255,255),1);
                        }
                    Log.i("dumbhash" , "170");
                } catch(Exception e) {}
                finally
                {
                    Imgcodecs.imwrite(root+"/MBAGO/out1.jpg",ref);
                    Imgcodecs.imwrite(root+"/MBAGO/thes.jpg",th);
                    Log.i("dumbhash" , "176");
                    for(int j=0;j<count;j++)
                    {
                        double t[]=cir.get(0, j);
                        x1.add(Math.round(t[0]));
                        y1.add(Math.round(t[1]));
                    }
                }
                Log.i("dumbhash" , "184");
                Mat stu = imread(root+"/MBAGO/stu.jpg", Imgcodecs.CV_LOAD_IMAGE_COLOR);
                Log.i("dumbhash" , "186");
                Imgproc.cvtColor(stu, gry, Imgproc.COLOR_BGR2GRAY);
                count=0;
                Log.i("dumbhash" , "189");
                try{
                    Imgproc.threshold(gry, th, 67, 200, Imgproc.THRESH_BINARY_INV);
                    Imgproc.GaussianBlur(th, th, new Size(51,51), 0);;
                    Imgproc.HoughCircles(th, cir, Imgproc.CV_HOUGH_GRADIENT, 1, ref.rows()/17&ref.cols()/17,70,12,20,27);int radius;
                    Point pt=new Point();
                    for(int x=0;x<cir.cols();x++)
                    {
                        double vCir[]=cir.get(0, x);
                        if(vCir==null)
                            break;
                        pt=new Point(Math.round(vCir[0]),Math.round(vCir[1]));
                        radius=(int)Math.round(vCir[2]);
                        Imgproc.circle(stu, pt, radius, new Scalar(0,0,255),1);
                        count=count+1;
                        Imgproc.circle(stu, pt, 4, new Scalar(0,255,255),1);
                    }
                    Log.i("dumbhash" , "207");
                }
                catch(Exception e) {}
                finally
                {
                    Imgcodecs.imwrite(root +"/MBAGO/stu1.jpg",stu);
                    Imgcodecs.imwrite(root +"/MBAGO/thes2.jpg",th);
                    for(int j=0;j<count;j++)
                    {
                        double t[]=cir.get(0, j);
                        x2.add(Math.round(t[0]));
                        y2.add(Math.round(t[1]));
                    }
                    Log.i("dumbhash" , "220");
                }
                int marks=0;
                Log.i("dumbhash" , "223");
                if(x1.size()>=10)
                {
                    Log.i("dumbhash" , "225");
                    for (int c=0;c<x1.size();c++)
                    {
                        for(int d=0;d<x2.size();d++)
                        {
                            long a1=x2.get(d);
                            long b1=y2.get(d);
                            long a2=x1.get(c);
                            long b2=y1.get(c);
                            long b3= b1-b2;
                            long a3= a1-a2;
                            long dis2=Math.abs(b3);
                            long dis=Math.abs(a3);
                            if(dis<=10&&dis2<=10)
                            {
                                marks++;
                            }
                        }
                    }
                    Log.i("dumbhash" , "239");
                    Toast tk=Toast.makeText(c , "Marks "+marks, Toast.LENGTH_LONG);
                    Log.i("dumbhash" , "241");
                    tk.show();
                    Log.i("dumbhash" , "243");
                    Intent intent = new Intent(c,Result.class);
                    Log.i("dumbhash" , "245");
                    intent.putExtra("marks", marks);
                    intent.putExtra("total",count);
                    Log.i("dumbhash" , "247");
                    c.startActivity(intent);
                    Log.i("dumbhash" , "249");
                    Log.i("dumbhash" , "250");
                }
                else
                {
                    Log.i("dumbhash" , "253");
                    Toast tk=Toast.makeText(c , "Reference sheet Error", Toast.LENGTH_LONG);
                    tk.show();}

            } });
        Log.i("dumbhash" , "250");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle action bar item clicks here. The action bar will
// automatically handle clicks on the Home/Up button, so long
// as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public static boolean deleteDir(File imf){
        if(imf.isDirectory())
        {
            String[] chi=imf.list();
            for(int i=0;i<chi.length;i++)
            {
                boolean suc=deleteDir(new File(imf,chi[i]));

                if(!suc){
                    return false;
                }
            }
        }
        return imf.delete();
    }
}
