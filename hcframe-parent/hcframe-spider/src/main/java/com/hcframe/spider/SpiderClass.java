package com.hcframe.spider;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.apache.commons.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Set;

public class SpiderClass {

    public static void main(String[] args)  {
        String str = "000000010001";
        System.out.println(str.length());
//        login();
//        WebClient client = getInstance();
//        try {
////            HtmlPage page  = client.getPage("https://www.letpub.com.cn/index.php?page=grant#opennewwindow");
//            HtmlPage page = client.getPage("https://yswk.csdl.ac.cn/style/module/yswk/page/zylxlist.jsp?id=ZZ");
//            Thread.sleep(10000);
////            System.out.println("+++++++++++++++++++++++++++++"+page.getTextContent());
//            DomElement domElement = page.getElementById("aa");
//            System.out.println("+++++++++"+domElement.asText());
//
////            Set<Cookie> cookies=null;
////            DomNodeList<DomElement> list = page.getElementsByTagName("li");
////            for (DomElement domElement : list) {
////                String text = domElement.asText();
////                System.out.println(text);
////            }
////            HtmlSelect selectA = (HtmlSelect) page.getElementById("addcomment_s1_bysubject_s");
////            selectA.setSelectedAttribute("F", true);
////            HtmlSelect selectB = (HtmlSelect) page.getElementById("addcomment_s2_bysubject_s");
////            selectB.setSelectedAttribute("F06", true);
////            HtmlOption htmlOption = selectB.getOptionByValue("F06");
////            String selB = htmlOption.asText();
////            HtmlSelect selectC = (HtmlSelect) page.getElementById("addcomment_s3_bysubject_s");
////            List<HtmlOption> cList = selectC.getOptions();
////            int i = 0;
////            for (HtmlOption c : cList) {
////                if (i > 0) {
////                    String selC = c.asText();
////                    selectC.setSelectedAttribute(c, true);
////                    HtmlSelect selectD = (HtmlSelect) page.getElementById("addcomment_s4_bysubject_s");
////                    List<HtmlOption> dList = selectD.getOptions();
////                    int j = 0;
////                    for (HtmlOption d : dList) {
////                        if (j != 0) {
////                            String selD = d.asText();
////                            selectD.setSelectedAttribute(d, true);
////                            DomElement element = page.getElementById("searchform_bysubject_s");
////                            HtmlInput htmlInput = (HtmlInput) element.querySelector("input[value='advSearch']");
////                            htmlInput.click();
////                            DomNodeList<DomElement> buttons = page.getElementsByTagName("button");
////                            Page p = null;
////                            for (DomElement domElement : buttons) {
////                                String str = domElement.asText();
////                                if (str.contains("下载Excel")) {
////                                    p = domElement.click();
////                                    break;
////                                }
////                            }
////                            if (p != null) {
////                                File file = new File("/Volumes/DATA/letpub/" + selC);
////                                file.mkdir();
////                                saveFile(p,"/Volumes/DATA/letpub/"+selC+"/"+selB+"-"+selC+"-"+selD+".xls");
////                            }
////                        }
////                        j++;
////                        Thread.sleep(10*1000);
////                    }
////                }
////                i++;
////            }
////            selectC.setSelectedAttribute("F0101", true);
////            HtmlSelect selectD = (HtmlSelect) page.getElementById("addcomment_s4_bysubject_s");
////            selectD.setSelectedAttribute("F010101", true);
////            DomElement element = page.getElementById("searchform_bysubject_s");
////            HtmlInput htmlInput = (HtmlInput) element.querySelector("input[value='advSearch']");
////            htmlInput.click();
////            System.out.println("++++++");
////            System.out.println(page.asText());
////            DomNodeList<DomElement> buttons = page.getElementsByTagName("button");
////            Page p = null;
////            for (DomElement domElement : buttons) {
////                String str = domElement.asText();
////                if (str.contains("下载Excel")) {
////                    p = domElement.click();
////                    break;
////                }
////            }
////            assert p != null;
////            saveFile(p,"/Volumes/DATA/a.xls");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private static class innerWebClient{
        private static final WebClient WEB_CLIENT = new WebClient(BrowserVersion.CHROME);
    }


    public static WebClient getInstance(){
        return innerWebClient.WEB_CLIENT;
    }

    /**
     * 获取指定网页实体
     * @param url
     * @return
     */
    public static HtmlPage getHtmlPage(String url){
        return null;
    }

    public static WebClient getClient() {
        //调用此方法时加载WebClient
        WebClient  client= innerWebClient.WEB_CLIENT;
        client.getCookieManager().setCookiesEnabled(true);
        // 取消 JS 支持
        client.getOptions().setRedirectEnabled(true);
        client.getOptions().setUseInsecureSSL(true);
        client.getOptions().setCssEnabled(false);
        // 取消 CSS 支持
        client.setAjaxController(new NicelyResynchronizingAjaxController());
        return client;
    }
    public static void login()  {
        WebClient client = getClient();
        try {
            HtmlPage page  = client.getPage("https://www.letpub.com.cn/index.php?page=login");
            Set<Cookie> cookies=null;
            HtmlInput user = page.getHtmlElementById("email");
            user.setValueAttribute("taixingyiji@126.com");
            HtmlInput pwd = page.getHtmlElementById("password");
            pwd.setValueAttribute("G4VnNjgVLczrNV6");
            DomNodeList<DomElement> imgs = page.getElementsByTagName("img");
            HtmlPage htmlPage = null;
            for (DomElement htmlElement : imgs) {
                HtmlImage img = (HtmlImage) htmlElement;
                String src = img.getSrcAttribute();
                if (src.contains("userlogin")) {
                    htmlPage = (HtmlPage) img.click();
                }
            }
            System.out.println(htmlPage.asText());
            cookies = client.getCookieManager().getCookies();
            for (Cookie cookie : cookies) {
                System.out.println(cookie.getName()+":"+cookie.getValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void saveFile(Page page, String file) throws Exception {
        InputStream is = page.getWebResponse().getContentAsStream();
        FileOutputStream output = new FileOutputStream(file);
        IOUtils.copy(is, output);
        output.close();
        is.close();
    }

}
