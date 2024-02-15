package com.kuit.chatdiary.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kuit.chatdiary.domain.Member;
import com.kuit.chatdiary.repository.MemberRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;

@Slf4j
@Service
public class LogInService {

    @Autowired
    private final MemberRepository memberRepository;

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    public LogInService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // AccessToken 발급하는 메서드
    public String getAccessToken(String code) {
        log.info("[KakaoService.getAccessToken]");

        String accessToken = "";
        String reqUrl = "https://kauth.kakao.com/oauth/token";

        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder sb = new StringBuilder();

            sb.append("grant_type=authorization_code");
            sb.append("&client_id=").append(kakaoApiKey);
            sb.append("&redirect_uri=").append(kakaoRedirectUri);
            sb.append("&code=").append(code);

            bw.write(sb.toString());
            bw.flush();

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            System.out.println("responseBody : "+result);

            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            accessToken = element.getAsJsonObject().get("access_token").getAsString();

            br.close();
            bw.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return accessToken;
    }

    // 사용자 정보 가져오는 메서드
    public HashMap<String, Object> getUserInfo(String accessToken) {
        HashMap<String, Object> userInfo = new HashMap<>();
        String reqUrl = "https://kapi.kakao.com/v2/user/me";
        try{
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            BufferedReader br;
            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String line = "";
            StringBuilder responseSb = new StringBuilder();
            while((line = br.readLine()) != null){
                responseSb.append(line);
            }
            String result = responseSb.toString();
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);

            JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            userInfo.put("nickname", nickname);

            br.close();

        }catch (Exception e){
            e.printStackTrace();
        }
        return userInfo;
    }

    // jwt 발급하는 메서드
    public String generateJwt(String nickname, long expirationTime){

        SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);

        return Jwts.builder()
                .setSubject(nickname)
                .setExpiration(expirationDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isMember(String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        if(member==null){
            return false;
        }
        return true;
    }

    public void saveMember(String nickname) {
        //이메일, 패스워드는 막넣음
        String defaultEmail = nickname+"@"+nickname;
        String defaultPassword = nickname+"123";

        Member member = new Member();
        member.setNickname(nickname);
        member.setEmail(defaultEmail);
        member.setPassword(defaultPassword);
        member.setStatus("ACTIVE");

        memberRepository.save(member);

        if(member.getUserId() !=null){
            log.info("가입 완료!");
        }else{
            log.info("가입 실패!");
        }

    }
}
