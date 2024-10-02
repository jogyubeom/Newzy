package com.newzy.backend.domain.dictionary.controller;

import com.newzy.backend.domain.dictionary.entity.Dictionary;
import com.newzy.backend.domain.dictionary.service.DictionaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/word")
public class DictionaryController {
    private final DictionaryService dictionaryService;

    @GetMapping
    public ResponseEntity<List<Dictionary>> searchByWord(@RequestParam(value = "search") String search) {
        log.info(">>> [GET] /word?search={} - 요청 파라미터: {}", search, search);
        List<Dictionary> dictionaryList = dictionaryService.searchByWord(search);
        log.info(String.valueOf(dictionaryList.size()));
        return ResponseEntity.status(200).body(dictionaryList);
    }
}
