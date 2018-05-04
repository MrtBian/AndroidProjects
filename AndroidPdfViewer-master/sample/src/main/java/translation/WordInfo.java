package translation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
/**
 * 单词信息类
 * Created by Wing on 2017/8/25.
 */

public class WordInfo {
    private String url = "http://dict.cn/"; // 查词使用的api
    private String word;
    private String explanation;
    private String pron_uk;
    private String pron_us;
    private String psymbol_uk;
    private String psymbol_us;
    private String sentences;
    public WordInfo(String w){
        word = w;
        getWordInfo();
    }
    public WordInfo(){

    }

    @Override
    protected void finalize () throws Throwable {
        super.finalize();
    }

    /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
    @Override
    public String toString() {
        return "WordInfo [word=" + word + "\\r\\n, explanation=" + explanation + "\\r\\n, pron_uk=" + pron_uk
                + "\\r\\n, pron_us=" + pron_us + "\\r\\n, psymbol_uk=" + psymbol_uk + "\\r\\n, psymbol_us=" + psymbol_us
                + "\\r\\n, sentences=" + sentences + "]";
    }
    public String getWord() {
        return word;
    }
    public void setWord(String word) {
        this.word = word;
    }
    public String getExplanation() {
        String temp=explanation.replaceAll("<br>","\n");
        if(temp.length()>1){
        temp=temp.substring(0,temp.length()-1);}
        return temp;
    }
    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
    public String getPron_uk() {/*
        int index = pron_uk.indexOf("?");
        return pron_uk.substring(0,index);*/
        return pron_uk;
    }
    public void setPron_uk(String pron_uk) {
        this.pron_uk = pron_uk;
    }
    public String getPron_us() {/*
        int index = pron_us.indexOf("?");
        return pron_us.substring(0,index);*/
        return pron_us;
    }
    public void setPron_us(String pron_us) {
        this.pron_us = pron_us;
    }
    public String getPsymbol_uk() {
        return psymbol_uk;
    }
    public void setPsymbol_uk(String psymbol_uk) {
        this.psymbol_uk = psymbol_uk;
    }
    public String getPsymbol_us() {
        return psymbol_us;
    }
    public void setPsymbol_us(String psymbol_us) {
        this.psymbol_us = psymbol_us;
    }
    public String getSentences() {
        String temp = sentences.replaceAll("<br>","\n");
        temp=temp.substring(0,temp.length()-1);
        return temp;
    }
    public void setSentences(String sentences) {
        this.sentences = sentences;
    }


    /**
     * 爬虫主体 通过word获得word的全部信息
     * @return
     */
    public void getWordInfo() {
        // System.out.println(word);
        explanation="";
        pron_uk="";
        pron_us="";
        psymbol_uk="";
        psymbol_us="";
        sentences="";
        String wordurl = url + word;
        try {
            Document doc = Jsoup.connect(wordurl).get();
            Element content = doc.getElementsByClass("word").first();
            if (content == null) {
                return ;// 单词不是合法英文单词
            }
            Elements bdos = content.getElementsByTag("bdo");
            Elements sounds = content.getElementsByClass("fsound");
            int flag = 0;

            for (Element bdo : bdos) {
                if (flag == 0) {
                    psymbol_uk=bdo.text();
                } else {
                    psymbol_us=bdo.text();
                }
                flag = 1;
                // System.out.println(bdo.text());
            }
            flag = 0;
            for (Element sound : sounds) {
                if (flag == 0) {
                    pron_uk="http://audio.dict.cn/" + sound.attr("naudio");
                } else {
                    pron_us="http://audio.dict.cn/" + sound.attr("naudio");
                }
                flag = 1;
                // System.out.println("audio.dict.cn/" + sound.attr("naudio"));
            }
            Element explanation = content.getElementsByClass("basic clearfix").first();
            if (explanation == null) {
                return ;// 单词不是合法英文单词
            }
            Elements lis = explanation.getElementsByTag("li");
            String temp = "";
            for (Element li : lis) {
                if (li.text().length() > 1)
                    temp += li.text() + "<br>";
            }
            this.explanation=temp;

            Element sort = doc.getElementsByClass("layout sort").first();
            if (sort == null) {
                sentences="";
            } else {
                lis = sort.getElementsByTag("li");
                String sens = "";
                for (Element li : lis) {
                    sens += li.html() + "<br>";
                }
                sentences=sens;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.err.println("IOException occurs");
            e.printStackTrace();
        }
    }

}
