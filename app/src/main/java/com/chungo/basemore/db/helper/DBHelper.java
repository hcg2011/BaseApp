package com.chungo.basemore.db.helper;

import java.util.List;

/**
 * @author: Est <codeest.dev@gmail.com>
 * @date: 2017/4/21
 * @description:
 */

public interface DBHelper<T> {

    void insertNewsId(int id);

    /**
     * 查询 阅读记录
     *
     * @param id
     * @return
     */
    boolean queryNewsId(int id);

    /**
     * 增加 收藏记录
     *
     * @param bean
     */
    void insertLikeBean(T bean);

    /**
     * 删除 收藏记录
     *
     * @param id
     */
    void deleteLikeBean(String id);

    /**
     * 查询 收藏记录
     *
     * @param id
     * @return
     */
    boolean queryLikeId(String id);

    List<T> getLikeList();

    /**
     * 修改 收藏记录 时间戳以重新排序
     *
     * @param id
     * @param time
     * @param isPlus
     */
    void changeLikeTime(String id, long time, boolean isPlus);

    /**
     * 更新 掘金首页管理列表
     *
     * @param bean
     */
    void updateGoldManagerList(T bean);

    /**
     * 获取 掘金首页管理列表
     *
     * @return
     */
    T getGoldManagerList();
}
