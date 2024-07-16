package com.earth_pointer.service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;

public class NewsService {

    private String clientId = "vqd6WxbQHRrgUpct8H1u"; // 애플리케이션 클라이언트 아이디
    private String clientSecret = "PfL0DLYKtz"; // 애플리케이션 클라이언트 시크릿

    // 뉴스 검색 메서드
    public void searchNews(String query) {
        String encodedQuery = encodeQuery(query);
        String apiURL = "https://openapi.naver.com/v1/search/news.json?query=" + encodedQuery;
        Map<String, String> requestHeaders = createRequestHeaders();

        String responseBody = get(apiURL, requestHeaders);
        parseAndPrintResponse(responseBody);
    }

    // 쿼리 문자열을 UTF-8로 인코딩
    private String encodeQuery(String query) {
        try {
            return URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("검색어 인코딩 실패", e);
        }
    }

    // 요청 헤더 생성
    private Map<String, String> createRequestHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Naver-Client-Id", clientId);
        headers.put("X-Naver-Client-Secret", clientSecret);
        return headers;
    }

    // API 호출
    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            return (responseCode == HttpURLConnection.HTTP_OK)
                    ? readBody(con.getInputStream())
                    : readBody(con.getErrorStream());
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    // 연결 설정
    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다: " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결 실패: " + apiUrl, e);
        }
    }

    // 응답 본문 읽기
    private String readBody(InputStream body) {
        StringBuilder responseBody = new StringBuilder();
        try (BufferedReader lineReader = new BufferedReader(new InputStreamReader(body))) {
            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }
        } catch (IOException e) {
            throw new RuntimeException("API 응답 읽기 실패", e);
        }
        return responseBody.toString();
    }

    // 응답 파싱 및 출력
    private void parseAndPrintResponse(String responseBody) {
        JSONObject jsonResponse = new JSONObject(responseBody);
        JSONArray items = jsonResponse.getJSONArray("items");

        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String title = item.getString("title").replace("<b>", "").replace("</b>", ""); // <b> 태그 제거
            String description = item.getString("description").replace("<b>", "").replace("</b>", ""); // <b> 태그 제거

            System.out.println("제목: " + title);
            System.out.println("내용: " + description);
            System.out.println();
        }
    }
}
