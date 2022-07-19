package com.web.pocketmoney.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@Log4j2
public class KakaoApiService {

    public String getAccessToken(String code) {
        String accessToken="";
        String refreshToken="";
        String reqUrl = "https://kauth.kakao.com/oauth/token";
        log.info("getAccessToken에 들어옴");
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            log.info(1);

            // post요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            log.info(2);
            //post요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();
            sb.append("grant_type=authorization_code");
            sb.append("&client_id=9b022ce48b033d5d885cb824be69e623");
            sb.append("&redirect_uri=http://localhost:8080/login/kakao");
            sb.append("&code="+code);
            bw.write(sb.toString());
            log.info(bw.toString());
            bw.flush();
            log.info(3 + " " + sb.toString());
            // 결과코드가 200이면 성공
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            log.info("3.1");
            String line = "";
            String result = "";
            log.info(4);
            while ((line = br.readLine()) != null) {
                log.info("line : " + line);
                result += line;
            }
            System.out.println("response body : " + result);

            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            accessToken = element.getAsJsonObject().get("access_token").getAsString();
            refreshToken = element.getAsJsonObject().get("refresh_token").getAsString();

            log.info("access_token : " + accessToken);
            log.info("refresh_token : " + refreshToken);

            br.close();
            bw.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return accessToken;
    }
}
