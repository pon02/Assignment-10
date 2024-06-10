package com.pon02.Assignment10.controller;

import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;

//バリデーションエラーの例外処理で使用するエラーレスポンスのクラス
public record ErrorResponse(HttpStatus status, String message, List<Map<String, String>> errors) {
}
