package cnpr.lcss.service;


import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.ImageRequestDto;
import cnpr.lcss.model.ImageResponseDTO;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.SubjectRepository;
import cnpr.lcss.util.Constant;
import com.google.api.client.util.Base64;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FirebaseService {
    StorageOptions storageOptions;
    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SubjectRepository subjectRepository;

    //<editor-fold desc="Encode Base64 to Image and Save">
    public void encodeBase64toImageandSave(String image64) throws IOException {
        byte[] data = Base64.decodeBase64(image64);
        //location of project resource
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        Path destinationFile = Paths.get(s, "myImage.jpg");
        Files.write(destinationFile, data);
    }
    //</editor-fold>

    //<editor-fold desc="Upload File">
    private String uploadFile(File file, String fileName) throws IOException, FirebaseAuthException {
        BlobId blobId = BlobId.of(Constant.BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        FileInputStream fileInputStream = new FileInputStream("./AccountService.json");
        storageOptions = StorageOptions.newBuilder().setProjectId(Constant.PROJECT_ID).setCredentials(GoogleCredentials.fromStream(fileInputStream)).build();
        Storage storage;
        if (storageOptions == null) {
            storage = StorageOptions.getDefaultInstance().getService();
        } else {
            storage = storageOptions.getService();
        }

        //String uid="huunt";
        //String customToken= FirebaseAuth.getInstance().createCustomToken(uid);
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));
        String dowloadURL = String.format("https://firebasestorage.googleapis.com/v0/b/app-test-c1bfb.appspot.com/o/%s?alt=media", URLEncoder.encode(fileName, String.valueOf(StandardCharsets.UTF_8)));
        //storage.createAcl(blobId, Acl.of(Acl.User.ofAllUsers(), Acl.Role.READER));
        //System.out.println(" public "+ fileName);
        return dowloadURL;
    }
    //</editor-fold>

    //<editor-fold desc="Get Extension">
    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }
    //</editor-fold>

    //<editor-fold desc="Upload">
    public ImageResponseDTO upload(ImageRequestDto imageRequestDto, String id) throws IOException, FirebaseAuthException {
        boolean result = false;
        String base64 = imageRequestDto.getImage();
        encodeBase64toImageandSave(base64);
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        File file = new File(s, "/myImage.jpg");//folder store image just encode, then delete the image
        String fileName = file.getName();                                               // to get original file name
        fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.
        String TEMP_URL = this.uploadFile(file, fileName);                               // to get uploaded file link
        file.delete();                                                                // to delete the copy of uploaded file stored in the project folder

        if (imageRequestDto.getKeyword().matches(Constant.AVATAR)) {
            if (accountRepository.existsByUsername(id)) {
                Account account = accountRepository.findOneByUsername(id);
                account.setImage(TEMP_URL);
                accountRepository.save(account);
                result = true;
            } else {
                throw new IllegalArgumentException(Constant.INVALID_USERNAME);
            }
        }

        ImageResponseDTO imageResponseDTO = new ImageResponseDTO(result, TEMP_URL);
        return imageResponseDTO;
    }
    //</editor-fold>
}