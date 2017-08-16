package com.hk.zhouyuyin.http;

/**
 * Created by tom on 2017/8/8.
 */

public class AnalysisMusicName {

    public static String music_name;
    public static String Analysis(String music_name_uncom){
//        if(music_name_uncom.contains("播放")){
//            music_name = music_name_uncom.replace("播放","");
//            return music_name;
//        }
//        else if(music_name_uncom.contains("我想听")){
//            music_name = music_name_uncom.substring(3,music_name_uncom.length());
//            return music_name;
//        }else if(music_name_uncom.contains("我要听")){
//            music_name = music_name_uncom.substring(3,music_name_uncom.length());
//            return music_name;
//        }



        if(music_name_uncom.contains("我要听")){
            music_name_uncom = music_name_uncom.replace("我要听","");
            music_name = music_name_uncom;

        }

        if(music_name_uncom.contains("我想听")){
            music_name_uncom = music_name_uncom.replace("我想听","");
            music_name = music_name_uncom;

        }

        if(music_name_uncom.contains("这首歌")){
            music_name_uncom = music_name_uncom.replace("这首歌","");
            music_name = music_name_uncom;

        }

        if(music_name_uncom.contains("把")){
            music_name_uncom = music_name_uncom.replace("把","");
            music_name = music_name_uncom;

        }


        if(music_name_uncom.contains("听吧")){
            music_name_uncom = music_name_uncom.replace("听吧","");
            music_name = music_name_uncom;
        }


        if(music_name_uncom.contains("给我听")){
            music_name_uncom = music_name_uncom.replace("给我听","");
            music_name = music_name_uncom;

        }

        if(music_name_uncom.contains("放给我听")){
            music_name_uncom = music_name_uncom.replace("放给我听","");
            music_name = music_name_uncom;

        }

        if(music_name_uncom.contains("放给我")){
            music_name_uncom = music_name_uncom.replace("放给我","");
            music_name = music_name_uncom;

        }



        if(music_name_uncom.contains("播放给我听")){
            music_name_uncom = music_name_uncom.replace("播放给我听","");
            music_name = music_name_uncom;

        }



        if(music_name_uncom.contains("请")){
            music_name_uncom = music_name_uncom.replace("请","");
            music_name = music_name_uncom;

        }

        // 要放在播放的前面

        if(music_name_uncom.contains("给我播放")){
            music_name_uncom = music_name_uncom.replace("给我播放","");
            music_name = music_name_uncom;
        }

        if(music_name_uncom.contains("播放")){
            music_name_uncom = music_name_uncom.replace("播放","");
            music_name = music_name_uncom;

        }


        return music_name;
    }
}
