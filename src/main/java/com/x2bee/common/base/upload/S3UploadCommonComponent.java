package com.x2bee.common.base.upload;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
//@Component
//@ConfigurationProperties(prefix = "cloud.aws.s3")
public class S3UploadCommonComponent {

	private String bucket;

}
