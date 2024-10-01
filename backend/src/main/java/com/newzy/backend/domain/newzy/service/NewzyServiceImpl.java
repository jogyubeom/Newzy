package com.newzy.backend.domain.newzy.service;

import com.newzy.backend.domain.newzy.dto.request.NewzyRequestDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyListGetResponseDTO;
import com.newzy.backend.domain.newzy.dto.response.NewzyResponseDTO;
import com.newzy.backend.domain.newzy.entity.NewzyBookmark;
import com.newzy.backend.domain.newzy.entity.NewzyLike;
import com.newzy.backend.domain.newzy.entity.Newzy;
import com.newzy.backend.domain.newzy.repository.NewzyBookmarkRepository;
import com.newzy.backend.domain.newzy.repository.NewzyLikeRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepository;
import com.newzy.backend.domain.newzy.repository.NewzyRepositorySupport;
import com.newzy.backend.global.exception.CustomIllegalStateException;
import com.newzy.backend.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class NewzyServiceImpl implements NewzyService {

    private final NewzyRepository newzyRepository;
    private final NewzyBookmarkRepository bookmarkRepository;
    private final NewzyLikeRepository newzyLikeRepository;
    private final NewzyRepositorySupport newzyRepositorySupport;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> getNewzyListWithLastPage(int page, int category) {
        log.info(">>> newzyServiceImpl getNewzyList - pages: {}, category: {}", page, category);
        int size = 10;
        Map<String, Object> newzyList = newzyRepositorySupport.findNewzyList(page, size, category);

        if (newzyList.isEmpty()) {
            throw new EntityNotFoundException("일치하는 뉴지 데이터를 조회할 수 없습니다.");
        }

        return newzyList;
    }

    @Override
    public NewzyResponseDTO getNewzyDetail(Long newzyId) {  // 조회수 + 1
        log.info(">>> newzyServiceImpl getNewzyDetail - newzyId: {}", newzyId);
        Newzy newzy = newzyRepository.findById(newzyId).orElseThrow(() -> new EntityNotFoundException("일치하는 뉴지 데이터를 찾을 수 없습니다."));
        newzy.setVisitCnt(newzy.getVisitCnt() + 1);
        newzyRepository.save(newzy);

        return NewzyResponseDTO.convertToDTO(newzy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<NewzyListGetResponseDTO> getHotNewzyList() {
        List<Newzy> hotNewzies = newzyRepository.findTop3ByIsDeletedFalseOrderByVisitCntDesc();
        List<NewzyListGetResponseDTO> hotNewzyList = new ArrayList<>();

        for (Newzy newzy : hotNewzies) {
            NewzyListGetResponseDTO dto = NewzyListGetResponseDTO.convertToDTO(newzy);
            hotNewzyList.add(dto);
        }

        return hotNewzyList;
    }

    @Override
    public void save(NewzyRequestDTO dto) {
        Newzy newzy = Newzy.convertToEntity(dto);
        newzyRepository.save(newzy);
    }

    @Override
    public NewzyResponseDTO update(Long newzyId, NewzyRequestDTO dto) {
        Newzy updatedNewzy = Newzy.convertToEntity(newzyId, dto);
        Newzy newzy = newzyRepository.updateNewzyInfo(updatedNewzy);
        NewzyResponseDTO newzyResponseDTO = NewzyResponseDTO.convertToDTO(newzy);

        return newzyResponseDTO;
    }

    @Override
    public void delete(Long newzyId) {
        newzyRepository.deleteNewzyById(newzyId);
    }

    @Override
    public void bookmark(Long newzyId) {
        // TODO: 유저 토큰으로 중복 처리

        Newzy newzy = newzyRepository.findById(newzyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 뉴스를 찾을 수 없습니다: " + newzyId));

        NewzyBookmark newzyBookmark = new NewzyBookmark();
        newzyBookmark.setNewzy(newzy);
        bookmarkRepository.save(newzyBookmark);
    }

    @Override
    public void deleteBookmark(Long bookmarkId) {

        // TODO: 유저 토큰으로 중복 처리

        NewzyBookmark bookmark = bookmarkRepository.findById(bookmarkId).orElseThrow(() -> new EntityNotFoundException("일치하는 북마크 데이터가 없습니다."));

        bookmarkRepository.delete(bookmark);
    }

    @Override
    public void likeNewzy(Long newzyId) {
        // TODO: 유저 토큰으로 중복 처리
        Newzy newzy = newzyRepository.findById(newzyId)
                .orElseThrow(() -> new EntityNotFoundException("해당 ID의 뉴스를 찾을 수 없습니다: " + newzyId));

        NewzyLike like = new NewzyLike();
        like.setNewzy(newzy);
        newzyLikeRepository.save(like);
    }

    @Override
    public void deleteLike(Long newzyLikeId) {
        // TODO: 유저 토큰으로 중복 처리
        NewzyLike like = newzyLikeRepository.findById(newzyLikeId)
                .orElseThrow(() -> new EntityNotFoundException("해당하는 좋아요가 없습니다."));

        newzyLikeRepository.delete(like);
    }

}
