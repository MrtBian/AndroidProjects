package ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.googlecode.tesseract.android.TessBaseAPI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;

import static com.github.barteksc.sample.PDFViewActivity.copyFilesFromRaw;

/**
 * Created by Wing on 2018/5/4.
 */

public class OCRTess {
    private TessBaseAPI mTess;
    public OCRTess(Context context){
        mTess = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory() + "";
        // String language = "num";
        String language = "eng";
        copyFilesFromRaw(context, "eng.traineddata", datapath + File.separator + "tesseract" + File.separator + "tessdata");
        mTess.init(datapath + "/tesseract/", language);
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
    }

    public OCRTess(Context context, String language){
        mTess = new TessBaseAPI();
        String datapath = Environment.getExternalStorageDirectory() + "";
        // String language = "num";
        copyFilesFromRaw(context, "eng.traineddata", datapath + File.separator + "tesseract" + File.separator + "tessdata");
        mTess.init(datapath + "/tesseract/", language);
        mTess.setPageSegMode(TessBaseAPI.PageSegMode.PSM_SPARSE_TEXT);
    }

    /**
     * 图片OCR识别
     * @param bmp
     * @return
     */
    public String defaultOCR(Bitmap bmp){
        if (mTess == null) {
            return "";
        }
        if (bmp.isRecycled()) {
            return "";
        }
        mTess.setImage(bmp);
        String result = mTess.getUTF8Text();
        mTess.clear();
//        mTess.end();
        return result;
    }

    /**
     * 图片指定位置OCR识别单词
     * @param bmp 图片
     * @param x 横坐标
     * @param y 纵坐标
     * @param bound 识别单词的边界
     * @return 识别到的单词
     */
    public String xyOCR(Bitmap bmp, float x, float y, int[] bound) {
        //设置要识别的图片
        if (mTess == null) {
            return "";
        }
        if (bmp.isRecycled()) {
            return "";
        }
        mTess.setImage(bmp);
        String result = mTess.getHOCRText(3);
        mTess.clear();
//        mTess.end();

        return findWord(result, x, y, bound);
    }

    /**
     *  从html中找到点击的单词
     * @param html
     * @param x
     * @param y
     * @param bound
     * @return
     */
    public String findWord(String html, float x, float y, int[] bound) {
        Document doc = Jsoup.parse(html);
        Elements elements = doc.select(".ocrx_word");
        for (Element element : elements) {
            String title = element.attr("title");
            String[] boxs = title.split("[ ;]");
            for (int i = 0; i < 4; i++) {
                bound[i] = Integer.parseInt(boxs[i + 1]);
            }
//            Log.v("Box",element.text()+" "+bound[0]+" "+bound[1]+" "+bound[2]+" "+bound[3]);
            if (bound[0] < x && bound[2] > x && bound[1] < y && bound[3] > y) {
                return element.text();
            }
        }
        return "";
    }
}
