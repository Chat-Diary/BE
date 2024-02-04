package com.kuit.chatdiary.controller.Login;

import com.kuit.chatdiary.dto.login.KakaoLoginResponseDTO;
import com.kuit.chatdiary.service.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LogInController {

    @Autowired
    public KakaoService kakaoService;


    @GetMapping("/kakao/callback")
    public KakaoLoginResponseDTO login(@RequestParam("code") String code) throws Exception {
        //1. 클라이언트에서 로그인 코드를 보내줌 (서버에서 할일 X)
        System.out.println("code: "+code);

        //2. 토큰 받기
        String accessToken = kakaoService.getAccessToken(code);
        System.out.println("accessToken: "+accessToken);

        //3. 사용자 정보 받기
        Map<String, Object> userInfo = kakaoService.getUserInfo(accessToken);
        String nickname = (String)userInfo.get("nickname");

        System.out.println("nickname = " + nickname);
        System.out.println("accessToken = " + accessToken);


        if(nickname != null) {
            //4. 사용자 정보 기반으로 jwt 생성
            String jwt = kakaoService.generateJwt(nickname, 3600000);
            System.out.println("jwt: "+jwt);

            return new KakaoLoginResponseDTO(jwt);
        }
        else{
            throw new Exception("인증되지 않은 사용자입니다");
        }

    }

}
