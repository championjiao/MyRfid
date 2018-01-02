package com.jxb.myrfid.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jxb on 2017-11-20.
 */

@Entity(indexes = {
        @Index(value = "text, date DESC", unique = true)
})
public class Note {
    @Id
    private Long id;

    @NotNull
    private String text;
    private Date date;
@Generated(hash = 1501873839)
public Note(Long id, @NotNull String text, Date date) {
    this.id = id;
    this.text = text;
    this.date = date;
}
@Generated(hash = 1272611929)
public Note() {
}
public Long getId() {
    return this.id;
}
public void setId(Long id) {
    this.id = id;
}
public String getText() {
    return this.text;
}
public void setText(String text) {
    this.text = text;
}
public Date getDate() {
    return this.date;
}
public void setDate(Date date) {
    this.date = date;
}
}
