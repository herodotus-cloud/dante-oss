/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Engine licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Dante Engine 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 <https://gitee.com/herodotus/dante-engine>
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package cn.herodotus.oss.minio.api.service;

import cn.herodotus.oss.minio.api.converter.BucketToEntityConverter;
import cn.herodotus.oss.minio.api.definition.pool.MinioClientObjectPool;
import cn.herodotus.oss.minio.api.definition.service.BaseMinioService;
import cn.herodotus.oss.minio.api.entity.BucketEntity;
import cn.herodotus.oss.minio.core.exception.*;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Description: Minio Bucket 存储通基础操作服务 Service </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/6/30 13:26
 */
@Service
public class BucketService extends BaseMinioService {

    private static final Logger log = LoggerFactory.getLogger(BucketService.class);
    private final Converter<Bucket, BucketEntity> toResult;

    public BucketService(MinioClientObjectPool minioClientObjectPool) {
        super(minioClientObjectPool);
        this.toResult = new BucketToEntityConverter();
    }

    /**
     * 查询所有存储桶
     * @param args {@link ListBucketsArgs}
     * @return Bucket 列表
     */
    public List<BucketEntity> listBuckets(ListBucketsArgs args) {
        String function = "listBuckets";
        MinioClient minioClient = getMinioClient();

        try {
            List<io.minio.messages.Bucket> buckets;
            if(ObjectUtils.isNotEmpty(args)) {
                buckets = minioClient.listBuckets(args);
            } else {
                buckets = minioClient.listBuckets();
            }
            return toEntities(buckets);
        } catch (ErrorResponseException e) {
            log.error("[Herodotus] |- Minio catch ErrorResponseException in [{}].", function, e);
            throw new OssErrorResponseException("Minio response error.");
        } catch (InsufficientDataException e) {
            log.error("[Herodotus] |- Minio catch InsufficientDataException in [{}].", function, e);
            throw new OssInsufficientDataException("Minio insufficient data error.");
        } catch (InternalException e) {
            log.error("[Herodotus] |- Minio catch InternalException in [{}].", function, e);
            throw new OssInternalException("Minio internal error.");
        } catch (InvalidKeyException e) {
            log.error("[Herodotus] |- Minio catch InvalidKeyException in [{}].", function, e);
            throw new OssInvalidKeyException("Minio key invalid.");
        } catch (InvalidResponseException e) {
            log.error("[Herodotus] |- Minio catch InvalidResponseException in [{}].", function, e);
            throw new OssInvalidResponseException("Minio response invalid.");
        } catch (IOException e) {
            log.error("[Herodotus] |- Minio catch IOException in [{}].", function, e);
            throw new OssIOException("Minio io error.");
        } catch (NoSuchAlgorithmException e) {
            log.error("[Herodotus] |- Minio catch NoSuchAlgorithmException in [{}].", function, e);
            throw new OssNoSuchAlgorithmException("Minio no such algorithm.");
        } catch (ServerException e) {
            log.error("[Herodotus] |- Minio catch ServerException in [{}].", function, e);
            throw new OssServerException("Minio server error.");
        } catch (XmlParserException e) {
            log.error("[Herodotus] |- Minio catch XmlParserException in in [{}].", function, e);
            throw new OssXmlParserException("Minio xml parser error.");
        } finally {
            close(minioClient);
        }
    }

    /**
     * 存储桶是否存在
     *
     * @param bucketExistsArgs {@link BucketExistsArgs}
     * @return true 存在，false 不存在
     */
    public boolean bucketExists(BucketExistsArgs bucketExistsArgs) {
        String function = "bucketExists";
        MinioClient minioClient = getMinioClient();

        try {
            return minioClient.bucketExists(bucketExistsArgs);
        } catch (ErrorResponseException e) {
            log.error("[Herodotus] |- Minio catch ErrorResponseException in [{}].", function, e);
            throw new OssErrorResponseException("Minio response error.");
        } catch (InsufficientDataException e) {
            log.error("[Herodotus] |- Minio catch InsufficientDataException in [{}].", function, e);
            throw new OssInsufficientDataException("Minio insufficient data error.");
        } catch (InternalException e) {
            log.error("[Herodotus] |- Minio catch InternalException in [{}].", function, e);
            throw new OssInternalException("Minio internal error.");
        } catch (InvalidKeyException e) {
            log.error("[Herodotus] |- Minio catch InvalidKeyException in [{}].", function, e);
            throw new OssInvalidKeyException("Minio key invalid.");
        } catch (InvalidResponseException e) {
            log.error("[Herodotus] |- Minio catch InvalidResponseException in [{}].", function, e);
            throw new OssInvalidResponseException("Minio response invalid.");
        } catch (IOException e) {
            log.error("[Herodotus] |- Minio catch IOException in [{}].", function, e);
            throw new OssIOException("Minio io error.");
        } catch (NoSuchAlgorithmException e) {
            log.error("[Herodotus] |- Minio catch NoSuchAlgorithmException in [{}].", function, e);
            throw new OssNoSuchAlgorithmException("Minio no such algorithm.");
        } catch (ServerException e) {
            log.error("[Herodotus] |- Minio catch ServerException in [{}].", function, e);
            throw new OssServerException("Minio server error.");
        } catch (XmlParserException e) {
            log.error("[Herodotus] |- Minio catch XmlParserException in [{}].", function, e);
            throw new OssXmlParserException("Minio xml parser error.");
        } finally {
            close(minioClient);
        }
    }

