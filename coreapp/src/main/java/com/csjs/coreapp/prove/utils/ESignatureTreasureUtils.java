package com.csjs.coreapp.prove.utils;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.eParam.*;
import com.csjs.coreapp.prove.entity.SysProve;
import com.csjs.coreapp.prove.entity.SysProveSubmit;
import com.csjs.coreapp.prove.service.ISysConfigService;
import com.csjs.coreapp.prove.service.ISysProveSubmitCheckService;
import com.csjs.coreapp.prove.service.ISysProveSubmitService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.Key;
import java.util.*;

@Component
public class ESignatureTreasureUtils {
    @Autowired
    private ISysConfigService sysConfigService;


    private String e_host = "http://125.72.35.211:8035";
    private String project_id = "1001003";
    private String signature = "5add874af25d089baff2eedce5f71f83";
    private String accountId = "f8b1aac4-b371-4000-ae77-020e4274607d";
    private String sealId = "5add874af25d089baff2eedce5f71f83";
    private String authorizationOrganizeId = "5add874af25d089baff2eedce5f71f83";

    public void setPram() {
        String e_host = sysConfigService.getValueByName(FinalUtils.e_host).getValueName();
        String project_id = sysConfigService.getValueByName(FinalUtils.e_x_timevale_project_id).getValueName();
        String signature = sysConfigService.getValueByName(FinalUtils.e_x_timevale_signature).getValueName();
        String accountId = sysConfigService.getValueByName(FinalUtils.e_accountId).getValueName();
        String sealId = sysConfigService.getValueByName(FinalUtils.e_sealId).getValueName();
        String authorizationOrganizeId = sysConfigService.getValueByName(FinalUtils.e_authorizationOrganizeId).getValueName();

        this.e_host = e_host;
        this.project_id = project_id;
        this.signature = signature;
        this.accountId = accountId;
        this.sealId = sealId;
        this.authorizationOrganizeId = authorizationOrganizeId;
    }

