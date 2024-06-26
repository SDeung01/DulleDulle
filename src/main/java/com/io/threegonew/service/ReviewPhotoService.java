package com.io.threegonew.service;

import com.io.threegonew.domain.ReviewPhoto;
import com.io.threegonew.repository.ReviewPhotoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewPhotoService {
    private final ReviewPhotoRepository reviewPhotoRepository;

    public ReviewPhoto savePhoto(ReviewPhoto photo){
        return reviewPhotoRepository.save(photo);
    }

    public ReviewPhoto findPhotoById(Long id) {
        return reviewPhotoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("사진 정보를 찾을 수 없습니다.")
        );
    }
}
