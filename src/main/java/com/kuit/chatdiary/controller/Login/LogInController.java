package com.kuit.chatdiary.controller.Login;

import com.kuit.chatdiary.dto.login.KakaoLoginResponseDTO;
import com.kuit.chatdiary.service.LogInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/kakao/callback")
    public KakaoLoginResponseDTO login(@RequestParam("code") String code) throws Exception {
        //1. 클라이언트에서 로그인 코드를 보내줌 (서버에서 할일 X)
        System.out.println("code: "+code);

        //2. 토큰 받기
        String accessToken = logInService.getAccessToken(code);
        System.out.println("accessToken: "+accessToken);

        //3. 사용자 정보 받기
        Map<String, Object> userInfo = logInService.getUserInfo(accessToken);
        String nickname = (String)userInfo.get("nickname");

        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);

        if(nickname == null){
            throw new Exception("인증되지 않은 사용자입니다");
        }

        String jwt = logInService.generateJwt(nickname, 3600000);
        System.out.println("jwt: "+jwt);

        Boolean isMember = logInService.isMember(nickname);

        //가입된 사용자 -> 바로 로그인
        if(isMember){
            log.info("가입된 사용자입니다.");
            return new KakaoLoginResponseDTO(jwt);
        }else{//미가입 사용자 -> 회원가입&로그인
            log.info("미가입 사용자입니다.");
            logInService.saveMember(nickname);

            return new KakaoLoginResponseDTO(jwt);
        }


    }

}
