package com.chungo.basemore.db;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * 使用realm数据库,所有的bean类需要继承该类
 *
 * @Description
 * @Author huangchangguo
 * @Created 2018/11/22 14:06
 */
public class RealmBean extends RealmObject implements Serializable {

    public RealmBean() {

    }

    @PrimaryKey
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
