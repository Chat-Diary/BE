package com.kuit.chatdiary.controller.Login;

import org.springframework.beans.factory.annotation.Value;

public class KakaoLogin {

    @Value("${KAKAO_API_KEY}")
    private String kakaoApiKey;

    @Value("${KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    @Value("${S3_BUCKET}")
    private String secretKey;



}