    /**
     * @param urlPath      下载路径
     * @param fileSavePath 下载存放目录,包含文件名
     */
    public File downloadFile(String urlPath, String fileSavePath) {

        File file = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        try {
            // 统一资源
            URL url = new URL(urlPath);
            // 连接类的父类，抽象类
            URLConnection urlConnection = url.openConnection();
            // http的连接类
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // 设定请求的方法，默认是GET
            httpURLConnection.setRequestMethod("GET");
            // 设置字符编码
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // 打开到此 URL 引用的资源的通信链接（如果尚未建立这样的连接）。
            httpURLConnection.connect();
            url.openConnection();
            bin = new BufferedInputStream(httpURLConnection.getInputStream());

            String path = fileSavePath;
            file = new File(path);
            out = new FileOutputStream(file);
            int size = 0;
            int len = 0;
            byte[] buf = new byte[1024];
            while ((size = bin.read(buf)) != -1) {
                len += size;
                out.write(buf, 0, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bin != null) {
                try {
                    bin.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * e签宝获取文件下载地址
     *
     * @return
     */
    public String getFile(String fileKeyPdf, String docId) {
        try {
            String url = e_host + "/V1/files/getDownloadUrl?docId=" + docId + "&fileKey=" + fileKeyPdf;
            String body = sendGet(url);
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Integer errCode = Integer.valueOf((Integer) jsonObject.get("errCode"));
            // 上传成功
            if (errCode == 0) {
                String downloadUrl = JSONUtil.parseObj(jsonObject.get("data")).get("downloadUrl").toString();
                return downloadUrl;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * e签宝文件上传
     *
     * @return
     */
    public String uploadFile(String filePath) {
        String strResponse = "";
        String uploadUrl = e_host + "/V1/files/upload";
        try {

            // 换行符
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // 定义数据分隔线
            String BOUNDARY = "========7d4a6d158c9";
            // 服务器地址
            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置为POST情
            conn.setRequestMethod("POST");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求头参数
            conn.setRequestProperty("X-timevale-project-id", project_id);
            conn.setRequestProperty("X-timevale-signature", signature);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            System.out.println(filePath);
            String fn = RandomStringUtils.randomAlphanumeric(16);
            //System.out.println(fn);
            // 上传文件
            File file = new File(filePath);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // 文件参数,photo参数名可以随意修改
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fn
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // 参数头设置完以后需要两个换行，然后才是参数内容
            sb.append(newLine);
            sb.append(newLine);

            // 将参数头的数据写入到输出流中
            out.write(sb.toString().getBytes());

            // 数据输入流,用于读取文件数据
            DataInputStream in = new DataInputStream(new FileInputStream(
                    file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // 每次读1KB数据,并且将文件数据写入到输出流中
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // 最后添加换行
            out.write(newLine.getBytes());
            in.close();

            // 定义最后数据分隔线，即--加上BOUNDARY再加上--。
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // 写上结尾标识
            out.write(end_data);
            out.flush();
            out.close();

            // 定义BufferedReader输入流来读取URL的响应
            String strLine = "";

            InputStream resin = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resin));
            while ((strLine = reader.readLine()) != null) {
                strResponse += strLine + "\n";
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONUtil.parseObj(strResponse);
        Integer errCode = Integer.valueOf((Integer) jsonObject.get("errCode"));
        // 上传成功
        if (errCode == 0) {
            String s = JSONUtil.parseObj(jsonObject.get("data")).get("fileKey").toString();
            return s;
        } else {
            return null;
        }
    }

    /**
     * e签宝签章流程
     *
     * @return
     */
    public String signetFlow(String fileKey, String docName, Integer posX, Integer posY, String subject) {
        try {
            String url = e_host + "/V1/signFlows/create";
            SignPo signPo = new SignPo();
            signPo.setSealId(sealId);
            signPo.setEdgePosition(2);
            signPo.setPosPage("1");
            signPo.setPosX(posX);
            signPo.setPosY(posY);
            signPo.setSignType(2);
            ArrayList<SignPo> signPos = new ArrayList<>();
            signPos.add(signPo);

            SignDocDetail signDocDetail = new SignDocDetail();
            signDocDetail.setDocFilekey(fileKey);
            signDocDetail.setSignPos(signPos);
            ArrayList<SignDocDetail> SignDocDetails = new ArrayList<>();
            SignDocDetails.add(signDocDetail);

            Signers signer = new Signers();
            signer.setAccountId(accountId);
            signer.setAuthorizationOrganizeId(authorizationOrganizeId);
            signer.setAccountType(1);
            signer.setAutoSign(true);
            signer.setSignDocDetails(SignDocDetails);
            ArrayList<Signers> signers = new ArrayList<>();
            signers.add(signer);

            SignDocs signDoc = new SignDocs();
            signDoc.setDocName(docName);
            signDoc.setDocFilekey(fileKey);
            ArrayList<SignDocs> signDocs = new ArrayList<>();
            signDocs.add(signDoc);

            ActiveMap activeMap = new ActiveMap();
            activeMap.setInitiatorAccountId(accountId);
            activeMap.setAccountType(1);
            activeMap.setSignDocs(signDocs);
            activeMap.setSigners(signers);
            activeMap.setSubject(subject);

            JSONObject jsonObject = JSONUtil.parseObj(activeMap);
            String post = post(url, jsonObject);
            return post;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 发送post 请求
     *
     * @param apiUrl
     * @param data
     * @return
     * @throws Exception
     */
    public String post(String apiUrl, JSONObject data) throws Exception {
        // 请求数据转为JSON字节流,作为HTTP请求体
        byte[] stream = data.toString().getBytes("UTF-8");
        // 签名数据,根据签名算法,对请求数据进行签名
        String signature = sign(stream);
        //System.out.println(signature);
        // 设置HTTP请求头
        HttpEntityEnclosingRequestBase req = new HttpPost(apiUrl);
        // project-id为用户的projectId
        req.addHeader("X-timevale-project-id", project_id);
        // signature为之前生成的签名
        req.addHeader("X-timevale-signature", signature);
        // 签名模式：1、sort-parameters——对参数名称按ASCII码排序后签名，2、package——对整个httpbody签名。默认为package模式
        req.addHeader("X-timevale-mode", "package");
        // 签名算法：1、HMAC-SHA256，2、RSA。默认为HMAC-SHA256
        req.addHeader("X-timevale-signature-algorithm", "HMAC-SHA256");

        // 设置HTTP请求体
        ContentType contentType = ContentType
                .create(ContentType.APPLICATION_JSON.getMimeType(), "UTF-8");
        AbstractHttpEntity entity = new ByteArrayEntity(stream, contentType);
        req.setEntity(entity);

        // 执行请求
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient cli = httpClientBuilder.build();
        HttpResponse res = cli.execute(req);
        int statusCode = res.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (200 != statusCode) {
            System.out.println(statusCode);
        }
        // 获取响应
        InputStream in = res.getEntity().getContent();

        byte[] resp = readStream(in);
        String strRes = new String(resp, "UTF-8");
        System.out.println(strRes);
        cli.close();
        return strRes;
    }

    private String sign(byte[] stream)
            throws Exception {
        // 获取消息验证码类的实例，算法选择"HmacSHA256"
        Mac mac = Mac.getInstance("HmacSHA256");

        // 获取安全密钥
        Key secKey = new SecretKeySpec(
                signature.getBytes("UTF-8"),
                mac.getAlgorithm());

        // 初始化
        mac.init(secKey);

        // 获得签名
        byte[] sign = mac.doFinal(stream);

        // 将byte[]格式的签名用binary编码转化为字符串返回
        return binaryEncode(sign);

    }

    public static String binaryEncode(byte[] data) {
        final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        StringBuilder builder = new StringBuilder();

        for (byte i : data) {
            builder.append(hexDigits[i >>> 4 & 0xf]);
            builder.append(hexDigits[i & 0xf]);
        }

        return builder.toString();
    }

    public static byte[] readStream(InputStream in) throws IOException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024 * 10];
        try {

            int n = 0;
            while ((n = in.read(buffer)) != -1) {
                output.write(buffer, 0, n);
            }

            return output.toByteArray();

        } finally {
            in.close();
            output.close();
        }
    }

    public String sendGet(String apiUrl) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = apiUrl;
            URL realUrl = new URL(urlNameString);

            URLConnection connection = realUrl.openConnection();
            String signatureStr = "";
            byte[] stream = signatureStr.getBytes("UTF-8");
            // 签名数据,根据签名算法,对请求数据进行签名
            String signature = sign(stream);

            connection.setRequestProperty("X-timevale-project-id", project_id);
            connection.setRequestProperty("X-timevale-signature", signature);
            connection.setRequestProperty("Content-type", "application/json");
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result = result + line;
            }
        } catch (Exception e2) {
            System.out.println("发送GET请求出现异常！" + e2);
            e2.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }
}
