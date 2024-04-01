package SublindWay_server.utility;

import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

public class ConnectionWithRealTimeServer {
    public static void main(String[] args) {
        String url = "http://swopenAPI.seoul.go.kr/api/subway/7471724567776f6f37306753556564/json/realtimeStationArrival/0/5/서울";
        WebClient webClient = WebClient.create();
        String responseBody = webClient.get()
                .uri(url)
                .header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        System.out.println(responseBody);
    }
}
