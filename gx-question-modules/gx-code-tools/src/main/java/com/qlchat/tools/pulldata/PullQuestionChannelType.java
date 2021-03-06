package com.qlchat.tools.pulldata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.protocol.HTTP;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.util.NodeList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PullQuestionChannelType {
    private static List<Integer> questionIdList = new ArrayList<>();
    private static boolean endPage = false;// 是否结束分页
    private static BufferedWriter out = null;
    private static Map<String, Integer> repeatMap = new HashMap<>();
    // shift + option + /
    // alt + enter  ---- import class
    public static void main(String[] args) throws Exception {
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/Users/kai.zhang/pulldatalog", true)));
            blankRequest(2, 3);
            handleCategoryQuestion();
    //        handleQuestion(2, 3, 6136,6136, 1);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static void handlerChidXd() throws Exception {
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT * FROM gx_chid_xd_relate ;";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                out.write(moduleObj.toJSONString());
                out.write("\r\n");
                int chid = moduleObj.getInteger("chid_id");
                int xd = moduleObj.getInteger("xd_id");

                blankRequest(chid, xd);

//                handleQuestionChannelType(chid, xd);
//                handleMaterial(chid, xd);
//                handleGrade(chid, xd);
//                handleChapter(chid, xd);
//                handleChapterTopic(chid, xd);
//                handleCategoryTree(chid, xd, 0, 0,"category");
//                handleCategoryTree(chid, xd, 0, 0, "knowledge");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void handleCategoryQuestion() throws Exception {
        /** 1. 先查询小学数学下的教材版本
         * 2. 查询教材下的年级分类
         * 3. 查询年级下的章节
         * 4. 按照章节查询题目
         * */
        // 查询没有子类的课程查询题目
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT * from gx_category_tree t where t.has_child=false and t.tree_type='category' and id>=9541 ORDER  BY  id; ";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement)conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                out.write(moduleObj.toJSONString());
                out.write("\r\n");
                int chid = moduleObj.getInteger("chid_id");
                int xd = moduleObj.getInteger("xd_id");
                int categoryId = moduleObj.getInteger("id");
                String title = moduleObj.getString("title");
                int parentId = moduleObj.getInteger("parent_id");

                blankRequest(chid, xd);

                CollectorThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                    }
                });

                questionIdList = new ArrayList<>();
                endPage = false;

                int page = 1;
                while (true){
                    int size = handleQuestion(chid, xd, categoryId, parentId, page);
                    out.write(categoryId + "正在查询:" + title + ", 第几页=" + page + ", 查询出条数=" + size);
                    out.write("\r\n");
                    page ++;
                    if(size < 10  || endPage){
                        break;
                    }
                }


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }



    private static int handleQuestion(int chid, int xd, int categoryId, int parentId, int page) throws Exception {
        String gradeQuery = "";
        if(xd == 1){
            gradeQuery = "&grade_id%5B%5D=0&grade_id%5B%5D=1&grade_id%5B%5D=2&grade_id%5B%5D=3&grade_id%5B%5D=4&grade_id%5B%5D=5&grade_id%5B%5D=6";
        }else if(xd == 2){
            gradeQuery = "&grade_id%5B%5D=0&grade_id%5B%5D=7&grade_id%5B%5D=8&grade_id%5B%5D=9";
        }else if(xd ==3){
            gradeQuery = "&grade_id%5B%5D=0&grade_id%5B%5D=10&grade_id%5B%5D=11&grade_id%5B%5D=12&grade_id%5B%5D=13";
        }
//        gradeQuery = URLDecoder.decode(gradeQuery, "utf-8");

        String url = "https://zujuan.21cnjy.com/question/list?categories="+categoryId+"&question_channel_type=&difficult_index=&exam_type=&kid_num="+gradeQuery+"&sortField=time&filterquestion=0&page="+page+"&page="+page+"&_="+new Date().getTime();
        String result = HttpUtil.getInstance().doGetForString(url);
        out.write("url=" +url +", result=" + result);
        out.write("\r\n");
        if(result == null){
            String key = categoryId + "" + page;
            Integer repeat = repeatMap.get(key);
            if(repeat != null && repeat > 10){
//                // 如果重复三次都失败，则放弃
                out.write("url=" +url +", 重复100次都失败，则放弃 result=" + result);
                out.write("\r\n");
//                return 0;
                MailUtil.sendMail("重复100次都失败，则放弃", "系统退出，请重试" + categoryId, new String[]{"kai.zhang@qlchat.com"});
                System.exit(1);
            }
            if(repeat == null){
                repeat = 0;
            }
            repeatMap.put(key, ++repeat);
            out.write("重复次数=" + repeat + "\r\n");
            return handleQuestion(chid, xd, categoryId, parentId, page);// 重试
        }

        // 查询categoryId章节对应的教材ID和年级ID
        int gradeversionId = findParentCategory(parentId, "GRADE");
        int textBookversionId = findParentCategory(parentId, "TEXT_BOOK_VERSION");

        // 返回数据数组大小
        JSONObject resultObj = (JSONObject)JSONObject.parse(result);
        JSONArray data = resultObj.getJSONArray("data");
        int size = 0;
        if(data != null && data.size() > 0){
            JSONArray array = data.getJSONObject(0).getJSONArray("questions");
            size = array.size();
            for (int i = 0; i < array.size(); i++) {
                JSONObject obj = array.getJSONObject(i);
                iterorQuestion(obj, chid, xd, categoryId, textBookversionId, gradeversionId);
            }
        }

        return size;
    }

    private static int findParentCategory(int categoryId, String type){
        Connection conn = ConnectUtil.getConn();
        String sql = "SELECT * from gx_category_tree t where t.id="+categoryId+"; ";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            JSONArray resultJson = ConnectUtil.handleToJson(rs);
            Iterator<Object> it = resultJson.iterator();
            while (it.hasNext()) {
                JSONObject moduleObj = (JSONObject) it.next();
                int parentCategoryId = moduleObj.getInteger("id");
                int parentId = moduleObj.getInteger("parent_id");
                String type_ = moduleObj.getString("type_");
                if(type.equals(type_)){
                    return parentCategoryId;
                }else{
                    return findParentCategory(parentId, type);
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private static void iterorQuestion(JSONObject obj, int chid, int xd, int categoryId, int textBookversionId, int gradeversionId) throws Exception {
        int question_id = obj.getInteger("question_id");

        if(questionIdList.contains(question_id)){
            out.write("重复分页了，强制结束分页。。。。。。。。。");
            endPage = true;
            return;
        }

        int question_channel_type = obj.getInteger("question_channel_type");
        int parent_id = obj.getInteger("parent_id");
        int difficult_index = obj.getInteger("difficult_index");
        int exam_type = obj.getInteger("exam_type");
        int grade_id = obj.getInteger("grade_id");
        int kid_num = obj.getInteger("kid_num");
        String region_ids = obj.getString("region_ids");
        String question_text = obj.getString("question_text");

        JSONObject scoreObj = obj.getJSONObject("score");
        int score = 0;
        String subscore = null;
        if(scoreObj != null){
            score = scoreObj.getInteger("score");
            subscore = scoreObj.getString("subscore");
        }
        int sort = obj.getInteger("sort");

        JSONObject paper = obj.getJSONObject("paper");
        int paperId = 0;
        if(paper != null && !"".equals(paper)){
            paperId = paper.getInteger("pid");
            String title = paper.getString("title");
            // 插入试卷
            String inserPaperSql = "INSERT IGNORE INTO gx_test_paper (id, title, chid_id, xd_id,grade_id,text_book_version_id,grade_version_id) VALUES (?,?,?,?,?,?,?);";
            List<Object> paperParam = new ArrayList<>();
            paperParam.add(paperId);
            paperParam.add(title);
            paperParam.add(chid);
            paperParam.add(grade_id);
            paperParam.add(xd);
            paperParam.add(textBookversionId);
            paperParam.add(gradeversionId);
            ConnectUtil.insertInto(inserPaperSql, paperParam);
            // 插入试卷和题目中间表
            String inserPaperQuestionSql = "INSERT IGNORE INTO gx_paper_question (paper_id, question_id, score_, sort_, status_) VALUES" +
                    " (?,?,?,?,?);";
            List<Object> paperQuestionParam = new ArrayList<>();
            paperQuestionParam.add(paperId);
            paperQuestionParam.add(question_id);
            paperQuestionParam.add(score);
            paperQuestionParam.add(sort);
            paperQuestionParam.add(true);
            ConnectUtil.insertInto(inserPaperQuestionSql, paperQuestionParam);
        }

        // 插入选项
        String options = obj.getString("options");
        if(options != null && !"".equals(options.trim()) && !"\"\"".equals(options.trim()) && !"[]".equals(options.trim())){
            out.write(options);
            out.write("\r\n");
            Object optionsObject = JSON.parse(options.trim());
            if(optionsObject instanceof JSONObject){
                JSONObject optionsObj = (JSONObject)optionsObject;
                for (Object map : optionsObj.entrySet()){
                    if(map != null){
                        String key = ((Map.Entry)map).getKey().toString();
                        String value = ((Map.Entry)map).getValue().toString();
                        if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                            String optionSql = "INSERT IGNORE INTO gx_option ( key_, value_, question_id) VALUES (?,?,?);";
                            List<Object> optionParam = new ArrayList<>();
                            optionParam.add(key);
                            optionParam.add(value);
                            optionParam.add(question_id);
                            ConnectUtil.insertInto(optionSql, optionParam);
                        }
                    }
                }
            }else if(optionsObject instanceof JSONArray){
                JSONArray optionArr = (JSONArray)optionsObject;
                for (int optioni = 0; optioni < optionArr.size(); optioni++) {
                    JSONObject optionsObj = optionArr.getJSONObject(optioni);
                    for (Object map : optionsObj.entrySet()){
                        if(map != null){
                            String key = ((Map.Entry)map).getKey().toString();
                            String value = ((Map.Entry)map).getValue().toString();
                            if(StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
                                String optionSql = "INSERT IGNORE INTO gx_option ( group_question, key_,value_, question_id) VALUES (?,?,?,?);";
                                List<Object> optionParam = new ArrayList<>();
                                optionParam.add(optioni + 1);
                                optionParam.add(key);
                                optionParam.add(value);
                                optionParam.add(question_id);
                                ConnectUtil.insertInto(optionSql, optionParam);
                            }
                        }
                    }
                }
            }
        }
        String knowledge = obj.getString("knowledge");



        int pic_answer = obj.getInteger("pic_answer");
        String answer = obj.getString("answer");

        int pic_explanation = obj.getInteger("pic_explanation");
        String explanation = obj.getString("explanation");



        JSONArray listArr = obj.getJSONArray("list");
        int level = 1;// 是否有子题目
        if(listArr != null && listArr.size() > 0){
            level = 2;
            for (int li = 0; li < listArr.size(); li++) {
                JSONObject questionObj = listArr.getJSONObject(li);
                iterorQuestion(questionObj, chid, xd, categoryId,textBookversionId,gradeversionId);
            }
        }

        String insertQuestionSql = "insert IGNORE INTO gx_question " +
                "(id,title, chid_id, xd_id, question_channel_type_id, exam_type_id, " +
                "difficult_id, category_id, kid_num, grade_id, source_paper_id, " +
                "create_by, create_time, update_time, update_by, parent_id, " +
                "sort_, score_, sub_score, pic_answer,answer, " +
                "pic_explanation,explanation, level_,region_ids,knowledge," +
                "text_book_version_id,grade_version_id) VALUES " +
                "(?,?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?,?,?,?," +
                "?,?)";
        List<Object> questionParam = new ArrayList<>();
        questionParam.add(question_id);
        questionParam.add(question_text);
        questionParam.add(chid);
        questionParam.add(xd);
        questionParam.add(question_channel_type);
        questionParam.add(exam_type);

        questionParam.add(difficult_index);
        questionParam.add(categoryId);
        questionParam.add(kid_num);
        questionParam.add(grade_id);
        questionParam.add(paperId);

        questionParam.add(0);
        questionParam.add(new Date());
        questionParam.add(new Date());
        questionParam.add(0);
        questionParam.add(parent_id);

        questionParam.add(sort);
        questionParam.add(score);
        questionParam.add(subscore);
        questionParam.add(pic_answer);
        questionParam.add(answer);

        questionParam.add(pic_explanation);
        questionParam.add(explanation);
        questionParam.add(level);
        questionParam.add(region_ids);
        questionParam.add(knowledge);

        questionParam.add(textBookversionId);
        questionParam.add(gradeversionId);


        ConnectUtil.insertInto(insertQuestionSql, questionParam);


        questionIdList.add(question_id);

            /*
            data.questions[0].question_id 题目ID
            question_channel_type 题型
            chid 科目
            xd 小初高
            parent_id 父ID
            difficult_index 困难程度
            exam_type 题类，真题。。。
            kid_num 知识点个数
            region_ids 地区
            grade_id 年级
            title,question_text 题目内容
            paper.pid  title 试卷ID，试卷名称
            options 选项
            knowledge 知识点图片
            score score全分 subscore多选分数[2,2]
            pic_answer 是否是图片答案
            answer 答案图片
            pic_explanation 是否是图片解析
            explanation 解析
            sort 排序
            list 子题目
             */
    }

    private static List<String> categoryType;
    private static List<String> knowledgeCcategoryType;

    static {
        categoryType = new ArrayList<>();
        knowledgeCcategoryType = new ArrayList<>();

        categoryType.add("TEXT_BOOK_VERSION");
        categoryType.add("GRADE");
        categoryType.add("CHAPTER");
        categoryType.add("TOPIC1");
        categoryType.add("TOPIC2");
        categoryType.add("TOPIC3");
        categoryType.add("TOPIC4");
        categoryType.add("TOPIC5");
        categoryType.add("TOPIC6");
        categoryType.add("TOPIC7");
        categoryType.add("TOPIC8");
        categoryType.add("TOPIC9");
        categoryType.add("TOPIC10");
        categoryType.add("TOPIC11");
        categoryType.add("TOPIC12");
        categoryType.add("TOPIC13");
        categoryType.add("TOPIC14");
        categoryType.add("TOPIC15");


        knowledgeCcategoryType.add("KNOWLEDGE1");
        knowledgeCcategoryType.add("KNOWLEDGE2");
        knowledgeCcategoryType.add("KNOWLEDGE3");
        knowledgeCcategoryType.add("KNOWLEDGE4");


    }

    // 教材版本
    // 第一次请求传入的chid 和xd参数不生效，PHP什么毛病
    private static void handleCategoryTree(int chid , int xd, int parentId, int hierarchy, String treeType)throws Exception{
        String url = "https://zujuan.21cnjy.com/question/tree?id="+parentId+"&chid="+chid+"&xd="+xd+"&type="+treeType;
        String result = HttpUtil.getInstance().doGetForString(url);
        out.write("url=" +url +", result=" + result);
        out.write("\r\n");
        if(result == null){// 增加重试
            handleCategoryTree(chid, xd, parentId, hierarchy, treeType);
            return;
        }
        JSONArray resultObj = (JSONArray)JSONObject.parse(result);
        for (int i = 0; i < resultObj.size(); i++) {
            JSONObject obj = resultObj.getJSONObject(i);
            int id = obj.getInteger("id");
            String title = obj.getString("title");
            boolean hasChild = obj.getBoolean("hasChild");
            String type = null;
            if("category".equals(treeType)){
                type = categoryType.get(hierarchy);
            }else if("knowledge".equals(treeType)){
                type = knowledgeCcategoryType.get(hierarchy);
            }
            String insertSql = "insert ignore INTO gx_category_tree (id, chid_id, xd_id, parent_id, title, has_child, type_, tree_type) VALUES (?,?,?,?,?,?,?,?)";
            out.write("parentId=" + parentId +", hierarchy=" +hierarchy + insertSql);
            out.write("\r\n");
            List<Object> params = new ArrayList<>();
            params.add(id);
            params.add(chid);
            params.add(xd);
            params.add(parentId);
            params.add(title);
            params.add(hasChild?1:0);
            params.add(type);
            params.add(treeType);

            ConnectUtil.insertInto(insertSql, params);
            if(hasChild){
                handleCategoryTree(chid, xd, id, hierarchy + 1, treeType);
            }
            Thread.sleep(100);
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
                out.write(elements.get(i2).toString());
                out.write("\r\n");
                String dataValue = elements.get(i2).attr("data-value");
                String textValue = elements.get(i2).text();

                if(StringUtils.isNotEmpty(dataValue)){

                    String channelTypeInsertSql = "INSERT IGNORE INTO gx_question_channel_type\n" +
                            "(id, name_)\n" +
                            "VALUES\n" +
                            "  ("+dataValue+", '"+textValue+"')";
                    ConnectUtil.insertInto(channelTypeInsertSql, null);
                    if(type == 0){
                        // <a data-name="question_channel_type" data-value="1" href="#">单选题</a>
                        String insertSql = "INSERT INTO gx_chid_xd_question_channel_type (chid_id, xd_id, question_channel_type_id) VALUES ("+chid+", "+xd+", "+dataValue+");";
                        out.write(insertSql + textValue);
                        out.write("\r\n");
                        ConnectUtil.insertInto(insertSql, null);
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

    private static void blankRequest(int chid, int xd) throws Exception {
        String url = "https://zujuan.21cnjy.com/question?chid=" + chid + "&xd=" + xd + "&tree_type=knowledge";
        String result = HttpUtil.getInstance().doGetForString(url);
    }
}
