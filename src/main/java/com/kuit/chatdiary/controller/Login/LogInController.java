package com.kuit.chatdiary.controller.Login;

import com.kuit.chatdiary.dto.login.KakaoLoginResponseDTO;
import com.kuit.chatdiary.service.LogInService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LogInController {

    @Autowired
    public LogInService logInService;


    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    @Value("${KAKAO_REDIRECT_URI_LOCAL}")
    private String kakaoRedirectUriLocal;

    /*배포용*/
    @GetMapping("/kakao/login")
    public ResponseEntity<KakaoLoginResponseDTO> login(@RequestParam(value = "code") String code) throws Exception {
        //1. 클라이언트에서 로그인 코드를 보내줌 (서버에서 할일 X)
        System.out.println("code: "+code);

        //2. 토큰 받기
        String accessToken = logInService.getAccessToken(code, kakaoRedirectUri);
        System.out.println("accessToken: "+accessToken);

        //3. 사용자 정보 받기
        Map<String, Object> userInfo = logInService.getUserInfo(accessToken);
        System.out.println("userinfo : "+userInfo);
        String nickname = (String)userInfo.get("nickname");
        String email = (String) userInfo.get("email");

        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);
        System.out.println("accessToken = " + accessToken);

        String jwt = logInService.generateJwt(email, 3600000);
        System.out.println("jwt: "+jwt);

        Boolean isMember = logInService.isMember(email);

        //가입된 사용자 -> 바로 로그인
        if(isMember){
            log.info("이미 가입된 사용자입니다.");
        }else{//미가입 사용자 -> 회원가입&로그인
            log.info("미가입 사용자입니다.");
            logInService.saveMember(nickname, email);
        }

        Long userId = logInService.getUserId(email);

        logInService.insertData(userId);

        KakaoLoginResponseDTO kakaoLoginResponseDTO = new KakaoLoginResponseDTO(jwt, userId, nickname);
        return ResponseEntity.ok().body(kakaoLoginResponseDTO);
    }

    /*로컬*/
    @GetMapping("/kakao/login/local")
    public ResponseEntity<KakaoLoginResponseDTO> loginLocal(@RequestParam(value = "code") String code) throws Exception {
        //1. 클라이언트에서 로그인 코드를 보내줌 (서버에서 할일 X)
        System.out.println("code: "+code);

        //2. 토큰 받기
        String accessToken = logInService.getAccessToken(code, kakaoRedirectUriLocal);
        System.out.println("accessToken: "+accessToken);

        //3. 사용자 정보 받기
        Map<String, Object> userInfo = logInService.getUserInfo(accessToken);
        System.out.println("userinfo : "+userInfo);
        String nickname = (String)userInfo.get("nickname");
        String email = (String) userInfo.get("email");

        System.out.println("nickname = " + nickname);
        System.out.println("email = " + email);
        System.out.println("accessToken = " + accessToken);

        String jwt = logInService.generateJwt(email, 3600000);
        System.out.println("jwt: "+jwt);

        Boolean isMember = logInService.isMember(email);

        //가입된 사용자 -> 바로 로그인
        if(isMember){
            log.info("이미 가입된 사용자입니다.");
        }else{//미가입 사용자 -> 회원가입&로그인
            log.info("미가입 사용자입니다.");
            logInService.saveMember(nickname, email);
        }

        Long userId = logInService.getUserId(email);

        logInService.insertData(userId);

        KakaoLoginResponseDTO kakaoLoginResponseDTO = new KakaoLoginResponseDTO(jwt, userId, nickname);
        return ResponseEntity.ok().body(kakaoLoginResponseDTO);
    }

}
