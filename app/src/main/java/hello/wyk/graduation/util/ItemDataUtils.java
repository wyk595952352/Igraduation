package hello.wyk.graduation.util;


import com.wyk.model.LeftMenuItemObj;

import java.util.ArrayList;
import java.util.List;

import hello.wyk.graduation.R;

/**
 * 当前类注释:
 * ProjectName：DragHelper4QQ
 * Author:<a href="http://www.cniao5.com">菜鸟窝</a>
 * Description：
 * 菜鸟窝是一个只专注做Android开发技能的在线学习平台，课程以实战项目为主，对课程与服务”吹毛求疵”般的要求，
 * 打造极致课程，是菜鸟窝不变的承诺
 */
public class ItemDataUtils {
    public static List<LeftMenuItemObj> getItemBeans(){
        List<LeftMenuItemObj> itemBeans=new ArrayList<>();
        itemBeans.add(new LeftMenuItemObj(R.mipmap.sidebar_purse,"主页",false));
        itemBeans.add(new LeftMenuItemObj(R.mipmap.sidebar_decoration,"知识点练习",false));
        itemBeans.add(new LeftMenuItemObj(R.mipmap.sidebar_favorit,"我的收藏",false));
        itemBeans.add(new LeftMenuItemObj(R.mipmap.sidebar_album,"提问区",false));
        itemBeans.add(new LeftMenuItemObj(R.mipmap.sidebar_file,"错题",false));
        return  itemBeans;
    }
    
}
