package hello.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @Description:
 * @vertion:
 * @author:yizhendong
 * @date:2017/8/5 12:59
 */
public class DidiSheetDTO {
    @Setter @Getter private String date;
    @Setter @Getter private String tripDuration;
    @Setter @Getter private String reason = "加班";
    @Setter @Getter private String start;
    @Setter @Getter private String end;
    @Setter @Getter private String cost;
    @Setter @Getter private String number;
    @Setter @Getter private String name;
    @Setter @Getter private String remark;

    @Override
    public String toString() {
        return "DidiSheetDTO{" +
                "date='" + date + '\'' +
                ", tripDuration='" + tripDuration + '\'' +
                ", reasion='" + reason + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", cost='" + cost + '\'' +
                ", number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
