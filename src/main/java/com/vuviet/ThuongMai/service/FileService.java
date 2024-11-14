//package com.vuviet.ThuongMai.service;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@Service
//public class FileService {
//
//    private static String baseURI="src/storage";
//
//
//    public void createUploadFolder(String folder) throws URISyntaxException {
//        String uploadFolderPath = ClassLoader.getSystemResource("static/shop/images/" + folder).getPath();
//        Path path = Paths.get(uploadFolderPath);
//        File tmpDir = new File(path.toString());
//        if (!tmpDir.isDirectory()) {
//            try {
//                Files.createDirectory(tmpDir.toPath());
//                System.out.println(">>> CREATE NEW DIRECTORY SUCCESSFUL, PATH = " + tmpDir.toPath());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            System.out.println(">>> SKIP MAKING DIRECTORY, ALREADY EXISTS");
//        }
//    }
//
//
//    public String store(MultipartFile file, String folder) throws URISyntaxException, IOException {
//        // create unique filename
//        String finalName = System.currentTimeMillis() + "-" + file.getOriginalFilename();
////        URI uri = new URI("file", null, baseURI + "images/" + folder + "/" + finalName, null, null);
//        String uploadFolderPath = ClassLoader.getSystemResource("static/shop/images/" + folder+finalName).getPath();
//        Path path = Paths.get(uploadFolderPath);
//        try (InputStream inputStream = file.getInputStream()) {
//            Files.copy(inputStream, path,
//                    StandardCopyOption.REPLACE_EXISTING);
//        }
//        return finalName;
//    }
//}
