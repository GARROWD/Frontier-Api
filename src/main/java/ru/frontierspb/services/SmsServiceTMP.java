package ru.frontierspb.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

@Service
@Slf4j
public class SmsServiceTMP {
    @Value("${smsService.username}")
    private String username;
    @Value("${smsService.password}")
    private String password;
    private final String charset = "utf-8";

    public void sendCode(String phoneNumber, String code) {
        log.info("Код подтверждения: {}", code);
        //sendSms(phoneNumber, "Код подтверждения: " + code, 0, "", "", 0, "Фронтир", "");
    }

    public String[] sendSms(
            String phones, String message, int translit, String time,
            String id, int format, String sender, String query) {
        String[] formats = {"", "flash=1", "push=1", "hlr=1", "bin=1", "bin=2", "ping=1", "mms=1", "mail=1", "call=1"
                , "viber=1", "soc=1"};
        String[] m = {};

        try {
            m = sendCmd("send", "cost=3&phones=" + URLEncoder.encode(phones, charset)
                                + "&mes=" + URLEncoder.encode(message, charset)
                                + "&translit=" + translit + "&id=" + id + (format > 0 ? "&" + formats[format] : "")
                                + (sender.equals("") ? "" : "&sender=" + URLEncoder.encode(sender, charset))
                                + (time.equals("") ? "" : "&time=" + URLEncoder.encode(time, charset))
                                + (query.equals("") ? "" : "&" + query));
        } catch(UnsupportedEncodingException e) {
            log.info("Specified character encoding is not supported");
        }

        return m;
    }

    private String[] sendCmd(String cmd, String arg) {
        String ret = ",";

        try {
            String url = "http" + "://smsc.ru/sys/" + cmd + ".php?login=" + URLEncoder.encode(
                    username, charset)
                         + "&psw=" + URLEncoder.encode(password, charset)
                         + "&fmt=1&charset=" + charset + "&" + arg;

            String newUrl = url;
            int i = 0;
            do {
                if(i++ > 0) {
                    newUrl = url;
                    newUrl = newUrl.replace("://smsc.ru/", "://www" + (i) + ".smsc.ru/");
                }
                ret = readUrl(newUrl);
            } while(ret.equals("") && i < 5);
        } catch(UnsupportedEncodingException e) {
            log.info("Specified character encoding is not supported");
        }

        return ret.split(",");
    }

    private String readUrl(String url) {
        String line = "", realUrl = url;
        String[] param = {};
        boolean isPost = url.length() > 2000;

        if(isPost) {
            param = url.split("\\?", 2);
            realUrl = param[0];
        }

        try {
            URL newUrl = new URL(realUrl);
            InputStream inputStream;

            if(isPost) {
                URLConnection urlConnection = newUrl.openConnection();
                urlConnection.setDoOutput(true);
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream(),
                                                                               charset);
                outputStreamWriter.write(param[1]);
                outputStreamWriter.flush();
                outputStreamWriter.close();
                inputStream = urlConnection.getInputStream();
            }
            else {
                inputStream = newUrl.openStream();
            }

            InputStreamReader reader = new InputStreamReader(inputStream, charset);

            int ch;
            while((ch = reader.read()) != - 1) {
                line += (char) ch;
            }

            reader.close();
        } catch(MalformedURLException e) {
            log.info("Error processing URL");
        } catch(IOException e) {
            log.info("Error during data transmission/reception operation");
        }

        return line;
    }
}


