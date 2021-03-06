package com.xiaowei.worksystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vividsolutions.jts.geom.Geometry;
import com.xiaowei.account.multi.entity.MultiBaseEntity;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

/**
 * 工程师处理工单附表实体
 */
@Entity
@Table(name = "W_ENGINEERWORK")
@SQLDelete(sql = "update w_engineerwork set delete_flag = true, delete_time = now() where id=?")
@Where(clause = "delete_flag <> true")
@Data
public class EngineerWork extends MultiBaseEntity {
    /**
     * 接单时间
     */
    private Date receivedTime;
    /**
     * 预约时间
     */
    private Date appointTime;
    /**
     * 出发时间
     */
    private Date departeTime;
    /**
     * 开始处理时间
     */
    private Date beginInhandTime;
    /**
     * 完成处理时间
     */
    private Date endInhandTime;
    /**
     * 出发地
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry startShape;
    /**
     * 目的地
     */
    @Column(columnDefinition = "geometry(POINT,4326)")
    @JsonIgnore
    private Geometry arriveShape;
    /**
     * 到达状态
     */
    private Integer arriveStatus;
    /**
     * 审核状态
     */
    private Integer pigeonholedStatus;
    /**
     * 到达图片
     */
    @Lob
    private String arriveFileStore;
    /**
     * 完成情况
     */
    private String state;

    @Transient
    private String startWkt;
    @Transient
    private String arriveWkt;

    @JsonIgnore
    public Geometry getStartShape() {
        return startShape;
    }

    @JsonIgnore
    public Geometry getArriveShape() {
        return arriveShape;
    }

    public String getStartWkt() {
        if (this.startShape != null) {
            return this.startShape.toText();
        }
        return startWkt;
    }

    public String getArriveWkt() {
        if (this.arriveShape != null) {
            return this.arriveShape.toText();
        }
        return arriveWkt;
    }
}
