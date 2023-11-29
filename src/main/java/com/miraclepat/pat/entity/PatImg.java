package com.miraclepat.pat.entity;

import com.miraclepat.pat.constant.ImgType;
import com.miraclepat.utils.entity.BaseModifiableEntity;
import com.miraclepat.utils.entity.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(indexes = {@Index(name = "pat_img_pat_id_index", columnList = "pat_id")})
@Getter //필수
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatImg extends BaseModifiableEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //mysql
    @Column(name = "pat_img_id")
    private Long id;

    private String imgName; //관리할 때 사용할 이름

    private String oriImgName; //원래 이미지 이름

    @Enumerated(EnumType.STRING)
    private ImgType imgType; //어떤 이미지인가?

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pat_id")
    private Pat pat;

    //변경감지
    public void updateImg(String oriImgName, String imgName) {
        this.imgName = imgName;
        this.oriImgName = oriImgName;
    }
}
