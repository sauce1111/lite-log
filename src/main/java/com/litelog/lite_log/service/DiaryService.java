package com.litelog.lite_log.service;

import com.litelog.lite_log.dto.DiaryDto;
import com.litelog.lite_log.entity.Diary;
import com.litelog.lite_log.entity.Member;
import com.litelog.lite_log.exception.DiaryNotFoundException;
import com.litelog.lite_log.exception.FileUploadException;
import com.litelog.lite_log.repository.DiaryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private static final String IMAGE_UPLOAD_DIR = "src/main/resources/static/images";
    private static final List<String> ALLOWED_EXTENSIONS = List.of("jpg", "jpeg", "png");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    private final DiaryRepository diaryRepository;

    @Transactional
    public void createDiary(Member member, LocalDate date, String title, String content, MultipartFile menuImage)
            throws IOException {
        String imageUrl = getImageUrl(menuImage);
        Diary diary = new Diary(member, date, title, content, imageUrl);
        diaryRepository.save(diary);
    }

    private String getImageUrl(MultipartFile menuImage) throws IOException {
        if (menuImage == null && menuImage.isEmpty()) {
            return null;
        }

        validateFile(menuImage);

        File uploadDir = new File(IMAGE_UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String imageName = UUID.randomUUID() + "_" + StringUtils.cleanPath(menuImage.getOriginalFilename());
        File serverFile = new File(uploadDir, imageName);

        try {
            Files.copy(menuImage.getInputStream(), serverFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "/images/" + imageName;
        } catch (IOException e) {
            Files.deleteIfExists(serverFile.toPath());
            throw new FileUploadException("File upload failed.");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new FileUploadException("File size must not exceed 5MB.");
        }

        String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
        if (extension == null || !ALLOWED_EXTENSIONS.contains(extension.toLowerCase())) {
            throw new FileUploadException("Invalid file type. Only JPG, JPEG, PNG are allowed.");
        }
    }

    public Diary getDiary(Member member, LocalDate date) {
        return diaryRepository.findByMemberAndDate(member, date)
                .orElseThrow(() -> new DiaryNotFoundException("Diary not found for date: " + date));
    }

    public List<DiaryDto> getDiaryDtoListByMember(Member member) {
        List<Diary> diaries = diaryRepository.findByMember(member);
        return diaries.stream()
                .map(DiaryDto::new)
                .collect(Collectors.toList());
    }
}
