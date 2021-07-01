package cnpr.lcss.service;


import cnpr.lcss.dao.Account;
import cnpr.lcss.dao.Curriculum;
import cnpr.lcss.dao.Subject;
import cnpr.lcss.model.ImageRequestDto;
import cnpr.lcss.repository.AccountRepository;
import cnpr.lcss.repository.CurriculumRepository;
import cnpr.lcss.repository.SubjectRepository;
import com.google.api.client.util.Base64;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    private static final String PROJECT_ID = "app-test-c1bfb";
    private static final String BUCKET_NAME = "app-test-c1bfb.appspot.com";
    private static final String CURRICULUM = "curriculum";
    private static final String AVATAR = "avatar";
    private static final String SUBJECT = "subject";
    private static final String CURRICULUM_ID_NOT_EXIST = "Curriculum ID doesn't exist";
    private static final String USERNAME_NOT_EXIST = "User Name doesn't exist";
    private static final String SUBJECT_ID_NOT_EXIST = "Subject ID doesn't exist";
    StorageOptions storageOptions;
    @Autowired
    CurriculumRepository curriculumRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    SubjectRepository subjectRepository;

    public void enodeBase64toImageandSave(String image64) throws IOException {
        byte[] data = Base64.decodeBase64(image64);
        //location of project resource
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        Path destinationFile = Paths.get(s, "myImage.jpg");
        Files.write(destinationFile, data);
    }

    private String uploadFile(File file, String fileName) throws IOException, FirebaseAuthException {
        BlobId blobId = BlobId.of(BUCKET_NAME, fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).build();
        FileInputStream fileInputStream = new FileInputStream("./AccountService.json");
        storageOptions = StorageOptions.newBuilder().setProjectId(PROJECT_ID).setCredentials(GoogleCredentials.fromStream(fileInputStream)).build();
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

    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }


    public ResponseEntity<?> upload(ImageRequestDto imageRequestDto, String id) {

        try {
            String base64 = imageRequestDto.getImage();
            enodeBase64toImageandSave(base64);
            Path currentRelativePath = Paths.get("");
            String s = currentRelativePath.toAbsolutePath().toString();
            File file = new File(s, "/myImage.jpg");//folder store image just encode, then delete the image
            String fileName = file.getName();                                               // to get original file name
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));  // to generated random string values for file name.
            String TEMP_URL = this.uploadFile(file, fileName);                               // to get uploaded file link
            file.delete();                                                                // to delete the copy of uploaded file stored in the project folder


            if (imageRequestDto.getKeyword().matches(CURRICULUM)) {
                int idCurInsert = Integer.parseInt(id);
                if (curriculumRepository.existsByCurriculumId(idCurInsert)) {
                    Curriculum insCur = curriculumRepository.findOneByCurriculumId(idCurInsert);
                    insCur.setImage(TEMP_URL);
                    curriculumRepository.save(insCur);
                } else {
                    throw new IllegalArgumentException(CURRICULUM_ID_NOT_EXIST);
                }
            }
            if (imageRequestDto.getKeyword().matches(AVATAR)) {
                if (accountRepository.existsByUsername(id)) {
                    Account account = accountRepository.findOneByUsername(id);
                    account.setImage(TEMP_URL);
                    accountRepository.save(account);
                } else {
                    throw new IllegalArgumentException(USERNAME_NOT_EXIST);
                }
            }
            if (imageRequestDto.getKeyword().matches(SUBJECT)) {
                int idSubIns = Integer.parseInt(id);
                if (subjectRepository.existsSubjectBySubjectId(idSubIns)) {
                    Subject insSub = subjectRepository.findBySubjectId(idSubIns);
                    insSub.setImage(TEMP_URL);
                    subjectRepository.save(insSub);
                } else {
                    throw new IllegalArgumentException(SUBJECT_ID_NOT_EXIST);
                }
            }
            return ResponseEntity.ok(TEMP_URL + ", " +Boolean.TRUE);    // Your customized response
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }


}