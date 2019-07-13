package kr.oklabs.meal;

import android.annotation.SuppressLint;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

class MealLibrary {

    private static HostnameVerifier hostnameVerifier = new HostnameVerifier() {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };

    static String[] getDateNew(String CountryCode, String schulCode, String schulCrseScCode,
                               String schulKndScCode, String year, String month, String day) {

        String[] date = new String[7];
        String url = "https://stu." + CountryCode + "/sts_sci_md01_001.do?schulCode=" + schulCode + "&schulCrseScCode="
                + schulCrseScCode + "&schulKndScCode=" + schulKndScCode + "&schMmealScCode=" + "1"
                + "&schYmd=" + year + "." + month + "." + day;

        return getDateNewSub(date, url);
    }

    private static String[] getDateNewSub(String[] date, String url) {
        Source mSource = null;

        try {
            URL mUrl = new URL(url);

            InputStream mStream = null;

            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) mUrl.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setHostnameVerifier(hostnameVerifier);
                mStream = urlConnection.getInputStream();
                mSource = new Source(mStream);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mStream != null) {
                    mStream.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert mSource != null;
        mSource.fullSequentialParse();
        List<?> table = mSource.getAllElements("table");

        for (int i = 0; i < table.size(); i++) {
            if (((Element) table.get(i)).getAttributeValue("class").equals("tbl_type3")) {
                List<?> tr = ((Element) table.get(i)).getAllElements("tr");
                List<?> th = ((Element) tr.get(0)).getAllElements("th");

                for (int j = 0; j < 7; j++) {
                    date[j] = ((Element) th.get(j + 1)).getContent().toString();
                }

                break;
            }
        }

        return date;
    }

    static String[] getKcalNew(String CountryCode, String schulCode, String schulCrseScCode,
                               String schulKndScCode, String schMmealScCode, String year, String month, String day) {
        String[] content = new String[7];
        String url = "https://stu." + CountryCode + "/sts_sci_md01_001.do?schulCode=" + schulCode + "&schulCrseScCode="
                + schulCrseScCode + "&schulKndScCode=" + schulKndScCode + "&schMmealScCode=" + schMmealScCode
                + "&schYmd=" + year + "." + month + "." + day;

        return getKcalSubNew(content, url);
    }

    private static String[] getKcalSubNew(String[] content, String url) {
        Source mSource = null;

        try {
            URL mUrl = new URL(url);

            InputStream mStream = null;

            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) mUrl.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setHostnameVerifier(hostnameVerifier);
                mStream = urlConnection.getInputStream();
                mSource = new Source(mStream);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mStream != null) {
                    mStream.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert mSource != null;
        mSource.fullSequentialParse();
        List<?> table = mSource.getAllElements("table");

        work: for (int i = 0; i < table.size(); i++) {
            if (((Element) table.get(i)).getAttributeValue("class").equals("tbl_type3")) {
                List<?> tbody = ((Element) table.get(i)).getAllElements("tbody");
                List<?> __tr = ((Element) tbody.get(0)).getAllElements("tr");

                for (int index = 42; index < 46; index++) {
                    List<?> __th = ((Element) __tr.get(index)).getAllElements("th");

                    if (((Element) __th.get(0)).getContent().toString().equals("에너지(kcal)")) {
                        List<?> td = ((Element) __tr.get(index)).getAllElements("td");

                        for (int j = 0; j < td.size(); j++) {
                            content[j] = ((Element) td.get(j)).getContent().toString();
                        }

                        break work;
                    }
                }

                for (int index = 0; index < content.length; index++) {
                    content[index] = null;
                }

                break;
            }
        }

        return content;
    }

    static String[] getMealNew(String CountryCode, String schulCode, String schulCrseScCode,
                               String schulKndScCode, String schMmealScCode, String year, String month, String day) {

        String[] content = new String[7];
        String url = "https://stu." + CountryCode + "/sts_sci_md01_001.do?schulCode=" + schulCode + "&schulCrseScCode="
                + schulCrseScCode + "&schulKndScCode=" + schulKndScCode + "&schMmealScCode=" + schMmealScCode
                + "&schYmd=" + year + "." + month + "." + day;

        return getMealNewSub(content, url);
    }

    private static String[] getMealNewSub(String[] content, String url) {
        Source mSource = null;

        try {
            URL mUrl = new URL(url);

            InputStream mStream = null;

            try {
                HttpsURLConnection urlConnection = (HttpsURLConnection) mUrl.openConnection();
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setHostnameVerifier(hostnameVerifier);
                mStream = urlConnection.getInputStream();
                mSource = new Source(mStream);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mStream != null) {
                    mStream.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        assert mSource != null;
        mSource.fullSequentialParse();
        List<?> table = mSource.getAllElements("table");

        for (int i = 0; i < table.size(); i++) {
            if (((Element) table.get(i)).getAttributeValue("class").equals("tbl_type3")) {
                List<?> tbody = ((Element) table.get(i)).getAllElements("tbody");
                List<?> tr = ((Element) tbody.get(0)).getAllElements("tr");
                List<?> title = ((Element) tr.get(2)).getAllElements("th");

                if (((Element) title.get(0)).getContent().toString().equals("식재료")) {
                    List<?> tdMeal = ((Element) tr.get(1)).getAllElements("td");

                    for (int j = 0; j < 7; j++) {
                        content[j] = ((Element) tdMeal.get(j)).getContent().toString();
                        content[j] = content[j].replace("<br />", "\n");
                    }

                    break;
                }

                for (int index = 0; index < content.length; index++) {
                    content[index] = null;
                }

                break;
            }
        }

        return content;
    }

    static boolean isMealCheck(String meal) {
        return !("".equals(meal) || " ".equals(meal) || meal == null);
    }
}