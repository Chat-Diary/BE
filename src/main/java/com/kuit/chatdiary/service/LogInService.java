package com.kuit.chatdiary.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kuit.chatdiary.domain.*;
import com.kuit.chatdiary.repository.*;
import com.kuit.chatdiary.repository.diary.DiaryTagRepository;
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
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

@Slf4j
@Service
public class LogInService {

    @Autowired
    private final MemberRepository memberRepository;

    @Autowired
    private final DiaryRepository diaryRepository;

    @Autowired
    private final DiaryTagRepository diaryTagRepository;

    @Autowired
    private final ChatRepository chatRepository;

    @Autowired
    private final PhotoRepository photoRepository;

    @Autowired
    private final DiaryPhotoRepository diaryPhotoRepository;




    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    public LogInService(MemberRepository memberRepository, DiaryRepository diaryRepository, DiaryTagRepository diaryTagRepository, ChatRepository chatRepository, PhotoRepository photoRepository, DiaryPhotoRepository diaryPhotoRepository) {
        this.memberRepository = memberRepository;
        this.diaryRepository = diaryRepository;
        this.diaryTagRepository = diaryTagRepository;
        this.chatRepository = chatRepository;
        this.photoRepository = photoRepository;
        this.diaryPhotoRepository = diaryPhotoRepository;
    }

    // AccessToken 발급하는 메서드
    public String getAccessToken(String code, String redirectUri) {
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
            sb.append("&redirect_uri=").append(redirectUri);
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
            JsonObject kakaoAccount = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

            String nickname = properties.getAsJsonObject().get("nickname").getAsString();
            String email = kakaoAccount.getAsJsonObject().get("email").getAsString();


            userInfo.put("nickname", nickname);
            userInfo.put("email", email);


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

    public Boolean isMember(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isPresent()){
            return true;
        }
        return false;
    }

    public void saveMember(String nickname, String email) {
        //패스워드는 막넣음
        String defaultPassword = nickname+"123";

        Member member = new Member();
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPassword(defaultPassword);
        member.setStatus("ACTIVE");

        memberRepository.save(member);

        if(member.getUserId() !=null){
            log.info("가입 완료!");
        }else{
            log.info("가입 실패!");
        }

    }

    public Long getUserId(String email){
        Long userId = memberRepository.findByEmail(email).get().getUserId();
        return userId;
    }

    public void insertData(Long userId) throws ParseException {

        Member member = memberRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        // diary
        Diary diary = diaryRepository.insertDiary(member);


        //diarytag
        diaryTagRepository.insertDiaryTag(diary);


        //chat
        Chat chat1 = new Chat(member, Sender.USER,"안녕", ChatType.CHAT);
        chatRepository.save(chat1);
        Chat chat2 = new Chat(member, Sender.DADA,"안녕하세요, 오늘 하루는 어땠어요?", ChatType.CHAT);
        chatRepository.save(chat2);
        Chat chat3 = new Chat(member, Sender.USER,"오늘 테니스 수업 듣고왔어", ChatType.CHAT);
        chatRepository.save(chat3);
        Chat chat4 = new Chat(member, Sender.DADA,"우와! 테니스 수업 어땠어요? 재밌었나요?", ChatType.CHAT);
        chatRepository.save(chat4);
        Chat chat5 = new Chat(member, Sender.USER,"첫 수업이라 자세만 잡았는데 그것도 힘들더라!", ChatType.CHAT);
        chatRepository.save(chat5);
        Chat chat6 = new Chat(member, Sender.DADA,"처음이라 어색하고 힘들 수도 있어요. 연습하다 보면 점점 나아질 거예요, 힘내요!", ChatType.CHAT);
        chatRepository.save(chat6);
        Chat chat7 = new Chat(member, Sender.USER,"https://chatdiary-bucket.s3.ap-northeast-2.amazonaws.com/test_images/upload_938f1186-e55f-43f7-bee4-c946816197b1.jpg", ChatType.IMG);
        chatRepository.save(chat7);
        Chat chat8 = new Chat(member, Sender.DADA,"테니스 코트에서 연습한 흔적이 보이네요! 공도 많고 라켓도 멋있어요. 열심히 하셨나봐요!", ChatType.CHAT);
        chatRepository.save(chat8);
        Chat chat9 = new Chat(member, Sender.USER,"선생님이 추가 레슨도 해주셔서 1시간 정도 쳤어", ChatType.CHAT);
        chatRepository.save(chat9);
        Chat chat10 = new Chat(member, Sender.DADA,"와, 선생님께서 추가로 레슨해주시다니 정말 행운이네요! 계속 연습하면 더 잘하게 될 거예요!", ChatType.CHAT);
        chatRepository.save(chat10);
        Chat chat11 = new Chat(member, Sender.USER,"얼른 배워서 멋있게 치고싶어!", ChatType.CHAT);
        chatRepository.save(chat11);
        Chat chat12 = new Chat(member, Sender.DADA,"정말 멋진 목표예요! 열심히 연습해서 멋진 플레이어가 될 수 있을 거예요, 화이팅!", ChatType.CHAT);
        chatRepository.save(chat12);
        Chat chat13 = new Chat(member, Sender.USER,"테니스 경기 할 수 있을 정도로 열심히 배워볼게", ChatType.CHAT);
        chatRepository.save(chat13);
        Chat chat14 = new Chat(member, Sender.DADA,"그래요! 그 정도 실력이 되면 정말 재밌겠어요! 꼭 목표 달성하시길 바랄게요!", ChatType.CHAT);
        chatRepository.save(chat14);

        //photo
        Photo photo = new Photo(chat7, "https://chatdiary-bucket.s3.ap-northeast-2.amazonaws.com/test_images/upload_938f1186-e55f-43f7-bee4-c946816197b1.jpg");
        photoRepository.save(photo);

        //diaryphoto
        DiaryPhoto diaryPhoto = new DiaryPhoto(diary, photo);
        diaryPhotoRepository.save(diaryPhoto);


    }
}