        /**
         * 创建存储桶
         * <p>
         * 该方法仅仅是 Minio 原始方法的封装，不包含校验等操作。
         *
         * @param makeBucketArgs {@link MakeBucketArgs}
         */
    public void makeBucket(MakeBucketArgs makeBucketArgs) {
        String function = "makeBucket";
        MinioClient minioClient = getMinioClient();

        try {
            minioClient.makeBucket(makeBucketArgs);
        } catch (ErrorResponseException e) {
            log.error("[Herodotus] |- Minio catch ErrorResponseException in [{}].", function, e);
            throw new OssErrorResponseException("Minio response error.");
        } catch (InsufficientDataException e) {
            log.error("[Herodotus] |- Minio catch InsufficientDataException in [{}].", function, e);
            throw new OssInsufficientDataException("Minio insufficient data error.");
        } catch (InternalException e) {
            log.error("[Herodotus] |- Minio catch InternalException in [{}].", function, e);
            throw new OssInternalException("Minio internal error.");
        } catch (InvalidKeyException e) {
            log.error("[Herodotus] |- Minio catch InvalidKeyException in [{}].", function, e);
            throw new OssInvalidKeyException("Minio key invalid.");
        } catch (InvalidResponseException e) {
            log.error("[Herodotus] |- Minio catch InvalidResponseException in [{}].", function, e);
            throw new OssInvalidResponseException("Minio response invalid.");
        } catch (IOException e) {
            log.error("[Herodotus] |- Minio catch IOException in [{}].", function, e);
            throw new OssIOException("Minio io error.");
        } catch (NoSuchAlgorithmException e) {
            log.error("[Herodotus] |- Minio catch NoSuchAlgorithmException in [{}].", function, e);
            throw new OssNoSuchAlgorithmException("Minio no such algorithm.");
        } catch (ServerException e) {
            log.error("[Herodotus] |- Minio catch ServerException in [{}].", function, e);
            throw new OssServerException("Minio server error.");
        } catch (XmlParserException e) {
            log.error("[Herodotus] |- Minio catch XmlParserException in [{}].", function, e);
            throw new OssXmlParserException("Minio xml parser error.");
        } finally {
            close(minioClient);
        }
    }

    /**
     * 删除一个空的存储桶 如果存储桶存在对象不为空时，删除会报错。
     *
     * @param removeBucketArgs {@link RemoveBucketArgs}
     */
    public void removeBucket(RemoveBucketArgs removeBucketArgs) {
        String function = "removeBucket";
        MinioClient minioClient = getMinioClient();

        try {
            minioClient.removeBucket(removeBucketArgs);
        } catch (ErrorResponseException e) {
            log.error("[Herodotus] |- Minio catch ErrorResponseException in [{}].", function, e);
            throw new OssErrorResponseException("Minio response error.");
        } catch (InsufficientDataException e) {
            log.error("[Herodotus] |- Minio catch InsufficientDataException in [{}].", function, e);
            throw new OssInsufficientDataException("Minio insufficient data error.");
        } catch (InternalException e) {
            log.error("[Herodotus] |- Minio catch InternalException in [{}].", function, e);
            throw new OssInternalException("Minio internal error.");
        } catch (InvalidKeyException e) {
            log.error("[Herodotus] |- Minio catch InvalidKeyException in [{}].", function, e);
            throw new OssInvalidKeyException("Minio key invalid.");
        } catch (InvalidResponseException e) {
            log.error("[Herodotus] |- Minio catch InvalidResponseException in [{}].", function, e);
            throw new OssInvalidResponseException("Minio response invalid.");
        } catch (IOException e) {
            log.error("[Herodotus] |- Minio catch IOException in [{}].", function, e);
            throw new OssIOException("Minio io error.");
        } catch (NoSuchAlgorithmException e) {
            log.error("[Herodotus] |- Minio catch NoSuchAlgorithmException in [{}].", function, e);
            throw new OssNoSuchAlgorithmException("Minio no such algorithm.");
        } catch (ServerException e) {
            log.error("[Herodotus] |- Minio catch ServerException in [{}].", function, e);
            throw new OssServerException("Minio server error.");
        } catch (XmlParserException e) {
            log.error("[Herodotus] |- Minio catch XmlParserException in in [{}].", function, e);
            throw new OssXmlParserException("Minio xml parser error.");
        } finally {
            close(minioClient);
        }
    }

    private List<BucketEntity> toEntities(List<io.minio.messages.Bucket> buckets) {
        if (CollectionUtils.isNotEmpty(buckets)) {
            return buckets.stream().map(toResult::convert).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }
}