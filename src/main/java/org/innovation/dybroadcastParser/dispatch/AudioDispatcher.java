package org.innovation.dybroadcastParser.dispatch;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.cvm.v20170312.CvmClient;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesRequest;
import com.tencentcloudapi.cvm.v20170312.models.DescribeInstancesResponse;

import javax.crypto.SecretKey;

public class AudioDispatcher {

    public void test(){
        try {
//            主账号ID 100012403366
//            用户名 audioUser
//            登录密码 %sluCQOl
//            SecretId AKIDoU7b8TjohpoiS4Yszta5NRLi7NQgojT3
//            SecretKey Vu9Azd5JJ7k9rPmyymNkwxmeNwLg91iF
            Credential cred = new Credential("AKIDoU7b8TjohpoiS4Yszta5NRLi7NQgojT3", "Vu9Azd5JJ7k9rPmyymNkwxmeNwLg91iF");
            CvmClient client = new CvmClient(cred, "ap-shanghai");

            DescribeInstancesRequest req = new DescribeInstancesRequest();
            DescribeInstancesResponse resp = client.DescribeInstances(req);

            System.out.println(DescribeInstancesResponse.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }
    }
}
