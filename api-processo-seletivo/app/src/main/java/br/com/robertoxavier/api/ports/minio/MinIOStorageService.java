package br.com.robertoxavier.api.ports.minio;

import br.com.robertoxavier.service.Resource;
import br.com.robertoxavier.service.StorageService;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import io.minio.messages.DeleteError;
import io.minio.messages.DeleteObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class MinIOStorageService implements StorageService {
    private final String bucket;
    private final MinioClient minioClient;
    private final String ip_public_s3;
    private final String ip_s3;

    public MinIOStorageService(String bucket, MinioClient minioClient,  String ipPublicS3, String ipS3) {
        this.bucket = bucket;
        this.minioClient = minioClient;
        ip_public_s3 = ipPublicS3;
        ip_s3 = ipS3;
    }

    @Override
    public void store(String id, Resource resource) {
        try{
            ByteArrayInputStream bais = new ByteArrayInputStream(resource.content());
            final var info = PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(id)
                    .contentType(resource.contentType())
                    .stream(bais, bais.available(), -1)
                    .build();

            minioClient.putObject(info);

        } catch (MinioException e) {
            System.out.println("Error occurred: " + e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Resource> get(String id) {
        GetObjectArgs objArgs = GetObjectArgs.builder()
                .bucket(bucket)
                .object(id) // ID do arquivo (nome do arquivo no MinIO)
                .build();

        try {
            // Recuperando o objeto do MinIO
            var object = minioClient.getObject(objArgs);

            // Convertendo o InputStream em byte[] (conteúdo)
            byte[] content = object.readAllBytes();

            // Recuperando o checksum (ETag)
            String checksum = object.headers().get("ETag");

            // Recuperando o content type
            String contentType = object.headers().get("Content-Type");

            // O nome do objeto é o ID que você passa
            String name = id;

            return Optional.of(Resource.with(content, checksum, contentType, name));

        } catch (ErrorResponseException | InsufficientDataException | InternalException
                 | InvalidKeyException | InvalidResponseException | IOException
                 | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            // Lançando a exceção com a mensagem de erro apropriada
            throw new RuntimeException("Erro ao recuperar o objeto do MinIO: " + e.getMessage(), e);
        }
    }

    @Override
    public List<String> list(String prefix) {
        return List.of();
    }

    @Override
    public void deleteAll(List<String> ids) {
        try {
            List<DeleteObject> idsArg = new LinkedList<>();

            for(String id : ids){
                idsArg.add(new DeleteObject(id));
            }
            RemoveObjectsArgs rmvArg= RemoveObjectsArgs.builder().bucket(bucket).objects(idsArg).build();
            Iterable<Result<DeleteError>> results = minioClient.removeObjects(rmvArg);
            for (Result<DeleteError> result : results) {
                DeleteError error = result.get();
                System.out.println(
                        "Error in deleting object " + error.objectName() + "; " + error.message());
            }
        } catch (Exception e) {
            System.out.println("Error occurred: " + e);
        }
    }

    @Override
    public String generateTemporaryLink(String id) {
        int expirationTimeSeconds= 300;
        final var args = GetPresignedObjectUrlArgs
                .builder()
                .expiry(expirationTimeSeconds)
                .method(Method.GET)
                .bucket(bucket)
                .object(id)
                .build();

        try{
            String url = minioClient.getPresignedObjectUrl(args);
            System.out.println(url);
            System.out.println( url.replace(ip_s3,ip_public_s3));

            return url.replace(ip_s3,ip_public_s3);
        }catch(ErrorResponseException | InsufficientDataException | InternalException
               | InvalidKeyException | InvalidResponseException | IOException
               | NoSuchAlgorithmException | ServerException | XmlParserException e){
            throw new RuntimeException("Erro ao gerar url temporaria: " + e.getMessage(), e);
        }
    }
}