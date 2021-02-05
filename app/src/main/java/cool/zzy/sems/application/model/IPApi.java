package cool.zzy.sems.application.model;

import lombok.Data;

/**
 * @author intent zzy.main@gmail.com
 * @date 2021/2/5 18:10
 * @since 1.0
 */
@Data
public class IPApi {
    private String status;
    private String country;
    private String countryCode;
    private String region;
    private String regionName;
    private String city;
    private String zip;
    private String lat;
    private String lon;
    private String timezone;
    private String isp;
    private String org;
    private String as;
    private String query;
}
