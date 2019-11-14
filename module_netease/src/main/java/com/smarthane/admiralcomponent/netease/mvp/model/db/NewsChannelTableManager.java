package com.smarthane.admiralcomponent.netease.mvp.model.db;

import com.smarthane.admiral.core.base.AppComponent;
import com.smarthane.admiral.core.util.AdmiralUtils;
import com.smarthane.admiralcomponent.netease.R;
import com.smarthane.admiralcomponent.netease.mvp.model.api.Api;
import com.smarthane.admiralcomponent.netease.mvp.model.entity.NewsChannelTable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author smarthane
 * @time 2019/11/10 17:03
 * @describe
 */
public class NewsChannelTableManager {

    /**
     * 加载新闻类型
     * @return
     */
    public static List<NewsChannelTable> loadNewsChannelsMine() {
        List<String> channelName = Arrays.asList(AdmiralUtils.getStringArray(AppComponent.get().fetchApplication(),
                R.array.netease_news_channel_name));
        List<String> channelId = Arrays.asList(AdmiralUtils.getStringArray(AppComponent.get().fetchApplication(),
                R.array.netease_news_channel_id));
        ArrayList<NewsChannelTable> newsChannelTables = new ArrayList<>();
        for (int i = 0; i < channelName.size(); i++) {
            NewsChannelTable entity = new NewsChannelTable(channelName.get(i), channelId.get(i)
                    , Api.getType(channelId.get(i)), i <= 5, i, false);
            newsChannelTables.add(entity);
        }
        return newsChannelTables;
    }
    /**
     * 加载固定新闻类型
     * @return
     */
    public static List<NewsChannelTable> loadNewsChannelsStatic() {
        List<String> channelName = Arrays.asList(AdmiralUtils.getStringArray(AppComponent.get().fetchApplication(),
                R.array.netease_news_channel_name_static));
        List<String> channelId = Arrays.asList(AdmiralUtils.getStringArray(AppComponent.get().fetchApplication(),
                R.array.netease_news_channel_id_static));
        ArrayList<NewsChannelTable>newsChannelTables = new ArrayList<>();
        for (int i = 0; i < channelName.size(); i++) {
            NewsChannelTable entity = new NewsChannelTable(channelName.get(i), channelId.get(i)
                    , Api.getType(channelId.get(i)), i <= 5, i, true);
            newsChannelTables.add(entity);
        }
        return newsChannelTables;
    }


}
