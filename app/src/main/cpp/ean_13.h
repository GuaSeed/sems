//
// Created by Intent on 2020/12/11.
//

#ifndef EAN_13_EAN_13_H
#define EAN_13_EAN_13_H

#include <iostream>
#include <opencv2/opencv.hpp>
#include "ean_13_constant.h"
#include "cpp_log.h"

class ean_13 {
public:
    struct barcode_info {
        // 开始的位置
        int start_pos;
        // 结束的位置
        int stop_pos;
        // 平均单位
        int min_unit;
    };
    struct barcode_ret {
        // 权值和
        float weight_sum;
        // 条形码
        std::string barcode;
    };

    static std::string recognition(cv::Mat &src, cv::Rect &rect);

private:
    static void print_line(const uchar *line, int width);

    static void print_line(const uchar *line, ean_13::barcode_info info);

    static void print_vector(const std::vector<float> &barcode_data, ean_13::barcode_info info);

    static ean_13::barcode_info get_min_unit(const uchar *line, int width);

    static std::string decode(const std::vector<float> &barcode_data);

    static std::vector<float> convert(const uchar *line, ean_13::barcode_info info);

    static int global_euclidean_distance(const float *a, bool is_l_code, float &weight);

    static float local_euclidean_distance(int index, const float *a, bool is_l_codeL);
};


#endif //EAN_13_EAN_13_H
