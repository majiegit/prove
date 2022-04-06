package com.csjs.coreapp.prove.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.ObjectUtil;
import com.csjs.coreapp.commen.FinalUtils;
import com.csjs.coreapp.prove.entity.SysProveSubmitField;
import com.csjs.coreapp.prove.vo.SysProveFieldVo;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.doc.ProtectionType;
import com.spire.doc.Section;
import com.spire.doc.documents.ImageType;
import com.spire.doc.documents.Paragraph;
import com.spire.doc.documents.TextWrappingStyle;
import com.spire.doc.fields.DocPicture;
import com.spire.pdf.PdfDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class DocmentOperationUtils {
    @Autowired
    private FileFtpUtils fileFtpUtils;
    @Resource
    private ProveTpSystemFinalUtils proveTpSystemFinalUtils;

    /**
     * 文档转换PDF
     *
     * @param wordPath
     * @return
     */
    public String wordToPdf(String wordPath) {
        fileFtpUtils.download(wordPath);
        //实例化Document类的对象
        Document doc = new Document();

        //加载Word
        doc.loadFromFile(wordPath);
        String substring = wordPath.substring(0, wordPath.lastIndexOf(".")) + ".pdf";
        //保存为PDF格式
        doc.saveToFile(substring, FileFormat.PDF);
        FileUtil.del(wordPath);
        fileFtpUtils.del(wordPath);
        return substring;
    }

    /**
     * 文档盖章
     *
     * @return
     */
    public String wordToSignet(String wordFilePath, String imagePath, Float x, Float y) {
        //加载示例文档
        Document doc = new Document();
        doc.loadFromFile(wordFilePath);
        //获取指定段落
        Section section = doc.getSections().get(0);
        Paragraph paragraph = section.getParagraphs().get(1);
        DocPicture picture = paragraph.appendPicture(imagePath);

        //指定电子章位置
        picture.setHorizontalPosition(x);
        picture.setVerticalPosition(y);
        //设置电子章大小
        picture.setWidth(150);
        picture.setHeight(150);

        //设置图片浮于文字上方
        picture.setTextWrappingStyle(TextWrappingStyle.In_Front_Of_Text);
        //保存文档
        String proveName = UUID.randomUUID().toString() + wordFilePath.substring(wordFilePath.lastIndexOf("."));
        String proveWordPath = FinalUtils.PROVE_PROVE_PATH + proveName;
        // 文档只读
        doc.protect(ProtectionType.Allow_Only_Reading, "123456");
        doc.saveToFile(proveWordPath, FileFormat.Docx);
        doc.dispose();
        fileFtpUtils.upload(FinalUtils.PROVE_PROVE_PATH, FileUtil.file(proveWordPath));
        return proveWordPath;
    }

    /**
     * word 文件转图片
     *
     * @param wordFilePath
     * @return
     */
    public String wordToImage(String wordFilePath) {
        fileFtpUtils.download(wordFilePath);
        String base64Src = null;
        Document doc = new Document();
        doc.loadFromFile(wordFilePath);
        BufferedImage image = doc.saveToImages(0, ImageType.Bitmap);
        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64Src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
        return base64Src;
    }

    public List<String> readWordParams(String wordFilePath, String regex) {
        fileFtpUtils.download(wordFilePath);
        //加载示例文档
        Document document = new Document();
        document.loadFromFile(wordFilePath);
        Pattern c = Pattern.compile(regex);
        LinkedList<String> params = new LinkedList<>();
        String text1 = document.getText();
        Matcher matcher = c.matcher(text1);
        // 处理匹配到的值
        while (matcher.find()) {
            String group = matcher.group();
            String param = group.substring(2, group.length() - 1);
            params.add(param);
        }
        List<String> collect = params.stream().distinct().collect(Collectors.toList());
        return collect;
    }
    /**
     * wrod 文本替换
     */
    public void wordContentReplace(String tpWordPath, String proveWordPath, ArrayList<HashMap<String, Object>> hashMaps,
                                   Map<String, Object> data, ArrayList<SysProveFieldVo> sysProveFieldVos, String userId) {
        //加载Word文档
        Document document = new Document(tpWordPath);
        // SQL文本替换文档中的指定文本
        hashMaps.stream().forEach(item -> {
            String varName = item.get("name").toString();
            Object varField = item.get("field");
            String varValue = "";
            if (ObjectUtil.isNotEmpty(varField)) {
                Object value = data.get(varField);
                if (ObjectUtil.isNotEmpty(value)) {
                    varValue = value.toString();
                }
            }
            document.replace("${" + varName + "}", varValue, false, true);
        });

        // 单据字段替换模板
        for (SysProveFieldVo item : sysProveFieldVos) {
            if (FinalUtils.FIELD_TYPE_SUBMIT.equals(item.getFieldType())) {
                // 单据变量
            } else if (FinalUtils.FIELD_TYPE_SYSTEM.equals(item.getFieldType())) {
                //  替换固定变量
                document.replace("@{" + item.getFieldName() + "}", proveTpSystemFinalUtils.getValue(item.getFieldName(),userId), false, true);
            } else if (FinalUtils.FIELD_TYPE_TEMP.equals(item.getFieldType())) {
                //  替换模板填写变量
                document.replace("&{" + item.getFieldName() + "}", item.getFieldValue(), false, true);
            }
        }
        //保存文档
        document.saveToFile(proveWordPath, FileFormat.Docx_2013);
        fileFtpUtils.upload(FinalUtils.PROVE_PROVE_PATH, FileUtil.file(proveWordPath));
    }

    /**
     * wrod 文本替换返回预览Base64图片
     */
    public String wordContentReplaceBase64Image(String tpWordPath, ArrayList<HashMap<String, Object>> hashMaps, Map<String, Object> data) {
        fileFtpUtils.download(tpWordPath);
        //加载Word文档
        String base64Src = "";
        Document document = new Document(tpWordPath);
        try {
            //使用新文本替换文档中的指定文本
            hashMaps.stream().forEach(item -> {
                String varName = item.get("name").toString();
                Object varField = item.get("field");
                String varValue = " ";
                if (ObjectUtil.isNotEmpty(varField)) {
                    Object value = data.get(varField);
                    if (ObjectUtil.isNotEmpty(value)) {
                        varValue = value.toString();
                    }
                }
                document.replace("${" + varName + "}", varValue, false, true);
            });
            BufferedImage image = document.saveToImages(0, ImageType.Bitmap);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", stream);
            base64Src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
        return base64Src;
    }

    /**
     * pdf  转图片
     */
    public static String pdfToImage(String filePath) {
        String base64Src = null;
        //加载PDF文件
        PdfDocument doc = new PdfDocument();
        doc.loadFromFile(filePath);
        try {
            //保存PDF的每一页到图片
            BufferedImage image;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            for (int i = 0; i < doc.getPages().getCount(); i++) {
                image = doc.saveAsImage(i);
                ImageIO.write(image, "PNG", stream);
            }
            base64Src = "data:image/jpg;base64," + Base64.getEncoder().encodeToString(stream.toByteArray());
            doc.close();
        } catch (Exception e) {
            doc.close();
        }
        return base64Src;
    }
}
