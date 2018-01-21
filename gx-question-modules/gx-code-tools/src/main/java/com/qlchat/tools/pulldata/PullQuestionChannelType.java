package com.qlchat.tools.pulldata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;

public class PullQuestionChannelType {
    // shift + option + /
    // alt + enter  ---- import class
    public static void main(String[] args) throws Exception {


        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT * FROM gx_chid_xd_relate;";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                System.out.println(moduleObj);
                int chid = moduleObj.getInteger("chid_id");
                int xd = moduleObj.getInteger("xd_id");

//                handleQuestionChannelType(chid, xd);
//                handleMaterial(chid, xd);
//                handleGrade(chid, xd);
//                handleChapter(chid, xd);
                handleChapterTopic(chid, xd);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 章节下的课程
    // 年级下的章节
    // 版本下面的年级
    private static void handleChapterTopic(int chid , int xd)throws Exception{
        // 查询教材版本
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT grade_id, textbook_version_id, id FROM gx_chapter WHERE chid_id="+chid+" and xd_id="+xd+";";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                int grade_id = moduleObj.getInteger("grade_id");
                int textbook_version_id = moduleObj.getInteger("textbook_version_id");
                int chatperId = moduleObj.getInteger("id");
                System.out.println(chid + ", xd=" + xd + ", grade_id=" +grade_id);

                String url = "https://zujuan.21cnjy.com/question/tree?id="+chatperId+"&type=category&chid="+chid+"&xd="+xd;
                String result = HttpUtil.getInstance().doGetForString(url);
                System.out.println(result);
                JSONArray resultObj = (JSONArray)JSONObject.parse(result);
                System.out.println(resultObj);
                for (int i = 0; i < resultObj.size(); i++) {
                    JSONObject obj = resultObj.getJSONObject(i);
                    int id = obj.getInteger("id");
                    String title = obj.getString("title");
                    boolean hasChild = obj.getBoolean("hasChild");

                    String insertSql = String.format("INSERT INTO gx_chapter_topic (id, chid_id, xd_id, text_version_id, chapter_id, title, grade_id, has_child) VALUES (%s, %s,%s, %s,%s, %s,%s,%s);"
                            , id, chid, xd, textbook_version_id, chatperId, "'"+title+"'", grade_id, hasChild);
                    System.out.println(insertSql);
                    ConnectUtil.insertInto(insertSql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // 年级下的章节
    // 版本下面的年级
    private static void handleChapter(int chid , int xd)throws Exception{
        // 查询教材版本
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT grade_id, textbook_version_id FROM gx_chid_xd_grade WHERE chid_id="+chid+" and xd_id="+xd+";";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                int grade_id = moduleObj.getInteger("grade_id");
                int textbook_version_id = moduleObj.getInteger("textbook_version_id");
                System.out.println(chid + ", xd=" + xd + ", grade_id=" +grade_id);

                String url = "https://zujuan.21cnjy.com/question/tree?id="+grade_id+"&type=category&chid="+chid+"&xd="+xd;
                String result = HttpUtil.getInstance().doGetForString(url);
                System.out.println(result);
                JSONArray resultObj = (JSONArray)JSONObject.parse(result);
                System.out.println(resultObj);
                for (int i = 0; i < resultObj.size(); i++) {
                    JSONObject obj = resultObj.getJSONObject(i);
                    int id = obj.getInteger("id");
                    String title = obj.getString("title");
                    boolean hasChild = obj.getBoolean("hasChild");

                    String insertSql = String.format("INSERT INTO gx_chapter (id, chid_id, xd_id, textbook_version_id, grade_id, title, has_child) VALUES (%s, %s,%s, %s,%s, %s,%s);"
                            , id, chid, xd, textbook_version_id, grade_id, "'"+title+"'", hasChild);
                    System.out.println(insertSql);
                    ConnectUtil.insertInto(insertSql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // 版本下面的年级
    private static void handleGrade(int chid , int xd)throws Exception{
        // 查询教材版本
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT textbook_version_id FROM gx_chid_xd_textbook_version WHERE chid_id="+chid+" and xd_id="+xd+";";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                int material_id = moduleObj.getInteger("material_id");
                System.out.println(chid + ", xd=" + xd + ", materialId=" +material_id);

                String url = "https://zujuan.21cnjy.com/question/tree?id="+material_id+"&type=category&chid="+chid+"&xd="+xd;
                String result = HttpUtil.getInstance().doGetForString(url);
                System.out.println(result);
                JSONArray resultObj = (JSONArray)JSONObject.parse(result);
                System.out.println(resultObj);
                for (int i = 0; i < resultObj.size(); i++) {
                    JSONObject obj = resultObj.getJSONObject(i);
                    int id = obj.getInteger("id");
                    String title = obj.getString("title");
                    boolean hasChild = obj.getBoolean("hasChild");

                    String insertSql = String.format("INSERT IGNORE INTO gx_grade (id, name_) VALUES (%s, %s);", id, "'"+title+"'");
                    System.out.println(insertSql);
                    ConnectUtil.insertInto(insertSql);
                    insertSql = String.format("INSERT INTO gx_chid_xd_grade (chid_id, xd_id, material_id, grade_id, has_child) VALUES (%s, %s, %s, %s, %s)", chid, xd, material_id , id, hasChild);
                    System.out.println(insertSql);
                    ConnectUtil.insertInto(insertSql);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    // 教材版本
    private static void handleMaterial(int chid , int xd)throws Exception{
        String url = "https://zujuan.21cnjy.com/question/tree?id=0&type=category&chid="+chid+"&xd="+xd;
        String result = HttpUtil.getInstance().doGetForString(url);
        System.out.println(result);
        JSONArray resultObj = (JSONArray)JSONObject.parse(result);
        System.out.println(resultObj);
        for (int i = 0; i < resultObj.size(); i++) {
            JSONObject obj = resultObj.getJSONObject(i);
            int id = obj.getInteger("id");
            String title = obj.getString("title");
            boolean hasChild = obj.getBoolean("hasChild");
            // ENUM('BOOK_VERSION', 'GRADE', 'CHAPTER', 'TOPIC')
//            String insertSql = String.format("insert INTO gx_category_tree (id, chid_id, xd_id, parent_id, title, has_child, type_) VALUES (%s, %s, %s, %s, %s, %s, %s)"
//                    , id, chid, xd, 0, "'"+title+"'", hasChild, "'BOOK_VERSION'");
            String insertSql = String.format("insert IGNORE INTO gx_material (id, name_) VALUES (%s, %s);", id, "'"+title+"'");
            System.out.println(insertSql);
            ConnectUtil.insertInto(insertSql);
            insertSql = String.format("INSERT INTO gx_chid_xd_material (material_id, chid_id, xd_id, has_child) VALUES (%s, %s, %s, %s)", id, chid, xd, hasChild);
            System.out.println(insertSql);
            ConnectUtil.insertInto(insertSql);
        }
    }

    // 每个年级对应的题型
    private static void handleQuestionChannelType(int chid, int xd) throws Exception {
        String url = "https://zujuan.21cnjy.com/question?chid="+chid+"&xd="+xd+"&tree_type=knowledge";
        String result = HttpUtil.getInstance().doGetForString(url);

        Parser parser = Parser.createParser(result, "utf-8");
        // 遍历所有的节点
        // http://free0007.iteye.com/blog/1131163
        NodeFilter divFilter = new NodeFilter() {
            @Override
            public boolean accept(Node node) {
                if(node.getText().contains("class=\"con-items\"")){
                    return true;
                }else{
                    return false;
                }
            }
        };

        NodeList nodes = parser.extractAllNodesThatMatch(divFilter);

        for (int type = 0; type < nodes.size(); type++) {
            Node node = nodes.elementAt(type);
            Document doc = Jsoup.parse(node.toHtml());
            Elements elements = doc.getElementsByAttribute("data-name");
            for (int i2 = 0; i2 < elements.size(); i2++) {
                System.out.println(elements.get(i2));
                String dataValue = elements.get(i2).attr("data-value");
                String textValue = elements.get(i2).text();

                if(StringUtils.isNotEmpty(dataValue)){

                    String channelTypeInsertSql = "INSERT IGNORE INTO gx_question_channel_type\n" +
                            "(id, name_)\n" +
                            "VALUES\n" +
                            "  ("+dataValue+", '"+textValue+"')";
                    ConnectUtil.insertInto(channelTypeInsertSql);
                    if(type == 0){
                        // <a data-name="question_channel_type" data-value="1" href="#">单选题</a>
                        String insertSql = "INSERT INTO gx_chid_xd_question_channel_type (chid_id, xd_id, question_channel_type_id) VALUES ("+chid+", "+xd+", "+dataValue+");";
                        System.out.println(insertSql + textValue);
                        ConnectUtil.insertInto(insertSql);
                    }else if(type == 1){
                        // // <a data-name="difficult_index" data-value="1" href="#">容易</a>
                        // 就三种：1=容易，2=普通，3=困难
                    }else if(type == 3){
                        // <a data-name="exam_type" data-value="1" href="#">中考真题</a>
                        // 1=小升初真题/中考真题/高考真题, 2常考题，7模拟题
                    }else if(type == 4){
                        // <a data-name="kid_num" data-value="1" href="#">1个知识点</a>
                        // 1=1个，2=2个，3=3个，4=4个
                    }
                }
            }
        }
    }
}
