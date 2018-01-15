package com.qlchat.tools.pulldata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import java.util.regex.Pattern;

public class PullQuestionChannelType {
    // shift + option + /
    public static void main(String[] args) throws Exception {
        String url = "https://zujuan.21cnjy.com/question?chid=2&xd=1&tree_type=knowledge";
        System.out.println(url);
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
//        OrFilter
        System.out.println(nodes.size());

        Node channelTypeNode = nodes.elementAt(0);
        System.out.println(channelTypeNode.toHtml());
//        for(Node node : nodes.toNodeArray()){
//            System.out.println(node.getChildren().size());
//        }
    }
}
