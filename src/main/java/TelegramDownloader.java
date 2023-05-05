import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utlis.DownLoadUtil;
import utlis.MultiThreadUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author BeamStark
 * 多线程自动下载telegram图集
 * @date 2023-04-20-02:29
 */
@Slf4j
public class TelegramDownloader {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        //这里放入图片连接，可以放入多个同时下载
        list.add("");
        list.add("");
        list.add("");
        domain(list);
    }

    private static String getFileName(String url) {
        return url.replace(getBaseUrl(url), "");
    }

    private static String getBaseUrl(String url) {
        String[] parts = url.split("/");
        return "https://" + parts[2] + "/";
    }

    @SneakyThrows
    private static void domain(List<String> list) {
        List<Runnable> tasks = new ArrayList<>();
        for (String url : list) {
            tasks.add(() -> dodoMain(url));
        }
        MultiThreadUtil.execute(tasks);
    }

    @SneakyThrows
    private static void dodoMain(String url) {
        Date sdate = new Date();
        String fileName = getFileName(url);
        log.info("开始下载【{}】...", fileName);
        Connection connect = Jsoup.connect(url).timeout(50000);
        connect.ignoreContentType(true)
                .ignoreHttpErrors(true);
        Document document = connect.get();
        int index = 0;
        for (Element element : document.getElementsByTag("img")) {
            String str = fileName + "/" + fileName + "-" + index;
            String src = element.attributes().get("src");
            DownLoadUtil.downloadImages(getBaseUrl(url) + src,
                    str);
            index++;
        }
        Date edate = new Date();
        log.info("【{}】下载完毕，用时{}秒，共{}张图片", fileName,
                (edate.getTime() - sdate.getTime()) / 1000, index);

    }

}
