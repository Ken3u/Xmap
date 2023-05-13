package com.shulpov.spots_app.services;

import com.shulpov.spots_app.file_manager.FileManager;
import com.shulpov.spots_app.models.ImageInfo;
import com.shulpov.spots_app.models.Spot;
import com.shulpov.spots_app.models.User;
import com.shulpov.spots_app.repo.ImageInfoRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ImageInfoService {
    @Value("${file-storage.images.users}")
    private String usersUploadPath;

    @Value("${file-storage.images.spots}")
    private String spotsUploadPath;

    private final ImageInfoRepo imageInfoRepo;
    private final FileManager imageManager;
    private final static Logger logger = LoggerFactory.getLogger(ImageInfoService.class);

    @Autowired
    public ImageInfoService(ImageInfoRepo imageInfoRepo, FileManager imageManager) {
        this.imageInfoRepo = imageInfoRepo;
        this.imageManager = imageManager;
    }

    //Сгенерировать название изображения
    private String generateName(String name) {
        logger.atInfo().log(" generateName({})", name);
        return (UUID.randomUUID().toString() + LocalDate.now().toString() + name).trim();
    }

    public Optional<ImageInfo> findById(Long id) {
        return imageInfoRepo.findById(id);
    }

    //Загрузить картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public ImageInfo uploadUserImage(MultipartFile file, User user) throws IOException {
        logger.atInfo().log("uploadUserImage file.name={} user.name={}", file.getOriginalFilename(), user.getName());
        String genName = generateName(file.getOriginalFilename());
        ImageInfo createdFile = new ImageInfo();
        createdFile.setOriginalName(file.getOriginalFilename());
        createdFile.setGenName(genName);
        createdFile.setSize((int) file.getSize());
        createdFile.setUploadDate(LocalDate.now());
        createdFile.setUser(user);

        createdFile = imageInfoRepo.save(createdFile);
        imageManager.upload(file.getBytes(), usersUploadPath, genName);
        logger.atInfo().log("uploadUserImage success " +
                        "file: id={} name={} genName={} uploadDate={}",
                createdFile.getId(),
                createdFile.getOriginalName(),
                createdFile.getGenName(),
                createdFile.getUploadDate());
        return createdFile;
    }

    //Скачать картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public Resource downloadUserImage(String genName) throws IOException{
        logger.atInfo().log("downloadUserImage genName={}", genName);
        return imageManager.download(usersUploadPath, genName);
    }

    //Скачать картинку спота
    @Transactional(rollbackFor = {IOException.class})
    public Resource downloadSpotImage(String genName) throws IOException{
        logger.atInfo().log("downloadSpotImage genName={}", genName);
        return imageManager.download(spotsUploadPath, genName);
    }

    //Удалить картинку пользователя
    @Transactional(rollbackFor = {IOException.class})
    public Long deleteUserImage(Long id) throws Exception {
        logger.atInfo().log("deleteUserImage id={}", id);
        Optional<ImageInfo> file = imageInfoRepo.findById(id);
        if (file.isPresent()) {
            logger.atInfo().log("deleteUserImage imageInfo with id={} exists", id);
            imageInfoRepo.delete(file.get());
            imageManager.delete(usersUploadPath, file.get().getGenName());
            return file.get().getId();
        }
        logger.atError().log("deleteUserImage imageInfo with id={} doesn't exist", id);
        throw new Exception("Images not found in DB");
    }

    //Удалить картинку спота
    @Transactional(rollbackFor = {IOException.class})
    public Long deleteSpotImage(Long id) throws Exception {
        logger.atInfo().log("deleteSpotImage id={}", id);
        Optional<ImageInfo> file = imageInfoRepo.findById(id);
        if (file.isPresent()) {
            logger.atInfo().log("deleteSpotImage imageInfo with id={} exists", id);
            imageInfoRepo.delete(file.get());
            imageManager.delete(spotsUploadPath, file.get().getGenName());
            return file.get().getId();
        }
        logger.atError().log("deleteSpotImage imageInfo with id={} doesn't exist", id);
        throw new Exception("Images not found in DB");
    }

    //Проверка на наличие у юзера картинки с определенным id
    public boolean userHasImageWithId(User user, Long id) {
        int size = user.getImageInfos().stream()
                .filter(el->Objects.equals(el.getId(), id)).toList().size();
        return size == 1;
    }

    //Проверка на наличие у спота картинки с определенным id
    public boolean spotHasImageWithId(Spot spot, Long id) {
        int size = spot.getImageInfos().stream()
                .filter(el->Objects.equals(el.getId(), id)).toList().size();
        return size == 1;
    }
}