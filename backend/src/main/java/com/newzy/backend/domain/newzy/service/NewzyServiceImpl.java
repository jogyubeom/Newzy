package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.image.service.ImageService;
import com.newzy.backend.domain.newzy.dto.request.NewzyListGetRequestDTO;
import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.entity.NewzyBookmark;
import com.newzy.backend.domain.newzy.entity.NewzyLike;
import com.newzy.backend.domain.newzy.repository.NewzyBookmarkRepository;
import com.newzy.backend.domain.newzy.repository.NewzyLikeRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepositorySupport;
import com.newzy.backend.domain.user.entity.User;
import com.newzy.backend.domain.user.repository.UserRepository;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityIsFoundException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {

    private final NewzyRepository newzyRepository;
    private final NewzyBookmarkRepository bookmarkRepository;
    private final NewzyLikeRepository newzyLikeRepository;
    private final NewzyRepositorySupport newzyRepositorySupport;
    private final UserRepository userRepository;
    private final NewzyBookmarkRepository newzyBookmarkRepository;
    private final ImageService imageService;

    private final RedisTemplate<String, String> redisTemplate;


    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getNewzyList(NewzyListGetRequestDTO requestDTO, Long userId) {
        log.info(">>> getNewzyList - dto: {}", requestDTO);

        int page = requestDTO.getPage();
        int category = requestDTO.getCategory();
        int sort = requestDTO.getSort();
        String keyword = requestDTO.getKeyword();

        // 내가 쓴 뉴지 목록이 아닐 경우 userId = 0
        Map<String, Object> newzyList = newzyRepositorySupport.findNewzyList(page, category, keyword, sort, userId);

        if (newzyList.isEmpty()) {
            throw new EntityNotFoundException("일치하는 뉴지 데이터를 조회할 수 없습니다.");
        }

        return newzyList;
    }


    @Override
    public NewzyResponseDTO getNewzyDetail(Long newzyId) {  // 조회수 + 1
        log.info(">>> newzyServiceImpl getNewzyDetail - newzyId: {}", newzyId);
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴지 데이터를 찾을 수 없습니다."));

        // redis 조회수 증가
        String todayDate = LocalDate.now().toString();  // 오늘 날짜
        String redisKey = "ranking:newzy:" + todayDate + ":" + newzyId;  // Redis 키

        redisTemplate.opsForValue().increment(redisKey);

        return NewzyResponseDTO.convertToDTO(newzy);
    }


    @Override
    @Transactional(readOnly = true)
    public List<NewzyListGetResponseDTO> getHotNewzyList() {
        String todayDate = LocalDate.now().toString();  // 오늘 날짜
        String pattern = "ranking:newzy:" + todayDate + ":*";  // 오늘 날짜의 모든 뉴지 조회수

        Set<String> keys = redisTemplate.keys(pattern);  // 해당 패턴에 맞는 키 가져오기

        // Redis에서 조회수 정보를 가져오고 내림차순으로 정렬 후 상위 3개의 키 추출
        List<String> topNewzyKeys = redisTemplate.opsForValue().multiGet(keys).stream()
                .sorted((v1, v2) -> Integer.compare(Integer.parseInt(v2), Integer.parseInt(v1)))  // 내림차순 정렬
                .limit(6)  // 상위 6개
                .map(key -> key.split(":")[3])  // key에서 newzyId 추출
                .toList();

        // 상위 3개의 newsId에 해당하는 News 객체들을 데이터베이스에서 조회한 후 DTO로 변환
        return topNewzyKeys.stream()
                .map(newsId -> newzyRepository.findById(Long.parseLong(newsId))
                        .map(NewzyListGetResponseDTO::convertToDTO)  // News 객체를 DTO로 변환
                        .orElseThrow(() -> new EntityNotFoundException("해당 뉴스 데이터를 찾을 수 없습니다.")))
                .collect(Collectors.toList());
    }


    @Override
    public void save(Long userId, NewzyRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));

        // HTML 파싱 적용 by Jsoup
        String content = dto.getContent();
        String ContentText = parseHtmlToText(content);

        Newzy newzy = Newzy.convertToEntity(user, dto);
        newzy.setContentText(ContentText);

        // 이미지 업로드 및 뉴지와 이미지 매핑 처리
        if (dto.getImages() != null && dto.getImages().length > 0) {
            String[] uploadedUrls = imageService.newzyUploadImages(dto.getImages(), newzy.getNewzyId(), 0);
            newzy.setThumbnail(uploadedUrls[0]);  // 첫 번째 이미지를 썸네일로 설정
        }

        newzyRepository.save(newzy);
    }

    // HTML 파싱
    private String parseHtmlToText(String content) {
        Document document = Jsoup.parse(content);
        return document.text();
    }

    @Override
    public NewzyResponseDTO update(Long userId, Long newzyId, NewzyRequestDTO dto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 엔티티를 찾을 수 없습니다."));

        // HTML 파싱 적용 by Jsoup
        String content = dto.getContent();
        String ContentText = parseHtmlToText(content);

        Newzy updatedNewzy = Newzy.convertToEntity(user, newzyId, dto, ContentText);

        Newzy newzy = newzyRepository.updateNewzyInfo(updatedNewzy);
        NewzyResponseDTO newzyResponseDTO = NewzyResponseDTO.convertToDTO(newzy);

        return newzyResponseDTO;
    }


    @Override
    public void delete(Long userId, Long newzyId) {
        Newzy newzy = newzyRepository.findById(newzyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 뉴스를 찾을 수 없습니다: " + newzyId));

        if (newzy.isDeleted()) {
            throw new CustomIllegalStateException("이미 삭제된 뉴지 입니다.");
        }

        if (userId.equals(newzy.getUser().getUserId())) {
            newzyRepository.deleteNewzyById(newzyId);
        }
    }

    @Override
    public void bookmark(Long userId, Long newzyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당하는 유저 데이터를 찾을 수 없습니다."));

        Newzy newzy = newzyRepository.findById(newzyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 뉴스를 찾을 수 없습니다: " + newzyId));

        boolean isBookmark = newzyBookmarkRepository.existsByUserAndNewzy(user, newzy);

        if (isBookmark) {
            throw new EntityIsFoundException("이미 북마크가 존재합니다.");
        }

        NewzyBookmark newzyBookmark = new NewzyBookmark();
        newzyBookmark.setUser(user);
        newzyBookmark.setNewzy(newzy);

        newzyBookmarkRepository.save(newzyBookmark);
    }


    @Override
    public void deleteBookmark(Long userId, Long newzyId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("일치하는 유저 데이터가 없습니다."));
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴지 데이터가 없습니다."));
        boolean isBookmark = newzyBookmarkRepository.existsByUserAndNewzy(user, newzy);

        if (!isBookmark) {
            throw new EntityNotFoundException("해당하는 북마크 데이터가 없습니다.");
        }

        newzyBookmarkRepository.deleteByUserAndNewzy(user, newzy);
    }


    @Override
    public void likeNewzy(Long userId, Long newzyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("일치하는 유저 데이터가 없습니다."));
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴지 데이터가 없습니다."));

        boolean isLike = newzyLikeRepository.existsByUserAndNewzy(user, newzy);

        if (isLike) {
            throw new EntityIsFoundException("이미 뉴지 좋아요가 존재합니다.");
        }

        NewzyLike like = new NewzyLike();
        like.setUser(user);
        like.setNewzy(newzy);
        newzyLikeRepository.save(like);
    }


    @Override
    public void deleteNewzyLike(Long userId, Long newzyId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("일치하는 유저 데이터가 없습니다."));
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴지 데이터가 없습니다."));

        boolean isLike = newzyLikeRepository.existsByUserAndNewzy(user, newzy);

        if (!isLike) {
            throw new EntityIsFoundException("해당하는 뉴지 좋아요가 없습니다.");
        }

        newzyLikeRepository.deleteByUserAndNewzy(user, newzy);
    }

}
