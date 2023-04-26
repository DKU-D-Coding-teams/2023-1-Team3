package DKUDCoding20231Team3.VISTA.util;

import com.oracle.bmc.ConfigFileReader;
import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class OCIUtil {

    @Value("${oci-cloud.configurationFilePath}")
    private String configurationFilePath;

    @Value("${oci-cloud.profile}")
    private String profile;

    @Value("${oci-cloud.compartmentId}")
    private String compartmentId;

    @Value("${oci-cloud.objectStorageLink}")
    private String objectStorageLink;

    private ObjectStorage client;

    public String putImage(String mail, MultipartFile image) throws IOException, NoSuchAlgorithmException {
        final String imageLink = sha256(mail) + ".png";

        System.out.println("Creating the source object");
        PutObjectRequest putObjectRequest =
                PutObjectRequest.builder()
                        .namespaceName(getNamespaceName())
                        .bucketName("vista")
                        .objectName(imageLink)
                        .contentType("multipart/form-data")
                        .contentLength(image.getSize())
                        .putObjectBody(
                                new ByteArrayInputStream(image.getBytes()))
                        .build();
        client.putObject(putObjectRequest);

        return imageLink;
    }

    private String getNamespaceName() throws IOException {
        final ConfigFileReader.ConfigFile configFile = ConfigFileReader.parseDefault();

        final AuthenticationDetailsProvider provider =
                new ConfigFileAuthenticationDetailsProvider(configFile);

        client = ObjectStorageClient.builder().region(Region.AP_CHUNCHEON_1).build(provider);

        GetNamespaceResponse namespaceResponse =
                client.getNamespace(GetNamespaceRequest.builder().build());
        return namespaceResponse.getValue();
    }

    private String sha256(String text) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes());

        StringBuilder builder = new StringBuilder();
        for (byte b : md.digest()) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

}
