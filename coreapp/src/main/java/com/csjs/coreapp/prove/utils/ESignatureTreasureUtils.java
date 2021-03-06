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
     * @param urlPath      ????????????
     * @param fileSavePath ??????????????????,???????????????
     */
    public File downloadFile(String urlPath, String fileSavePath) {

        File file = null;
        BufferedInputStream bin = null;
        OutputStream out = null;
        try {
            // ????????????
            URL url = new URL(urlPath);
            // ??????????????????????????????
            URLConnection urlConnection = url.openConnection();
            // http????????????
            HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
            // ?????????????????????????????????GET
            httpURLConnection.setRequestMethod("GET");
            // ??????????????????
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // ???????????? URL ????????????????????????????????????????????????????????????????????????
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
     * e??????????????????????????????
     *
     * @return
     */
    public String getFile(String fileKeyPdf, String docId) {
        try {
            String url = e_host + "/V1/files/getDownloadUrl?docId=" + docId + "&fileKey=" + fileKeyPdf;
            String body = sendGet(url);
            JSONObject jsonObject = JSONUtil.parseObj(body);
            Integer errCode = Integer.valueOf((Integer) jsonObject.get("errCode"));
            // ????????????
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
     * e??????????????????
     *
     * @return
     */
    public String uploadFile(String filePath) {
        String strResponse = "";
        String uploadUrl = e_host + "/V1/files/upload";
        try {

            // ?????????
            final String newLine = "\r\n";
            final String boundaryPrefix = "--";
            // ?????????????????????
            String BOUNDARY = "========7d4a6d158c9";
            // ???????????????
            URL url = new URL(uploadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // ?????????POST???
            conn.setRequestMethod("POST");
            // ??????POST??????????????????????????????
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // ?????????????????????
            conn.setRequestProperty("X-timevale-project-id", project_id);
            conn.setRequestProperty("X-timevale-signature", signature);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charsert", "UTF-8");
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

            OutputStream out = new DataOutputStream(conn.getOutputStream());
            System.out.println(filePath);
            String fn = RandomStringUtils.randomAlphanumeric(16);
            //System.out.println(fn);
            // ????????????
            File file = new File(filePath);
            StringBuilder sb = new StringBuilder();
            sb.append(boundaryPrefix);
            sb.append(BOUNDARY);
            sb.append(newLine);
            // ????????????,photo???????????????????????????
            sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + fn
                    + "\"" + newLine);
            sb.append("Content-Type:application/octet-stream");
            // ?????????????????????????????????????????????????????????????????????
            sb.append(newLine);
            sb.append(newLine);

            // ??????????????????????????????????????????
            out.write(sb.toString().getBytes());

            // ???????????????,????????????????????????
            DataInputStream in = new DataInputStream(new FileInputStream(
                    file));
            byte[] bufferOut = new byte[1024];
            int bytes = 0;
            // ?????????1KB??????,??????????????????????????????????????????
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            // ??????????????????
            out.write(newLine.getBytes());
            in.close();

            // ?????????????????????????????????--??????BOUNDARY?????????--???
            byte[] end_data = (newLine + boundaryPrefix + BOUNDARY + boundaryPrefix + newLine)
                    .getBytes();
            // ??????????????????
            out.write(end_data);
            out.flush();
            out.close();

            // ??????BufferedReader??????????????????URL?????????
            String strLine = "";

            InputStream resin = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(resin));
            while ((strLine = reader.readLine()) != null) {
                strResponse += strLine + "\n";
            }
        } catch (Exception e) {
            System.out.println("??????POST?????????????????????" + e);
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONUtil.parseObj(strResponse);
        Integer errCode = Integer.valueOf((Integer) jsonObject.get("errCode"));
        // ????????????
        if (errCode == 0) {
            String s = JSONUtil.parseObj(jsonObject.get("data")).get("fileKey").toString();
            return s;
        } else {
            return null;
        }
    }

    /**
     * e??????????????????
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
     * ??????post ??????
     *
     * @param apiUrl
     * @param data
     * @return
     * @throws Exception
     */
    public String post(String apiUrl, JSONObject data) throws Exception {
        // ??????????????????JSON?????????,??????HTTP?????????
        byte[] stream = data.toString().getBytes("UTF-8");
        // ????????????,??????????????????,???????????????????????????
        String signature = sign(stream);
        //System.out.println(signature);
        // ??????HTTP?????????
        HttpEntityEnclosingRequestBase req = new HttpPost(apiUrl);
        // project-id????????????projectId
        req.addHeader("X-timevale-project-id", project_id);
        // signature????????????????????????
        req.addHeader("X-timevale-signature", signature);
        // ???????????????1???sort-parameters????????????????????????ASCII?????????????????????2???package???????????????httpbody??????????????????package??????
        req.addHeader("X-timevale-mode", "package");
        // ???????????????1???HMAC-SHA256???2???RSA????????????HMAC-SHA256
        req.addHeader("X-timevale-signature-algorithm", "HMAC-SHA256");

        // ??????HTTP?????????
        ContentType contentType = ContentType
                .create(ContentType.APPLICATION_JSON.getMimeType(), "UTF-8");
        AbstractHttpEntity entity = new ByteArrayEntity(stream, contentType);
        req.setEntity(entity);

        // ????????????
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        CloseableHttpClient cli = httpClientBuilder.build();
        HttpResponse res = cli.execute(req);
        int statusCode = res.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (200 != statusCode) {
            System.out.println(statusCode);
        }
        // ????????????
        InputStream in = res.getEntity().getContent();

        byte[] resp = readStream(in);
        String strRes = new String(resp, "UTF-8");
        System.out.println(strRes);
        cli.close();
        return strRes;
    }

    private String sign(byte[] stream)
            throws Exception {
        // ????????????????????????????????????????????????"HmacSHA256"
        Mac mac = Mac.getInstance("HmacSHA256");

        // ??????????????????
        Key secKey = new SecretKeySpec(
                signature.getBytes("UTF-8"),
                mac.getAlgorithm());

        // ?????????
        mac.init(secKey);

        // ????????????
        byte[] sign = mac.doFinal(stream);

        // ???byte[]??????????????????binary??????????????????????????????
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
            // ????????????,??????????????????,???????????????????????????
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
            System.out.println("??????GET?????????????????????" + e2);
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
